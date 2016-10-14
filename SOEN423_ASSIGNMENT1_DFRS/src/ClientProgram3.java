import java.util.Date;
import java.util.List;
import java.util.Scanner;

import client.ManagerClient;
import client.PassengerClient;
import enums.FlightClassEnum;
import enums.FlightDbOperation;
import enums.FlightParameter;
import models.Address;
import models.City;
import models.Flight;
import models.FlightModificationOperation;
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
						System.out.println("Choose a flight by selecting it's record ID");
						flightChoice = keyboard.nextInt();
						System.out.println("Which flight class would you like to book?");
						System.out.println("1 : ECONOMY CLASS");
						System.out.println("2 : BUSINESS CLASS");
						System.out.println("3 : FIRST CLASS");
						int classChoice = keyboard.nextInt();
						FlightClassEnum flightClass = null;
						switch (classChoice)
						{
						case 1:
							flightClass = FlightClassEnum.ECONOMY;
							break;
						case 2:
							flightClass = FlightClassEnum.BUSINESS;
							break;
						case 3:
							flightClass = FlightClassEnum.FIRST;
							break;
						}
						if (flightChoice == -1)
						{
							break;
						}
						Flight chosenFlight = availableFlights.get(flightChoice);
						Address address = new Address("Street", "City", "Province", "PostalCode", "Country");
						if (passengerClient.bookFlight(firstName, lastName, address, "514-678-9890", chosenFlight, flightClass))
						{
							System.out.println(flightClass.toString() + " class on" + chosenFlight + " was booked!");
						} else
						{
							System.out.println(flightClass.toString() + " class on" + chosenFlight + " could not be booked!");
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
							FlightClassEnum flightClass = null;
							switch (classChoice)
							{
							case 1:
								flightClass = FlightClassEnum.ECONOMY;
								break;
							case 2:
								flightClass = FlightClassEnum.BUSINESS;
								break;
							case 3:
								flightClass = FlightClassEnum.FIRST;
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
								System.out.println("How many seats in First class?");
								int firstClassSeats = keyboard.nextInt();
								System.out.println("How many seats in Business class?");
								int businessClassSeats = keyboard.nextInt();
								System.out.println("How many seats in Economy class?");
								int economyClassSeats = keyboard.nextInt();
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
								FlightParameterValues params = new FlightParameterValues(destination, new Date(dateEdit), firstClassSeats, businessClassSeats, economyClassSeats);
								FlightModificationOperation flightModificationOperation = new FlightModificationOperation(FlightParameter.NONE, FlightClassEnum.FIRST);
								boolean result = managerClient.editFlightRecord(0, FlightDbOperation.ADD, flightModificationOperation, params);
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
								int editChoiceForRecord = keyboard.nextInt();
								switch (editChoiceForRecord)
								{
								case 1:
									System.out.println("For which flight class?");
									System.out.println("1 : ECONOMY CLASS");
									System.out.println("2 : BUSINESS CLASS");
									System.out.println("3 : FIRST CLASS");
									int classChoiceEdit = keyboard.nextInt();
									FlightClassEnum flightClassEdit = null;
									switch (classChoiceEdit)
									{
									case 1:
										flightClassEdit = FlightClassEnum.ECONOMY;
										break;
									case 2:
										flightClassEdit = FlightClassEnum.BUSINESS;
										break;
									case 3:
										flightClassEdit = FlightClassEnum.FIRST;
										break;
									}
									System.out.println("How many seats?");
									int seats = keyboard.nextInt();
									FlightParameterValues paramsSeats = null;
									switch (flightClassEdit)
									{
									case ECONOMY:
										paramsSeats = new FlightParameterValues(null, null, 0, 0, seats);
										break;
									case BUSINESS:
										paramsSeats = new FlightParameterValues(null, null, 0, seats, 0);
										break;
									case FIRST:
										paramsSeats = new FlightParameterValues(null, null, seats, 0, 0);
										break;
									}
									FlightModificationOperation flightModificationOperationEdit = new FlightModificationOperation(FlightParameter.SEATS, flightClassEdit);
									boolean resultSeats = managerClient.editFlightRecord(recordId, FlightDbOperation.EDIT, flightModificationOperationEdit, paramsSeats);
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
									FlightParameterValues paramsDate = new FlightParameterValues(null, new Date(dateEditForRecord), 0, 0, 0);
									FlightModificationOperation flightModificationOperationDate = new FlightModificationOperation(FlightParameter.DATE, FlightClassEnum.FIRST);
									boolean resultDate = managerClient.editFlightRecord(recordId,
											FlightDbOperation.EDIT, flightModificationOperationDate, paramsDate);
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
									System.out.println("To which destination?");
									System.out.println("1 : MTL");
									System.out.println("2 : WST");
									System.out.println("3 : NDL");
									int destinationChoiceEdit = keyboard.nextInt();
									City destinationEdit = null;
									switch (destinationChoiceEdit)
									{
									case 1:
										destinationEdit = new City("Montreal", "MTL");
										break;
									case 2:
										destinationEdit = new City("Washington", "WST");
										break;
									case 3:
										destinationEdit = new City("NewDelhi", "NDL");
										break;
									}
									FlightParameterValues paramsFlightClass = new FlightParameterValues(destinationEdit, new Date(), 0, 0, 0);
									FlightModificationOperation flightModificationOperationDestination = new FlightModificationOperation(FlightParameter.DESTINATION, FlightClassEnum.FIRST);
									boolean resultDestination = managerClient.editFlightRecord(recordId,
											FlightDbOperation.EDIT, flightModificationOperationDestination, paramsFlightClass);
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
								FlightModificationOperation flightModificationOperationRemove = new FlightModificationOperation(FlightParameter.DESTINATION, FlightClassEnum.FIRST);
								boolean resultRemove = managerClient.editFlightRecord(recordIdRemove,
										FlightDbOperation.REMOVE, flightModificationOperationRemove, paramsRemove);
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
