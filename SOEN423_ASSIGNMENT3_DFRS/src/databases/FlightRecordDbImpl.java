package databases;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import db_models.AddFlightRecord;
import enums.City;
import enums.FlightClass;
import models.FlightRecord;
import models.FlightSeats;

public class FlightRecordDbImpl implements FlightRecordDb {
	private int RECORD_ID = 0;
	private HashMap<Date, HashMap<Integer, FlightRecord>> records;
	private Lock idLock;

	public FlightRecordDbImpl() {
		this.records = new HashMap<Date, HashMap<Integer, FlightRecord>>();
		this.idLock = new ReentrantLock(true);
	}

	@Override
	public FlightRecord getFlightRecord(Date date, Integer id) {
		HashMap<Integer, FlightRecord> flightRecords = records.get(date);
		if(flightRecords == null){
			return null;
		}
		synchronized (flightRecords) {
			return flightRecords.get(id);
		}
	}
	
	@Override
	public FlightRecord getFlightRecord(Integer id) {
		for (Entry<Date, HashMap<Integer, FlightRecord>> entry : records.entrySet()) {
			HashMap<Integer, FlightRecord> flightRecords = entry.getValue();
			synchronized(flightRecords){
				if(flightRecords.containsKey(id)){
					return flightRecords.get(id);
				}
			}
		}
		
		return null;
	}

	@Override
	public FlightRecord[] getFlightRecords(Date date, FlightClass flightClass) {
		HashMap<Integer, FlightRecord> flightRecords = records.get(date);
		if(flightRecords == null){
			return null;
		}
		List<FlightRecord> flightRecordsList = new ArrayList<FlightRecord>();
		synchronized(flightRecords){
			for (Entry<Integer, FlightRecord> flightRecordEntry : flightRecords.entrySet()) {
				FlightRecord flightRecord = flightRecordEntry.getValue();
				HashMap<FlightClass, FlightSeats> flightClasses = flightRecord.getFlightClasses();
				if (flightClasses.containsKey(flightClass) && flightClasses.get(flightClass).getAvailableSeats() > 0){
					flightRecordsList.add(flightRecordEntry.getValue());	
				}
			}	
		}
		FlightRecord[] flightRecordsArray = new FlightRecord[flightRecordsList.size()];
		return flightRecordsList.toArray(flightRecordsArray);
	}

	@Override
	public FlightRecord[] getFlightRecords() {
		List<FlightRecord> flightRecordsList = new ArrayList<FlightRecord>();
		for (Entry<Date, HashMap<Integer, FlightRecord>> entry : records.entrySet()) {
			HashMap<Integer, FlightRecord> flightRecords = entry.getValue();
			synchronized(flightRecords){
				for (Entry<Integer, FlightRecord> flightRecordEntry : flightRecords.entrySet()) {
					flightRecordsList.add(flightRecordEntry.getValue());
				}	
			}
		}
		FlightRecord[] flightRecords = new FlightRecord[flightRecordsList.size()];
		return flightRecordsList.toArray(flightRecords);
	}

	@Override
	public FlightRecord removeFlightRecord(Date date, Integer id) {
		if(!records.containsKey(date)){
			return null;
		}
		HashMap<Integer, FlightRecord> flightRecords = records.get(date);
		synchronized (flightRecords) {
			return flightRecords.remove(id);
		}
	}
	
	@Override
	public FlightRecord removeFlightRecord(Integer id) {	
		for (Entry<Date, HashMap<Integer, FlightRecord>> entry : records.entrySet()) {
			HashMap<Integer, FlightRecord> flightRecords = entry.getValue();
			synchronized(flightRecords){
				if(flightRecords.containsKey(id)){
					return flightRecords.remove(id);
				}
			}
		}
		return null;
	}

	@Override
	public FlightRecord addFlightRecord(City origin, City destination, Date date,
			HashMap<FlightClass, FlightSeats> flightClasses) {
		int id = 0;
		idLock.lock();
		try {
			id = RECORD_ID++;
		} finally {
			idLock.unlock();
		}
		
		FlightRecord record = new FlightRecord(id, origin, destination, date, flightClasses);
		if (!records.containsKey(date)){
			addDate(date);
		}
		HashMap<Integer, FlightRecord> flightRecords = records.get(date);
		synchronized (flightRecords) {
			flightRecords.put(id, record);
			return record;
		}
	}
	
	@Override
	public FlightRecord addFlightRecord(AddFlightRecord addFlightRecord) {
		int id = 0;
		idLock.lock();
		try {
			id = RECORD_ID++;
		} finally {
			idLock.unlock();
		}
		
		City origin = addFlightRecord.getOrigin();
		City destination = addFlightRecord.getDestination();
		Date date = addFlightRecord.getDate();
		HashMap<FlightClass, FlightSeats> flightClasses = addFlightRecord.getFlightClasses();
		
		FlightRecord record = new FlightRecord(id, origin, destination, date, flightClasses);
		if (!records.containsKey(date)){
			addDate(date);
		}
		HashMap<Integer, FlightRecord> flightRecords = records.get(date);
		synchronized (flightRecords) {
			flightRecords.put(id, record);
			return record;
		}
	}
	
	private void addDate(Date date){
		synchronized(records){
			if (!records.containsKey(date)){
				HashMap<Integer, FlightRecord> flightRecords = new HashMap<Integer, FlightRecord>();
				records.put(date, flightRecords);
			}
		}
	}
}