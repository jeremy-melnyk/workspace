package flight_edit;

import models.Flight;

public abstract class FlightEditStrategy implements IFlightEditStrategy {
	protected Flight flight;

	public FlightEditStrategy(Flight flight) {
		super();
		this.flight = flight;
	}
	
	public Flight getFlight() {
		return flight;
	}

	public void setFlight(Flight flight) {
		this.flight = flight;
	}
}
