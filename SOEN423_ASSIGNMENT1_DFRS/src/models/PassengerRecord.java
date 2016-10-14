package models;
import java.io.Serializable;
import java.util.Date;

import enums.FlightClassEnum;

public class PassengerRecord implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private int recordId;
	private Passenger passenger;
	private Flight flight;
	private FlightClassEnum flightClassEnum;
	private Date bookingDate;

	public PassengerRecord(Passenger passenger, Flight flight, FlightClassEnum flightClassEnum, Date bookingDate) {
		super();
		this.recordId = -1;
		this.passenger = passenger;
		this.flight = flight;
		this.flightClassEnum = flightClassEnum;
		this.bookingDate = bookingDate;
	}

	public int getRecordId() {
		return recordId;
	}

	public void setRecordId(int recordId) {
		this.recordId = recordId;
	}

	public Passenger getPassenger() {
		return passenger;
	}

	public void setPassenger(Passenger passenger) {
		this.passenger = passenger;
	}

	public Flight getFlight() {
		return flight;
	}

	public void setFlight(Flight flight) {
		this.flight = flight;
	}

	public FlightClassEnum getFlightClassEnum() {
		return flightClassEnum;
	}

	public void setFlightClassEnum(FlightClassEnum flightClassEnum) {
		this.flightClassEnum = flightClassEnum;
	}

	public Date getBookingDate() {
		return bookingDate;
	}

	public void setBookingDate(Date bookingDate) {
		this.bookingDate = bookingDate;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((bookingDate == null) ? 0 : bookingDate.hashCode());
		result = prime * result + ((flight == null) ? 0 : flight.hashCode());
		result = prime * result + ((flightClassEnum == null) ? 0 : flightClassEnum.hashCode());
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
		if (bookingDate == null) {
			if (other.bookingDate != null)
				return false;
		} else if (!bookingDate.equals(other.bookingDate))
			return false;
		if (flight == null) {
			if (other.flight != null)
				return false;
		} else if (!flight.equals(other.flight))
			return false;
		if (flightClassEnum != other.flightClassEnum)
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

	@Override
	public String toString() {
		return "PassengerRecord [recordId=" + recordId + ", passenger=" + passenger + ", flight=" + flight
				+ ", flightClassEnum=" + flightClassEnum + ", bookingDate=" + bookingDate + "]";
	}
}
