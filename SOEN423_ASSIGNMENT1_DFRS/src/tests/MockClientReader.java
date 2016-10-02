package tests;

import client.Client;
import database.IPassengerRecordDb;
import models.PassengerRecord;

public class MockClientReader extends Client implements Runnable {
	private IPassengerRecordDb passengerRecordDb;
	
	public MockClientReader(IPassengerRecordDb passengerRecordDb){
		this.passengerRecordDb = passengerRecordDb;
	}

	@Override
	public void run() {
		for(int i = 0; i < 100; ++i){
			PassengerRecord passengerRecord = this.passengerRecordDb.retrieveRecord(i);
			if(passengerRecord != null){
				System.out.print("Retrieved: " + passengerRecord.getRecordId() + " : " + passengerRecord.getPassenger().getLastName() + ", " + passengerRecord.getPassenger().getFirstName() + "\n");
			}
		}	
	}
}
