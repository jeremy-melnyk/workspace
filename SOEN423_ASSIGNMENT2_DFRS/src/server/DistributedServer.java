package server;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.omg.CORBA.ORB;
import org.omg.CosNaming.NameComponent;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAHelper;

import database.FlightDb;
import database.PassengerRecordDb;
import dfrs.FlightReservationServer;
import dfrs.FlightReservationServerHelper;
import enums.FlightDbOperation;
import enums.FlightParameter;
import log.CustomLogger;
import log.TextFileLog;
import models.City;
import models.Flight;
import models.FlightServerAddress;
import models.FlightRecordOperation;

public class DistributedServer
{
	private static final int PORT = 1099;
	private static final int UDP_PORT_MTL = 1100;
	private static final int UDP_PORT_WST = 1101;
	private static final int UDP_PORT_NDL = 1102;
	private static final int UDP_PORT_MTL2 = 1103;
	private static final int UDP_PORT_WST2 = 1104;
	private static final int UDP_PORT_NDL2 = 1105;
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
			FlightServerAddress montrealAddress2 = new FlightServerAddress("MTL2", UDP_PORT_MTL2, "localhost");
			FlightServerAddress washingtonAddress2 = new FlightServerAddress("WST2", UDP_PORT_WST2, "localhost");
			FlightServerAddress newDelhiAddress2 = new FlightServerAddress("NDL2", UDP_PORT_NDL2, "localhost");
			
			List<FlightServerAddress> othersForMontreal = new ArrayList<FlightServerAddress>();
			othersForMontreal.add(washingtonAddress);
			othersForMontreal.add(newDelhiAddress);
			othersForMontreal.add(washingtonAddress2);
			othersForMontreal.add(newDelhiAddress2);
			
			List<FlightServerAddress> othersForWashington = new ArrayList<FlightServerAddress>();
			othersForWashington.add(montrealAddress);
			othersForWashington.add(newDelhiAddress);
			othersForWashington.add(montrealAddress2);
			othersForWashington.add(newDelhiAddress2);
			
			List<FlightServerAddress> othersForNewDelhi = new ArrayList<FlightServerAddress>();
			othersForNewDelhi.add(washingtonAddress);
			othersForNewDelhi.add(montrealAddress);
			othersForNewDelhi.add(washingtonAddress2);
			othersForNewDelhi.add(montrealAddress2);
			
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
			
			ORB orb = ORB.init(args, null);
			POA rootPOA = POAHelper.narrow(orb.resolve_initial_references("RootPOA"));
			rootPOA.the_POAManager().activate();
			
			org.omg.CORBA.Object nameServiceRef = orb.resolve_initial_references("NameService");
			NamingContextExt namingContextRef = NamingContextExtHelper.narrow(nameServiceRef);
			
			// Initialize Servers
			FlightReservationServerImpl montrealImpl = new FlightReservationServerImpl(UDP_PORT_MTL, THREAD_POOL_SIZE, othersForMontreal, "MTL", new PassengerRecordDb(), montrealFlights, montrealManagers, new CustomLogger(new TextFileLog()));
			org.omg.CORBA.Object montrealRef = rootPOA.servant_to_reference(montrealImpl);
			FlightReservationServer montreal = FlightReservationServerHelper.narrow(montrealRef);
			String montrealName = "MontrealServer";
			NameComponent pathMtl[] = namingContextRef.to_name(montrealName);
			namingContextRef.rebind(pathMtl, montreal);
			
			FlightReservationServerImpl washingtonImpl = new FlightReservationServerImpl(UDP_PORT_WST, THREAD_POOL_SIZE, othersForWashington, "WST", new PassengerRecordDb(), washingtonFlights, washingtonManagers, new CustomLogger(new TextFileLog()));
			org.omg.CORBA.Object wstRef = rootPOA.servant_to_reference(washingtonImpl);
			FlightReservationServer washington = FlightReservationServerHelper.narrow(wstRef);
			String wstName = "WashingtonServer";
			NameComponent pathWst[] = namingContextRef.to_name(wstName);
			namingContextRef.rebind(pathWst, washington);
			
			FlightReservationServerImpl newDelhiImpl = new FlightReservationServerImpl(UDP_PORT_NDL, THREAD_POOL_SIZE, othersForNewDelhi, "NDL", new PassengerRecordDb(), newDelhiFlights, newDelhiManagers, new CustomLogger(new TextFileLog()));
			org.omg.CORBA.Object ndlRef = rootPOA.servant_to_reference(newDelhiImpl);
			FlightReservationServer newDelhi = FlightReservationServerHelper.narrow(ndlRef);
			String ndlName = "NewDelhiServer";
			NameComponent pathNdl[] = namingContextRef.to_name(ndlName);
			namingContextRef.rebind(pathNdl, newDelhi);
			
			// Create some initial flights
			City montrealCity = new City("Montreal", "MTL");
			City washingtonCity = new City("Washington", "WST");
			City newDelhiCity = new City("NewDelhi", "NDL");
			
			Date date0 = new GregorianCalendar(2016, Calendar.OCTOBER, 17).getTime();
			Date date1 = new GregorianCalendar(2016, Calendar.DECEMBER, 20).getTime();
			
			FlightRecordOperation recordOperation = new FlightRecordOperation("STARTUP", -1 , FlightDbOperation.ADD);
			
			String startupCommand = "STARTUP-0-ADD";
			
			String flight0 = "-1,WST-Washington,07-Jun-2013,2,2,2";
			montreal.editFlightRecord(startupCommand, "NONE", flight0);
			
			String flight1 = "-1,NDL-NewDelhi,08-Jun-2013,2,2,2";
			montreal.editFlightRecord(startupCommand, "NONE", flight1);
			
			String flight2 = "-1,MTL-Montreal,09-Jun-2013,2,2,2";
			washington.editFlightRecord(startupCommand, "NONE", flight2);
			
			String flight3 = "-1,NDL-NewDelhi,10-Jun-2013,2,2,2";
			washington.editFlightRecord(startupCommand, "NONE", flight3);
			
			String flight4 = "-1,MTL-Montreal,11-Jun-2013,2,2,2";
			newDelhi.editFlightRecord(startupCommand, "NONE", flight4);
			
			String flight5 = "-1,WST-Washington,12-Jun-2013,2,2,2";
			newDelhi.editFlightRecord(startupCommand, "NONE", flight5);
			
			montreal.registerServer();
			washington.registerServer();
			newDelhi.registerServer();
			
			System.out.println("Servers initialized.");
			
			new Thread(()->{
				try
				{
					montreal.serveRequests(UDP_PORT_MTL);
				} catch (Exception e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}	
			}).start();
			
			new Thread(()->{
				try
				{
					montreal.serveRequests(UDP_PORT_MTL2);
				} catch (Exception e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}	
			}).start();
			
			new Thread(()->{
				try
				{
					washington.serveRequests(UDP_PORT_WST);
				} catch (Exception e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}	
			}).start();
			
			new Thread(()->{
				try
				{
					washington.serveRequests(UDP_PORT_WST2);
				} catch (Exception e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}	
			}).start();
			
			new Thread(()->{
				try
				{
					newDelhi.serveRequests(UDP_PORT_NDL);
				} catch (Exception e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}	
			}).start();
			
			new Thread(()->{
				try
				{
					newDelhi.serveRequests(UDP_PORT_NDL2);
				} catch (Exception e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}	
			}).start();
			
			orb.run();
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
