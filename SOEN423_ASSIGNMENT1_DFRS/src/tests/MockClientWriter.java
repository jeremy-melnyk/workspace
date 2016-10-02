package tests;

import java.util.Date;

import client.Client;
import database.IPassengerRecordDb;
import models.Address;
import models.City;
import models.Flight;
import models.FirstClassFlight;
import models.Passenger;
import models.PassengerRecord;

public class MockClientWriter extends Client implements Runnable {
	private IPassengerRecordDb passengerRecordDb;
	
	public MockClientWriter(IPassengerRecordDb passengerRecordDb){
		this.passengerRecordDb = passengerRecordDb;
	}

	@Override
	public void run() {
		for(int i = 0; i < 100; ++i){
			Address address = new Address("RandomStreet", "RandomCity", "RandomProvince","H8Z4P2", "Canada");
			Passenger passenger = new Passenger("John", "Doe", "5141231234", address);
			City city = new City("Montreal", "MTL");
			Date date = new Date();
			Flight flight = new FirstClassFlight(null, 10);
			PassengerRecord passengerRecord = new PassengerRecord(passenger, city, flight, date);
			if(this.passengerRecordDb.addRecord(passengerRecord)){
				System.out.print("Added: " + passengerRecord.getRecordId() + " : " + passengerRecord.getPassenger().getLastName() + ", " + passengerRecord.getPassenger().getFirstName() + "\n");	
			}
		}	
	}
}
