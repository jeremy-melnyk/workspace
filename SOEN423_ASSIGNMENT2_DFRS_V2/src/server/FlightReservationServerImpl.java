package server;

import java.util.List;
import java.util.concurrent.ExecutorService;

import database.IFlightDb;
import database.IPassengerRecordDb;
import dfrs.FlightReservationServerPOA;

public class FlightReservationServerImpl extends FlightReservationServerPOA {
	private final int BUFFER_SIZE = 5000;
	private final int THREAD_POOL_SIZE = 16;
    private final ExecutorService threadPool;
	private List<Integer> udpPorts;
	private List<FlightServerAddress> otherServers;
	private String cityAcronym;
	private IPassengerRecordDb passengerRecordDb;
	private IFlightDb flightDb;
	private List<String> managerIds;
	private ILogger logger;

	@Override
	public boolean bookFlight(String firstName, String lastName, String address, String phoneNumber, String flightIdAndFlightClass) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getBookedFlightCount(String managerIdAndFlightClass) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean editFlightRecord(String managerIdAndRecordIdAndDbOperation, String flightParameter,
			String newValue) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean transferReservation(String passengerRecordId, String currentCity, String otherCity) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String[] getFlights() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] getAvailableFlights() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] getManagerIds() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] getPassengerRecords() {
		
		return null;
	}

}
