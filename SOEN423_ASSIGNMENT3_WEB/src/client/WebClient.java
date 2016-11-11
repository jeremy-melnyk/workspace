package client;

import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;

import enums.City;
import server.FlightServer;

public class WebClient {
	protected final String DELIMITER = "|";
	protected City city;
	protected String lastName;
	protected String firstName;
	
	public WebClient(City city, String lastName, String firstName) {
		super();
		this.city = city;
		this.lastName = lastName;
		this.firstName = firstName;
	}

	public City getCity() {
		return city;
	}

	public void setCity(City city) {
		this.city = city;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	protected String[] getFlightRecords(){
		FlightServer flightServer = getFlightServer();
		if (flightServer == null){
			System.out.println("FlightServer was null for " + city);
			return new String[0];
		}
		return flightServer.getFlightRecords();
	}
	
	protected String[] getFlightReservations(){
		FlightServer flightServer = getFlightServer();
		if (flightServer == null){
			System.out.println("FlightServer was null for " + city);
			return new String[0];
		}
		return flightServer.getFlightReservations();
	}
	
	protected String[] getPassengerRecords(){
		FlightServer flightServer = getFlightServer();
		if (flightServer == null){
			System.out.println("FlightServer was null for " + city);
			return new String[0];
		}
		return flightServer.getPassengerRecords();
	}

	protected FlightServer getFlightServer(){
		URL url;
		QName qName;
		try {
			String urlPath = "http://localhost:8080/" + city + "?wsdl";
			url = new URL(urlPath);
			qName = new QName("http://server/", "FlightServerImplService");
			Service service = Service.create(url, qName);
			FlightServer flightServer = service.getPort(FlightServer.class);
			return flightServer;
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
