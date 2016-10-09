package server;
import java.rmi.registry.LocateRegistry;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import database.FlightDb;
import database.PassengerRecordDb;
import enums.FlightClass;
import models.City;
import models.Flight;
import models.FlightServerAddress;

public class DistributedServer
{
	private static final int PORT = 1099;
	private static final int UDP_PORT_MTL = 1100;
	private static final int UDP_PORT_WST = 1101;
	private static final int UDP_PORT_NDL = 1102;
	private static final int THREAD_POOL_SIZE = 16;

	public static void main(String[] args)
	{
		try
		{	
			FlightDb montrealFlights = new FlightDb();
			FlightDb washingtonFlights = new FlightDb();
			FlightDb newDelhiFlights = new FlightDb();
			
			FlightServerAddress montrealAddress = new FlightServerAddress("MTL", UDP_PORT_MTL, "localhost");
			FlightServerAddress washingtonAddress = new FlightServerAddress("WST", UDP_PORT_WST, "localhost");
			FlightServerAddress newDelhiAddress = new FlightServerAddress("NDL", UDP_PORT_NDL, "localhost");
			
			List<FlightServerAddress> othersForMontreal = new ArrayList<FlightServerAddress>();
			othersForMontreal.add(washingtonAddress);
			othersForMontreal.add(newDelhiAddress);
			
			List<FlightServerAddress> othersForWashington = new ArrayList<FlightServerAddress>();
			othersForWashington.add(montrealAddress);
			othersForWashington.add(newDelhiAddress);
			
			List<FlightServerAddress> othersForNewDelhi = new ArrayList<FlightServerAddress>();
			othersForNewDelhi.add(washingtonAddress);
			othersForNewDelhi.add(montrealAddress);
			
			IFlightReservationServer montreal = new FlightReservationServer(PORT, UDP_PORT_MTL, THREAD_POOL_SIZE, othersForMontreal, "MTL", new PassengerRecordDb(), montrealFlights);
			IFlightReservationServer washington = new FlightReservationServer(PORT, UDP_PORT_WST, THREAD_POOL_SIZE, othersForWashington, "WST", new PassengerRecordDb(), washingtonFlights);
			IFlightReservationServer newDelhi = new FlightReservationServer(PORT, UDP_PORT_NDL, THREAD_POOL_SIZE, othersForNewDelhi, "NDL", new PassengerRecordDb(), newDelhiFlights);
			LocateRegistry.createRegistry(PORT);
			
			// Create some initial flights
			City montrealCity = new City("Montreal", "MTL");
			City washingtonCity = new City("Washington", "WST");
			City newDelhiCity = new City("NewDelhi", "NDL");
			
			Date date0 = new GregorianCalendar(2016, Calendar.OCTOBER, 17).getTime();
			Date date1 = new GregorianCalendar(2016, Calendar.DECEMBER, 20).getTime();
			
			Flight flight0 = new Flight(FlightClass.FIRST, washingtonCity, date0, 20);
			montreal.editFlightRecord(flight0);
			
			Flight flight1 = new Flight(FlightClass.BUSINESS, washingtonCity, date0, 25);
			montreal.editFlightRecord(flight1);
			
			Flight flight2 = new Flight(FlightClass.ECONOMY, washingtonCity, date0, 50);
			montreal.editFlightRecord(flight2);
			
			Flight flight3 = new Flight(FlightClass.FIRST, montrealCity, date1, 25);
			washington.editFlightRecord(flight3);
			
			Flight flight4 = new Flight(FlightClass.ECONOMY, newDelhiCity, date1, 75);
			washington.editFlightRecord(flight4);
			
			Flight flight5 = new Flight(FlightClass.FIRST, washingtonCity, date1, 75);
			newDelhi.editFlightRecord(flight5);
			
			montreal.registerServer();
			washington.registerServer();
			newDelhi.registerServer();
			
			System.out.println("Servers initialized.");
			
			new Thread(()->{
				try
				{
					montreal.serveRequests();
				} catch (Exception e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}	
			}).start();
			
			new Thread(()->{
				try
				{
					washington.serveRequests();
				} catch (Exception e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}	
			}).start();
			
			new Thread(()->{
				try
				{
					newDelhi.serveRequests();
				} catch (Exception e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}	
			}).start();
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
