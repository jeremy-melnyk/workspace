package models;

import java.util.List;

public class FlightServerAddress
{
	private String name;
	private List<Integer> udpPort;
	private String host;

	public FlightServerAddress(String name, int udpPort, String host) {
		super();
		this.name = name;
		this.udpPort = udpPort;
		this.host = host;
	}
	
	public static FlightServerAddress toFlightServerAddress(String input){
		String[] tokens = input.split("-");
		
		String name = tokens[0];
		int udpPort = Integer.parseInt(tokens[1]);
		String host = tokens[2];
		
		return new FlightServerAddress(name, udpPort, host);
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
