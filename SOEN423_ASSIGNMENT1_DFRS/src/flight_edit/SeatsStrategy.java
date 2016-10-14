package flight_edit;

import java.util.ArrayList;
import java.util.List;

import database.IPassengerRecordDb;
import models.Flight;
import models.FlightClass;
import models.PassengerRecord;

public abstract class SeatsStrategy extends FlightEditStrategy {
	protected int seats;
	protected IPassengerRecordDb passengerRecordDb;
	
	public SeatsStrategy(Flight flight, int seats, IPassengerRecordDb passengerRecordDb) {
		super(flight);
		this.setSeats(seats);
		this.passengerRecordDb = passengerRecordDb;
	}

	public int getSeats() {
		return seats;
	}

	public void setSeats(int seats) {
		this.seats = seats;
	}
	
	protected List<PassengerRecord> modifySeats(FlightClass flightClass)
	{
		flightClass.setSeats(seats);
		int seatOverflow = flightClass.getAvailableSeats();
		return correctSeatOverflow(seatOverflow);
	}
	
	protected List<PassengerRecord> correctSeatOverflow(int seatOverflow) {
		if (seatOverflow >= 0){
			return new ArrayList<PassengerRecord>();
		}
		
		int numOfRecords = -seatOverflow;
		int flightRecordId = flight.getRecordId();
		return passengerRecordDb.removeRecords(flightRecordId, numOfRecords);
	}
}
