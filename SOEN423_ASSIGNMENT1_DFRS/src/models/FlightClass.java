package models;

import java.io.Serializable;

import concurrent.ConcurrentObject;
import enums.FlightClassEnum;

public class FlightClass extends ConcurrentObject implements Serializable {
	private static final long serialVersionUID = 1L;
	private FlightClassEnum flightClassEnum;
	private int seats;
	private int availableSeats;

	public FlightClass(FlightClassEnum flightClassEnum, int seats, int availableSeats) {
		super();
		this.flightClassEnum = flightClassEnum;
		this.seats = seats;
		this.availableSeats = availableSeats;
	}

	public FlightClassEnum getFlightClassEnum() {
		requestRead();
		try {
			return flightClassEnum;
		} finally {
			releaseRead();
		}
	}

	public void setFlightClassEnum(FlightClassEnum flightClassEnum) {
		requestWrite();
		try {
			this.flightClassEnum = flightClassEnum;
		} finally {
			releaseWrite();
		}
	}

	public int getSeats() {
		requestRead();
		try {
			return seats;
		} finally {
			releaseRead();
		}
	}

	public void setSeats(int seats) {
		requestWrite();
		try {
			if (seats < 0) {
				seats = 0;
			}

			if (seats > this.seats) {
				int numOfNewSeats = seats - this.seats;
				availableSeats += numOfNewSeats;
			} else if (seats < this.seats) {
				int numOfReducedSeats = this.seats - seats;
				availableSeats -= numOfReducedSeats;
			}
			this.seats = seats;
		} finally {
			releaseWrite();
		}
	}

	public int getAvailableSeats() {
		requestRead();
		try {
			return availableSeats;
		} finally {
			releaseRead();
		}
	}

	public void setAvailableSeats(int availableSeats) {
		requestWrite();
		try {
			this.availableSeats = availableSeats;
		} finally {
			releaseWrite();
		}
	}

	public boolean acquireSeat() {
		requestWrite();
		try {
			if (this.availableSeats > 0) {
				--this.availableSeats;
				return true;
			}
			return false;
		} finally {
			releaseWrite();
		}
	}

	public boolean releaseSeat() {
		requestWrite();
		try {
			if (this.availableSeats < this.seats) {
				++this.availableSeats;
				return true;
			}
			return false;
		} finally {
			releaseWrite();
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + availableSeats;
		result = prime * result + ((flightClassEnum == null) ? 0 : flightClassEnum.hashCode());
		result = prime * result + seats;
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
		FlightClass other = (FlightClass) obj;
		if (availableSeats != other.availableSeats)
			return false;
		if (flightClassEnum != other.flightClassEnum)
			return false;
		if (seats != other.seats)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "FlightClass [flightClassEnum=" + flightClassEnum + ", seats=" + seats + ", availableSeats="
				+ availableSeats + "]";
	}
}
