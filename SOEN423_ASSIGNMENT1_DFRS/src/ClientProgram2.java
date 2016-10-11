import java.util.Date;
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
import models.FlightParameterValues;

public class ClientProgram2
{
	public static void main(String[] args)
	{
		List<Thread> threads = new LinkedList<Thread>();

		ManagerClient mtlManager1 = new ManagerClient("rmi://localhost:1099/");
		mtlManager1.login("MTL1111");
		ManagerClient mtlManager2 = new ManagerClient("rmi://localhost:1099/");
		mtlManager2.login("MTL1112");
		ManagerClient mtlManager3 = new ManagerClient("rmi://localhost:1099/");
		mtlManager3.login("MTL1113");
		
		ManagerClient wstManager1 = new ManagerClient("rmi://localhost:1099/");
		wstManager1.login("WST1111");
		ManagerClient wstManager2 = new ManagerClient("rmi://localhost:1099/");
		wstManager2.login("WST1112");
		ManagerClient wstManager3 = new ManagerClient("rmi://localhost:1099/");
		wstManager3.login("WST1113");
		
		ManagerClient ndlManager1 = new ManagerClient("rmi://localhost:1099/");
		ndlManager1.login("NDL1111");
		ManagerClient ndlManager2 = new ManagerClient("rmi://localhost:1099/");
		ndlManager2.login("NDL1112");
		ManagerClient ndlManager3 = new ManagerClient("rmi://localhost:1099/");
		ndlManager3.login("NDL1113");
		
		City mtl = new City("Montreal", "MTL");
		City wst = new City("Washington", "WST");
		City ndl = new City("NewDelhi", "NDL");
		
		int dateCount = 1000;
		
		// MTL 25 seats each class = 75 total
		FlightParameterValues businessToMtl = new FlightParameterValues(FlightClassEnum.BUSINESS, mtl, new Date(dateCount++), 25);
		FlightParameterValues economyToMtl = new FlightParameterValues(FlightClassEnum.ECONOMY, mtl, new Date(dateCount++), 25);
		FlightParameterValues firstToMtl = new FlightParameterValues(FlightClassEnum.FIRST, mtl, new Date(dateCount++), 25);
		
		// WST 50 seats each class = 150 total
		FlightParameterValues businessToWst = new FlightParameterValues(FlightClassEnum.BUSINESS, wst, new Date(dateCount++), 50);
		FlightParameterValues economyToWst = new FlightParameterValues(FlightClassEnum.ECONOMY, wst, new Date(dateCount++), 50);
		FlightParameterValues firstToWst = new FlightParameterValues(FlightClassEnum.ECONOMY, wst, new Date(dateCount++), 50);
		
		// NDL 100 seats each class = 300 total
		FlightParameterValues businessToNdl = new FlightParameterValues(FlightClassEnum.FIRST, ndl, new Date(dateCount++), 100);
		FlightParameterValues economyToNdl = new FlightParameterValues(FlightClassEnum.FIRST, ndl, new Date(dateCount++), 100);
		FlightParameterValues firstToNdl = new FlightParameterValues(FlightClassEnum.FIRST, ndl, new Date(dateCount++), 100);
		
		FlightParameterValues seats0Edit = new FlightParameterValues();
		seats0Edit.setSeats(0);
		
		FlightParameterValues seats50Edit = new FlightParameterValues();
		seats50Edit.setSeats(50);
		
		FlightParameterValues businessEdit = new FlightParameterValues();
		businessEdit.setFlightClass(FlightClassEnum.BUSINESS);
		
		FlightParameterValues economyEdit = new FlightParameterValues();
		economyEdit.setFlightClass(FlightClassEnum.ECONOMY);
		
		FlightParameterValues firstEdit = new FlightParameterValues();
		firstEdit.setFlightClass(FlightClassEnum.FIRST);
		
		FlightParameterValues mtlEdit = new FlightParameterValues();
		mtlEdit.setDestination(mtl);
		
		FlightParameterValues wstEdit = new FlightParameterValues();
		wstEdit.setDestination(wst);
		
		FlightParameterValues ndlEdit = new FlightParameterValues();
		ndlEdit.setDestination(wst);
		
		FlightParameterValues dateEdit = new FlightParameterValues();
		ndlEdit.setDate(new Date(2000));
		
		Thread mtlManagerThread1 = new Thread(() -> {
			mtlManager1.editFlightRecord(0, FlightDbOperation.ADD, FlightParameter.NONE, businessToNdl);
			//mtlManager1.editFlightRecord(0, FlightDbOperation.EDIT, FlightParameter.SEATS, seats0Edit);
			mtlManager1.editFlightRecord(0, FlightDbOperation.EDIT, FlightParameter.FLIGHTCLASS, economyEdit);
			mtlManager1.getBookedFlightCount(FlightClassEnum.BUSINESS);
		});
		threads.add(mtlManagerThread1);
		mtlManagerThread1.start();
		
		Thread mtlManagerThread2 = new Thread(() -> {
			mtlManager2.editFlightRecord(0, FlightDbOperation.ADD, FlightParameter.NONE, economyToNdl);
			//mtlManager2.editFlightRecord(1, FlightDbOperation.EDIT, FlightParameter.SEATS, seats0Edit);
			mtlManager2.editFlightRecord(1, FlightDbOperation.EDIT, FlightParameter.FLIGHTCLASS, economyEdit);
			mtlManager2.getBookedFlightCount(FlightClassEnum.ECONOMY);
		});
		threads.add(mtlManagerThread2);
		mtlManagerThread2.start();
		
		Thread mtlManagerThread3 = new Thread(() -> {
			mtlManager3.editFlightRecord(0, FlightDbOperation.ADD, FlightParameter.NONE, firstToNdl);
			//mtlManager3.editFlightRecord(2, FlightDbOperation.EDIT, FlightParameter.SEATS, seats0Edit);
			mtlManager3.editFlightRecord(2, FlightDbOperation.EDIT, FlightParameter.FLIGHTCLASS, economyEdit);
			mtlManager3.getBookedFlightCount(FlightClassEnum.FIRST);
		});
		threads.add(mtlManagerThread3);
		mtlManagerThread3.start();
		
		Thread wstManagerThread1 = new Thread(() -> {
			wstManager1.editFlightRecord(0, FlightDbOperation.ADD, FlightParameter.NONE, businessToMtl);
			//wstManager1.editFlightRecord(0, FlightDbOperation.EDIT, FlightParameter.SEATS, seats50Edit);
			wstManager1.editFlightRecord(0, FlightDbOperation.EDIT, FlightParameter.FLIGHTCLASS, businessEdit);
			wstManager1.getBookedFlightCount(FlightClassEnum.BUSINESS);
		});
		threads.add(wstManagerThread1);
		wstManagerThread1.start();
		
		Thread wstManagerThread2 = new Thread(() -> {
			wstManager2.editFlightRecord(0, FlightDbOperation.ADD, FlightParameter.NONE, economyToMtl);
			//wstManager2.editFlightRecord(1, FlightDbOperation.EDIT, FlightParameter.SEATS, seats50Edit);
			wstManager2.editFlightRecord(1, FlightDbOperation.EDIT, FlightParameter.FLIGHTCLASS, businessEdit);
			wstManager2.getBookedFlightCount(FlightClassEnum.ECONOMY);
		});
		threads.add(wstManagerThread2);
		wstManagerThread2.start();
		
		Thread wstManagerThread3 = new Thread(() -> {
			wstManager3.editFlightRecord(0, FlightDbOperation.ADD, FlightParameter.NONE, firstToMtl);
			//wstManager3.editFlightRecord(2, FlightDbOperation.EDIT, FlightParameter.SEATS, seats50Edit);
			wstManager3.editFlightRecord(2, FlightDbOperation.EDIT, FlightParameter.FLIGHTCLASS, businessEdit);
			wstManager3.getBookedFlightCount(FlightClassEnum.FIRST);
			wstManager3.editFlightRecord(2, FlightDbOperation.EDIT, FlightParameter.DESTINATION, ndlEdit);
		});
		threads.add(wstManagerThread3);
		wstManagerThread3.start();
		
		Thread ndlManagerThread1 = new Thread(() -> {
			ndlManager1.editFlightRecord(0, FlightDbOperation.ADD, FlightParameter.NONE, businessToWst);
			ndlManager1.editFlightRecord(0, FlightDbOperation.EDIT, FlightParameter.FLIGHTCLASS, firstEdit);
			ndlManager1.getBookedFlightCount(FlightClassEnum.BUSINESS);
			//ndlManager1.editFlightRecord(0, FlightDbOperation.REMOVE, FlightParameter.NONE, firstEdit);
		});
		threads.add(ndlManagerThread1);
		ndlManagerThread1.start();
		
		Thread ndlManagerThread2 = new Thread(() -> {
			ndlManager2.editFlightRecord(0, FlightDbOperation.ADD, FlightParameter.NONE, economyToWst);
			ndlManager2.editFlightRecord(1, FlightDbOperation.EDIT, FlightParameter.FLIGHTCLASS, firstEdit);
			ndlManager2.getBookedFlightCount(FlightClassEnum.ECONOMY);
			//ndlManager2.editFlightRecord(1, FlightDbOperation.REMOVE, FlightParameter.NONE, firstEdit);
		});
		threads.add(ndlManagerThread2);
		ndlManagerThread2.start();
		
		Thread ndlManagerThread3 = new Thread(() -> {
			ndlManager3.editFlightRecord(0, FlightDbOperation.ADD, FlightParameter.NONE, firstToWst);
			ndlManager3.editFlightRecord(2, FlightDbOperation.EDIT, FlightParameter.FLIGHTCLASS, firstEdit);
			ndlManager3.getBookedFlightCount(FlightClassEnum.FIRST);
			//ndlManager3.editFlightRecord(2, FlightDbOperation.REMOVE, FlightParameter.NONE, firstEdit);
			ndlManager3.editFlightRecord(2, FlightDbOperation.EDIT, FlightParameter.DATE, dateEdit);
		});
		threads.add(ndlManagerThread3);
		ndlManagerThread3.start();
		
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
		
		// Passengers booking
		PassengerClient passengerClientMtl = new PassengerClient("rmi://localhost:1099/MTL");
		PassengerClient passengerClientWst = new PassengerClient("rmi://localhost:1099/WST");
		PassengerClient passengerClientNdl = new PassengerClient("rmi://localhost:1099/NDL");
		
		Thread clientMtl = new Thread(() -> {
			List<Flight> availableFlights = passengerClientMtl.getAvailableFlights();
			while(!availableFlights.isEmpty()){
				Flight chosenFlight = availableFlights.get(0);
				Address address = new Address("Street", "City", "Province", "PostalCode", "Country");
				if(passengerClientMtl.bookFlight("Dominic", "Dan", address, "514-456-7890", chosenFlight)){
					System.out.println("Successful booking MTL!");
				} else {
					System.out.println("Failed booking MTL!");
				}
				availableFlights = passengerClientMtl.getAvailableFlights();
			}
		});
		threads.add(clientMtl);
		clientMtl.start();
		
		Thread clientWst = new Thread(() -> {
			List<Flight> availableFlights = passengerClientWst.getAvailableFlights();
			while(!availableFlights.isEmpty()){
				Flight chosenFlight = availableFlights.get(0);
				Address address = new Address("Street", "City", "Province", "PostalCode", "Country");
				if(passengerClientWst.bookFlight("Alex", "Galchenyuk", address, "514-456-7890", chosenFlight)){
					System.out.println("Successful booking WST!");
				} else {
					System.out.println("Failed booking WST!");
				}
				availableFlights = passengerClientWst.getAvailableFlights();
			}
		});
		threads.add(clientWst);
		clientWst.start();
		
		Thread clientNdl = new Thread(() -> {
			List<Flight> availableFlights = passengerClientNdl.getAvailableFlights();
			while(!availableFlights.isEmpty()){
				Flight chosenFlight = availableFlights.get(0);
				Address address = new Address("Street", "City", "Province", "PostalCode", "Country");
				if(passengerClientNdl.bookFlight("Bob", "Bizzle", address, "514-456-7890", chosenFlight)){
					System.out.println("Successful booking NDL!");
				} else {
					System.out.println("Failed booking NDL!");
				}
				availableFlights = passengerClientNdl.getAvailableFlights();
			}
		});
		threads.add(clientNdl);
		clientNdl.start();
		
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
		System.exit(0);
	}
}
