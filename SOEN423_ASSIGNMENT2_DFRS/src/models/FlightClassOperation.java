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
	
	public static FlightClassOperation toFlightClassOperation(String input){
		String[] tokens = input.split("-");
		
		String managerId = tokens[0];
		FlightClassEnum flightClassEnum = FlightClassEnum.toFlightClass(tokens[1]);
		
		return new FlightClassOperation(managerId, flightClassEnum);
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
