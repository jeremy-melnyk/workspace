package server;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Date;
import java.util.List;

import database.IFlightDb;
import database.IPassengerRecordDb;
import enums.FlightClass;
import models.Address;
import models.Flight;
import models.Passenger;
import models.PassengerRecord;

public class FlightReservationServer implements IFlightReservationServer
{
	private int port;
	private String cityAcronym;
	private IPassengerRecordDb passengerRecordDb;
	private IFlightDb flightDb;

	public FlightReservationServer(int port, String cityAcronym, IPassengerRecordDb passengerRecordDb, IFlightDb flightDb) {
		if (port < 0)
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
		
		this.port = port;
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
	public int getBookedFlightCount(FlightClass flightClass) throws RemoteException
	{
		return this.passengerRecordDb.numberOfRecords(flightClass);
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
	public boolean addFlight(Flight flight) throws RemoteException
	{
		if (flight == null){
			return false;
		}
		
		return this.flightDb.addFlight(flight);
	}
	
	@Override
	public void registerServer() throws Exception
	{
		Remote remote = UnicastRemoteObject.exportObject(this, this.port);
		Registry registry = LocateRegistry.getRegistry(this.port);	
		registry.rebind(this.cityAcronym, remote);
	}

	@Override
	public void unregisterServer() throws Exception
	{
		Registry registry = LocateRegistry.getRegistry(this.port);	
		registry.unbind(this.cityAcronym);
		UnicastRemoteObject.unexportObject(this, true);
	}
}
