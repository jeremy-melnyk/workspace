package models;

import java.io.Serializable;
import java.util.Date;

import enums.FlightClass;

public class Flight implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private int recordId;
	private FlightClass flightClass;
	private City destination;
	private Date date;
	private int seats;
	private int availableSeats;

	public Flight(FlightClass flightClass, City destination, Date date, int seats) {
		super();
		this.recordId = -1;
		this.flightClass = flightClass;
		this.destination = destination;
		this.date = date;
		this.seats = seats;
		this.availableSeats = seats;
	}
	
	public Flight(FlightParameterValues flightParameters) {
		super();
		this.recordId = -1;
		this.flightClass = flightParameters.getFlightClass();
		this.destination = flightParameters.getDestination();
		this.date = flightParameters.getDate();
		this.seats = flightParameters.getSeats();
		this.availableSeats = this.seats;
	}

	public int getRecordId()
	{
		return recordId;
	}
	
	public void setRecordId(int recordId)
	{
		this.recordId = recordId;
	}
	
	public FlightClass getFlightClass()
	{
		return flightClass;
	}

	public void setFlightClass(FlightClass flightClass)
	{
		this.flightClass = flightClass;
	}

	public City getDestination()
	{
		return destination;
	}

	public void setDestination(City destination)
	{
		this.destination = destination;
	}
	
	public Date getDate()
	{
		return date;
	}

	public void setDate(Date date)
	{
		this.date = date;
	}

	public int getSeats()
	{
		return seats;
	}

	public void setSeats(int seats)
	{
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
	
	public int getAvailableSeats(){
		return availableSeats;
	}
	
	public void setAvailableSeats(int availableSeats){
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
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + availableSeats;
		result = prime * result + ((date == null) ? 0 : date.hashCode());
		result = prime * result + ((destination == null) ? 0 : destination.hashCode());
		result = prime * result + ((flightClass == null) ? 0 : flightClass.hashCode());
		result = prime * result + recordId;
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
		Flight other = (Flight) obj;
		if (availableSeats != other.availableSeats)
			return false;
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
		if (recordId != other.recordId)
			return false;
		if (seats != other.seats)
			return false;
		return true;
	}

	@Override
	public String toString()
	{
		return "Flight [recordId=" + recordId + ", flightClass=" + flightClass + ", destination=" + destination
				+ ", date=" + date + ", seats=" + seats + ", availableSeats=" + availableSeats + "]";
	}	
}
