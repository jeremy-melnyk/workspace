package models;

public class FlightServerAddress
{
	private int udpPort;
	private String host;

	public FlightServerAddress(int udpPort, String host) {
		super();
		this.udpPort = udpPort;
		this.host = host;
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
