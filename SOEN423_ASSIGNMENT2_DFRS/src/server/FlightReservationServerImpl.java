package server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import database.IFlightDb;
import database.IPassengerRecordDb;
import dfrs.FlightReservationServerHelper;
import dfrs.FlightReservationServerPOA;
import enums.FlightClassEnum;
import enums.FlightDbOperation;
import enums.FlightParameter;
import enums.LogOperation;
import log.ILogger;
import models.Address;
import models.City;
import models.Flight;
import models.FlightClass;
import models.FlightClassOperation;
import models.FlightCountResult;
import models.FlightServerAddress;
import models.Passenger;
import models.PassengerRecord;
import models.FlightRecordOperation;

public class FlightReservationServerImpl extends FlightReservationServerPOA
{
	private final int BUFFER_SIZE = 1000;
	private final int THREAD_POOL_SIZE = 16;
    private final ExecutorService threadPool;
	private int udpPort;
	private List<FlightServerAddress> otherServers;
	private String cityAcronym;
	private IPassengerRecordDb passengerRecordDb;
	private IFlightDb flightDb;
	private List<String> managerIds;
	private ILogger logger;
	public FlightReservationServerImpl(int udpPort, int threadPoolSize, List<FlightServerAddress> otherServers, String cityAcronym, IPassengerRecordDb passengerRecordDb, IFlightDb flightDb, List<String> managerIds, ILogger logger) {
		if (udpPort < 0)
		{
			throw new IllegalArgumentException();
		}
		if (threadPoolSize < 0)
		{
			throw new IllegalArgumentException();
		}
		if (otherServers == null)
		{
			throw new IllegalArgumentException();
		}
		if (cityAcronym == null)
		{
			throw new IllegalArgumentException();
		}
		if (passengerRecordDb == null)
		{
			throw new IllegalArgumentException();
		}
		if (flightDb == null)
		{
			throw new IllegalArgumentException();
		}
		if (managerIds == null)
		{
			throw new IllegalArgumentException();
		}
		if (logger == null)
		{
			throw new IllegalArgumentException();
		}
		
		this.threadPool = Executors.newFixedThreadPool(threadPoolSize);
		this.udpPort = udpPort;
		this.otherServers = otherServers;
		this.cityAcronym = cityAcronym;
		this.passengerRecordDb = passengerRecordDb;
		this.flightDb = flightDb;
		this.managerIds = managerIds;
		this.logger = logger;
	}

	@Override
	public boolean bookFlight(String firstName, String lastName, String address, String phoneNumber, String flightWithClass)
	{
		Passenger passenger = new Passenger(firstName, lastName, phoneNumber, new Address(address));
		
		String[] tokens = flightWithClass.split("-");
		int flightId = Integer.parseInt(tokens[0]);
		FlightClassEnum flightClassEnum = FlightClassEnum.toFlightClass(tokens[1]);	
		Flight existingFlight = this.flightDb.getFlight(flightId);
		if (existingFlight == null){
			this.logger.log(this.cityAcronym, LogOperation.BOOK_FLIGHT.name(), flightId + " no longer exists!");
			return false;
		}
		int flightRecordId = existingFlight.getRecordId();
		boolean acquireSeatResult = this.flightDb.acquireSeat(flightRecordId, flightClassEnum);
		if(acquireSeatResult){
			PassengerRecord passengerRecord = new PassengerRecord(passenger, existingFlight, flightClassEnum, new Date());
			boolean result;
			try
			{
				result = this.passengerRecordDb.addRecord(passengerRecord);
				if(result){
					this.logger.log(this.cityAcronym, LogOperation.BOOK_FLIGHT.name(),"Booked " + flightClassEnum.name() + " class " + passengerRecord.toString());
				}
				return result;
			} catch (Exception e)
			{
				this.logger.log(this.cityAcronym, LogOperation.BOOK_FLIGHT.name(), e.getMessage());
			}
		} else {
			this.logger.log(this.cityAcronym, LogOperation.BOOK_FLIGHT.name(), "Could not acquire " + flightClassEnum.name() + " class seat on " + existingFlight);
		}
		return false;
	}
	
	@Override
	public void setOtherServers(String[] otherServers)
	{
		List<FlightServerAddress> otherServersOutput = new ArrayList<FlightServerAddress>();
		for (String s : otherServers){
			otherServersOutput.add(FlightServerAddress.toFlightServerAddress(s));
		}
		this.otherServers = otherServersOutput;
	}

