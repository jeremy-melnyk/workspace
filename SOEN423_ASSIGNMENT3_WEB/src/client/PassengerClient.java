package client;

import enums.City;
import models.Address;
import server.FlightServer;

public class PassengerClient extends WebClient {
	private Address address;
	private String phoneNumber;
		
	public PassengerClient(City city, String lastName, String firstName, String address, String phoneNumber) {
		super(city, lastName, firstName);
		this.address = new Address(address);
		this.phoneNumber = phoneNumber;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	
	public void bookFlight(String destination, String date, String flightClass){
		FlightServer flightServer = getFlightServer();
		if (flightServer == null){
			System.out.println("FlightServer was null for " + city);
			return;
		}
		String result = flightServer.bookFlight(firstName, lastName, address.toString(), phoneNumber, destination, date, flightClass);
		if(result.equals("No flight available")){
			System.out.println("BOOKFLIGHT_FAIL: " + result);
		} else {
			System.out.println("BOOKFLIGHT_SUCCESS: " + result);
		}
	}
}
