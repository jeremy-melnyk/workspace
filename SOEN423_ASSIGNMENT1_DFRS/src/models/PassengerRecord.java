package models;
import java.util.Date;

public class PassengerRecord {

	private static int RECORD_ID = 0;
	private int recordId;
	private Passenger passenger;
	private City destination;
	private Flight flight;
	private Date date;

	public PassengerRecord(Passenger passenger, City destination, Flight flight, Date date) {
		super();
		this.recordId = RECORD_ID++;
		this.passenger = passenger;
		this.destination = destination;
		this.flight = flight;
		this.date = date;
	}

	public int getRecordId() {
		return this.recordId;
	}
	
	public Passenger getPassenger() {
		return passenger;
	}

	public void setPassenger(Passenger passenger) {
		this.passenger = passenger;
	}

	public City getDestination() {
		return destination;
	}

	public void setDestination(City destination) {
		this.destination = destination;
	}

	public Flight getFlight() {
		return flight;
	}

	public void setFlight(Flight flight) {
		this.flight = flight;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((date == null) ? 0 : date.hashCode());
		result = prime * result + ((destination == null) ? 0 : destination.hashCode());
		result = prime * result + ((flight == null) ? 0 : flight.hashCode());
		result = prime * result + ((passenger == null) ? 0 : passenger.hashCode());
		result = prime * result + recordId;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PassengerRecord other = (PassengerRecord) obj;
		if (date == null) {
			if (other.date != null)
				return false;
		} else if (!date.equals(other.date))
			return false;
		if (destination == null) {
			if (other.destination != null)
				return false;
		} else if (!destination.equals(other.destination))
			return false;
		if (flight == null) {
			if (other.flight != null)
				return false;
		} else if (!flight.equals(other.flight))
			return false;
		if (passenger == null) {
			if (other.passenger != null)
				return false;
		} else if (!passenger.equals(other.passenger))
			return false;
		if (recordId != other.recordId)
			return false;
		return true;
	}
	
}
