package server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import corba_server.FlightReservationServerPOA;
import databases.DatabaseRepository;
import databases.FlightRecordDb;
import databases.FlightReservationDb;
import databases.PassengerRecordDb;
import db_models.AddFlightRecord;
import db_models.EditSeats;
import enums.City;
import enums.EditType;
import enums.FlightClass;
import enums.FlightRecordField;
import log.CustomLogger;
import log.ILogger;
import log.TextFileLog;
import manager_requests.BookedFlightCountRequest;
import manager_requests.EditFlightRecordRequest;
import models.Address;
import models.FlightRecord;
import models.FlightReservation;
import models.FlightSeats;
import models.FlightServerAddress;
import models.ManagerRecord;
import models.PassengerRecord;
import udp.BookedFlightCountTask;
import udp.RequestDispatcher;

public class FlightServerImpl extends FlightReservationServerPOA implements Runnable{
	private final int BUFFER_SIZE = 1000;
	private final int THREAD_POOL_SIZE = 16;
	private final ExecutorService threadPool;
	DatagramSocket socket;
	int port;
	City city;
	private DatabaseRepository databaseRepository;
	private FlightServerAddress[] flightServerAddresses;
	private ILogger logger;

	public FlightServerImpl(int port, City city, DatabaseRepository databaseRepository, FlightServerAddress[] flightServerAddresses) {
		this.threadPool = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
		this.socket = null;
		this.port = port;
		this.city = city;
		this.databaseRepository = databaseRepository;
		this.flightServerAddresses = flightServerAddresses;
		this.logger = new CustomLogger(new TextFileLog());
	}

	public void run() {
		serveRequests();
	}

	@SuppressWarnings("deprecation")
	@Override
	public String bookFlight(String firstName, String lastName, String address, String phoneNumber, String destination,
			String date, String flightClass) {
		FlightClass flightClassValue = FlightClass.valueOf(flightClass.toUpperCase());
		Date dateValue = new Date(date);
		Address addressValue = new Address(address);
		
		PassengerRecordDb passengerRecordDb = databaseRepository.getPassengerRecordDb();
		PassengerRecord passengerRecord = passengerRecordDb.addPassengerRecord(lastName.toUpperCase(), firstName.toUpperCase(), addressValue, phoneNumber);
		FlightRecordDb flightRecordDb = databaseRepository.getFlightRecordDb();
		FlightRecord[] flightRecords = flightRecordDb.getFlightRecords(dateValue, flightClassValue);
		FlightReservationDb flightReservationDb = databaseRepository.getFlightReservationDb();
		for (FlightRecord flightRecord : flightRecords){
			// Book first available flight
			FlightReservation flightReservation = flightReservationDb.addFlightReservation(flightClassValue, passengerRecord, flightRecord);
			logger.log(city.toString(), "BOOK_FLIGHT_SUCCESS", flightReservation.toString());
			return flightReservation.toString();
		}
		logger.log(city.toString(), "BOOK_FLIGHT_FAIL", "Either no Flight Records match Date: " + date + " or there are no available seats for " + flightClassValue);
		return "No flight available";
	}

