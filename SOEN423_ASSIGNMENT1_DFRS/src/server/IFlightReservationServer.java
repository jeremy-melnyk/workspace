package server;

import java.util.List;
import java.rmi.Remote;
import java.rmi.RemoteException;

import enums.FlightClass;
import models.Address;
import models.Flight;

public interface IFlightReservationServer extends Remote
{
	public boolean bookFlight(String firstName, String lastName, Address address, String phoneNumber, Flight flight) throws RemoteException;
	public int getBookedFlightCount(FlightClass flightClass) throws RemoteException;
	
	public List<Flight> getFlights() throws RemoteException;
	public List<Flight> getAvailableFlights() throws RemoteException;
	public boolean addFlight(Flight flight) throws RemoteException;

	public void registerServer() throws Exception;
	public void unregisterServer() throws Exception;
}
