package client;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import models.Address;
import models.Flight;
import server.IFlightReservationServer;

public class PassengerClient extends Client
{
	public PassengerClient(String baseUrl) {
		super(baseUrl);
	}
	
	public boolean bookFlight(String firstName, String lastName, Address address, String phoneNumber, Flight flight)
	{
		try
		{
			IFlightReservationServer flightReservationServer = ServerLocator.locateServer(this.baseUrl);
			return flightReservationServer.bookFlight(firstName, lastName, address, phoneNumber, flight);
		} catch (Exception e)
		{
			System.out.println(e.getMessage());
			return false;
		}
	}
	
	public void displayFlights(List<Flight> flights){
		if(flights == null){
			return;
		}
		
		for(Flight flight : flights){
			System.out.println(flight);
		}
	}
	
	public List<Flight> getAvailableFlights(){
		try
		{
			final ExecutorService executorService = Executors.newFixedThreadPool(3);
			final Future<List<Flight>> flights = executorService.submit(new GetAvailableFlightsTask(this.baseUrl));
			return flights.get();
		} catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
}
