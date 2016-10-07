package client;

import java.util.concurrent.Callable;

import enums.FlightClass;
import server.IFlightReservationServer;

public class BookedFlightCountTask implements Callable<Integer>
{
	private final String serverUrl;
	private final FlightClass flightClass;
	
	public BookedFlightCountTask(String serverUrl, FlightClass flightClass){
		this.serverUrl = serverUrl;
		this.flightClass = flightClass;
	}

	@Override
	public Integer call() throws Exception
	{
		return getBookedFlightCount(this.serverUrl, this.flightClass);
	}

	private int getBookedFlightCount(String serverUrl, FlightClass flightClass) throws Exception{
		IFlightReservationServer flightReservationServer = ServerLocator.locateServer(serverUrl);
		return flightReservationServer.getBookedFlightCount(flightClass);
	}
}
