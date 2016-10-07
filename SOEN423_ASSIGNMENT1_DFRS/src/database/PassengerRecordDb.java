package database;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import concurrent.ConcurrentDb;
import enums.FlightClass;
import models.Flight;
import models.Passenger;
import models.PassengerRecord;

public class PassengerRecordDb extends ConcurrentDb implements IPassengerRecordDb
{
	private int RECORD_ID;
	private HashMap<Character, HashMap<Integer, PassengerRecord>> outerRecords;
	
	public PassengerRecordDb() {
		super();
		this.RECORD_ID = 0;
		this.outerRecords = new HashMap<Character, HashMap<Integer, PassengerRecord>>();
	}

	@Override
	public int numberOfRecords()
	{
		int count = 0;
		requestRead();
		try{
			Set<Character> outerKeys = this.outerRecords.keySet();
			for (Character outerKey : outerKeys)
			{
				HashMap<Integer, PassengerRecord> innerRecords = this.outerRecords.get(outerKey);
				count += innerRecords.size();
			}
		} finally{
			releaseRead();	
		} 
		return count;
	}

	@Override
	public int numberOfRecords(FlightClass flightClass)
	{
		int count = 0;
		requestRead();
		try{
			Set<Character> outerKeys = this.outerRecords.keySet();
			for (Character outerKey : outerKeys)
			{
				HashMap<Integer, PassengerRecord> innerRecords = this.outerRecords.get(outerKey);
				Set<Integer> innerKeys = innerRecords.keySet();
				for (Integer innerKey : innerKeys)
				{
					PassengerRecord record = innerRecords.get(innerKey);
					Flight flight = record.getFlight();
					if(flight.getFlightClass().equals(flightClass)){
						++count;
					}
				}
			}
		} finally{
			releaseRead();	
		} 
		return count;
	}

	@Override
	public boolean addRecord(PassengerRecord passengerRecord)
	{
		if(passengerRecord == null){
			return false;
		}
		
		Passenger passenger = passengerRecord.getPassenger();
		String lastName = passenger.getLastName();
		if(lastName.length() < 1){
			return false;
		}
		
		char firstLetter = (char) lastName.charAt(0);
		// TODO : PassengerRecordDb can't acquire seat on flight if it is currently being modified by a manager.
		requestWrite();
		try{
			if(!this.outerRecords.containsKey(firstLetter)){
				HashMap<Integer, PassengerRecord> innerRecords = new HashMap<Integer, PassengerRecord>();
				Flight flight = passengerRecord.getFlight();
				if(flight.acquireSeat()){
					innerRecords.put(this.RECORD_ID++, passengerRecord);
					this.outerRecords.put(firstLetter, innerRecords);
					return true;
				}
			} else {
				HashMap<Integer, PassengerRecord> innerRecords = this.outerRecords.get(firstLetter);
				if(!innerRecords.containsValue(passengerRecord)){
					Flight flight = passengerRecord.getFlight();
					if(flight.acquireSeat()){
						innerRecords.put(this.RECORD_ID++, passengerRecord);
						return true;
					}
				}	
			}
		} finally{
			releaseWrite();	
		} 
		return false;
	}

	@Override
	public PassengerRecord getRecord(int recordId)
	{
		if(recordId < 0){
			return null;
		}
		
		requestRead();
		try{
			Set<Character> outerKeys = this.outerRecords.keySet();
			for (Character outerKey : outerKeys)
			{
				HashMap<Integer, PassengerRecord> innerRecords = this.outerRecords.get(outerKey);
				return innerRecords.get(recordId);
			}
		} finally{
			releaseRead();	
		} 
		return null;
	}

	@Override
	public PassengerRecord removeRecord(int recordId)
	{
		if(recordId < 0){
			return null;
		}
		
		requestWrite();
		try{
			Set<Character> outerKeys = this.outerRecords.keySet();
			for (Character outerKey : outerKeys)
			{
				HashMap<Integer, PassengerRecord> innerRecords = this.outerRecords.get(outerKey);
				return innerRecords.remove(recordId);
			}
		} finally{
			releaseWrite();	
		} 
		return null;
	}

