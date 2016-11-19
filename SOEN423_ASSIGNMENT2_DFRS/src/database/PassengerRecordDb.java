package database;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import enums.FlightClassEnum;
import models.Flight;
import models.Passenger;
import models.PassengerRecord;

public class PassengerRecordDb implements IPassengerRecordDb
{
	private Object recordCountLock = new Object();
	private int RECORD_ID = 0;
	private HashMap<Character, HashMap<Integer, PassengerRecord>> outerRecords;
	
	public PassengerRecordDb() {
		super();
		this.RECORD_ID = 0;
		this.outerRecords = new HashMap<Character, HashMap<Integer, PassengerRecord>>();
		this.initPassengerRecordsList();
	}

	@Override
	public int numberOfRecords()
	{
		int count = 0;
		Set<Character> outerKeys = this.outerRecords.keySet();		
		for (Character outerKey : outerKeys)
		{
			HashMap<Integer, PassengerRecord> innerRecords = this.outerRecords.get(outerKey);
			count += innerRecords.size();
		}
		
		return count;
	}

	@Override
	public int numberOfRecords(FlightClassEnum flightClassEnum)
	{
		int count = 0;
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
			throw new IllegalArgumentException("Passenger last name was empty");
		}
		
		char firstLetter = (char) lastName.charAt(0);
		firstLetter = Character.toUpperCase(firstLetter);
		HashMap<Integer, PassengerRecord> innerRecords = this.outerRecords.get(firstLetter);

		int recordId  = -1;
		synchronized(recordCountLock)
		{
			recordId = this.RECORD_ID++;	
		}
		passengerRecord.setRecordId(recordId);
		innerRecords.put(recordId, passengerRecord);
		return true;
	}

	@Override
	public PassengerRecord getRecord(int recordId)
	{
		if(recordId < 0){
			return null;
		}
		
		Set<Character> outerKeys = this.outerRecords.keySet();
		for (Character outerKey : outerKeys)
		{
			HashMap<Integer, PassengerRecord> innerRecords = this.outerRecords.get(outerKey);
			if (innerRecords.containsKey(recordId))
			{
				return innerRecords.remove(recordId);		
			}
		}
		return null;
	}

	@Override
	public PassengerRecord removeRecord(int recordId)
	{
		if(recordId < 0){
			return null;
		}
		
		Set<Character> outerKeys = this.outerRecords.keySet();
		for (Character outerKey : outerKeys)
		{
			HashMap<Integer, PassengerRecord> innerRecords = this.outerRecords.get(outerKey);
			if (innerRecords.containsKey(recordId))
			{
				return innerRecords.remove(recordId);		
			}
		}
		return null;
	}

	@Override
	public List<PassengerRecord> getRecords()
	{
		List<PassengerRecord> passengerRecords = new ArrayList<PassengerRecord>();
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
		return passengerRecords;
	}
	
	@Override
	public List<PassengerRecord> getRecords(FlightClassEnum flightClassEnum)
	{
		List<PassengerRecord> passengerRecords = new ArrayList<PassengerRecord>();
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
		return passengerRecords;
	}

	@Override
	public List<PassengerRecord> getRecords(char character)
	{
		character = Character.toUpperCase(character);
		List<PassengerRecord> passengerRecords = null;
		if(this.outerRecords.containsKey(character)){
			HashMap<Integer, PassengerRecord> innerRecords = this.outerRecords.get(character);
			passengerRecords = new ArrayList<PassengerRecord>(innerRecords.values());
		}
		return passengerRecords;
	}

	@Override
	public List<PassengerRecord> removeRecords()
	{
		List<PassengerRecord> passengerRecords = new ArrayList<PassengerRecord>();
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
		return passengerRecords;
	}

	@Override
	public List<PassengerRecord> removeRecords(FlightClassEnum flightClassEnum)
	{
		List<PassengerRecord> passengerRecords = new ArrayList<PassengerRecord>();
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
		return passengerRecords;
	}
	
	@Override
	public List<PassengerRecord> removeRecords(int flightRecordId, FlightClassEnum flightClassEnum, int numOfRecords)
	{
		List<PassengerRecord> passengerRecords = new ArrayList<PassengerRecord>();
		Set<Character> outerKeys = this.outerRecords.keySet();
		for (Character outerKey : outerKeys)
		{
			HashMap<Integer, PassengerRecord> innerRecords = this.outerRecords.get(outerKey);
			Iterator<HashMap.Entry<Integer,PassengerRecord>> iterator = innerRecords.entrySet().iterator();
			while (iterator.hasNext()) {
				HashMap.Entry<Integer,PassengerRecord> entry = iterator.next();
				PassengerRecord record = entry.getValue();
				Flight flight = record.getFlight();
				FlightClassEnum recordFlightClassEnum = record.getFlightClassEnum();
				int recordflightRecordId = flight.getRecordId();
				if(numOfRecords > 0 && flightRecordId == recordflightRecordId && recordFlightClassEnum.equals(flightClassEnum)){
					--numOfRecords;
					passengerRecords.add(record);		
			    	iterator.remove();	
				}
			}
		}
		return passengerRecords;
	}
	
	@Override
	public List<PassengerRecord> removeRecords(int flightRecordId)
	{
		List<PassengerRecord> passengerRecords = new ArrayList<PassengerRecord>();
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
		return passengerRecords;
	}

	@Override	
	public List<PassengerRecord> removeRecords(char character)
	{
		character = Character.toUpperCase(character);
		List<PassengerRecord> passengerRecords = new ArrayList<PassengerRecord>();
		HashMap<Integer, PassengerRecord> innerRecords = this.outerRecords.get(character);
		Set<Integer> innerKeys = innerRecords.keySet();
		for (Integer innerKey : innerKeys)
		{
			passengerRecords.add(innerRecords.remove(innerKey));
		}
		return passengerRecords;
	}
	
	private void initPassengerRecordsList()
	{
		char c = 'A';
		while(c != 'Z' + 1)
		{
			HashMap<Integer, PassengerRecord> innerRecords = new HashMap<Integer, PassengerRecord>();
			this.outerRecords.put(c, innerRecords);
			++c;
		}
	}
}
