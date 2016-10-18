package server;

import java.util.List;

import enums.FlightParameter;

import java.rmi.Remote;
import java.rmi.RemoteException;

import models.Address;
import models.Flight;
import models.FlightClassOperation;
import models.FlightServerAddress;
import models.FlightWithClass;
import models.FlightRecordOperation;

public interface IFlightReservationServer extends Remote
{	
	public boolean bookFlight(String firstName, String lastName, Address address, String phoneNumber, FlightWithClass flightWithClass) throws RemoteException;
	public String getBookedFlightCount(FlightClassOperation flightClassOperation) throws RemoteException;
	public boolean editFlightRecord(FlightRecordOperation recordOperation, FlightParameter flightParameter, Object newValue) throws RemoteException;
	
	public List<Flight> getFlights() throws RemoteException;
	public List<Flight> getAvailableFlights() throws RemoteException;
	public List<String> getManagerIds() throws RemoteException;

	public void serveRequests() throws RemoteException;
	public void registerServer() throws Exception;
	public void unregisterServer() throws Exception;
	public void setOtherServers(List<FlightServerAddress> otherServers) throws Exception;
}
