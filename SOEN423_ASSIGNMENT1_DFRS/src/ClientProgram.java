import java.util.List;

import client.ManagerClient;
import client.PassengerClient;
import enums.FlightClass;
import enums.FlightDbOperation;
import models.Address;
import models.Flight;

public class ClientProgram
{
	public static void main(String[] args)
	{
		int mtlId = 1111;
		int wstId = 1111;
		int ndlId = 1111;
		for(int i = 0; i < 3; ++i){
			ManagerClient managerClient = new ManagerClient("rmi://localhost:1099/");
			managerClient.login("MTL" + mtlId++);
			Flgith
			managerClient.editFlightRecord(0, FlightDbOperation.EDIT, );
		}
		for(int i = 0; i < 3; ++i){
			ManagerClient managerClient = new ManagerClient("rmi://localhost:1099/");
			managerClient.login("WST" + wstId++);
		}
		for(int i = 0; i < 3; ++i){
			ManagerClient managerClient = new ManagerClient("rmi://localhost:1099/");
			managerClient.login("NDL" + ndlId++);		
		}
		
		long startTime = System.nanoTime();
		PassengerClient passengerClient = new PassengerClient("rmi://localhost:1099/MTL");				
		Thread t0 = new Thread(() -> {
			List<Flight> availableFlights = passengerClient.getAvailableFlights();
			while(!availableFlights.isEmpty()){
				passengerClient.displayFlights(availableFlights);
				Flight chosenFlight = availableFlights.get(0);
				Address address = new Address("Street", "City", "Province", "PostalCode", "Country");
				if(passengerClient.bookFlight("John", "Doe", address, "514-456-7890", chosenFlight)){
					System.out.println("Succesful booking MTL!");
				}
				availableFlights = passengerClient.getAvailableFlights();
			}
		});
		t0.start();
		
		Thread tM = new Thread(() -> {
			for(int i = 0; i < 200; ++i){
				System.out.println(managerClient.getBookedFlightCount(FlightClass.FIRST));	
			}
		});
		tM.start();
		
		PassengerClient passengerClient2 = new PassengerClient("rmi://localhost:1099/WST");				
		Thread t1 = new Thread(() -> {
			List<Flight> availableFlights = passengerClient2.getAvailableFlights();
			while(!availableFlights.isEmpty()){
				passengerClient2.displayFlights(availableFlights);
				Flight chosenFlight = availableFlights.get(0);
				Address address = new Address("Street", "City", "Province", "PostalCode", "Country");
				if(passengerClient2.bookFlight("John", "Doe", address, "514-456-7890", chosenFlight)){
					System.out.println("Succesful booking WST!");
				}
				availableFlights = passengerClient2.getAvailableFlights();
			}
		});
		t1.start();
		
		PassengerClient passengerClient3 = new PassengerClient("rmi://localhost:1099/NDL");				
		Thread t2 = new Thread(() -> {
			List<Flight> availableFlights = passengerClient3.getAvailableFlights();
			while(!availableFlights.isEmpty()){
				passengerClient3.displayFlights(availableFlights);
				Flight chosenFlight = availableFlights.get(0);
				Address address = new Address("Street", "City", "Province", "PostalCode", "Country");
				if(passengerClient3.bookFlight("John", "Doe", address, "514-456-7890", chosenFlight)){
					System.out.println("Succesful booking NDL!");
				}
				availableFlights = passengerClient3.getAvailableFlights();
			}
		});
		t2.start();
		try
		{
			t0.join();
			t1.join();
			t2.join();
		} catch (InterruptedException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		long endTime = System.nanoTime();
		long duration = (endTime - startTime);  //divide by 1000000 to get milliseconds.
		System.out.println(duration / 1000000 + "ms");
		/*
	    Scanner keyboard = new Scanner(System.in);
	    int choice = 0;
	    while(choice != -1){
		    choice = keyboard.nextInt();
		    Flight chosenFlight = null;
		    if(choice >= 0 && choice < availableFlights.size()){
			    chosenFlight = availableFlights.get(choice);	
		    } else if (choice == 99){
		    	availableFlights = passengerClient.getAvailableFlights();
				passengerClient.displayFlights(availableFlights);
		    }
			Address address = new Address("Street", "City", "Province", "PostalCode", "Country");
			if(chosenFlight != null){
				if(passengerClient.bookFlight("John", "Doe", address, "514-456-7890", chosenFlight)){
					System.out.println("Succesful booking!");
				}		
			} else {
				System.out.println("Invalid choice");
			}
	    }
	    keyboard.close();
		System.exit(0);
		*/
		/*
		Address address = new Address("Street", "City", "Province", "PostalCode", "Country");
		City montreal = new City("Montreal", "Mtl");
		Date now = new Date();
		passengerClient.bookFlight("John", "Doe", address, "514-456-7890", montreal, now, flight);
		*/
	}
}