	@Override
	public String getBookedFlightCount(String flightClassOperation)
	{		
		StringBuilder builder = new StringBuilder();
		FlightClassOperation flightClassOperationObject = FlightClassOperation.toFlightClassOperation(flightClassOperation);
		FlightClassEnum flightClass = flightClassOperationObject.getFlightClassEnum();
		String managerId = flightClassOperationObject.getManagerId();
		try
		{
			final ExecutorService executorService = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
			final Future<FlightCountResult> flightCountFirst = executorService.submit(new BookedFlightCountTask(flightClass, otherServers.get(0)));
			final Future<FlightCountResult> flightCountSecond = executorService.submit(new BookedFlightCountTask(flightClass, otherServers.get(1)));
			int flightCountThird = this.passengerRecordDb.numberOfRecords(flightClass);
			builder.append(flightClass + " : ");
			builder.append(cityAcronym + " " + flightCountThird + ", ");
			FlightCountResult firstResult = flightCountFirst.get();
			FlightCountResult secondResult = flightCountSecond.get();
			builder.append(firstResult.getServerName() + " " + firstResult.getCount() + ", ");
			builder.append(secondResult.getServerName() + " " + secondResult.getCount());
			this.logger.log(this.cityAcronym, LogOperation.BOOK_COUNT_REQUEST.name(), builder.toString());
			this.logger.log(managerId, LogOperation.BOOK_COUNT_REQUEST.name(), builder.toString());
			return builder.toString();
		} catch (Exception e)
		{
			this.logger.log(this.cityAcronym, LogOperation.BOOK_COUNT_REQUEST.name(), e.getMessage());
			this.logger.log(managerId, LogOperation.BOOK_COUNT_REQUEST.name(), e.getMessage());
			return null;
		}
	}
	
	@Override
	public String[] getFlights()
	{
		List<Flight> flightList = this.flightDb.getFlights();
		int size = flightList.size();
		String[] flights = new String[size];
		for(int i = 0; i < size; ++i){
			flights[i] = flightList.get(i).toString();
		}
		return flights;
	}
	
	@Override
	public String[] getAvailableFlights()
	{
		List<Flight> availableFlights = this.flightDb.getAvailableFlights();
		int size = availableFlights.size();
		String[] flights = new String[size];
		for(int i = 0; i < size; ++i){
			flights[i] = availableFlights.get(i).toString();
		}
		return flights;
	}
	
	@Override
	public boolean editFlightRecord(String flightRecordOperation, String flightParameter, String newValue)
	{
		if (flightRecordOperation == null){
			throw new IllegalArgumentException();
		}
		if (flightParameter == null){
			throw new IllegalArgumentException();
		}
		
		FlightRecordOperation flightRecordOperationObject = FlightRecordOperation.toFlightRecordOperation(flightRecordOperation);
		
		FlightDbOperation flightDbOperation = flightRecordOperationObject.getFlightDbOperation();
		String managerId = flightRecordOperationObject.getManagerId();
		
		FlightParameter flightParameterEnum = FlightParameter.toFlightParameter(flightParameter);
		
		switch(flightDbOperation){
		case ADD:
			return addFlight(flightRecordOperation, newValue);
		case REMOVE:
			return removeFlight(flightRecordOperationObject);
		case EDIT:
			return editFlight(flightRecordOperationObject, flightParameterEnum, newValue);
		default:
			this.logger.log(this.cityAcronym, LogOperation.UNKNOWN.name(), managerId + " : An error occured, nothing happened");
			this.logger.log(managerId, LogOperation.UNKNOWN.name(), "An error occured, nothing happened");
			return false;
		}		
	}

	private boolean addFlight(String flightRecordOperation, String newValue) {
		FlightRecordOperation flightRecordOperationObject = FlightRecordOperation.toFlightRecordOperation(flightRecordOperation);
		String managerId = flightRecordOperationObject.getManagerId();
		if(newValue == null){
			this.logger.log(this.cityAcronym, LogOperation.ILLEGAL_ARGUMENT_EXCEPTION.name(), managerId + " : newValue was null");
			this.logger.log(managerId, LogOperation.ILLEGAL_ARGUMENT_EXCEPTION.name(), "newValue was null");
			return false;
		}
		
		Flight flight = new Flight(newValue);
		boolean result = flightDb.addFlight(flight);
		if(result)
		{
			this.logger.log(this.cityAcronym, LogOperation.ADD_FLIGHT.name(), managerId + " : " + flight.toString());
			this.logger.log(managerId, LogOperation.ADD_FLIGHT.name(), flight.toString());	
		}
		else {
			this.logger.log(this.cityAcronym, LogOperation.FLIGHT_ALREADY_EXISTS.name(), managerId + " : " + flight.toString());
			this.logger.log(managerId, LogOperation.FLIGHT_ALREADY_EXISTS.name(), flight.toString());	
		}
		return result;
	}
	
