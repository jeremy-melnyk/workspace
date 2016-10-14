package flight_edit;

import java.util.Date;

import models.Flight;

public class DateStrategy extends FlightEditStrategy {
	protected Date date;
	
	public DateStrategy(Flight flight, Date date) {
		super(flight);
		this.date = date;
	}

	@Override
	public Object editFlight(Flight flight) {
		flight.setDate(date);
		return true;
	}
}
