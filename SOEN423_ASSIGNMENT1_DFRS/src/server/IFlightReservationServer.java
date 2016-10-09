package server;

import java.util.List;
import java.rmi.Remote;
import java.rmi.RemoteException;

import enums.FlightClass;
import models.Address;
import models.Flight;
import models.FlightServerAddress;

public interface IFlightReservationServer extends Remote
{	
	public boolean bookFlight(String firstName, String lastName, Address address, String phoneNumber, Flight flight) throws RemoteException;
	public String getBookedFlightCount(FlightClass flightClass) throws RemoteException;
	public boolean editFlightRecord(Flight flight) throws RemoteException;
	
	public List<Flight> getFlights() throws RemoteException;
	public List<Flight> getAvailableFlights() throws RemoteException;

	public void serveRequests() throws RemoteException;
	public void registerServer() throws Exception;
	public void unregisterServer() throws Exception;
	public void setOtherServers(List<FlightServerAddress> otherServers) throws Exception;
}
