package database;
import java.util.List;

import enums.FlightClass;
import models.PassengerRecord;

public interface IPassengerRecordDb {
	
	public int numberOfRecords();
	public int numberOfRecords(FlightClass flightClass);
	
	public boolean addRecord(PassengerRecord passengerRecord);
	public PassengerRecord getRecord(int recordId);
	public PassengerRecord removeRecord(int recordId);
	
	public List<PassengerRecord> getRecords();
	public List<PassengerRecord> getRecords(FlightClass flightClass);
	public List<PassengerRecord> getRecords(char character);
	
	public List<PassengerRecord> removeRecords();
	public List<PassengerRecord> removeRecords(FlightClass flightClass);
	public List<PassengerRecord> removeRecords(char character);
}
