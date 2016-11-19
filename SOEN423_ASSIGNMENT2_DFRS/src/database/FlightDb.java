package database;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import enums.FlightClassEnum;
import enums.FlightParameter;
import models.City;
import models.Flight;
import models.FlightClass;

public class FlightDb implements IFlightDb
{
	private Object recordCountLock = new Object();
	private int RECORD_ID;
	private HashMap<Integer, Flight> flights;
	
	public FlightDb() {
		super();
		this.RECORD_ID = 0;
		this.flights = new HashMap<Integer, Flight>();
	}

	@Override
	public int numberOfFlights()
	{
		return this.flights.size();
	}

	@Override
	public boolean addFlight(Flight flight)
	{
		if (flight == null)
		{
			throw new IllegalArgumentException();
		}
		
		int recordId = -1;
		
		synchronized(recordCountLock)
		{
			recordId = RECORD_ID++;
		}
		
		flight.setRecordId(recordId);
		this.flights.put(recordId, flight);
		return true;
	}

	@Override
	public Flight getFlight(int recordId)
	{
		if (recordId < 0)
		{
			return null;
		}

		return this.flights.get(recordId);
	}

	@Override
	public Flight removeFlight(int recordId)
	{
		if (recordId < 0)
		{
			return null;
		}
		return this.flights.remove(recordId);
	}
	
	@Override
	public Flight editFlight(int recordId, FlightParameter flightParameter, String newValue)
	{
		if (recordId < 0)
		{
			return null;
		}
		
		if (flightParameter == null)
		{
			return null;
		}
		
		if (newValue == null)
		{
			return null;
		}
		
		
		Flight flight = this.flights.get(recordId);	
		if (flight == null) {
			return null;
		}
		
		FlightClass flightClass = null;
		int seats = 0;
		switch (flightParameter) {
		case BUSINESS_CLASS_SEATS:
			seats = Integer.parseInt(newValue);
			flightClass = flight.getBusinessClass();
			flightClass.setSeats(seats);
			break;
		case DATE:
			Date date = new Date(newValue);
			flight.setDate(date);
			break;
		case DESTINATION:
			City destination = new City(newValue);
			flight.setDestination(destination);
			break;
		case ECONOMY_CLASS_SEATS:
			seats = Integer.parseInt(newValue);
			flightClass = flight.getEconomyClass();
			flightClass.setSeats(seats);
			break;
		case FIRST_CLASS_SEATS:
			seats = Integer.parseInt(newValue);
			flightClass = flight.getFirstClass();
			flightClass.setSeats(seats);
			break;
		default:
			break;
		}
		return flight;
	}

	@Override
	public List<Flight> getFlights()
	{
		Collection<Flight> flightCollection = this.flights.values();
		return new ArrayList<Flight>(flightCollection);
	}
	
	@Override
	public List<Flight> getAvailableFlights()
	{
		ArrayList<Flight> flights = new ArrayList<Flight>();
		Set<Integer> keys = this.flights.keySet();	
		for (Integer key : keys)
		{
			Flight flight = this.flights.get(key);
			
			boolean flightIsAvailable = false;
			FlightClass firstClass = flight.getFirstClass();
			FlightClass businessClass = flight.getBusinessClass();
			FlightClass economyClass = flight.getEconomyClass();
			
			flightIsAvailable |= firstClass.getAvailableSeats() > 0;
			flightIsAvailable |= businessClass.getAvailableSeats() > 0;
			flightIsAvailable |= economyClass.getAvailableSeats() > 0;
			
			if(flightIsAvailable)
			{
				flights.add(flight);	
			}
		}
		
		return flights;
	}

	@Override
	public List<Flight> getFlights(Date date)
	{
		if (date == null)
		{
			throw new IllegalArgumentException();
		}
		
		ArrayList<Flight> flights = new ArrayList<Flight>();
		Set<Integer> keys = this.flights.keySet();
		
		for (Integer key : keys)
		{
			Flight flight = this.flights.get(key);
			Date flightDate = flight.getDate();
			if (flightDate.equals(date))
			{
				flights.add(flight);
			}
		}
		
		return flights;
	}

	@Override
	public List<Flight> removeFlights()
	{
		ArrayList<Flight> flights = new ArrayList<Flight>();
		Iterator<HashMap.Entry<Integer,Flight>> iterator = this.flights.entrySet().iterator();
		
		while (iterator.hasNext()) {
			HashMap.Entry<Integer,Flight> entry = iterator.next();
			Flight flight = entry.getValue();
			flights.add(flight);
	    	iterator.remove();
		}
		
		return flights;
	}

	@Override
	public List<Flight> removeFlights(Date date)
	{
		ArrayList<Flight> flights = new ArrayList<Flight>();
		Iterator<HashMap.Entry<Integer,Flight>> iterator = this.flights.entrySet().iterator();
		
		while (iterator.hasNext()) {
			HashMap.Entry<Integer,Flight> entry = iterator.next();
			Flight flight = entry.getValue();
		    if(flight.getDate().equals(date)){
				flights.add(flight);	
				iterator.remove();	
		    }
		}
		
		return flights;
	}

	@Override
	public boolean acquireSeat(int recordId, FlightClassEnum flightClassEnum)
	{
		if (recordId < 0)
		{
			return false;
		}

		Flight flight = this.flights.get(recordId);
		if (flight == null) {
			return false;
		}
		
		boolean result = false;
		FlightClass flightClass = null;
		switch(flightClassEnum)
		{
		case FIRST:
			flightClass = flight.getFirstClass();
			result = flightClass.acquireSeat();
			return result;
		case BUSINESS:
			flightClass = flight.getBusinessClass();
			result = flightClass.acquireSeat();
			return result;
		case ECONOMY:
			flightClass = flight.getEconomyClass();
			result = flightClass.acquireSeat();
			return result;
		default:
			return false;			
		}
	}

	@Override
	public boolean releaseSeat(int recordId, FlightClassEnum flightClassEnum)
	{
		if (recordId < 0)
		{
			return false;
		}

		Flight flight = this.flights.get(recordId);
		if (flight == null) {
			return false;
		}
		
		boolean result = false;
		FlightClass flightClass = null;
		switch(flightClassEnum)
		{
		case FIRST:
			flightClass = flight.getFirstClass();
			result = flightClass.releaseSeat();
			return result;
		case BUSINESS:
			flightClass = flight.getBusinessClass();
			result = flightClass.releaseSeat();
			return result;
		case ECONOMY:
			flightClass = flight.getEconomyClass();
			result = flightClass.releaseSeat();
			return result;
		default:
			return false;			
		}
	}
}
