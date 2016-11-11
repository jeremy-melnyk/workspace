package server;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import databases.DatabaseRepository;
import databases.FlightRecordDb;
import databases.FlightRecordDbImpl;
import databases.FlightReservationDb;
import databases.FlightReservationDbImpl;
import databases.ManagerRecordDb;
import databases.ManagerRecordDbImpl;
import databases.PassengerRecordDb;
import databases.PassengerRecordDbImpl;
import enums.City;
import enums.FlightClass;
import models.FlightSeats;
import models.FlightServerAddress;

public class DistributedServer {
	private final int MTL_PORT = 50152;
	private final int WST_PORT = 50153;
	private final int NDL_PORT = 50154;
	private final String HOST = "localhost";

	public HashMap<String, FlightServerImpl> init(){
		HashMap<String, FlightServerImpl> flightServers = new HashMap<String, FlightServerImpl>();
		
		List<String> mtlFlights = new ArrayList<String>();
		mtlFlights.add("MTL|WST|5-Jun-2016|10|5|2");
		mtlFlights.add("MTL|NDL|5-Jun-2016|10|5|2");
		
		List<String> wstFlights = new ArrayList<String>();
		wstFlights.add("WST|MTL|5-Jun-2016|10|5|2");
		wstFlights.add("WST|NDL|5-Jun-2016|10|5|2");
		
		List<String> ndlFlights = new ArrayList<String>();
		ndlFlights.add("NDL|MTL|5-Jun-2016|10|5|2");
		ndlFlights.add("NDL|WST|5-Jun-2016|10|5|2");
		
		flightServers.put("MTL",
				initServer(City.MTL, MTL_PORT,
						new FlightServerAddress[] { new FlightServerAddress(City.WST, WST_PORT, HOST),
								new FlightServerAddress(City.NDL, NDL_PORT, HOST) }, mtlFlights));
		
		flightServers.put("WST",
				initServer(City.WST, WST_PORT,
						new FlightServerAddress[] { new FlightServerAddress(City.MTL, MTL_PORT, HOST),
								new FlightServerAddress(City.NDL, NDL_PORT, HOST) }, wstFlights));
		
		flightServers.put("NDL",
				initServer(City.NDL, NDL_PORT,
						new FlightServerAddress[] { new FlightServerAddress(City.MTL, MTL_PORT, HOST),
								new FlightServerAddress(City.WST, WST_PORT, HOST) }, ndlFlights));
		
		for(Entry<String, FlightServerImpl> entry : flightServers.entrySet()){
			FlightServerImpl flightServer = entry.getValue();
			new Thread(flightServer).start();
		}
		
		// Add Manager Records
		/*
		String result = flightServers.get("MTL").bookFlight("John", "Doe", "1000 Somewhere Land|Montreal|QC|H1B 5U0|Canada", "514-123-4567", "WST", "5-Jun-2016", "FIRST");
		String count = flightServers.get("MTL").getBookedFlightCount("MTL1111|FIRST");
		System.out.println(result);
		System.out.println(count);
		
		String result2 = flightServers.get("MTL").bookFlight("John", "Bell", "1000 Somewhere Land|Montreal|QC|H1B 5U0|Canada", "514-123-4567", "WST", "5-Jun-2016", "FIRST");
		String count2 = flightServers.get("MTL").getBookedFlightCount("MTL1111|FIRST");
		System.out.println(result2);
		System.out.println(count2);
		
		String[] passengerRecords = flightServers.get("MTL").getPassengerRecords();
		for (String passengerRecord : passengerRecords){
			System.out.println(passengerRecord);
		}
		
		System.out.println(flightServers.get("MTL").editFlightRecord("MTL1111|EDIT|0", "ORIGIN", "WST"));
		System.out.println(flightServers.get("MTL").editFlightRecord("MTL1111|EDIT|0", "DESTINATION", "MTL"));
		System.out.println(flightServers.get("MTL").editFlightRecord("MTL1111|EDIT|0", "DATE", "10-Jul-2017"));
		System.out.println(flightServers.get("MTL").editFlightRecord("MTL1111|EDIT|0", "SEATS", "FIRST|30"));
		System.out.println(flightServers.get("MTL").editFlightRecord("MTL1111|EDIT|0", "SEATS", "FIRST|10"));
		System.out.println(flightServers.get("MTL").editFlightRecord("MTL1111|ADD|0", "NONE", "MTL|NDL|15-Jul-2017|5|5|5"));
		System.out.println(flightServers.get("MTL").editFlightRecord("MTL1111|REMOVE|0", "NONE", ""));
		System.out.println(flightServers.get("MTL").getBookedFlightCount("MTL1111|FIRST"));
		*/
		
		return flightServers;
	}

	private FlightServerImpl initServer(City city, int port, FlightServerAddress[] flightServerAddresses, List<String> flights) {
		FlightRecordDb flightRecordDb = initFlightRecordDb(flights);
		FlightReservationDb flightReservationDb = new FlightReservationDbImpl();
		PassengerRecordDb passengerRecordDb = new PassengerRecordDbImpl();
		ManagerRecordDb managerRecordDb = new ManagerRecordDbImpl();
		DatabaseRepository dbRepository = new DatabaseRepository(flightReservationDb, flightRecordDb, passengerRecordDb,
				managerRecordDb);
		
		FlightServerImpl flightServer = new FlightServerImpl(port, city, dbRepository, flightServerAddresses);
		return flightServer;
	}
	
	@SuppressWarnings("deprecation")
	private FlightRecordDb initFlightRecordDb(List<String> flights){
		FlightRecordDb flightRecordDb = new FlightRecordDbImpl();
		for (String flight : flights){
			String[] tokens = flight.split("\\|");
			HashMap<FlightClass, FlightSeats> flightClasses = new HashMap<FlightClass, FlightSeats>();
			flightClasses.put(FlightClass.FIRST, new FlightSeats(Integer.parseInt(tokens[3])));
			flightClasses.put(FlightClass.BUSINESS, new FlightSeats(Integer.parseInt(tokens[4])));
			flightClasses.put(FlightClass.ECONOMY, new FlightSeats(Integer.parseInt(tokens[5])));
			flightRecordDb.addFlightRecord(City.valueOf(tokens[0]), City.valueOf(tokens[1]), new Date(tokens[2]), flightClasses);
		}
		return flightRecordDb;
	}
}
