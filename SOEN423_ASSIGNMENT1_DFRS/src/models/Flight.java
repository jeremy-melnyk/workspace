package models;

import java.io.Serializable;
import java.util.Date;

import enums.FlightClassEnum;

public class Flight implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private int recordId;
	private City destination;
	private Date date;
	private FlightClass firstClass;
	private FlightClass businessClass;
	private FlightClass economyClass;

	public Flight(City destination, Date date, int firstClassSeats, int businessClassSeats, int economyClassSeats) {
		super();
		this.recordId = -1;
		this.destination = destination;
		this.date = date;
		this.firstClass = new FlightClass(FlightClassEnum.FIRST, firstClassSeats, firstClassSeats);
		this.businessClass = new FlightClass(FlightClassEnum.BUSINESS, businessClassSeats, businessClassSeats);
		this.economyClass = new FlightClass(FlightClassEnum.ECONOMY, economyClassSeats, economyClassSeats);
	}
	
	public Flight(FlightParameterValues flightParameterValues) {
		super();
		this.recordId = -1;
		this.destination = flightParameterValues.getDestination();
		this.date = flightParameterValues.getDate();
		
		FlightClassParameterValues firstClassValues = flightParameterValues.getFirstClass();
		FlightClassParameterValues businessClassValues = flightParameterValues.getBusinessClass();
		FlightClassParameterValues economyClassValues = flightParameterValues.getEconomyClass();
		
		this.firstClass = new FlightClass(FlightClassEnum.FIRST, firstClassValues.getSeats(), firstClassValues.getAvailableSeats());
		this.businessClass = new FlightClass(FlightClassEnum.BUSINESS, businessClassValues.getSeats(), businessClassValues.getAvailableSeats());
		this.economyClass = new FlightClass(FlightClassEnum.ECONOMY, economyClassValues.getSeats(), economyClassValues.getAvailableSeats());
	}

	public int getRecordId()
	{
		return recordId;
	}
	
	public void setRecordId(int recordId)
	{
		this.recordId = recordId;
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

	public FlightClass getFirstClass() {
		return firstClass;
	}

	public void setFirstClass(FlightClass firstClass) {
		this.firstClass = firstClass;
	}

	public FlightClass getBusinessClass() {
		return businessClass;
	}

	public void setBusinessClass(FlightClass businessClass) {
		this.businessClass = businessClass;
	}

	public FlightClass getEconomyClass() {
		return economyClass;
	}

	public void setEconomyClass(FlightClass economyClass) {
		this.economyClass = economyClass;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((businessClass == null) ? 0 : businessClass.hashCode());
		result = prime * result + ((date == null) ? 0 : date.hashCode());
		result = prime * result + ((destination == null) ? 0 : destination.hashCode());
		result = prime * result + ((economyClass == null) ? 0 : economyClass.hashCode());
		result = prime * result + ((firstClass == null) ? 0 : firstClass.hashCode());
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
		Flight other = (Flight) obj;
		if (businessClass == null) {
			if (other.businessClass != null)
				return false;
		} else if (!businessClass.equals(other.businessClass))
			return false;
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
		if (economyClass == null) {
			if (other.economyClass != null)
				return false;
		} else if (!economyClass.equals(other.economyClass))
			return false;
		if (firstClass == null) {
			if (other.firstClass != null)
				return false;
		} else if (!firstClass.equals(other.firstClass))
			return false;
		if (recordId != other.recordId)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Flight [recordId=" + recordId + ", destination=" + destination + ", date=" + date + ", firstClass="
				+ firstClass + ", businessClass=" + businessClass + ", economyClass=" + economyClass + "]";
	}	
}
