import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;

import client.ManagerClient;
import client.PassengerClient;
import enums.FlightClassEnum;
import enums.FlightDbOperation;
import enums.FlightParameter;
import models.Address;
import models.City;
import models.Flight;

public class ClientProgram
{
	public static void main(String[] args)
	{
		List<Thread> threads = new LinkedList<Thread>();
		int mtlId = 1111;
		int wstId = 1111;
		int ndlId = 1111;
		for(int i = 0; i < 3; ++i){
			ManagerClient managerClient = new ManagerClient("rmi://localhost:1099/");
			managerClient.login("MTL" + mtlId++);
			Thread tM = new Thread(() -> {
				for(int j = 0; j < 20; ++j){
					City newCity = new City("Washington", "WST");
					Flight newFlight = new Flight(newCity, new Date(), 2, 2, 2);
					managerClient.editFlightRecord(j, FlightDbOperation.ADD, FlightParameter.NONE , newFlight);
					managerClient.editFlightRecord(j, FlightDbOperation.EDIT, FlightParameter.BUSINESS_CLASS_SEATS, 2);
					managerClient.editFlightRecord(j, FlightDbOperation.EDIT, FlightParameter.FIRST_CLASS_SEATS, 2);
					managerClient.editFlightRecord(j, FlightDbOperation.EDIT, FlightParameter.ECONOMY_CLASS_SEATS, 2);
					System.out.println(managerClient.getBookedFlightCount(FlightClassEnum.FIRST));
					System.out.println(managerClient.getBookedFlightCount(FlightClassEnum.BUSINESS));
					System.out.println(managerClient.getBookedFlightCount(FlightClassEnum.ECONOMY));
				}
			});
			threads.add(tM);
			tM.start();
		}
		for(int i = 0; i < 3; ++i){
			ManagerClient managerClient = new ManagerClient("rmi://localhost:1099/");
			managerClient.login("WST" + wstId++);
			Thread tM = new Thread(() -> {
				for(int j = 0; j < 20; ++j){
					Date date = new GregorianCalendar(2018, Calendar.SEPTEMBER, 20).getTime();
					City newCity = new City("NewDelhi", "NDL");
					Flight newFlight = new Flight(newCity, new Date(), 2, 2, 32);
					managerClient.editFlightRecord(j, FlightDbOperation.ADD, FlightParameter.NONE, newFlight);
					managerClient.editFlightRecord(j, FlightDbOperation.EDIT, FlightParameter.DATE, date);
					managerClient.editFlightRecord(j/2, FlightDbOperation.REMOVE, FlightParameter.NONE, null);
					System.out.println(managerClient.getBookedFlightCount(FlightClassEnum.FIRST));
					System.out.println(managerClient.getBookedFlightCount(FlightClassEnum.BUSINESS));
					System.out.println(managerClient.getBookedFlightCount(FlightClassEnum.ECONOMY));
				}
			});
			threads.add(tM);
			tM.start();
		}
		for(int i = 0; i < 3; ++i){
			ManagerClient managerClient = new ManagerClient("rmi://localhost:1099/");
			managerClient.login("NDL" + ndlId++);		
			Thread tM = new Thread(() -> {
				for(int j = 0; j < 20; ++j){
					City newCity = new City("Montreal", "MTL");
					Flight newFlight = new Flight(newCity, new Date(), 2, 2, 2);
					managerClient.editFlightRecord(j, FlightDbOperation.ADD, FlightParameter.NONE, newFlight);
					managerClient.editFlightRecord(j, FlightDbOperation.EDIT, FlightParameter.DESTINATION, newCity);
					managerClient.editFlightRecord(j/2, FlightDbOperation.REMOVE, FlightParameter.NONE, null);
					System.out.println(managerClient.getBookedFlightCount(FlightClassEnum.FIRST));
					System.out.println(managerClient.getBookedFlightCount(FlightClassEnum.BUSINESS));
					System.out.println(managerClient.getBookedFlightCount(FlightClassEnum.ECONOMY));
				}
			});
			threads.add(tM);
			tM.start();
		}
		
		//long startTime = System.nanoTime();
		PassengerClient passengerClient = new PassengerClient("rmi://localhost:1099/MTL");				
		Thread t0 = new Thread(() -> {
			List<Flight> availableFlights = passengerClient.getAvailableFlights();
			while(!availableFlights.isEmpty()){
				passengerClient.displayFlights(availableFlights);
				Flight chosenFlight = availableFlights.get(0);
				Address address = new Address("Street", "City", "Province", "PostalCode", "Country");
				if(passengerClient.bookFlight("Dominic", "Dan", address, "514-456-7890", chosenFlight, FlightClassEnum.FIRST)){
					System.out.println("Successful booking MTL!");
				}
				if(passengerClient.bookFlight("Dominic", "Dan", address, "514-456-7890", chosenFlight, FlightClassEnum.BUSINESS)){
					System.out.println("Successful booking MTL!");
				}
				if(passengerClient.bookFlight("Dominic", "Dan", address, "514-456-7890", chosenFlight, FlightClassEnum.ECONOMY)){
					System.out.println("Successful booking MTL!");
				}
				availableFlights = passengerClient.getAvailableFlights();
			}
		});
		threads.add(t0);
		t0.start();
		
		PassengerClient passengerClient2 = new PassengerClient("rmi://localhost:1099/WST");				
		Thread t1 = new Thread(() -> {
			List<Flight> availableFlights = passengerClient2.getAvailableFlights();
			while(!availableFlights.isEmpty()){
				passengerClient2.displayFlights(availableFlights);
				Flight chosenFlight = availableFlights.get(0);
				Address address = new Address("Street", "City", "Province", "PostalCode", "Country");
				if(passengerClient2.bookFlight("Alex", "Galchenyuk", address, "514-456-7890", chosenFlight, FlightClassEnum.FIRST)){
					System.out.println("Successful booking WST!");
				}
				if(passengerClient2.bookFlight("Alex", "Galchenyuk", address, "514-456-7890", chosenFlight, FlightClassEnum.BUSINESS)){
					System.out.println("Successful booking WST!");
				}
				if(passengerClient2.bookFlight("Alex", "Galchenyuk", address, "514-456-7890", chosenFlight, FlightClassEnum.ECONOMY)){
					System.out.println("Successful booking WST!");
				}
				availableFlights = passengerClient2.getAvailableFlights();
			}
		});
		threads.add(t1);
		t1.start();
		
		PassengerClient passengerClient3 = new PassengerClient("rmi://localhost:1099/NDL");				
		Thread t2 = new Thread(() -> {
			List<Flight> availableFlights = passengerClient3.getAvailableFlights();
			while(!availableFlights.isEmpty()){
				passengerClient3.displayFlights(availableFlights);
				Flight chosenFlight = availableFlights.get(0);
				Address address = new Address("Street", "City", "Province", "PostalCode", "Country");
				if(passengerClient3.bookFlight("John", "Doe", address, "514-456-7890", chosenFlight, FlightClassEnum.FIRST)){
					System.out.println("Successful booking NDL!");
				}
				if(passengerClient3.bookFlight("John", "Doe", address, "514-456-7890", chosenFlight, FlightClassEnum.BUSINESS)){
					System.out.println("Successful booking NDL!");
				}
				if(passengerClient3.bookFlight("John", "Doe", address, "514-456-7890", chosenFlight, FlightClassEnum.ECONOMY)){
					System.out.println("Successful booking NDL!");
				}
				availableFlights = passengerClient3.getAvailableFlights();
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
		ManagerClient lastClient = new ManagerClient("rmi://localhost:1099/");
		lastClient.login("MTL1111");
		System.out.println(lastClient.getBookedFlightCount(FlightClassEnum.FIRST));
		System.out.println(lastClient.getBookedFlightCount(FlightClassEnum.BUSINESS));
		System.out.println(lastClient.getBookedFlightCount(FlightClassEnum.ECONOMY));
		System.exit(0);
	}
}
