package models;

import java.io.Serializable;
import java.util.Date;

import enums.FlightClass;

public class FlightParameterValues implements Serializable{
	private static final long serialVersionUID = 1L;
	private FlightClass flightClass;
	private City destination;
	private Date date;
	private int seats;
	
	public FlightParameterValues() {
		super();
		// TODO Auto-generated constructor stub
	}
		
	public FlightParameterValues(FlightClass flightClass, City destination, Date date, int seats) {
		super();
		this.flightClass = flightClass;
		this.destination = destination;
		this.date = date;
		this.seats = seats;
	}

	public FlightClass getFlightClass() {
		return flightClass;
	}
	public void setFlightClass(FlightClass flightClass) {
		this.flightClass = flightClass;
	}
	public City getDestination() {
		return destination;
	}
	public void setDestination(City destination) {
		this.destination = destination;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public int getSeats() {
		return seats;
	}
	public void setSeats(int seats) {
		this.seats = seats;
	}
}
