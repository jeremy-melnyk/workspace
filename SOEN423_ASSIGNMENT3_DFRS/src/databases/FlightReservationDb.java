package databases;

import enums.FlightClass;
import models.FlightRecord;
import models.FlightReservation;
import models.PassengerRecord;

public interface FlightReservationDb {
	public FlightReservation getFlightReservation(FlightClass flightClass, Character c, Integer id);
	public FlightReservation removeFlightReservation(FlightClass flightClass, Character c, Integer id);
	public FlightReservation[] removeFlightReservations(int flightRecordId);
	public FlightReservation[] removeFlightReservations(FlightClass flightClass, int count);
	public FlightReservation[] getFlightReservations();
	public FlightReservation addFlightReservation(FlightClass flightClass, PassengerRecord passengerRecord, FlightRecord flightRecord);
	int getFlightReservationCount(FlightClass flightClass);
}
