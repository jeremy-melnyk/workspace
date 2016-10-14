package models;

import java.io.Serializable;
import java.util.Date;

import enums.FlightClassEnum;

public class FlightParameterValues implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private City destination;
	private Date date;
	private FlightClassParameterValues firstClass;
	private FlightClassParameterValues businessClass;
	private FlightClassParameterValues economyClass;

	public FlightParameterValues() {
		super();
	}
	
	public FlightParameterValues(City destination, Date date, int firstClassSeats, int businessClassSeats, int economyClassSeats) {
		super();
		this.destination = destination;
		this.date = date;
		this.firstClass = new FlightClassParameterValues(FlightClassEnum.FIRST, firstClassSeats, firstClassSeats);
		this.businessClass = new FlightClassParameterValues(FlightClassEnum.BUSINESS, businessClassSeats, businessClassSeats);
		this.economyClass = new FlightClassParameterValues(FlightClassEnum.ECONOMY, economyClassSeats, economyClassSeats);
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

	public FlightClassParameterValues getFirstClass() {
		return firstClass;
	}

	public void setFirstClass(FlightClassParameterValues firstClass) {
		this.firstClass = firstClass;
	}

	public FlightClassParameterValues getBusinessClass() {
		return businessClass;
	}

	public void setBusinessClass(FlightClassParameterValues businessClass) {
		this.businessClass = businessClass;
	}

	public FlightClassParameterValues getEconomyClass() {
		return economyClass;
	}

	public void setEconomyClass(FlightClassParameterValues economyClass) {
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
		FlightParameterValues other = (FlightParameterValues) obj;
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
		return true;
	}

	@Override
	public String toString() {
		return "FlightParameterValues [destination=" + destination + ", date=" + date + ", firstClass=" + firstClass
				+ ", businessClass=" + businessClass + ", economyClass=" + economyClass + "]";
	}
}
