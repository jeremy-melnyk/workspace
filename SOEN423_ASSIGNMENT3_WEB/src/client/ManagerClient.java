package client;

import enums.City;
import server.FlightServer;

public class ManagerClient extends WebClient {
	private Integer id;

	public ManagerClient(City city, String lastName, String firstName, Integer id) {
		super(city, lastName, firstName);
		this.id = id;
	}

	public String getId() {
		return city + id.toString();
	}
	
	public void getBookedFlightCount(String flightClass){
		FlightServer flightServer = getFlightServer();
		if (flightServer == null){
			System.out.println("FlightServer was null for " + city);
		}
		String request = getId() + DELIMITER + flightClass;
		String result = flightServer.getBookedFlightCount(request);
		System.out.println(result);
	}
	
	public void editFlightRecord(String editParameters, String fieldToEdit, String newValue){
		FlightServer flightServer = getFlightServer();
		if (flightServer == null){
			System.out.println("FlightServer was null for " + city);
		}
		String request = getId() + DELIMITER + editParameters;
		String result = flightServer.editFlightRecord(request, fieldToEdit, newValue);
		System.out.println(result);
	}
	
	protected String[] getManagerRecords(){
		FlightServer flightServer = getFlightServer();
		if (flightServer == null){
			System.out.println("FlightServer was null for " + city);
			return new String[0];
		}
		return flightServer.getManagerRecords();
	}
}
