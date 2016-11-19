import java.util.Date;
import java.util.List;
import java.util.Scanner;

import org.omg.CORBA.ORB;

import client.ManagerClient;
import client.PassengerClient;
import enums.FlightClassEnum;
import enums.FlightDbOperation;
import enums.FlightParameter;
import models.Address;
import models.City;
import models.Flight;

public class ConsoleProgram
{
	public static void main(String[] args)
	{
		ORB orb = ORB.init(args, null);
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
					PassengerClient passengerClient = new PassengerClient(orb, cityChoice);
					System.out.println("What is your first name?");
					String firstName = keyboard.next();
					System.out.println("What is your last name?");
					String lastName = keyboard.next();
					int flightChoice = 1;
					while (true)
					{
						System.out.println("Here are the available flights: ");
						String[] availableFlights = passengerClient.getAvailableFlights();
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
						Address address = new Address("Street", "City", "Province", "PostalCode", "Country");
						if (passengerClient.bookFlight(firstName, lastName, address, "514-678-9890", flightChoice, flightClass))
						{
							System.out.println("Booked " + flightClass.name() + " class on " + flightChoice);
						} else
						{
							System.out.println("Unable to book " + flightClass.name() + " class on " + flightChoice);
						}
					}
				} else if (choice == 2)
				{
					System.out.println("Please enter your manager ID");
					String managerId = keyboard.next();
					String cityAcronym = managerId.substring(0,3);
					ManagerClient managerClient = new ManagerClient(orb, cityAcronym);
					managerClient.login(managerId);
					while (true)
					{
						System.out.println("What would you like to do?");
						System.out.println("1 : Get booked flight count");
						System.out.println("2 : Edit flight record");
						System.out.println("3 : Transfer reservation");
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
								Flight newFlight = new Flight(destination, new Date(dateEdit), firstClassSeats, businessClassSeats, economyClassSeats);
								boolean result = managerClient.editFlightRecord(0, FlightDbOperation.ADD, FlightParameter.NONE, newFlight.formatToString());
								if (result)
								{
									System.out.println("Successfully added: " + newFlight);
									managerClient.displayList(managerClient.getFlights());
								} else
								{
									System.out.println("Failed to add: " + newFlight);
								}
								break;
							case 2:
								managerClient.displayList(managerClient.getFlights());
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
									FlightParameter flightParameter = null;
									switch (classChoiceEdit)
									{
									case 1:
										flightParameter = FlightParameter.ECONOMY_CLASS_SEATS;
										break;
									case 2:
										flightParameter = FlightParameter.BUSINESS_CLASS_SEATS;
										break;
									case 3:
										flightParameter = FlightParameter.FIRST_CLASS_SEATS;
										break;
									}
									System.out.println("How many seats?");
									int seats = keyboard.nextInt();
									boolean resultSeats = managerClient.editFlightRecord(recordId, FlightDbOperation.EDIT, flightParameter, Integer.toString(seats));
									if (resultSeats)
									{
										System.out.println("Successfully edited: " + recordId + "");
										managerClient.displayList(managerClient.getFlights());
									} else
									{
										System.out.println("Failed to edit: " + recordId + "");
									}
									break;
								case 2:
									System.out.println("Which Date? Enter as an integer i.e. 1000");
									int dateEditForRecord = keyboard.nextInt();
									boolean resultDate = managerClient.editFlightRecord(recordId, FlightDbOperation.EDIT, FlightParameter.DATE, new Date(dateEditForRecord).toLocaleString());
									if (resultDate)
									{
										System.out.println("Successfully edited: " + recordId + "");
										managerClient.displayList(managerClient.getFlights());
									} else
									{
										System.out.println("Failed to edit: " + recordId + "");
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
									boolean resultDestination = managerClient.editFlightRecord(recordId, FlightDbOperation.EDIT, FlightParameter.DESTINATION, destinationEdit.formatToString());
									if (resultDestination)
									{
										System.out.println("Successfully edit: " + recordId + "");
										managerClient.displayList(managerClient.getFlights());
									} else
									{
										System.out.println("Failed to edit: " + recordId + "");
									}
									break;
								}
								break;
							case 3:
								managerClient.displayList(managerClient.getFlights());
								System.out.println("Which record Id?");
								int recordIdRemove = keyboard.nextInt();
								boolean resultRemove = managerClient.editFlightRecord(recordIdRemove, FlightDbOperation.REMOVE, FlightParameter.NONE, null);
								if (resultRemove)
								{
									System.out.println("Successfully removed: " + recordIdRemove);
									managerClient.displayList(managerClient.getFlights());
								} else
								{
									System.out.println("Failed to edit: " + recordIdRemove);
								}
								break;
							}
						case 3:
							System.out.println("Select a record Id to transfer:");
							managerClient.displayList(managerClient.getPassengerRecords());
							int passengerRecordId = keyboard.nextInt();
							System.out.println("To which destination?");
							System.out.println("1 : MTL");
							System.out.println("2 : WST");
							System.out.println("3 : NDL");
							int destinationChoiceTransfer = keyboard.nextInt();
							City destinationTransfer = null;
							switch (destinationChoiceTransfer)
							{
							case 1:
								destinationTransfer = new City("Montreal", "MTL");
								break;
							case 2:
								destinationTransfer = new City("Washington", "WST");
								break;
							case 3:
								destinationTransfer = new City("NewDelhi", "NDL");
								break;
							}
							if(passengerRecordId == -1){
								break;
							}
							boolean resultTransfer = managerClient.transferReservation(passengerRecordId, destinationTransfer);
							if (resultTransfer)
							{
								System.out.println("Successfully transferred: " + passengerRecordId);
								managerClient.displayList(managerClient.getPassengerRecords());
							} else
							{
								System.out.println("Failed to transfer: " + passengerRecordId);
							}
							break;
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
