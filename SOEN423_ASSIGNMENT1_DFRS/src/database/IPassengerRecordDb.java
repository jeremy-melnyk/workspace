package database;
import java.util.List;

import enums.FlightClassEnum;
import models.PassengerRecord;

public interface IPassengerRecordDb {
	
	public int numberOfRecords();
	public int numberOfRecords(FlightClassEnum flightClass);
	
	public boolean addRecord(PassengerRecord passengerRecord) throws Exception;
	public PassengerRecord getRecord(int recordId);
	public PassengerRecord removeRecord(int recordId);
	
	public List<PassengerRecord> getRecords();
	public List<PassengerRecord> getRecords(FlightClassEnum flightClass);
	public List<PassengerRecord> getRecords(char character);
	
	public List<PassengerRecord> removeRecords();
	public List<PassengerRecord> removeRecords(int flightRecordId);
	public List<PassengerRecord> removeRecords(int flightRecordId, int numOfRecords);
	public List<PassengerRecord> removeRecords(FlightClassEnum flightClass);
	public List<PassengerRecord> removeRecords(char character);
}
