import java.util.Date;

import database.IPassengerRecordDb;
import models.Address;
import models.City;
import models.Flight;
import models.Passenger;
import models.PassengerRecord;

public class FlightReservationServer {
	private IPassengerRecordDb passengerRecordDb;
	
	public FlightReservationServer(IPassengerRecordDb passengerRecordDb) {
		this.passengerRecordDb = passengerRecordDb;
	}

	public void bookFlight(String firstName, String lastName, Address address, String phoneNumber, City destination,
			Date date, Flight flight) {
		Passenger passenger = new Passenger(firstName, lastName, phoneNumber, address);
		PassengerRecord passengerRecord = new PassengerRecord(passenger, destination, flight, date);
		this.passengerRecordDb.addRecord(passengerRecord);
	}
}