	private boolean removeFlight(FlightRecordOperation flightRecordOperation) {
		String managerId = flightRecordOperation.getManagerId();
		int flightRecordId = flightRecordOperation.getRecordId();
		Flight flight = flightDb.removeFlight(flightRecordId);
		if(flight != null)
		{
			this.logger.log(this.cityAcronym, LogOperation.REMOVE_FLIGHT.name(), managerId + " : " + flight.toString());
			this.logger.log(managerId, LogOperation.REMOVE_FLIGHT.name(), flight.toString());	
		}
		else {
			this.logger.log(this.cityAcronym, LogOperation.FLIGHT_NO_LONGER_EXISTS.name(), managerId + " : " + flightRecordId);
			this.logger.log(managerId, LogOperation.FLIGHT_NO_LONGER_EXISTS.name(), flightRecordId + "");	
		}
		return flight != null;
	}
	
	private boolean editFlight(FlightRecordOperation flightRecordOperation, FlightParameter flightParameter, String newValue) {
		String managerId = flightRecordOperation.getManagerId();
		if(flightParameter == null){
			this.logger.log(this.cityAcronym, LogOperation.ILLEGAL_ARGUMENT_EXCEPTION.name(), managerId + " : flightParameter was null");
			this.logger.log(managerId, LogOperation.ILLEGAL_ARGUMENT_EXCEPTION.name(), "flightParameter was null");
			return false;
		}
		if(newValue == null){
			this.logger.log(this.cityAcronym, LogOperation.ILLEGAL_ARGUMENT_EXCEPTION.name(), managerId + " : newValue was null");
			this.logger.log(managerId, LogOperation.ILLEGAL_ARGUMENT_EXCEPTION.name(), "newValue was null");
			return false;
		}
		int flightRecordId = flightRecordOperation.getRecordId();
		int seatOverflow = 0;
		FlightClass flightClass = null;
		List<PassengerRecord> removedRecords = null;
		Flight flight = flightDb.editFlight(flightRecordId, flightParameter, newValue);
		switch(flightParameter)
		{
		case BUSINESS_CLASS_SEATS:
			if(flight == null){
				this.logger.log(this.cityAcronym, LogOperation.ILLEGAL_ARGUMENT_EXCEPTION.name(), managerId + " : An error occured, flight was null");
				this.logger.log(managerId, LogOperation.ILLEGAL_ARGUMENT_EXCEPTION.name(), "An error occured, flight was null");
				return false;
			}
			flightClass = flight.getBusinessClass();
			seatOverflow = flightClass.getAvailableSeats();
			removedRecords = correctSeatOverflow(flightRecordId, FlightClassEnum.BUSINESS, seatOverflow);
			this.logger.log(this.cityAcronym, LogOperation.EDIT_FLIGHT.name(), managerId + " : Changed seats for " + FlightClassEnum.BUSINESS.name() + " class of " + flight.toString());
			this.logger.log(managerId, LogOperation.EDIT_FLIGHT.name(), "Changed seats for " + FlightClassEnum.BUSINESS.name() + " class of " + flight.toString());
			if(removedRecords != null && !removedRecords.isEmpty())
			{
				for (int i = 0; i < removedRecords.size(); ++i)
				{
					this.logger.log(this.cityAcronym, LogOperation.EDIT_FLIGHT.name(), managerId + " : Removed " + removedRecords.get(i).toString() + " due to reduced available seats in " + FlightClassEnum.BUSINESS.name() + " class of " + flight.toString());
					this.logger.log(managerId, LogOperation.EDIT_FLIGHT.name(), "Removed " + removedRecords.get(i).toString() + " due to reduced available seats in " + FlightClassEnum.BUSINESS.name() + " class of " + flight.toString());
					flightClass.setAvailableSeats(0);
				}
			}
			break;
		case DATE:
			if(flight == null){
				this.logger.log(this.cityAcronym, LogOperation.ILLEGAL_ARGUMENT_EXCEPTION.name(), managerId + " : An error occured, flight was null");
				this.logger.log(managerId, LogOperation.ILLEGAL_ARGUMENT_EXCEPTION.name(), "An error occured, flight was null");
				return false;
			}
			this.logger.log(this.cityAcronym, LogOperation.EDIT_FLIGHT.name(), managerId + " : Date was edited for " + flight.toString());
			this.logger.log(managerId, LogOperation.EDIT_FLIGHT.name(), "Date was edited for " + flight.toString());
			break;
		case DESTINATION:
			if(flight == null){
				this.logger.log(this.cityAcronym, LogOperation.ILLEGAL_ARGUMENT_EXCEPTION.name(), managerId + " : An error occured, flight was null");
				this.logger.log(managerId, LogOperation.ILLEGAL_ARGUMENT_EXCEPTION.name(), "An error occured, flight was null");
				return false;
			}
			this.logger.log(this.cityAcronym, LogOperation.EDIT_FLIGHT.name(), managerId + " : Destination was edited for " + flight.toString());
			this.logger.log(managerId, LogOperation.EDIT_FLIGHT.name(), "Destination was edited for " + flight.toString());
			break;
		case ECONOMY_CLASS_SEATS:
			if(flight == null){
				this.logger.log(this.cityAcronym, LogOperation.ILLEGAL_ARGUMENT_EXCEPTION.name(), managerId + " : An error occured, flight was null");
				this.logger.log(managerId, LogOperation.ILLEGAL_ARGUMENT_EXCEPTION.name(), "An error occured, flight was null");
				return false;
			}
			flightClass = flight.getEconomyClass();
			seatOverflow = flightClass.getAvailableSeats();
			removedRecords = correctSeatOverflow(flightRecordId, FlightClassEnum.ECONOMY, seatOverflow);
			this.logger.log(this.cityAcronym, LogOperation.EDIT_FLIGHT.name(), managerId + " : Changed seats for " + FlightClassEnum.ECONOMY.name() + " class of " + flight.toString());
			this.logger.log(managerId, LogOperation.EDIT_FLIGHT.name(), "Changed seats for " + FlightClassEnum.ECONOMY.name() + " class of " + flight.toString());
			if(removedRecords != null && !removedRecords.isEmpty())
			{
				for (int i = 0; i < removedRecords.size(); ++i)
				{
					this.logger.log(this.cityAcronym, LogOperation.EDIT_FLIGHT.name(), managerId + " : Removed " + removedRecords.get(i).toString() + " due to reduced available seats in " + FlightClassEnum.ECONOMY.name() + " class of " + flight.toString());
					this.logger.log(managerId, LogOperation.EDIT_FLIGHT.name(), "Removed " + removedRecords.get(i).toString() + " due to reduced available seats in " + FlightClassEnum.ECONOMY.name() + " class of " + flight.toString());
					flightClass.setAvailableSeats(0);
				}
			}
			break;
		case FIRST_CLASS_SEATS:
			if(flight == null){
				this.logger.log(this.cityAcronym, LogOperation.ILLEGAL_ARGUMENT_EXCEPTION.name(), managerId + " : An error occured, flight was null");
				this.logger.log(managerId, LogOperation.ILLEGAL_ARGUMENT_EXCEPTION.name(), "An error occured, flight was null");
				return false;
			}
			flightClass = flight.getFirstClass();
			seatOverflow = flightClass.getAvailableSeats();
			removedRecords = correctSeatOverflow(flightRecordId, FlightClassEnum.FIRST, seatOverflow);
			this.logger.log(this.cityAcronym, LogOperation.EDIT_FLIGHT.name(), managerId + " : Changed seats for " + FlightClassEnum.FIRST.name() + " class of " + flight.toString());
			this.logger.log(managerId, LogOperation.EDIT_FLIGHT.name(), "Changed seats for " + FlightClassEnum.FIRST.name() + " class of " + flight.toString());
			if(removedRecords != null && !removedRecords.isEmpty())
			{
				for (int i = 0; i < removedRecords.size(); ++i)
				{
					this.logger.log(this.cityAcronym, LogOperation.EDIT_FLIGHT.name(), managerId + " : Removed " + removedRecords.get(i).toString() + " due to reduced available seats in " + FlightClassEnum.FIRST.name() + " class of " + flight.toString());
					this.logger.log(managerId, LogOperation.EDIT_FLIGHT.name(), "Removed " + removedRecords.get(i).toString() + " due to reduced available seats in " + FlightClassEnum.FIRST.name() + " class of " + flight.toString());
					flightClass.setAvailableSeats(0);
				}
			}
			break;
		default:
			this.logger.log(this.cityAcronym, LogOperation.UNKNOWN.name(), managerId + " : An error occured, flight was not edited");
			this.logger.log(managerId, LogOperation.UNKNOWN.name(), "An error occured, was flight was not edited");
			break;
		}
		
		return flight != null;
	}
	
