package models;

public class FlightServerAddress
{
	private String name;
	private int udpPort;
	private String host;

	public FlightServerAddress(String name, int udpPort, String host) {
		super();
		this.name = name;
		this.udpPort = udpPort;
		this.host = host;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public int getUdpPort()
	{
		return udpPort;
	}

	public void setUdpPort(int udpPort)
	{
		this.udpPort = udpPort;
	}

	public String getHost()
	{
		return host;
	}

	public void setHost(String host)
	{
		this.host = host;
	}
}
