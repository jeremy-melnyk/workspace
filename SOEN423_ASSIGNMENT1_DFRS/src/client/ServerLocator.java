package client;
import java.rmi.Naming;

import server.IFlightReservationServer;

public final class ServerLocator
{
	private ServerLocator(){	
	}
	
	public static IFlightReservationServer locateServer(String serverUrl) throws Exception
	{
		if (serverUrl == null)
		{
			throw new IllegalArgumentException();
		}
		return (IFlightReservationServer) Naming.lookup(serverUrl);
	}
}
