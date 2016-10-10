package models;

import java.io.Serializable;

public class RecordOperation implements Serializable
{
	private static final long serialVersionUID = 1L;
	private String managerId;
	private int recordId;
	
	public RecordOperation(String managerId, int recordId) {
		super();
		this.managerId = managerId;
		this.recordId = recordId;
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
}