	@Override
	public String getBookedFlightCount(String bookedFlightCountRequest) {
		BookedFlightCountRequest bookedFlightCountRequestValue = new BookedFlightCountRequest(bookedFlightCountRequest);
		FlightClass flightClass = bookedFlightCountRequestValue.getFlightClass();
		String managerTag = bookedFlightCountRequestValue.getManagerId();
		
		StringBuilder sb = new StringBuilder();
		try
		{
			final ExecutorService executorService = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
			List<Future<String>> flightCounts = new ArrayList<Future<String>>();
			for(FlightServerAddress flightServerAddress : flightServerAddresses){
				Future<String> result = executorService.submit(new BookedFlightCountTask(flightServerAddress, flightClass));
				flightCounts.add(result);
			}
			FlightReservationDb flightReservationDb = databaseRepository.getFlightReservationDb();
			String localFlightCount = city + " " + flightReservationDb.getFlightReservationCount(flightClass);
			sb.append(localFlightCount).append(", ");
			for(int i = 0; i < flightCounts.size() - 1; ++i){
				String flightCount = flightCounts.get(i).get();
				sb.append(flightCount).append(", ");
			}
			if (flightCounts.size() > 0){
				int lastIndex = flightCounts.size() - 1;
				String flightCount = flightCounts.get(lastIndex).get();
				sb.append(flightCount);
			}
			logger.log(city.toString(), "BOOKED_FLIGHTCOUNT_SUCCESS", sb.toString());
			logger.log(managerTag, "BOOKED_FLIGHTCOUNT_SUCCESS", sb.toString());
			return sb.toString();
		} catch (Exception e)
		{
			logger.log(city.toString(), "BOOKED_FLIGHTCOUNT_FAIL", e.getMessage());
			logger.log(managerTag, "BOOKED_FLIGHTCOUNT_FAIL", e.getMessage());
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public String editFlightRecord(String editFlightRecordRequest, String fieldToEdit, String newValue) {
		EditFlightRecordRequest editFlightRecordRequestValue = new EditFlightRecordRequest(editFlightRecordRequest);
		FlightRecordField fieldToEditValue = FlightRecordField.valueOf(fieldToEdit.toUpperCase());	
		String managerId = editFlightRecordRequestValue.getManagerId();
		EditType editType = editFlightRecordRequestValue.getEditType();
		int flightRecordId = editFlightRecordRequestValue.getFlightRecordId();
		
		switch(editType){
		case ADD:
			return addFlightRecord(managerId, newValue);
		case EDIT:
			return editFlightRecord(managerId, flightRecordId, fieldToEditValue, newValue);
		case REMOVE:
			return removeFlightRecord(managerId, flightRecordId);
		default:
			logger.log(city.toString(), "ERROR", "Invalid EditType: " + editType);
			logger.log(managerId, "ERROR", "Invalid EditType: " + editType);
			break;		
		}
		return null;
	}

	@Override
	public String transferReservation(String transferReservationRequest, String currentCity, String otherCity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] getFlightRecords() {
		FlightRecord[] flightRecords = databaseRepository.getFlightRecordDb().getFlightRecords();
		String[] records = new String[flightRecords.length];
		for (int i = 0; i < flightRecords.length; ++i){
			records[i] = flightRecords[i].toString();
		}
		return records;
	}

	@Override
	public String[] getFlightReservations() {
		FlightReservation[] flightReservations = databaseRepository.getFlightReservationDb().getFlightReservations();
		String[] records = new String[flightReservations.length];
		for (int i = 0; i < flightReservations.length; ++i){
			records[i] = flightReservations[i].toString();
		}
		return records;
	}

	@Override
	public String[] getPassengerRecords() {
		PassengerRecord[] passengerRecords = databaseRepository.getPassengerRecordDb().getPassengerRecords();
		String[] records = new String[passengerRecords.length];
		for (int i = 0; i < passengerRecords.length; ++i){
			records[i] = passengerRecords[i].toString();
		}
		return records;
	}

	@Override
	public String[] getManagerRecords() {
		ManagerRecord[] managerRecords = databaseRepository.getManagerRecordDb().getManagerRecords();
		String[] records = new String[managerRecords.length];
		for (int i = 0; i < managerRecords.length; ++i){
			records[i] = managerRecords[i].toString();
		}
		return records;
	}
	
	private String addFlightRecord(String managerTag, String newValue) {
		FlightRecordDb flightRecordDb = databaseRepository.getFlightRecordDb();
		AddFlightRecord addFlightRecord = new AddFlightRecord(newValue);
		FlightRecord flightRecord = flightRecordDb.addFlightRecord(addFlightRecord);
		if(flightRecord == null){
			logger.log(city.toString(), "FLIGHT_RECORD_NOT_ADDED", newValue);
			logger.log(managerTag, "FLIGHT_RECORD_NOT_ADDED", newValue);
			return null;
		}
		logger.log(city.toString(), "FLIGHT_RECORD_ADDED", flightRecord.toString());
		logger.log(managerTag, "FLIGHT_RECORD_ADDED", flightRecord.toString());
		return flightRecord.toString();
	}
	
	private String removeFlightRecord(String managerTag, int flightRecordId) {
		FlightRecordDb flightRecordDb = databaseRepository.getFlightRecordDb();
		FlightRecord flightRecord = flightRecordDb.removeFlightRecord(flightRecordId);
		if (flightRecord == null){
			logger.log(city.toString(), "FLIGHT_RECORD_NOT_FOUND", Integer.toString(flightRecordId));
			logger.log(managerTag, "FLIGHT_RECORD_NOT_FOUND", Integer.toString(flightRecordId));
			return "FlightRecord does not exist.";
		}
		logger.log(city.toString(), "FLIGHT_RECORD_REMOVED", flightRecord.toString());
		logger.log(managerTag, "FLIGHT_RECORD_REMOVED", flightRecord.toString());
		FlightReservationDb flightReservationDb = databaseRepository.getFlightReservationDb();
		FlightReservation[] flightReservationsRemoved = flightReservationDb.removeFlightReservations(flightRecord.getId());
		for(FlightReservation flightReservation : flightReservationsRemoved){
			logger.log(city.toString(), "REMOVED_FLIGHT_RESERVATION_FOR_REMOVED_FLIGHT_RECORD", flightReservation.toString());
			logger.log(managerTag, "REMOVED_FLIGHT_RESERVATION_FOR_REMOVED_FLIGHT_RECORD", flightReservation.toString());
		}
		return flightRecord.toString();
	}
	
	@SuppressWarnings("deprecation")
	private String editFlightRecord(String managerTag, int flightRecordId, FlightRecordField fieldToEdit, String newValue) {
		FlightRecordDb flightRecordDb = databaseRepository.getFlightRecordDb();
		FlightRecord flightRecord = flightRecordDb.getFlightRecord(flightRecordId);
		if (flightRecord == null){
			logger.log(city.toString(), "FLIGHT_RECORD_NOT_FOUND", Integer.toString(flightRecordId));
			logger.log(managerTag, "FLIGHT_RECORD_NOT_FOUND", Integer.toString(flightRecordId));
			return "FlightRecord does not exist.";
		}
		
		switch(fieldToEdit){
		case DATE:
			Date date = new Date(newValue);
			flightRecord.setFlightDate(date);
			logger.log(city.toString(), "FLIGHT_RECORD_DATE_CHANGED", flightRecord.toString());
			logger.log(managerTag, "FLIGHT_RECORD_DATE_CHANGED", flightRecord.toString());
			break;
		case DESTINATION:
			City destination = City.valueOf(newValue.toUpperCase());
			flightRecord.setDestination(destination);
			logger.log(city.toString(), "FLIGHT_RECORD_DESTINATION_CHANGED", flightRecord.toString());
			logger.log(managerTag, "FLIGHT_RECORD_DESTINATION_CHANGED", flightRecord.toString());
			break;
		case ORIGIN:
			City origin = City.valueOf(newValue.toUpperCase());
			flightRecord.setOrigin(origin);
			logger.log(city.toString(), "FLIGHT_RECORD_ORIGIN_CHANGED", flightRecord.toString());
			logger.log(managerTag, "FLIGHT_RECORD_ORIGIN_CHANGED", flightRecord.toString());
			break;
		case SEATS:
			EditSeats editSeats = new EditSeats(newValue);
			FlightClass flightClass = editSeats.getFlightClass();
			int seats = editSeats.getSeats();
			HashMap<FlightClass, FlightSeats> flightClasses = flightRecord.getFlightClasses();
			FlightSeats flightSeats = flightClasses.get(flightClass);
			int seatOverflow = flightSeats.setSeats(seats);
			logger.log(city.toString(), "FLIGHT_RECORD_" + flightClass + "_SEATS_CHANGED", flightRecord.toString());
			logger.log(managerTag, "FLIGHT_RECORD_" + flightClass + "_SEATS_CHANGED", flightRecord.toString());
			if (seatOverflow > 0){
				logger.log(city.toString(), "SEAT_OVERFLOW_OCURRED", Integer.toString(seatOverflow));
				logger.log(managerTag, "SEAT_OVERFLOW_OCCURED", Integer.toString(seatOverflow));
				FlightReservationDb flightReservationDb = databaseRepository.getFlightReservationDb();
				FlightReservation[] flightReservationsRemoved = flightReservationDb.removeFlightReservations(flightClass, seatOverflow);
				for(FlightReservation flightReservation : flightReservationsRemoved){
					logger.log(city.toString(), "REMOVED_FLIGHT_RESERVATION", flightReservation.toString());
					logger.log(managerTag, "REMOVED_FLIGHT_RESERVATION", flightReservation.toString());
				}
			}
			break;
		default:
			logger.log(city.toString(), "ERROR", "Invalid FlightRecordField: " + fieldToEdit);
			logger.log(managerTag, "ERROR", "Invalid FlightRecordField: " + fieldToEdit);
			break;		
		}	
		return flightRecord.toString();
	}
	
	private void serveRequests() {
		try {
			socket = new DatagramSocket(port);
			while (true) {
				byte[] buffer = new byte[BUFFER_SIZE];
				DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
				socket.receive(packet);
				threadPool.execute(new RequestDispatcher(socket, packet, databaseRepository));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}