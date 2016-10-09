package server;
import java.rmi.registry.LocateRegistry;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import database.FlightDb;
import database.PassengerRecordDb;
import enums.FlightClass;
import models.City;
import models.Flight;

public class DistributedServer
{
	private static final int PORT = 1099;

	public static void main(String[] args)
	{
		try
		{	
			FlightDb montrealFlights = new FlightDb();
			FlightDb washingtonFlights = new FlightDb();
			FlightDb newDelhiFlights = new FlightDb();
			
			IFlightReservationServer montreal = new FlightReservationServer(PORT, "MTL", new PassengerRecordDb(), montrealFlights);
			IFlightReservationServer washington = new FlightReservationServer(PORT, "WST", new PassengerRecordDb(), washingtonFlights);
			IFlightReservationServer newDelhi = new FlightReservationServer(PORT, "NDL", new PassengerRecordDb(), newDelhiFlights);
			LocateRegistry.createRegistry(PORT);
			
			// Create some initial flights
			City montrealCity = new City("Montreal", "MTL");
			City washingtonCity = new City("Washington", "WST");
			City newDelhiCity = new City("NewDelhi", "NDL");
			
			Date date0 = new GregorianCalendar(2016, Calendar.OCTOBER, 17).getTime();
			Date date1 = new GregorianCalendar(2016, Calendar.DECEMBER, 20).getTime();
			
			Flight flight0 = new Flight(FlightClass.FIRST, washingtonCity, date0, 10);
			montreal.addFlight(flight0);
			
			Flight flight1 = new Flight(FlightClass.BUSINESS, washingtonCity, date0, 25);
			montreal.addFlight(flight1);
			
			Flight flight2 = new Flight(FlightClass.ECONOMY, washingtonCity, date0, 50);
			montreal.addFlight(flight2);
			
			Flight flight3 = new Flight(FlightClass.FIRST, montrealCity, date1, 25);
			washington.addFlight(flight3);
			
			Flight flight4 = new Flight(FlightClass.ECONOMY, newDelhiCity, date1, 75);
			washington.addFlight(flight4);
			
			Flight flight5 = new Flight(FlightClass.FIRST, washingtonCity, date1, 75);
			newDelhi.addFlight(flight5);
			
			montreal.registerServer();
			washington.registerServer();
			newDelhi.registerServer();
			System.out.println("Servers initialized.");
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
