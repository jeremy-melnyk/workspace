package models;

import java.io.Serializable;

import enums.FlightClassEnum;

public class FlightClassOperation implements Serializable {
	private static final long serialVersionUID = 1L;
	private String managerId;
	private FlightClassEnum flightClassEnum;
	
	public FlightClassOperation(String managerId, FlightClassEnum flightClassEnum) {
		super();
		this.managerId = managerId;
		this.flightClassEnum = flightClassEnum;
	}

	public String getManagerId() {
		return managerId;
	}

	public void setManagerId(String managerId) {
		this.managerId = managerId;
	}

	public FlightClassEnum getFlightClassEnum() {
		return flightClassEnum;
	}

	public void setFlightClassEnum(FlightClassEnum flightClassEnum) {
		this.flightClassEnum = flightClassEnum;
	}
}
