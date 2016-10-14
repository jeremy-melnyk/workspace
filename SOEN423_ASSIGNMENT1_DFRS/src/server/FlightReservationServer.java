package server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import database.IFlightDb;
import database.IPassengerRecordDb;
import enums.FlightClassEnum;
import enums.FlightDbOperation;
import enums.FlightParameter;
import enums.LogOperation;
import log.ILogger;
import models.Address;
import models.Flight;
import models.FlightClass;
import models.FlightClassOperation;
import models.FlightCountResult;
import models.FlightServerAddress;
import models.FlightWithClass;
import models.Passenger;
import models.PassengerRecord;
import models.FlightRecordOperation;

public class FlightReservationServer implements IFlightReservationServer
{
	private final int BUFFER_SIZE = 1000;
	private final int THREAD_POOL_SIZE = 16;
    private final ExecutorService threadPool;
	private int rmiPort;
	private int udpPort;
	private List<FlightServerAddress> otherServers;
	private String cityAcronym;
	private IPassengerRecordDb passengerRecordDb;
	private IFlightDb flightDb;
	private List<String> managerIds;
	private ILogger logger;
	public FlightReservationServer(int rmiPort, int udpPort, int threadPoolSize, List<FlightServerAddress> otherServers, String cityAcronym, IPassengerRecordDb passengerRecordDb, IFlightDb flightDb, List<String> managerIds, ILogger logger) {
		if (rmiPort < 0)
		{
			throw new IllegalArgumentException();
		}
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
		this.rmiPort = rmiPort;
		this.udpPort = udpPort;
		this.otherServers = otherServers;
		this.cityAcronym = cityAcronym;
		this.passengerRecordDb = passengerRecordDb;
		this.flightDb = flightDb;
		this.managerIds = managerIds;
		this.logger = logger;
	}

	@Override
	public boolean bookFlight(String firstName, String lastName, Address address, String phoneNumber, FlightWithClass flightWithClass) throws RemoteException
	{
		Passenger passenger = new Passenger(firstName, lastName, phoneNumber, address);
		Flight flight = flightWithClass.getFlight();
		FlightClassEnum flightClassEnum = flightWithClass.getFlightClassEnum();
		Flight existingFlight = this.flightDb.getFlight(flight.getRecordId());
		if (existingFlight == null){
			this.logger.log(this.cityAcronym, LogOperation.BOOK_FLIGHT.name(), flight.toString() + " no longer exists!");
			return false;
		}
		int flightRecordId = existingFlight.getRecordId();
		boolean acquireSeatResult = this.flightDb.acquireSeat(flightRecordId, flightClassEnum);
		if(acquireSeatResult){
			PassengerRecord passengerRecord = new PassengerRecord(passenger, flight, flightClassEnum, new Date());
			boolean result;
			try
			{
				result = this.passengerRecordDb.addRecord(passengerRecord);
				if(result){
					this.logger.log(this.cityAcronym, LogOperation.BOOK_FLIGHT.name(), passengerRecord.toString());
				}
				return result;
			} catch (Exception e)
			{
				this.logger.log(this.cityAcronym, LogOperation.BOOK_FLIGHT.name(), e.getMessage());
			}
		}
		return false;
	}
	
	@Override
	public void setOtherServers(List<FlightServerAddress> otherServers)
	{
		this.otherServers = otherServers;
	}

