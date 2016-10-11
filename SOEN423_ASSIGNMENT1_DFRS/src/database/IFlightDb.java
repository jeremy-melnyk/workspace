package database;
import java.util.Date;
import java.util.List;

import enums.FlightClassEnum;
import enums.FlightParameter;
import models.Flight;
import models.FlightParameterValues;

public interface IFlightDb {
	
	public int numberOfFlights();
	public int numberOfFlights(FlightClassEnum flightClass);
	
	public boolean addFlight(Flight flight);
	public Flight getFlight(int recordId);
	public Flight removeFlight(int recordId);
	public Flight editFlight(int recordId, FlightParameter flightParameter, FlightParameterValues flightParameters);
	
	public boolean acquireSeat(int recordId);
	public boolean releaseSeat(int recordId);
	
	public List<Flight> getFlights();
	public List<Flight> getAvailableFlights();
	public List<Flight> getFlights(FlightClassEnum flightClass);
	public List<Flight> getFlights(Date date);
	
	public List<Flight> removeFlights();
	public List<Flight> removeFlights(FlightClassEnum flightClass);
	public List<Flight> removeFlights(Date date);
}
