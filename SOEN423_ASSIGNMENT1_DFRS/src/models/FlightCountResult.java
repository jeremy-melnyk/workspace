package models;

public class FlightCountResult
{
	private String serverName;
	private int count;

	public FlightCountResult(String serverName, int count) {
		super();
		this.serverName = serverName;
		this.count = count;
	}

	public String getServerName()
	{
		return serverName;
	}

	public void setServerName(String serverName)
	{
		this.serverName = serverName;
	}

	public int getCount()
	{
		return count;
	}

	public void setCount(int count)
	{
		this.count = count;
	}
}
