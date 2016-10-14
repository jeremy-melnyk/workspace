package flight_edit;

import models.City;
import models.Flight;

public class DestinationStrategy extends FlightEditStrategy {
	protected City destination;
	
	public DestinationStrategy(Flight flight, City destination) {
		super(flight);
		this.destination = destination;
	}

	@Override
	public Object editFlight(Flight flight) {
		flight.setDestination(destination);
		return true;
	}
}
