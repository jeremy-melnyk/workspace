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
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import database.IFlightDb;
import database.IPassengerRecordDb;
import enums.FlightClass;
import enums.FlightDbOperation;
import log.ILogger;
import models.Address;
import models.Flight;
import models.FlightClassOperation;
import models.FlightCountResult;
import models.FlightServerAddress;
import models.Passenger;
import models.PassengerRecord;
import models.RecordOperation;

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
	public boolean bookFlight(String firstName, String lastName, Address address, String phoneNumber, Flight flight) throws RemoteException
	{
		Passenger passenger = new Passenger(firstName, lastName, phoneNumber, address);
		Flight existingFlight = this.flightDb.getFlight(flight.getRecordId());
		if (existingFlight == null){
			throw new RemoteException("Flight no longer exists.");
		}
		int flightRecordId = existingFlight.getRecordId();
		if(this.flightDb.acquireSeat(flightRecordId)){
			PassengerRecord passengerRecord = new PassengerRecord(passenger, existingFlight, new Date());
			boolean result;
			try
			{
				result = this.passengerRecordDb.addRecord(passengerRecord);
				if(result){
					this.logger.log(this.cityAcronym, "Successfully booked PassengerRecord: " + passengerRecord);
				}
			} catch (Exception e)
			{
				this.logger.log(this.cityAcronym ,e.getMessage());
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
		FlightClass flightClass = flightClassOperation.getFlightClass();
		String managerId = flightClassOperation.getManagerId();
		try
		{
			final ExecutorService executorService = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
			final Future<FlightCountResult> flightCountFirst = executorService.submit(new BookedFlightCountTask(flightClass, otherServers.get(0)));
			final Future<FlightCountResult> flightCountSecond = executorService.submit(new BookedFlightCountTask(flightClass, otherServers.get(1)));
			int flightCountThird = this.passengerRecordDb.numberOfRecords(flightClass);
			builder.append(flightClass + " class results:\n");
			builder.append(cityAcronym + " " + flightCountThird + ", \n");
			FlightCountResult firstResult = flightCountFirst.get();
			FlightCountResult secondResult = flightCountSecond.get();
			builder.append(firstResult.getServerName() + " " + firstResult.getCount() + ", \n");
			builder.append(secondResult.getServerName() + " " + secondResult.getCount() + "\n");
			this.logger.log(managerId, "Booked flight count request succeeded");
			this.logger.log(this.cityAcronym, managerId + ": booked flight request succeeded");
			return builder.toString();
		} catch (Exception e)
		{
			this.logger.log(this.cityAcronym, e.getMessage());
			this.logger.log(managerId, e.getMessage());
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
	public boolean editFlightRecord(RecordOperation recordOperation, FlightDbOperation operation, Flight flight) throws RemoteException
	{
		//TODO Move logs to inside database.
		
		if (recordOperation == null){
			recordOperation = new RecordOperation("SERVER", -1);
		}
		if (operation == null){
			return false;
		}
		
		String managerId = recordOperation.getManagerId();
		switch(operation){
		case ADD:
			if (flight == null){
				return false;
			}
			boolean result =  this.flightDb.addFlight(flight);
			this.logger.log(this.cityAcronym, "Added flight: " + flight);
			this.logger.log(managerId, "Added flight: " + flight);
			return result;
		case EDIT:
			if (flight == null)
			{
				return false;
			}
			int recordIdToEdit = recordOperation.getRecordId();
			Flight editedFlight = this.flightDb.editFlight(recordIdToEdit, flight);
			this.logger.log(this.cityAcronym, "Edited flight: " + flight);
			this.logger.log(managerId, "Edited flight: " + flight);
			return editedFlight != null;
		case REMOVE:
			int recordIdToRemove = recordOperation.getRecordId();
			Flight removedFlight = this.flightDb.removeFlight(recordIdToRemove);
			this.logger.log(this.cityAcronym, "Removed flight: " + flight);
			this.logger.log(managerId, "Removed flight: " + flight);
			return removedFlight != null;
		default:
			this.logger.log(this.cityAcronym, "FlightDbOperation could not be determined " + operation);
			this.logger.log(managerId, "FlightDbOperation could not be determined " + operation);
			return false;
		}
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
			e.printStackTrace();
		} catch (IOException e)
		{
			e.printStackTrace();
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
	}

	@Override
	public void unregisterServer() throws Exception
	{
		Registry registry = LocateRegistry.getRegistry(this.rmiPort);
		registry.unbind(this.cityAcronym);
		UnicastRemoteObject.unexportObject(this, true);
	}

	@Override
	public List<String> getManagerIds() throws RemoteException
	{
		return managerIds;
	}
}
