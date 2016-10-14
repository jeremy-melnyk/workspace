package database;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import concurrent.ConcurrentObject;
import enums.FlightClassEnum;
import models.Flight;
import models.Passenger;
import models.PassengerRecord;

public class PassengerRecordDb extends ConcurrentObject implements IPassengerRecordDb
{
	private int RECORD_ID = 0;
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
	public int numberOfRecords(FlightClassEnum flightClassEnum)
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
					FlightClassEnum recordFlightClassEnum = record.getFlightClassEnum();
					if(recordFlightClassEnum.equals(flightClassEnum)){
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
	public boolean addRecord(PassengerRecord passengerRecord) throws Exception
	{
		if(passengerRecord == null){
			return false;
		}
		
		Passenger passenger = passengerRecord.getPassenger();
		String lastName = passenger.getLastName();
		if(lastName.length() < 1){
			throw new IllegalArgumentException("Passenger last name was empty");
		}
		
		char firstLetter = (char) lastName.charAt(0);
		
		HashMap<Integer, PassengerRecord> innerRecords = null;
		requestWrite();
		try{
			boolean containsRecord = this.outerRecords.containsKey(firstLetter);
			if(!containsRecord){
				innerRecords = new HashMap<Integer, PassengerRecord>();
				this.outerRecords.put(firstLetter, innerRecords);
			} else {
				innerRecords = this.outerRecords.get(firstLetter);
			}
		} finally {
			releaseWrite();
		}
		
		requestWrite();
		try{
			if(!innerRecords.containsValue(passengerRecord)){
				int recordId = this.RECORD_ID++;
				passengerRecord.setRecordId(recordId);
				innerRecords.put(recordId, passengerRecord);
				return true;
			}
			throw new Exception("[FAILED] Already exists : " + passengerRecord);
		} finally{
			releaseWrite();	
		}
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
	public List<PassengerRecord> getRecords(FlightClassEnum flightClassEnum)
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
					FlightClassEnum recordFlightClassEnum = record.getFlightClassEnum();
					if(recordFlightClassEnum.equals(flightClassEnum)){
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
				Iterator<HashMap.Entry<Integer,PassengerRecord>> iterator = innerRecords.entrySet().iterator();
				while (iterator.hasNext()) {
					HashMap.Entry<Integer,PassengerRecord> entry = iterator.next();
					PassengerRecord record = entry.getValue();
					passengerRecords.add(record);		
			    	iterator.remove();
				}
			}
		} finally{
			releaseWrite();	
		} 
		return passengerRecords;
	}

	@Override
	public List<PassengerRecord> removeRecords(FlightClassEnum flightClassEnum)
	{
		List<PassengerRecord> passengerRecords = new ArrayList<PassengerRecord>();
		requestWrite();
		try{
			Set<Character> outerKeys = this.outerRecords.keySet();
			for (Character outerKey : outerKeys)
			{
				HashMap<Integer, PassengerRecord> innerRecords = this.outerRecords.get(outerKey);
				Iterator<HashMap.Entry<Integer,PassengerRecord>> iterator = innerRecords.entrySet().iterator();
				while (iterator.hasNext()) {
					HashMap.Entry<Integer,PassengerRecord> entry = iterator.next();
					PassengerRecord record = entry.getValue();
					FlightClassEnum recordFlightClassEnum = record.getFlightClassEnum();
					if(recordFlightClassEnum.equals(flightClassEnum)){
						passengerRecords.add(record);
						iterator.remove();
					}
				}
			}
		} finally{
			releaseWrite();	
		} 
		return passengerRecords;
	}
	
	@Override
	public List<PassengerRecord> removeRecords(int flightRecordId, int numOfRecords)
	{
		List<PassengerRecord> passengerRecords = new ArrayList<PassengerRecord>();
		requestWrite();
		try{
			Set<Character> outerKeys = this.outerRecords.keySet();
			for (Character outerKey : outerKeys)
			{
				HashMap<Integer, PassengerRecord> innerRecords = this.outerRecords.get(outerKey);
				Iterator<HashMap.Entry<Integer,PassengerRecord>> iterator = innerRecords.entrySet().iterator();
				while (iterator.hasNext()) {
					HashMap.Entry<Integer,PassengerRecord> entry = iterator.next();
					PassengerRecord record = entry.getValue();
					Flight flight = record.getFlight();
					if(numOfRecords > 0 && flight.getRecordId() == flightRecordId){
						--numOfRecords;
						passengerRecords.add(record);		
				    	iterator.remove();	
					}
				}
			}
		} finally{
			releaseWrite();	
		} 
		return passengerRecords;
	}
	
	@Override
	public List<PassengerRecord> removeRecords(int flightRecordId)
	{
		List<PassengerRecord> passengerRecords = new ArrayList<PassengerRecord>();
		requestWrite();
		try{
			Set<Character> outerKeys = this.outerRecords.keySet();
			for (Character outerKey : outerKeys)
			{
				HashMap<Integer, PassengerRecord> innerRecords = this.outerRecords.get(outerKey);
				Iterator<HashMap.Entry<Integer,PassengerRecord>> iterator = innerRecords.entrySet().iterator();
				while (iterator.hasNext()) {
					HashMap.Entry<Integer,PassengerRecord> entry = iterator.next();
					PassengerRecord record = entry.getValue();
					Flight flight = record.getFlight();
					if(flight.getRecordId() == flightRecordId){
						passengerRecords.add(record);		
				    	iterator.remove();	
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
		List<PassengerRecord> passengerRecords = new ArrayList<PassengerRecord>();
		requestWrite();
		try{
			HashMap<Integer, PassengerRecord> innerRecords = this.outerRecords.get(character);
			Set<Integer> innerKeys = innerRecords.keySet();
			for (Integer innerKey : innerKeys)
			{
				passengerRecords.add(innerRecords.remove(innerKey));
			}
		} finally{
			releaseWrite();	
		} 
		return passengerRecords;
	}
}
