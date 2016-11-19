package client;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.omg.CORBA.ORB;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;

import dfrs.FlightReservationServer;
import dfrs.FlightReservationServerHelper;
import enums.FlightClassEnum;
import models.Address;
import models.Flight;
import models.FlightWithClass;
import server.IFlightReservationServer;

public class PassengerClient extends Client
{
	public PassengerClient(ORB orb, String serverAcronym) {
		super(orb, serverAcronym);
	}
	
	public boolean bookFlight(String firstName, String lastName, Address address, String phoneNumber, int flightRecordId, FlightClassEnum flightClassEnum)
	{
		try
		{
			org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");
			NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);
			FlightReservationServer flightReservationServer = null;
			switch(serverAcronym){
			case "MTL":
				flightReservationServer = FlightReservationServerHelper.narrow(ncRef.resolve_str("MontrealServer"));
				break;
			case "WST":
				flightReservationServer = FlightReservationServerHelper.narrow(ncRef.resolve_str("WashingtonServer"));
				break;
			case "NDL":
				flightReservationServer = FlightReservationServerHelper.narrow(ncRef.resolve_str("NewDelhiServer"));
				break;
			default:
				break;
			}
			String flightEnum = flightClassEnum.name();
			String flightWithClassString = flightRecordId + "-" + flightEnum;
			return flightReservationServer.bookFlight(firstName, lastName, address.formatToString(), phoneNumber, flightWithClassString);
		} catch (Exception e)
		{
			System.out.println(e.getMessage());
			return false;
		}
	}
	
	public void displayFlights(String[] flights){
		if(flights == null){
			return;
		}
		
		for(String flight : flights){
			System.out.println(flight);
		}
	}
	
	public String[] getAvailableFlights(){
		try
		{
			org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");
			NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);
			FlightReservationServer flightReservationServer = null;
			switch(serverAcronym){
			case "MTL":
				flightReservationServer = FlightReservationServerHelper.narrow(ncRef.resolve_str("MontrealServer"));
				break;
			case "WST":
				flightReservationServer = FlightReservationServerHelper.narrow(ncRef.resolve_str("WashingtonServer"));
				break;
			case "NDL":
				flightReservationServer = FlightReservationServerHelper.narrow(ncRef.resolve_str("NewDelhiServer"));
				break;
			default:
				break;
			}
			String[] availableFlights = flightReservationServer.getAvailableFlights();
			return availableFlights;
		} catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
}