	@Override
	public boolean transferReservation(String passengerRecordId, String currentCity, String otherCity) {
		String[] tokens = passengerRecordId.split("-");
		String managerId = tokens[0].toUpperCase();
		int recordId = Integer.parseInt(tokens[1]);
		
		City currentCityObject = new City(currentCity);
		City otherCityObject = new City(otherCity);
		
		try
		{
			final ExecutorService executorService = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
			PassengerRecord record = this.passengerRecordDb.getRecord(recordId);
			if(record == null){
				this.logger.log(this.cityAcronym, LogOperation.EDIT_FLIGHT.name(), managerId + ": Record " + recordId + " does not exist in the current city.");
				return false;
			}
			String queryCityAcronym = otherCityObject.getAcronym() + "2";
			FlightServerAddress fsa = null;
			for(FlightServerAddress s : otherServers){
				if(s.getName().equals(queryCityAcronym)){
					fsa = s;
				}
			}
			if(fsa == null){
				this.logger.log(this.cityAcronym, LogOperation.EDIT_FLIGHT.name(), managerId + ": Could not locate server.");
				return false;
			}
			final Future<Boolean> transferResult = executorService.submit(new TransferReservationTask(record, fsa));
			boolean result = transferResult.get();
			if(!result){
				this.logger.log(this.cityAcronym, LogOperation.EDIT_FLIGHT.name(), "Transfer reservation operation was not completed.");
			} else {
				this.passengerRecordDb.removeRecord(recordId);
			}
			return result;
		} catch (Exception e)
		{
			this.logger.log(this.cityAcronym, LogOperation.EDIT_FLIGHT.name(), e.getMessage());
			this.logger.log(managerId, LogOperation.EDIT_FLIGHT.name(), e.getMessage());
			return false;
		}
	}
	
