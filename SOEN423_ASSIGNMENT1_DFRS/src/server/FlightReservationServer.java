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
import enums.FlightClass;
import models.Address;
import models.Flight;
import models.FlightServerAddress;
import models.Passenger;
import models.PassengerRecord;

public class FlightReservationServer implements IFlightReservationServer
{
	private final int BUFFER_SIZE = 1000;
    private final ExecutorService threadPool;
	private int rmiPort;
	private int udpPort;
	private List<FlightServerAddress> otherServers;
	private String cityAcronym;
	private IPassengerRecordDb passengerRecordDb;
	private IFlightDb flightDb;
	
	public FlightReservationServer(int rmiPort, int udpPort, int threadPoolSize, List<FlightServerAddress> otherServers, String cityAcronym, IPassengerRecordDb passengerRecordDb, IFlightDb flightDb) {
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
		
		this.threadPool = Executors.newFixedThreadPool(threadPoolSize);
		this.rmiPort = rmiPort;
		this.udpPort = udpPort;
		this.otherServers = otherServers;
		this.cityAcronym = cityAcronym;
		this.passengerRecordDb = passengerRecordDb;
		this.flightDb = flightDb;
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
			return this.passengerRecordDb.addRecord(passengerRecord);
		}
		return false;
	}
	
	@Override
	public void setOtherServers(List<FlightServerAddress> otherServers)
	{
		this.otherServers = otherServers;
	}

	@Override
	public int getBookedFlightCount(FlightClass flightClass) throws RemoteException
	{		
		int bookedFlightCount = 0;
		try
		{
			List<Future<Integer>> flightCounts = new ArrayList<Future<Integer>>();
			final ExecutorService executorService = Executors.newFixedThreadPool(3);
			for (FlightServerAddress flightServerAddress : this.otherServers){
				final Future<Integer> flightCount = executorService.submit(new BookedFlightCountTask(flightClass, flightServerAddress));
				flightCounts.add(flightCount);
			}
			bookedFlightCount += this.passengerRecordDb.numberOfRecords(flightClass);
			for(Future<Integer> flightCount : flightCounts){
				bookedFlightCount += flightCount.get().intValue();
			}
			return bookedFlightCount;
		} catch (Exception e)
		{
			e.printStackTrace();
			return 0;
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
	public boolean editFlightRecord(Flight flight) throws RemoteException
	{
		// TODO Implement
		if (flight == null){
			return false;
		}
		
		return this.flightDb.addFlight(flight);
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
}
