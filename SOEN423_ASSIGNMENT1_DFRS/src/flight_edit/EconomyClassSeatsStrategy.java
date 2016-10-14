package flight_edit;

import database.IPassengerRecordDb;
import models.Flight;
import models.FlightClass;

public class EconomyClassSeatsStrategy extends SeatsStrategy {
	
	public EconomyClassSeatsStrategy(Flight flight, int seats, IPassengerRecordDb passengerRecordDb) {
		super(flight, seats, passengerRecordDb);
	}

	@Override
	public Object editFlight(Flight flight) {
		FlightClass flightClass = flight.getEconomyClass();
		return modifySeats(flightClass);
	}
}
