package models;

import java.io.Serializable;

import enums.FlightClass;

public class FlightClassOperation implements Serializable
{
	private static final long serialVersionUID = 1L;
	private String managerId;
	private FlightClass flightClass;

	public FlightClassOperation(String managerId, FlightClass flightClass) {
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

	public FlightClass getFlightClass()
	{
		return flightClass;
	}

	public void setFlightClass(FlightClass flightClass)
	{
		this.flightClass = flightClass;
	};
}
