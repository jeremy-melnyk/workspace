package client;

import java.util.List;
import java.util.concurrent.Callable;

import models.Flight;
import server.IFlightReservationServer;

public class GetAvailableFlightsTask implements Callable<List<Flight>>
{
	private final String serverUrl;
	
	public GetAvailableFlightsTask(String serverUrl){
		this.serverUrl = serverUrl;
	}

	@Override
	public List<Flight> call() throws Exception
	{
		return getFlights(this.serverUrl);
	}

	private List<Flight> getFlights(String serverUrl) throws Exception{
		IFlightReservationServer flightReservationServer = ServerLocator.locateServer(serverUrl);
		return flightReservationServer.getAvailableFlights();
	}
}