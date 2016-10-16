package database;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import concurrent.ConcurrentObject;
import enums.FlightClassEnum;
import enums.FlightParameter;
import models.City;
import models.Flight;
import models.FlightClass;

public class FlightDb extends ConcurrentObject implements IFlightDb
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
		int count = 0;
		requestRead();
		try{
			count = this.flights.size();
		} finally{
			releaseRead();	
		} 
		return count;
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

		requestRead();
		try
		{
			return this.flights.get(recordId);
		} finally
		{
			releaseRead();
		}
	}

	@Override
	public Flight removeFlight(int recordId)
	{
		if (recordId < 0)
		{
			return null;
		}

		requestWrite();
		try
		{
			return this.flights.remove(recordId);
		} finally
		{
			releaseWrite();
		}
	}
	
	@Override
	public Flight editFlight(int recordId, FlightParameter flightParameter, Object newValue)
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
		
		
		Flight flight = null;
		requestRead();
		try
		{
			flight = this.flights.get(recordId);	
		} finally {
			releaseRead();
		}
		
		if (flight == null) {
			return null;
		}
		
		FlightClass flightClass = null;
		int seats = 0;
		switch (flightParameter) {
		case BUSINESS_CLASS_SEATS:
			seats = (int) newValue;
			flightClass = flight.getBusinessClass();
			synchronized (flightClass) {
				flightClass.setSeats(seats);
			}
			break;
		case DATE:
			Date date = (Date) newValue;
			synchronized (flight) {
				flight.setDate(date);
			}
			break;
		case DESTINATION:
			City destination = (City) newValue;
			synchronized (flight) {
				flight.setDestination(destination);
			}
			break;
		case ECONOMY_CLASS_SEATS:
			seats = (int) newValue;
			flightClass = flight.getEconomyClass();
			synchronized (flightClass) {
				flightClass.setSeats(seats);
			}
			break;
		case FIRST_CLASS_SEATS:
			seats = (int) newValue;
			flightClass = flight.getFirstClass();
			synchronized (flightClass) {
				flightClass.setSeats(seats);
			}
			break;
		default:
			break;
		}
		return flight;
	}

	@Override
	public List<Flight> getFlights()
	{
		Collection<Flight> flightCollection = null;
		requestRead();
		try
		{
			flightCollection = this.flights.values();
		} finally
		{
			releaseRead();
		}
		return new ArrayList<Flight>(flightCollection);
	}
	
	@Override
	public List<Flight> getAvailableFlights()
	{
		ArrayList<Flight> flights = new ArrayList<Flight>();
		Set<Integer> keys = null;
		requestRead();
		try
		{
			keys = this.flights.keySet();
		} finally {
			releaseRead();
		}
		
		for (Integer key : keys)
		{
			Flight flight = this.flights.get(key);
			if (flight.getFirstClass().getAvailableSeats() > 0 || flight.getBusinessClass().getAvailableSeats() > 0 || flight.getEconomyClass().getAvailableSeats() > 0)
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
		Set<Integer> keys = null;
		requestRead();
		try
		{
			keys = this.flights.keySet();
		} finally {
			releaseRead();
		}
		
		for (Integer key : keys)
		{
			Flight flight = this.flights.get(key);
			if (flight.getDate().equals(date))
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
		Iterator<HashMap.Entry<Integer,Flight>> iterator = null;
		requestRead();
		try
		{
			iterator = this.flights.entrySet().iterator();
		} finally {
			releaseRead();
		}
		
		while (iterator.hasNext()) {
			HashMap.Entry<Integer,Flight> entry = iterator.next();
			Flight flight = entry.getValue();
			flights.add(flight);
			requestWrite();
			try
			{
		    	iterator.remove();	
			} finally{
				releaseWrite();
			}
		}
		
		return flights;
	}

	@Override
	public List<Flight> removeFlights(Date date)
	{
		ArrayList<Flight> flights = new ArrayList<Flight>();
		Iterator<HashMap.Entry<Integer,Flight>> iterator = null;
		requestRead();
		try
		{
			iterator = this.flights.entrySet().iterator();
		} finally {
			releaseRead();
		}
		
		while (iterator.hasNext()) {
			HashMap.Entry<Integer,Flight> entry = iterator.next();
			Flight flight = entry.getValue();
			flights.add(flight);
			requestWrite();
		    if(flight.getDate().equals(date)){
				flights.add(flight);	
				requestWrite();
				try
				{
			    	iterator.remove();	
				} finally{
					releaseWrite();
				}
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

		Flight flight = null;
		requestRead();
		try{
			flight = this.flights.get(recordId);	
		} finally {
			releaseRead();
		}
		
		boolean result = false;
		FlightClass flightClass = null;
		switch(flightClassEnum)
		{
		case FIRST:
			flightClass = flight.getFirstClass();
			synchronized(flightClass)
			{
				result = flightClass.acquireSeat();
			}
			return result;
		case BUSINESS:
			flightClass = flight.getBusinessClass();
			synchronized(flightClass)
			{
				result = flightClass.acquireSeat();
			}
			return result;
		case ECONOMY:
			flightClass = flight.getEconomyClass();
			synchronized(flightClass)
			{
				result = flightClass.acquireSeat();
			}
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

		Flight flight = null;
		requestRead();
		try{
			flight = this.flights.get(recordId);	
		} finally {
			releaseRead();
		}
		
		boolean result = false;
		FlightClass flightClass = null;
		switch(flightClassEnum)
		{
		case FIRST:
			flightClass = flight.getFirstClass();
			synchronized(flightClass)
			{
				result = flightClass.releaseSeat();
			}
			return result;
		case BUSINESS:
			flightClass = flight.getBusinessClass();
			synchronized(flightClass)
			{
				result = flightClass.releaseSeat();
			}
			return result;
		case ECONOMY:
			flightClass = flight.getEconomyClass();
			synchronized(flightClass)
			{
				result = flightClass.releaseSeat();
			}
			return result;
		default:
			return false;			
		}
	}
}
