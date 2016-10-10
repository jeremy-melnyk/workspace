package database;
import java.util.Date;
import java.util.List;

import enums.FlightClass;
import models.Flight;

public interface IFlightDb {
	
	public int numberOfFlights();
	public int numberOfFlights(FlightClass flightClass);
	
	public boolean addFlight(Flight flight);
	public Flight getFlight(int recordId);
	public Flight removeFlight(int recordId);
	public Flight editFlight(int recordId, Flight newFlight);
	
	public boolean acquireSeat(int recordId);
	public boolean releaseSeat(int recordId);
	
	public List<Flight> getFlights();
	public List<Flight> getAvailableFlights();
	public List<Flight> getFlights(FlightClass flightClass);
	public List<Flight> getFlights(Date date);
	
	public List<Flight> removeFlights();
	public List<Flight> removeFlights(FlightClass flightClass);
	public List<Flight> removeFlights(Date date);
}
