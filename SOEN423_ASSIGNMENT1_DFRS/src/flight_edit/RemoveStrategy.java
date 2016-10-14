package flight_edit;

import database.IFlightDb;
import models.Flight;

public class RemoveStrategy extends FlightEditStrategy {
	protected IFlightDb flightDb;
	
	public RemoveStrategy(Flight flight, IFlightDb flightDb) {
		super(flight);
		this.flightDb = flightDb;
	}
	
	@Override
	public Object editFlight(Flight flight) {
		int flightRecordId = flight.getRecordId();
		return this.flightDb.removeFlight(flightRecordId);
	}
}