	@Override
	public List<PassengerRecord> getRecords()
	{
		List<PassengerRecord> passengerRecords = new ArrayList<PassengerRecord>();
		requestRead();
		try{
			Set<Character> outerKeys = this.outerRecords.keySet();
			for (Character outerKey : outerKeys)
			{
				HashMap<Integer, PassengerRecord> innerRecords = this.outerRecords.get(outerKey);
				Set<Integer> innerKeys = innerRecords.keySet();
				for (Integer innerKey : innerKeys)
				{
					PassengerRecord record = innerRecords.get(innerKey);
					passengerRecords.add(record);
				}
			}
		} finally{
			releaseRead();	
		} 
		return passengerRecords;
	}
	
	@Override
	public List<PassengerRecord> getRecords(FlightClass flightClass)
	{
		List<PassengerRecord> passengerRecords = new ArrayList<PassengerRecord>();
		requestRead();
		try{
			Set<Character> outerKeys = this.outerRecords.keySet();
			for (Character outerKey : outerKeys)
			{
				HashMap<Integer, PassengerRecord> innerRecords = this.outerRecords.get(outerKey);
				Set<Integer> innerKeys = innerRecords.keySet();
				for (Integer innerKey : innerKeys)
				{
					PassengerRecord record = innerRecords.get(innerKey);
					Flight flight = record.getFlight();
					if(flight.getFlightClass().equals(flightClass)){
						passengerRecords.add(record);
					}
				}
			}
		} finally{
			releaseRead();	
		} 
		return passengerRecords;
	}

	@Override
	public List<PassengerRecord> getRecords(char character)
	{
		character = Character.toUpperCase(character);
		List<PassengerRecord> passengerRecords = null;
		requestRead();
		try{
			if(this.outerRecords.containsKey(character)){
				HashMap<Integer, PassengerRecord> innerRecords = this.outerRecords.get(character);
				passengerRecords = new ArrayList<PassengerRecord>(innerRecords.values());
			}
		} finally{
			releaseRead();	
		} 
		return passengerRecords;
	}

	@Override
	public List<PassengerRecord> removeRecords()
	{
		List<PassengerRecord> passengerRecords = new ArrayList<PassengerRecord>();
		requestWrite();
		try{
			Set<Character> outerKeys = this.outerRecords.keySet();
			for (Character outerKey : outerKeys)
			{
				HashMap<Integer, PassengerRecord> innerRecords = this.outerRecords.get(outerKey);
				Set<Integer> innerKeys = innerRecords.keySet();
				for (Integer innerKey : innerKeys)
				{
					PassengerRecord record = innerRecords.remove(innerKey);
					passengerRecords.add(record);
				}
			}
		} finally{
			releaseWrite();	
		} 
		return passengerRecords;
	}

	@Override
	public List<PassengerRecord> removeRecords(FlightClass flightClass)
	{
		List<PassengerRecord> passengerRecords = new ArrayList<PassengerRecord>();
		requestWrite();
		try{
			Set<Character> outerKeys = this.outerRecords.keySet();
			for (Character outerKey : outerKeys)
			{
				HashMap<Integer, PassengerRecord> innerRecords = this.outerRecords.get(outerKey);
				Set<Integer> innerKeys = innerRecords.keySet();
				for (Integer innerKey : innerKeys)
				{
					PassengerRecord record = innerRecords.get(innerKey);
					Flight flight = record.getFlight();
					if(flight.getFlightClass().equals(flightClass)){
						passengerRecords.add(innerRecords.remove(record));
					}
				}
			}
		} finally{
			releaseWrite();	
		} 
		return passengerRecords;
	}

	@Override	
	public List<PassengerRecord> removeRecords(char character)
	{
		character = Character.toUpperCase(character);
		List<PassengerRecord> passengerRecords = null;
		requestWrite();
		try{
			HashMap<Integer, PassengerRecord> innerRecords = this.outerRecords.remove(character);
			if(innerRecords != null){
				passengerRecords = new ArrayList<PassengerRecord>(innerRecords.values());
			}
		} finally{
			releaseWrite();	
		} 
		return passengerRecords;
	}
}
