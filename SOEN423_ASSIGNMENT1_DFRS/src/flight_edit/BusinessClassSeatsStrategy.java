package flight_edit;

import database.IPassengerRecordDb;
import models.Flight;
import models.FlightClass;

public class BusinessClassSeatsStrategy extends SeatsStrategy {
	
	public BusinessClassSeatsStrategy(Flight flight, int seats, IPassengerRecordDb passengerRecordDb) {
		super(flight, seats, passengerRecordDb);
	}

	@Override
	public Object editFlight(Flight flight) {
		FlightClass flightClass = flight.getBusinessClass();
		return modifySeats(flightClass);
	}
}
