package client;

import java.util.Scanner;

import enums.City;
import enums.FlightClass;

public class ConsoleProgram {

	public static void main(String[] args) {
		Scanner keyboard = new Scanner(System.in);
		System.out.println("To exit anytime, enter -1 at a prompt.");
		System.out.println("Welcome to DFRS!");
		clientSelect(keyboard);
	}
	
	public static void clientSelect(Scanner keyboard){
		while(true){
			System.out.println("What kind of client are you?");
			System.out.println("1 : Passenger");
			System.out.println("2 : Manager");
			int choice = keyboard.nextInt();
			switch(choice){
				case 1:
					passengerSelect(keyboard);
					break;
				case 2:
					//managerSelect(keyboard);
					break;
			}
			if(choice == -1){
				break;
			}
		}
	}
	
	public static void passengerSelect(Scanner keyboard){
		City city = citySelect(keyboard);
		if(city == null){
			System.out.println("Invalid Choice");
			return;
		}
		String firstName = stringSelect(keyboard, "first name");
		String lastName = stringSelect(keyboard, "last name");
		// Default address & phoneNumber
		String address = "1000 Somewhere Land|Montreal|QC|H1B 5U0|Canada";
		String phoneNumber = "514-123-4567";
		PassengerClient client = new PassengerClient(city, lastName, firstName, address, phoneNumber);
		flightSelect(keyboard, client);	
	}
		
	public static City citySelect(Scanner keyboard){
		System.out.println("Which city?");
		System.out.println("1 : MTL");
		System.out.println("2 : WST");
		System.out.println("3 : NDL");
		int choice = keyboard.nextInt();
		City city = null;
		switch (choice)
		{
		case 1:
			city = City.MTL;
			break;
		case 2:
			city = City.WST;
			break;
		case 3:
			city = City.NDL;
			break;
		}
		return city;
	}
	
	public static String stringSelect(Scanner keyboard, String parameter){
		System.out.println("Whats is your " + parameter + "?");
		String value = keyboard.next();
		return value;
	}
	
	public static String dateSelect(Scanner keyboard){
		System.out.println("Which date?");
		String date = keyboard.next();
		return date;
	}
	
	public static void flightSelect(Scanner keyboard, PassengerClient passengerClient) {
		while(true){
			System.out.println("Here are the existing flights you can book: ");
			String[] flightRecords = passengerClient.getFlightRecords();
			display(flightRecords);
			String date = dateSelect(keyboard);
			try{
				if (Integer.parseInt(date) == -1){
					return;
				}
			} catch(Exception e) {
				// Continue
			}
			City destination = citySelect(keyboard);
			if(destination == null){
				System.out.println("Invalid Choice");
				return;
			}
			System.out.println("Which flight class?");
			System.out.println("1 : ECONOMY CLASS");
			System.out.println("2 : BUSINESS CLASS");
			System.out.println("3 : FIRST CLASS");
			int classChoice = keyboard.nextInt();
			FlightClass flightClass = null;
			switch (classChoice)
			{
			case 1:
				flightClass = FlightClass.ECONOMY;
				break;
			case 2:
				flightClass = FlightClass.BUSINESS;
				break;
			case 3:
				flightClass = FlightClass.FIRST;
				break;
			}
			if(classChoice == -1){
				break;
			}
			passengerClient.bookFlight(destination.toString(), date, flightClass.toString());
		}	
	}
	
	public static void display(String[] items){
		for (String item : items){
			System.out.println(item);
		}
	}
}
