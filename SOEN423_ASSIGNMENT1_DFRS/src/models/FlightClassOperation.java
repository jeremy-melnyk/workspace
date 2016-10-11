package models;

import java.io.Serializable;

import enums.FlightClassEnum;

public class FlightClassOperation implements Serializable
{
	private static final long serialVersionUID = 1L;
	private String managerId;
	private FlightClassEnum flightClass;

	public FlightClassOperation(String managerId, FlightClassEnum flightClass) {
		super();
		this.managerId = managerId;
		this.flightClass = flightClass;
	}

	public String getManagerId()
	{
		return managerId;
	}

	public void setManagerId(String managerId)
	{
		this.managerId = managerId;
	}

	public FlightClassEnum getFlightClass()
	{
		return flightClass;
	}

	public void setFlightClass(FlightClassEnum flightClass)
	{
		this.flightClass = flightClass;
	};
}
