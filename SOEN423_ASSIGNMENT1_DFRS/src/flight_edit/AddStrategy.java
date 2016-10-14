package flight_edit;

import database.IFlightDb;
import models.Flight;

public class AddStrategy extends FlightEditStrategy {
	protected IFlightDb flightDb;
	
	public AddStrategy(Flight flight, IFlightDb flightDb) {
		super(flight);
		this.flightDb = flightDb;
	}
	
	@Override
	public Object editFlight(Flight flight) {
		return this.flightDb.addFlight(flight);
	}
}
