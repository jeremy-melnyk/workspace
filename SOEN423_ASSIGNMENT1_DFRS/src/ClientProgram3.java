import java.util.Date;
import java.util.List;
import java.util.Scanner;

import client.ManagerClient;
import client.PassengerClient;
import enums.FlightClass;
import enums.FlightDbOperation;
import enums.FlightParameter;
import models.Address;
import models.City;
import models.Flight;
import models.FlightParameterValues;

public class ClientProgram3
{
	public static void main(String[] args)
	{
		String BASE_URL = "rmi://localhost:1099/";
		Scanner keyboard = new Scanner(System.in);
		while (true)
		{
			System.out.println("To exit anytime, enter -1 at a prompt.");
			System.out.println("Welcome to DFRS! What kind of client are you?");
			System.out.println("1 : Passenger");
			System.out.println("2 : Manager");
			int choice = keyboard.nextInt();
			if(choice == -1){
				break;
			}
			while (true)
			{
				if (choice == 1)
				{
					System.out.println("Which city?");
					System.out.println("1 : MTL");
					System.out.println("2 : WST");
					System.out.println("3 : NDL");
					int city = keyboard.nextInt();
					String cityChoice = "";
					switch (city)
					{
					case 1:
						cityChoice = "MTL";
						break;
					case 2:
						cityChoice = "WST";
						break;
					case 3:
						cityChoice = "NDL";
						break;
					}
					PassengerClient passengerClient = new PassengerClient(BASE_URL + cityChoice);
					System.out.println("What is your first name?");
					String firstName = keyboard.next();
					System.out.println("What is your last name?");
					String lastName = keyboard.next();
					int flightChoice = 1;
					while (true)
					{
						System.out.println("Here are the available flights: ");
						List<Flight> availableFlights = passengerClient.getAvailableFlights();
						passengerClient.displayFlights(availableFlights);
						System.out.println("Book a flight by selecting it's record ID");
						flightChoice = keyboard.nextInt();
						if (flightChoice == -1)
						{
							break;
						}
						Flight chosenFlight = availableFlights.get(flightChoice);
						Address address = new Address("Street", "City", "Province", "PostalCode", "Country");
						if (passengerClient.bookFlight(firstName, lastName, address, "514-678-9890", chosenFlight))
						{
							chosenFlight.acquireSeat();
							System.out.println(chosenFlight + " was booked!");
						} else
						{
							System.out.println(chosenFlight + " could not be booked!");
						}
					}
				} else if (choice == 2)
				{
					System.out.println("Please enter your manager ID");
					String managerId = keyboard.next();
					ManagerClient managerClient = new ManagerClient(BASE_URL);
					managerClient.login(managerId);
					while (true)
					{
						System.out.println("What would you like to do?");
						System.out.println("1 : Get booked flight count");
						System.out.println("2 : Edit flight record");
						int operationChoice = keyboard.nextInt();
						if (operationChoice == -1)
						{
							break;
						}
						switch (operationChoice)
						{
						case 1:
							System.out.println("For which flight class?");
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
							System.out.println(managerClient.getBookedFlightCount(flightClass));
							break;
						case 2:
							System.out.println("What kind of edit?");
							System.out.println("1 : ADD");
							System.out.println("2 : EDIT");
							System.out.println("3 : REMOVE");
							int editChoice = keyboard.nextInt();
							switch (editChoice)
							{
							case 1:
								System.out.println("How many seats?");
								int seats = keyboard.nextInt();
								System.out.println("For which flight class?");
								System.out.println("1 : ECONOMY CLASS");
								System.out.println("2 : BUSINESS CLASS");
								System.out.println("3 : FIRST CLASS");
								int classChoiceEdit = keyboard.nextInt();
								FlightClass flightClassEdit = null;
								switch (classChoiceEdit)
								{
								case 1:
									flightClassEdit = FlightClass.ECONOMY;
									break;
								case 2:
									flightClassEdit = FlightClass.BUSINESS;
									break;
								case 3:
									flightClassEdit = FlightClass.FIRST;
									break;
								}
								System.out.println("Destination?");
								System.out.println("1 : MTL");
								System.out.println("2 : WST");
								System.out.println("3 : NDL");
								int destinationChoiceAdd = keyboard.nextInt();
								City destination = null;
								switch (destinationChoiceAdd)
								{
								case 1:
									destination = new City("Montreal", "MTL");
									break;
								case 2:
									destination = new City("Washington", "WST");
									break;
								case 3:
									destination = new City("NewDelhi", "NDL");
									break;
								}
								System.out.println("Which Date? Enter as an integer i.e. 1000");
								int dateEdit = keyboard.nextInt();
								FlightParameterValues params = new FlightParameterValues();
								params.setSeats(seats);
								params.setDestination(destination);
								params.setFlightClass(flightClassEdit);
								params.setDate(new Date(dateEdit));
								boolean result = managerClient.editFlightRecord(0, FlightDbOperation.ADD,
										FlightParameter.NONE, params);
								if (result)
								{
									System.out.println("Successfully added: " + params);
									managerClient.displayFlights(managerClient.getFlights());
								} else
								{
									System.out.println("Failed to add: " + params);
								}
								break;
							case 2:
								managerClient.displayFlights(managerClient.getFlights());
								System.out.println("Which record Id?");
								int recordId = keyboard.nextInt();
								System.out.println("Which parameter?");
								System.out.println("1 : Seats");
								System.out.println("2 : Date");
								System.out.println("3 : Destination");
								System.out.println("4 : Flight Class");
								int editChoiceForRecord = keyboard.nextInt();
								switch (editChoiceForRecord)
								{
								case 1:
									System.out.println("How many seats?");
									int seatsForRecord = keyboard.nextInt();
									FlightParameterValues paramsSeats = new FlightParameterValues();
									paramsSeats.setSeats(seatsForRecord);
									boolean resultSeats = managerClient.editFlightRecord(recordId,
											FlightDbOperation.EDIT, FlightParameter.SEATS, paramsSeats);
									if (resultSeats)
									{
										System.out.println("Successfully edited: " + paramsSeats);
										managerClient.displayFlights(managerClient.getFlights());
									} else
									{
										System.out.println("Failed to edit: " + paramsSeats);
									}
									break;
								case 2:
									System.out.println("Which Date? Enter as an integer i.e. 1000");
									int dateEditForRecord = keyboard.nextInt();
									FlightParameterValues paramsDate = new FlightParameterValues();
									paramsDate.setDate(new Date(dateEditForRecord));
									boolean resultDate = managerClient.editFlightRecord(recordId,
											FlightDbOperation.EDIT, FlightParameter.DATE, paramsDate);
									if (resultDate)
									{
										System.out.println("Successfully edit: " + paramsDate);
										managerClient.displayFlights(managerClient.getFlights());
									} else
									{
										System.out.println("Failed to edit: " + paramsDate);
									}
									break;
								case 3:
									System.out.println("To which flight class?");
									System.out.println("1 : ECONOMY CLASS");
									System.out.println("2 : BUSINESS CLASS");
									System.out.println("3 : FIRST CLASS");
									int classChoiceEditForRecord = keyboard.nextInt();
									FlightClass flightClassEditForRecord = null;
									switch (classChoiceEditForRecord)
									{
									case 1:
										flightClassEditForRecord = FlightClass.ECONOMY;
										break;
									case 2:
										flightClassEditForRecord = FlightClass.BUSINESS;
										break;
									case 3:
										flightClassEditForRecord = FlightClass.FIRST;
										break;
									}
									FlightParameterValues paramsFlightClass = new FlightParameterValues();
									paramsFlightClass.setFlightClass(flightClassEditForRecord);
									;
									boolean resultDestination = managerClient.editFlightRecord(recordId,
											FlightDbOperation.EDIT, FlightParameter.FLIGHTCLASS, paramsFlightClass);
									if (resultDestination)
									{
										System.out.println("Successfully edit: " + paramsFlightClass);
										managerClient.displayFlights(managerClient.getFlights());
									} else
									{
										System.out.println("Failed to edit: " + paramsFlightClass);
									}
									break;
								}
								break;
							case 3:
								managerClient.displayFlights(managerClient.getFlights());
								System.out.println("Which record Id?");
								int recordIdRemove = keyboard.nextInt();
								FlightParameterValues paramsRemove = new FlightParameterValues();
								boolean resultRemove = managerClient.editFlightRecord(recordIdRemove,
										FlightDbOperation.REMOVE, FlightParameter.FLIGHTCLASS, paramsRemove);
								if (resultRemove)
								{
									System.out.println("Successfully removed: " + recordIdRemove);
									managerClient.displayFlights(managerClient.getFlights());
								} else
								{
									System.out.println("Failed to edit: " + recordIdRemove);
								}
								break;
							}
						}
					}
				}
				break;
			}
		}
		keyboard.close();
		System.exit(0);
	}
}
