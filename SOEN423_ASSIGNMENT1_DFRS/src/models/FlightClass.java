package models;

import enums.FlightClassEnum;

public class FlightClass {
	private FlightClassEnum flightClass;
	private int seats;
	private int availableSeats;
	
	public FlightClass(FlightClassEnum flightClass, int seats, int availableSeats) {
		super();
		this.flightClass = flightClass;
		this.seats = seats;
		this.availableSeats = availableSeats;
	}

	public FlightClassEnum getFlightClass() {
		return flightClass;
	}

	public void setFlightClass(FlightClassEnum flightClass) {
		this.flightClass = flightClass;
	}

	public int getSeats() {
		return seats;
	}

	public void setSeats(int seats) {
		if(seats < 0){
			seats = 0;
		}
		
		if(seats > this.seats){
			int numOfNewSeats = seats - this.seats;
			availableSeats += numOfNewSeats;
		} else if (seats < this.seats){
			int numOfReducedSeats = this.seats - seats;
			availableSeats -= numOfReducedSeats;
		}
		this.seats = seats;
	}

	public int getAvailableSeats() {
		return availableSeats;
	}

	public void setAvailableSeats(int availableSeats) {
		this.availableSeats = availableSeats;
	}
	
	public boolean acquireSeat(){
		if (this.availableSeats > 0){
			--this.availableSeats;
			return true;
		}
		return false;
	}
	
	public boolean releaseSeat(){
		if (this.availableSeats < this.seats){
			++this.availableSeats;
			return true;
		}
		return false;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + availableSeats;
		result = prime * result + ((flightClass == null) ? 0 : flightClass.hashCode());
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
		if (flightClass != other.flightClass)
			return false;
		if (seats != other.seats)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "FlightClass [flightClass=" + flightClass + ", seats=" + seats + ", availableSeats=" + availableSeats
				+ "]";
	}
	
}
