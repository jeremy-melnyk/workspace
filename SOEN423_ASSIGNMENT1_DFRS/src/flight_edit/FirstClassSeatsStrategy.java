package flight_edit;

import database.PassengerRecordDb;
import models.Flight;
import models.FlightClass;

public class FirstClassSeatsStrategy extends SeatsStrategy {
	
	public FirstClassSeatsStrategy(Flight flight, int seats, PassengerRecordDb passengerRecordDb) {
		super(flight, seats, passengerRecordDb);
	}

	@Override
	public Object editFlight(Flight flight) {
		FlightClass flightClass = flight.getFirstClass();
		return modifySeats(flightClass);
	}
}
