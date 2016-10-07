package client;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import enums.FlightClass;

public class ManagerClient extends Client
{
	public static void main(String[] args)
	{
	}

	public ManagerClient(String baseUrl) {
		super(baseUrl);
	}

	public int getBookedFlightCount(FlightClass flightClass)
	{
		int bookedFlightCount = 0;
		try
		{
			final ExecutorService executorService = Executors.newFixedThreadPool(3);
			final Future<Integer> montrealFlightCount = executorService.submit(new BookedFlightCountTask(this.baseUrl + "MTL", flightClass));
			final Future<Integer> washingtonFlightCount = executorService.submit(new BookedFlightCountTask(this.baseUrl + "WST", flightClass));
			final Future<Integer> newDelhiFlightCount = executorService.submit(new BookedFlightCountTask(this.baseUrl + "NDL", flightClass));
			bookedFlightCount += montrealFlightCount.get().intValue()
					+ washingtonFlightCount.get().intValue() 
					+ newDelhiFlightCount.get().intValue();
			return bookedFlightCount;
		} catch (Exception e)
		{
			e.printStackTrace();
			return 0;
		}
	}
}
