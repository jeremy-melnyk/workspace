package models;

import java.io.Serializable;

import enums.FlightDbOperation;

public class FlightRecordOperation implements Serializable
{
	private static final long serialVersionUID = 1L;
	private String managerId;
	private int recordId;
	private FlightDbOperation flightDbOperation;
	
	public FlightRecordOperation(String managerId, int recordId, FlightDbOperation flightDbOperation) {
		super();
		this.managerId = managerId;
		this.recordId = recordId;
		this.flightDbOperation = flightDbOperation;
	}
	
	public static FlightRecordOperation toFlightRecordOperation(String input){
		String[] tokens = input.split("-");
		
		String managerId = tokens[0];
		int recordId = Integer.parseInt(tokens[1]);
		FlightDbOperation flightDbOperation = FlightDbOperation.toFlightDbOperation(tokens[2]);
		
		return new FlightRecordOperation(managerId, recordId, flightDbOperation);
	}

	public String getManagerId()
	{
		return managerId;
	}
	public void setManagerId(String managerId)
	{
		this.managerId = managerId;
	}
	public int getRecordId()
	{
		return recordId;
	}
	public void setRecordId(int recordId)
	{
		this.recordId = recordId;
	}
	
	public FlightDbOperation getFlightDbOperation() {
		return flightDbOperation;
	}

	public void setFlightDbOperation(FlightDbOperation flightDbOperation) {
		this.flightDbOperation = flightDbOperation;
	}
}
