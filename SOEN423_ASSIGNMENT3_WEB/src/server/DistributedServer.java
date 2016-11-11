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

	public HashMap<String, FlightServer> init(){
		HashMap<String, FlightServer> flightServers = new HashMap<String, FlightServer>();
		
		// Initialization of some flight records
		
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
		
		for(Entry<String, FlightServer> entry : flightServers.entrySet()){
			FlightServer flightServer = entry.getValue();
			new Thread(flightServer).start();
		}
		
		// TODO: Add Manager Records
		
		return flightServers;
	}

	private FlightServer initServer(City city, int port, FlightServerAddress[] flightServerAddresses, List<String> flights) {
		FlightRecordDb flightRecordDb = initFlightRecordDb(flights);
		FlightReservationDb flightReservationDb = new FlightReservationDbImpl();
		PassengerRecordDb passengerRecordDb = new PassengerRecordDbImpl();
		ManagerRecordDb managerRecordDb = new ManagerRecordDbImpl();
		DatabaseRepository dbRepository = new DatabaseRepository(flightReservationDb, flightRecordDb, passengerRecordDb,
				managerRecordDb);
		
		FlightServer flightServer = new FlightServerImpl(port, city, dbRepository, flightServerAddresses);
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
