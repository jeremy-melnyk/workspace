package client;

import enums.FlightClass;
import server.IFlightReservationServer;

public class ManagerClient extends Client
{
	public static void main(String[] args)
	{
		ManagerClient managerClient = new ManagerClient("rmi://localhost:1099/MTL");
		System.out.println(managerClient.getBookedFlightCount(FlightClass.FIRST));
	}

	public ManagerClient(String baseUrl) {
		super(baseUrl);
	}

	public int getBookedFlightCount(FlightClass flightClass)
	{
		try
		{
			IFlightReservationServer flightReservationServer = ServerLocator.locateServer(this.baseUrl);
			return flightReservationServer.getBookedFlightCount(flightClass);
		} catch (Exception e)
		{
			e.printStackTrace();
			return 0;
		}
	}
}
