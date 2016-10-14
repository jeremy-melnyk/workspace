package database;
import java.util.Date;
import java.util.List;

import enums.FlightClassEnum;
import enums.FlightParameter;
import models.Flight;

public interface IFlightDb {
	
	public int numberOfFlights();
	
	public boolean addFlight(Flight flight);
	public Flight getFlight(int recordId);
	public Flight removeFlight(int recordId);
	public Flight editFlight(int recordId, FlightParameter flightParameter, Object newValue);
	
	public boolean acquireSeat(int recordId, FlightClassEnum flightClassEnum);
	public boolean releaseSeat(int recordId, FlightClassEnum flightClassEnum);
	
	public List<Flight> getFlights();
	public List<Flight> getAvailableFlights();
	public List<Flight> getFlights(Date date);
	
	public List<Flight> removeFlights();
	public List<Flight> removeFlights(Date date);
}