	@Override
	public void serveRequests(float udpPort){		
		DatagramSocket socket = null;
		try
		{
			socket = new DatagramSocket((int)udpPort);
			while(true){
				byte[] buffer = new byte[BUFFER_SIZE];
				DatagramPacket request = new DatagramPacket(buffer, buffer.length);
				socket.receive(request);
				if(udpPort == 1100 || udpPort == 1101 || udpPort == 1102)
				{
					threadPool.execute(new BookedFlightCountHandler(socket, request, passengerRecordDb));	
				} else {
					threadPool.execute(new TransferReservationHandler(socket, request, passengerRecordDb, flightDb));	
				}
			}
		} catch (SocketException e)
		{
			this.logger.log(this.cityAcronym, LogOperation.SOCKET_EXCEPTION.name(), e.getMessage());
		} catch (IOException e)
		{
			this.logger.log(this.cityAcronym, LogOperation.IO_EXCEPTION.name(), e.getMessage());
		}finally {
			if (socket != null){
				socket.close();
			}
		}
	}
	
	@Override
	public void registerServer()
	{
		
	}

	@Override
	public void unregisterServer()
	{

	}

	@Override
	public String[] getManagerIds() {
		List<String> managerList = this.managerIds;
		int size = managerList.size();
		String[] managers = new String[size];
		for(int i = 0; i < size; ++i){
			managers[i] = managerList.get(i).toString();
		}
		return managers;
	}
	
	private List<PassengerRecord> correctSeatOverflow(int flightRecordId, FlightClassEnum flightClassEnum, int seatOverflow) {
		if (seatOverflow >= 0){
			return new ArrayList<PassengerRecord>();
		}
		
		int numOfRecords = -seatOverflow;
		return passengerRecordDb.removeRecords(flightRecordId, flightClassEnum, numOfRecords);
	}

	@Override
	public String[] getPassengerRecords() {
		List<PassengerRecord> passengerList = this.passengerRecordDb.getRecords();
		int size = passengerList.size();
		String[] passengers = new String[size];
		for(int i = 0; i < size; ++i){
			passengers[i] = passengerList.get(i).toString();
		}
		return passengers;
	}
}
