package database;
import java.util.List;

import models.Passenger;
import models.PassengerRecord;

public interface IPassengerRecordDb {
	public boolean addRecord(PassengerRecord passengerRecord);
	
	public PassengerRecord retrieveRecord(int recordId);
	public PassengerRecord removeRecord(int recordId);
	
	public List<PassengerRecord> retrieveRecords(Passenger passenger);
	public List<PassengerRecord> retrieveRecords(char character);
	public List<PassengerRecord> removeRecords(Passenger passenger);
}
