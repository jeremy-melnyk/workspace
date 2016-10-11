package database;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import concurrent.ConcurrentObject;
import enums.FlightClass;
import enums.FlightParameter;
import models.Flight;
import models.FlightParameterValues;

public class FlightDb extends ConcurrentObject implements IFlightDb
{
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
	public int numberOfFlights(FlightClass flightClass)
	{
		if (flightClass == null)
		{
			throw new IllegalArgumentException();
		}

		int count = 0;
		requestRead();
		try
		{
			Set<Integer> keys = this.flights.keySet();
			for (Integer key : keys)
			{
				Flight flight = this.flights.get(key);
				if (flight.getFlightClass() == flightClass)
				{
					++count;
				}
			}
		} finally
		{
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

		requestWrite();
		try
		{
			if (!this.flights.containsValue(flight))
			{
				int recordId = this.RECORD_ID++;
				flight.setRecordId(recordId);
				this.flights.put(recordId, flight);
				return true;
			}
			return false;
		} finally
		{
			releaseWrite();
		}
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
	public Flight editFlight(int recordId, FlightParameter flightParameter, FlightParameterValues flightParameters)
	{
		if (recordId < 0)
		{
			return null;
		}
		
		if (flightParameters == null)
		{
			return null;
		}
		
		requestWrite();
		try
		{
			Flight flight = this.flights.get(recordId);
			if(flight == null){
				return null;
			}
			switch(flightParameter){
			case DATE:
				flight.setDate(flightParameters.getDate());
				break;
			case DESTINATION:
				flight.setDestination(flightParameters.getDestination());
				break;
			case FLIGHTCLASS:
				flight.setFlightClass(flightParameters.getFlightClass());
				break;
			case SEATS:
				flight.setSeats(flightParameters.getSeats());
				break;
			default:
				break;		
			}
			return flight;
		} finally
		{
			releaseWrite();
		}
	}

	@Override
	public List<Flight> getFlights()
	{
		ArrayList<Flight> flights = null;
		requestRead();
		try
		{
			flights = new ArrayList<Flight>(this.flights.values());
		} finally
		{
			releaseRead();
		}
		return flights;
	}
	
	@Override
	public List<Flight> getAvailableFlights()
	{
		ArrayList<Flight> flights = new ArrayList<Flight>();
		requestRead();
		try
		{
			Set<Integer> keys = this.flights.keySet();
			for (Integer key : keys)
			{
				Flight flight = this.flights.get(key);
				if (flight.getAvailableSeats() > 0)
				{
					flights.add(flight);
				}
			}
		} finally
		{
			releaseRead();
		}
		return flights;
	}

	@Override
	public List<Flight> getFlights(FlightClass flightClass)
	{
		if (flightClass == null)
		{
			throw new IllegalArgumentException();
		}
		
		ArrayList<Flight> flights = new ArrayList<Flight>();
		requestRead();
		try
		{
			Set<Integer> keys = this.flights.keySet();
			for (Integer key : keys)
			{
				Flight flight = this.flights.get(key);
				if (flight.getFlightClass().equals(flightClass))
				{
					flights.add(flight);
				}
			}
		} finally
		{
			releaseRead();
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
		requestRead();
		try
		{
			Set<Integer> keys = this.flights.keySet();
			for (Integer key : keys)
			{
				Flight flight = this.flights.get(key);
				if (flight.getDate().equals(date))
				{
					flights.add(flight);
				}
			}
		} finally
		{
			releaseRead();
		}
		return flights;
	}

	@Override
	public List<Flight> removeFlights()
	{
		ArrayList<Flight> flights = new ArrayList<Flight>();
		requestWrite();
		try
		{
			Iterator<HashMap.Entry<Integer,Flight>> iterator = this.flights.entrySet().iterator();
			while (iterator.hasNext()) {
				HashMap.Entry<Integer,Flight> entry = iterator.next();
				Flight flight = entry.getValue();
				flights.add(flight);	
		    	iterator.remove();
			}
		} finally
		{
			releaseWrite();
		}
		return flights;
	}

	@Override
	public List<Flight> removeFlights(FlightClass flightClass)
	{
		ArrayList<Flight> flights = new ArrayList<Flight>();
		requestWrite();
		try
		{
			Iterator<HashMap.Entry<Integer,Flight>> iterator = this.flights.entrySet().iterator();
			while (iterator.hasNext()) {
				HashMap.Entry<Integer,Flight> entry = iterator.next();
				Flight flight = entry.getValue();
			    if(flight.getFlightClass().equals(flightClass)){
					flights.add(flight);	
			    	iterator.remove();
			    }
			}
		} finally
		{
			releaseWrite();
		}
		return flights;
	}

	@Override
	public List<Flight> removeFlights(Date date)
	{
		ArrayList<Flight> flights = new ArrayList<Flight>();
		requestWrite();
		try
		{
			Iterator<HashMap.Entry<Integer,Flight>> iterator = this.flights.entrySet().iterator();
			while (iterator.hasNext()) {
				HashMap.Entry<Integer,Flight> entry = iterator.next();
				Flight flight = entry.getValue();
			    if(flight.getDate().equals(date)){
					flights.add(flight);	
			    	iterator.remove();
			    }
			}
		} finally
		{
			releaseWrite();
		}
		return flights;
	}

	@Override
	public boolean acquireSeat(int recordId)
	{
		if (recordId < 0)
		{
			return false;
		}

		requestWrite();
		try
		{
			Flight flight = this.flights.get(recordId);
			return flight.acquireSeat();
		} finally
		{
			releaseWrite();
		}
	}

	@Override
	public boolean releaseSeat(int recordId)
	{
		if (recordId < 0)
		{
			return false;
		}

		requestWrite();
		try
		{
			Flight flight = this.flights.get(recordId);
			return flight.releaseSeat();
		} finally
		{
			releaseWrite();
		}
	}
}
