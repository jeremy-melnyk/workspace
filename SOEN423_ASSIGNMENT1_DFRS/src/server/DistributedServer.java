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
import enums.FlightDbOperation;
import log.CustomLogger;
import log.TextFileLog;
import models.City;
import models.FlightParameterValues;
import models.FlightServerAddress;
import models.RecordOperation;

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
			
			List<String> montrealManagers = new ArrayList<String>();
			montrealManagers.add("MTL1111");
			montrealManagers.add("MTL1112");
			montrealManagers.add("MTL1113");
			
			List<String> washingtonManagers = new ArrayList<String>();
			washingtonManagers.add("WST1111");
			washingtonManagers.add("WST1112");
			washingtonManagers.add("WST1113");
			
			List<String> newDelhiManagers = new ArrayList<String>();
			newDelhiManagers.add("NDL1111");
			newDelhiManagers.add("NDL1112");
			newDelhiManagers.add("NDL1113");
			
			IFlightReservationServer montreal = new FlightReservationServer(PORT, UDP_PORT_MTL, THREAD_POOL_SIZE, othersForMontreal, "MTL", new PassengerRecordDb(), montrealFlights, montrealManagers, new CustomLogger(new TextFileLog()));
			IFlightReservationServer washington = new FlightReservationServer(PORT, UDP_PORT_WST, THREAD_POOL_SIZE, othersForWashington, "WST", new PassengerRecordDb(), washingtonFlights, washingtonManagers, new CustomLogger(new TextFileLog()));
			IFlightReservationServer newDelhi = new FlightReservationServer(PORT, UDP_PORT_NDL, THREAD_POOL_SIZE, othersForNewDelhi, "NDL", new PassengerRecordDb(), newDelhiFlights, newDelhiManagers, new CustomLogger(new TextFileLog()));
			LocateRegistry.createRegistry(PORT);
			
			// Create some initial flights
			City montrealCity = new City("Montreal", "MTL");
			City washingtonCity = new City("Washington", "WST");
			City newDelhiCity = new City("NewDelhi", "NDL");
			
			Date date0 = new GregorianCalendar(2016, Calendar.OCTOBER, 17).getTime();
			Date date1 = new GregorianCalendar(2016, Calendar.DECEMBER, 20).getTime();
			
			RecordOperation recordOperation = new RecordOperation("INITIAL", -1 , FlightDbOperation.ADD);
			
			FlightParameterValues flight0 = new FlightParameterValues(FlightClass.FIRST, washingtonCity, date0, 20);
			montreal.editFlightRecord(recordOperation, null, flight0);
			
			FlightParameterValues flight1 = new FlightParameterValues(FlightClass.BUSINESS, washingtonCity, date0, 25);
			montreal.editFlightRecord(recordOperation, null, flight1);
			
			FlightParameterValues flight2 = new FlightParameterValues(FlightClass.ECONOMY, washingtonCity, date0, 50);
			montreal.editFlightRecord(recordOperation, null, flight2);
			
			FlightParameterValues flight3 = new FlightParameterValues(FlightClass.FIRST, montrealCity, date1, 25);
			washington.editFlightRecord(recordOperation, null, flight3);
			
			FlightParameterValues flight4 = new FlightParameterValues(FlightClass.ECONOMY, newDelhiCity, date1, 75);
			washington.editFlightRecord(recordOperation, null, flight4);
			
			FlightParameterValues flight5 = new FlightParameterValues(FlightClass.FIRST, washingtonCity, date1, 75);
			newDelhi.editFlightRecord(recordOperation, null, flight5);
			
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