	@Override
	public String getBookedFlightCount(FlightClassOperation flightClassOperation) throws RemoteException
	{		
		StringBuilder builder = new StringBuilder();
		FlightClassEnum flightClass = flightClassOperation.getFlightClass();
		String managerId = flightClassOperation.getManagerId();
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
	public List<Flight> getFlights() throws RemoteException
	{
		return this.flightDb.getFlights();
	}
	
	@Override
	public List<Flight> getAvailableFlights() throws RemoteException
	{
		return this.flightDb.getAvailableFlights();
	}
	
	@Override
	public boolean editFlightRecord(FlightRecordOperation flightRecordOperation, FlightParameter flightParameter, Object newValue) throws RemoteException
	{
		if (flightRecordOperation == null){
			throw new IllegalArgumentException();
		}
		if (flightParameter == null){
			throw new IllegalArgumentException();
		}
		
		FlightDbOperation flightDbOperation = flightRecordOperation.getFlightDbOperation();
		String managerId = flightRecordOperation.getManagerId();
		
		switch(flightDbOperation){
		case ADD:
			return addFlight(flightRecordOperation, newValue);
		case REMOVE:
			return removeFlight(flightRecordOperation);
		case EDIT:
			return editFlight(flightRecordOperation, flightParameter, newValue);
		default:
			this.logger.log(this.cityAcronym, LogOperation.UNKNOWN.name(), managerId + " : An error occured, nothing happened");
			this.logger.log(managerId, LogOperation.UNKNOWN.name(), "An error occured, nothing happened");
			return false;
		}		
	}

	private boolean addFlight(FlightRecordOperation flightRecordOperation, Object newValue) {
		String managerId = flightRecordOperation.getManagerId();
		if(newValue == null){
			this.logger.log(this.cityAcronym, LogOperation.ILLEGAL_ARGUMENT_EXCEPTION.name(), managerId + " : newValue was null");
			this.logger.log(managerId, LogOperation.ILLEGAL_ARGUMENT_EXCEPTION.name(), "newValue was null");
			return false;
		}
		
		Flight flight = (Flight) newValue;
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
	
	private boolean editFlight(FlightRecordOperation flightRecordOperation, FlightParameter flightParameter, Object newValue) {
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
		case DESTINATION:
			if(flight == null){
				this.logger.log(this.cityAcronym, LogOperation.ILLEGAL_ARGUMENT_EXCEPTION.name(), managerId + " : An error occured, flight was null");
				this.logger.log(managerId, LogOperation.ILLEGAL_ARGUMENT_EXCEPTION.name(), "An error occured, flight was null");
				return false;
			}
			this.logger.log(this.cityAcronym, LogOperation.EDIT_FLIGHT.name(), managerId + " : Destination was edited for " + flight.toString());
			this.logger.log(managerId, LogOperation.EDIT_FLIGHT.name(), "Destination was edited for " + flight.toString());
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
		default:
			this.logger.log(this.cityAcronym, LogOperation.UNKNOWN.name(), managerId + " : An error occured, flight was not edited");
			this.logger.log(managerId, LogOperation.UNKNOWN.name(), "An error occured, was flight was not edited");		
		}
		
		return flight != null;
	}
	
	private List<PassengerRecord> correctSeatOverflow(int flightRecordId, FlightClassEnum flightClassEnum, int seatOverflow) {
		if (seatOverflow >= 0){
			return new ArrayList<PassengerRecord>();
		}
		
		int numOfRecords = -seatOverflow;
		return passengerRecordDb.removeRecords(flightRecordId, flightClassEnum, numOfRecords);
	}
	
	@Override
	public void serveRequests(){		
		DatagramSocket socket = null;
		try
		{
			socket = new DatagramSocket(udpPort);
			while(true){
				byte[] buffer = new byte[BUFFER_SIZE];
				DatagramPacket request = new DatagramPacket(buffer, buffer.length);
				socket.receive(request);
				threadPool.execute(new BookedFlightCountHandler(socket, request, passengerRecordDb));
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
	public void registerServer() throws Exception
	{
		Remote remote = UnicastRemoteObject.exportObject(this, this.rmiPort);
		Registry registry = LocateRegistry.getRegistry(this.rmiPort);	
		registry.rebind(this.cityAcronym, remote);
		this.logger.log(this.cityAcronym, LogOperation.REGISTER_SERVER.name(), "Port: " + this.rmiPort);
	}

	@Override
	public void unregisterServer() throws Exception
	{
		Registry registry = LocateRegistry.getRegistry(this.rmiPort);
		registry.unbind(this.cityAcronym);
		UnicastRemoteObject.unexportObject(this, true);
		this.logger.log(this.cityAcronym, LogOperation.UNREGISTER_SERVER.name(), "Port: " + this.rmiPort);
	}

	@Override
	public List<String> getManagerIds() throws RemoteException
	{
		return managerIds;
	}
}
