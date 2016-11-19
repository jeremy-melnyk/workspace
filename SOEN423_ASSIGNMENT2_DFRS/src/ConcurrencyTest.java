import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;

import org.omg.CORBA.ORB;

import client.ManagerClient;
import client.PassengerClient;
import enums.FlightClassEnum;
import enums.FlightDbOperation;
import enums.FlightParameter;
import models.Address;
import models.City;
import models.Flight;

public class ConcurrencyTest
{
	public static void main(String[] args)
	{
		ORB orb = ORB.init(args, null);
		List<Thread> threads = new LinkedList<Thread>();
		int mtlId = 1111;
		int wstId = 1111;
		int ndlId = 1111;
		for(int i = 0; i < 3; ++i){
			ManagerClient managerClient = new ManagerClient(orb, "MTL");
			managerClient.login("MTL" + mtlId++);
			Thread tM = new Thread(() -> {
				for(int j = 0; j < 20; ++j){
					City newCity = new City("Washington", "WST");
					City otherCity = new City("Washington", "WST");
					Flight newFlight = new Flight(newCity, new Date(), 2, 2, 2);
					managerClient.editFlightRecord(j, FlightDbOperation.ADD, FlightParameter.NONE , newFlight.formatToString());
					managerClient.editFlightRecord(j, FlightDbOperation.EDIT, FlightParameter.BUSINESS_CLASS_SEATS, 2 + "");
					managerClient.editFlightRecord(j, FlightDbOperation.EDIT, FlightParameter.FIRST_CLASS_SEATS, 2 + "");
					managerClient.editFlightRecord(j, FlightDbOperation.EDIT, FlightParameter.ECONOMY_CLASS_SEATS, 2 + "");
					//managerClient.editFlightRecord(j, FlightDbOperation.REMOVE, FlightParameter.NONE, null);
					if(managerClient.transferReservation(j, otherCity)){
						System.out.println("Transferred passenger record " + j + " to " + otherCity.formatToString());
					}
					System.out.println(managerClient.getBookedFlightCount(FlightClassEnum.FIRST));
					System.out.println(managerClient.getBookedFlightCount(FlightClassEnum.BUSINESS));
					System.out.println(managerClient.getBookedFlightCount(FlightClassEnum.ECONOMY));
				}
			});
			threads.add(tM);
			tM.start();
		}
		for(int i = 0; i < 3; ++i){
			ManagerClient managerClient = new ManagerClient(orb, "WST");
			managerClient.login("WST" + wstId++);
			Thread tM = new Thread(() -> {
				for(int j = 0; j < 20; ++j){
					Date date = new GregorianCalendar(2018, Calendar.SEPTEMBER, 20).getTime();
					City newCity = new City("NewDelhi", "NDL");
					City otherCity = new City("Montreal", "MTL");
					Flight newFlight = new Flight(newCity, new Date(), 2, 2, 2);
					managerClient.editFlightRecord(j, FlightDbOperation.ADD, FlightParameter.NONE, newFlight.formatToString());
					managerClient.editFlightRecord(j, FlightDbOperation.EDIT, FlightParameter.DATE, date.toString());
					//managerClient.editFlightRecord(j, FlightDbOperation.REMOVE, FlightParameter.NONE, null);
					if(managerClient.transferReservation(j, otherCity)){
						System.out.println("Transferred passenger record " + j + " to " + otherCity.formatToString());
					}
					System.out.println(managerClient.getBookedFlightCount(FlightClassEnum.FIRST));
					System.out.println(managerClient.getBookedFlightCount(FlightClassEnum.BUSINESS));
					System.out.println(managerClient.getBookedFlightCount(FlightClassEnum.ECONOMY));
				}
			});
			threads.add(tM);
			tM.start();
		}
		for(int i = 0; i < 3; ++i){
			ManagerClient managerClient = new ManagerClient(orb, "NDL");
			managerClient.login("NDL" + ndlId++);		
			Thread tM = new Thread(() -> {
				for(int j = 0; j < 20; ++j){
					City newCity = new City("Montreal", "MTL");
					City otherCity = new City("Washington", "WST");
					Flight newFlight = new Flight(newCity, new Date(), 2, 2, 2);
					managerClient.editFlightRecord(j, FlightDbOperation.ADD, FlightParameter.NONE, newFlight.formatToString());
					managerClient.editFlightRecord(j, FlightDbOperation.EDIT, FlightParameter.DESTINATION, newCity.formatToString());
					//managerClient.editFlightRecord(j, FlightDbOperation.REMOVE, FlightParameter.NONE, null);
					System.out.println("Transferred passenger record " + j + " to " + otherCity.formatToString());
					System.out.println(managerClient.getBookedFlightCount(FlightClassEnum.FIRST));
					System.out.println(managerClient.getBookedFlightCount(FlightClassEnum.BUSINESS));
					System.out.println(managerClient.getBookedFlightCount(FlightClassEnum.ECONOMY));
				}
			});
			threads.add(tM);
			tM.start();
		}
		//long startTime = System.nanoTime();
		PassengerClient passengerClient = new PassengerClient(orb, "MTL");				
		Thread t0 = new Thread(() -> {
			char passengerLastChar = 'A';
			String[] availableFlights = passengerClient.getAvailableFlights();
			int count = 0;
			while(count < availableFlights.length){
				passengerClient.displayFlights(availableFlights);
				String chosenFlight = availableFlights[count];
				Address address = new Address("Street", "City", "Province", "PostalCode", "Country");
				if(passengerClient.bookFlight("Dominic", passengerLastChar++ + "an", address, "514-456-7890", count, FlightClassEnum.FIRST)){
					System.out.println("Successful booking MTL!");
				}
				if(passengerClient.bookFlight("Dominic", passengerLastChar++ + "an", address, "514-456-7890", count, FlightClassEnum.BUSINESS)){
					System.out.println("Successful booking MTL!");
				}
				if(passengerClient.bookFlight("Dominic", passengerLastChar++ + "an", address, "514-456-7890", count, FlightClassEnum.ECONOMY)){
					System.out.println("Successful booking MTL!");
				}
				if (passengerLastChar <=  'Z' - 3)
				{
					passengerLastChar = 'A';
				}
				availableFlights = passengerClient.getAvailableFlights();
				++count;
			}
		});
		threads.add(t0);
		t0.start();
		
		PassengerClient passengerClient2 = new PassengerClient(orb, "WST");				
		Thread t1 = new Thread(() -> {
			char passengerLastChar = 'A';
			String[] availableFlights = passengerClient.getAvailableFlights();
			int count = 0;
			while(count < availableFlights.length){
				passengerClient2.displayFlights(availableFlights);
				String chosenFlight = availableFlights[count];
				Address address = new Address("Street", "City", "Province", "PostalCode", "Country");
				if(passengerClient2.bookFlight("Alex", passengerLastChar++ + "alchenyuk", address, "514-456-7890", count, FlightClassEnum.FIRST)){
					System.out.println("Successful booking WST!");
				}
				if(passengerClient2.bookFlight("Alex", passengerLastChar++ + "alchenyuk", address, "514-456-7890", count, FlightClassEnum.BUSINESS)){
					System.out.println("Successful booking WST!");
				}
				if(passengerClient2.bookFlight("Alex", passengerLastChar++ + "alchenyuk", address, "514-456-7890", count, FlightClassEnum.ECONOMY)){
					System.out.println("Successful booking WST!");
				}
				if (passengerLastChar <=  'Z' - 3)
				{
					passengerLastChar = 'A';
				}
				availableFlights = passengerClient2.getAvailableFlights();
				++count;
			}
		});
		threads.add(t1);
		t1.start();
		
		PassengerClient passengerClient3 = new PassengerClient(orb, "NDL");				
		Thread t2 = new Thread(() -> {
			char passengerLastChar = 'A';
			String[] availableFlights = passengerClient.getAvailableFlights();
			int count = 0;
			while(count < availableFlights.length){
				passengerClient3.displayFlights(availableFlights);
				String chosenFlight = availableFlights[count];
				Address address = new Address("Street", "City", "Province", "PostalCode", "Country");
				if(passengerClient3.bookFlight("John", passengerLastChar++ + "oe", address, "514-456-7890", count, FlightClassEnum.FIRST)){
					System.out.println("Successful booking NDL!");
				}
				if(passengerClient3.bookFlight("John", passengerLastChar++ + "oe", address, "514-456-7890", count, FlightClassEnum.BUSINESS)){
					System.out.println("Successful booking NDL!");
				}
				if(passengerClient3.bookFlight("John", passengerLastChar++ + "oe", address, "514-456-7890", count, FlightClassEnum.ECONOMY)){
					System.out.println("Successful booking NDL!");
				}
				if (passengerLastChar <=  'Z' - 3)
				{
					passengerLastChar = 'A';
				}
				availableFlights = passengerClient3.getAvailableFlights();
				++count;
			}
		});
		threads.add(t2);
		t2.start();
		
		for(int i = 0; i < threads.size(); ++i){
			try
			{
				threads.get(i).join();
			} catch (InterruptedException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		System.out.println("Finished!");
		ManagerClient lastClient = new ManagerClient(orb, "MTL");
		lastClient.login("MTL1111");
		System.out.println(lastClient.getBookedFlightCount(FlightClassEnum.FIRST));
		System.out.println(lastClient.getBookedFlightCount(FlightClassEnum.BUSINESS));
		System.out.println(lastClient.getBookedFlightCount(FlightClassEnum.ECONOMY));
		System.exit(0);
	}
	
}
