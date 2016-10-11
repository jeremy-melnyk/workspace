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

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((date == null) ? 0 : date.hashCode());
		result = prime * result + ((destination == null) ? 0 : destination.hashCode());
		result = prime * result + ((flightClass == null) ? 0 : flightClass.hashCode());
		result = prime * result + seats;
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FlightParameterValues other = (FlightParameterValues) obj;
		if (date == null)
		{
			if (other.date != null)
				return false;
		} else if (!date.equals(other.date))
			return false;
		if (destination == null)
		{
			if (other.destination != null)
				return false;
		} else if (!destination.equals(other.destination))
			return false;
		if (flightClass != other.flightClass)
			return false;
		if (seats != other.seats)
			return false;
		return true;
	}

	@Override
	public String toString()
	{
		return "FlightParameterValues [flightClass=" + flightClass + ", destination=" + destination + ", date=" + date
				+ ", seats=" + seats + "]";
	}
	
}
