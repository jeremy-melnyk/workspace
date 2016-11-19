package client;

import java.rmi.RemoteException;
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
import enums.FlightDbOperation;
import enums.FlightParameter;
import enums.LogOperation;
import log.CustomLogger;
import log.ILogger;
import log.TextFileLog;
import models.City;
import models.Flight;
import models.FlightClassOperation;
import models.FlightRecordOperation;
import server.IFlightReservationServer;

public class ManagerClient extends Client
{
	private String managerId;
	ILogger logger;
	
	public ManagerClient(ORB orb, String serverAcronym) {
		super(orb, serverAcronym);
		this.managerId = null;
		this.logger = new CustomLogger(new TextFileLog());
	}
	
	public boolean login(String managerId){
		this.managerId = managerId;
		String cityAcronym = managerId.substring(0, 3);
		try
		{
			org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");
			NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);
			FlightReservationServer flightReservationServer = null;
			switch(cityAcronym){
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
			String[] managerIds = flightReservationServer.getManagerIds();
			boolean containsId = false;
			for (String s : managerIds){
				if (s.equals(managerId)){
					containsId = true;
					break;
				}
			}
			if (containsId){
				this.managerId = managerId;
				this.logger.log(managerId, LogOperation.LOGIN.name(), managerId);
				return true;
			}
			return false;
		} catch (Exception e)
		{
			this.logger.log(managerId, LogOperation.EXCEPTION.name(), e.getMessage());	
			return false;
		}
	}
	
	public void logout(){
		System.out.println("Logged out of: " + this.managerId);
		this.managerId = null;
	}
	
	public String getBookedFlightCount(FlightClassEnum flightClass)
	{
		if(this.managerId == null){
			System.out.println("No manager is logged in");
		}
		String cityAcronym = this.managerId.substring(0, 3);
		try
		{
			org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");
			NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);
			FlightReservationServer flightReservationServer = null;
			switch(cityAcronym){
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
			String flightClassOperationAsString = this.managerId + "-" + flightClass.name();
			return flightReservationServer.getBookedFlightCount(flightClassOperationAsString);
		} catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
	
	public boolean transferReservation(int recordId, City otherCity)
	{
		if(this.managerId == null){
			System.out.println("No manager is logged in");
		}
		String cityAcronym = this.managerId.substring(0, 3);
		try
		{
			org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");
			NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);
			FlightReservationServer flightReservationServer = null;
			City currentCity = null;
			switch(cityAcronym){
			case "MTL":
				flightReservationServer = FlightReservationServerHelper.narrow(ncRef.resolve_str("MontrealServer"));
				currentCity = new City("MTL-Montreal");
				break;
			case "WST":
				flightReservationServer = FlightReservationServerHelper.narrow(ncRef.resolve_str("WashingtonServer"));
				currentCity = new City("WST-Washington");
				break;
			case "NDL":
				flightReservationServer = FlightReservationServerHelper.narrow(ncRef.resolve_str("NewDelhiServer"));
				currentCity = new City("NDL-NewDelhi");
				break;
			default:
				break;
			}
			String passengerRecordId = this.managerId + "-" + recordId;
			if(recordId == -1){
				return false;
			}
			return flightReservationServer.transferReservation(passengerRecordId, currentCity.formatToString(), otherCity.formatToString());
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean editFlightRecord(int recordId, FlightDbOperation operation, FlightParameter flightParameter, String newValue){
		if(managerId == null){
			this.logger.log(managerId, LogOperation.BAD_LOGIN.name(), "No ManagerId was set.");
			return false;
		}
		
		if(operation == null){
			this.logger.log(managerId, LogOperation.ILLEGAL_ARGUMENT_EXCEPTION.name(), "No FlightDbOperation was set.");
			return false;
		}
		
		if(flightParameter == null){
			this.logger.log(managerId, LogOperation.ILLEGAL_ARGUMENT_EXCEPTION.name(), "No FlightParameter was set.");
			return false;
		}
		
		try
		{
			String cityAcronym = this.managerId.substring(0, 3);
			org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");
			NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);
			FlightReservationServer flightReservationServer = null;
			switch(cityAcronym){
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
			String recordOperationAsString = this.managerId + "-" + recordId + "-" + operation.name();
			FlightRecordOperation recordOperation = new FlightRecordOperation(this.managerId, recordId, operation);
			String flightParameterAsString = flightParameter.name();
			switch(operation){
			case ADD:
				try{
					Flight flight = new Flight(newValue);
					if(cityAcronym.equals(flight.getDestination().getAcronym())){
						this.logger.log(managerId, LogOperation.ADD_FLIGHT.name() + " [Fail]", "Cannot add current city as destination : " + flight.toString());
						return false;
					}
					boolean result = flightReservationServer.editFlightRecord(recordOperationAsString, flightParameterAsString, newValue);
					if(result){
						this.logger.log(managerId, LogOperation.ADD_FLIGHT.name() + " [SUCCESS]", flight.toString());	
					} else {
						this.logger.log(managerId, LogOperation.ADD_FLIGHT.name() + " [FAIL]", flight.toString());	
					}
					return result;
				}catch(Exception e){
					this.logger.log(managerId, LogOperation.EXCEPTION.name(), e.getMessage());	
				}
			case EDIT:
				try{
					boolean result = flightReservationServer.editFlightRecord(recordOperationAsString, flightParameterAsString, newValue);
					if(result){
						this.logger.log(managerId, LogOperation.EDIT_FLIGHT.name() + " [SUCCESS]", recordOperation.getRecordId() + "");	
					} else {
						this.logger.log(managerId, LogOperation.EDIT_FLIGHT.name() + " [FAIL]", recordOperation.getRecordId() + "");	
					}
					return result;
				}catch(Exception e){
					this.logger.log(managerId, LogOperation.REMOTE_EXCEPTION.name(), e.getMessage());	
				}
			case REMOVE:
				try{
					boolean result = flightReservationServer.editFlightRecord(recordOperationAsString, flightParameterAsString, "");
					if(result){
						this.logger.log(managerId, LogOperation.REMOVE_FLIGHT.name() + " [SUCCESS]", recordOperation.getRecordId() + "");	
					} else {
						this.logger.log(managerId, LogOperation.REMOVE_FLIGHT.name() + " [FAIL]", recordOperation.getRecordId() + "");	
					}
					return result;
				}catch(Exception e){
					this.logger.log(managerId, LogOperation.REMOTE_EXCEPTION.name(), e.getMessage());	
				}
			default:
				this.logger.log(managerId, LogOperation.UNKNOWN.name(), operation.name());
				return false;	
			}
		} catch (Exception e)
		{
			e.printStackTrace();
			this.logger.log(managerId, LogOperation.EXCEPTION.name(), e.getMessage());
			return false;
		}
	}
	
	public void displayList(String[] items){
		if(items == null){
			return;
		}
		
		for(String item : items){
			System.out.println(item);
		}
	}
	
	public String[] getFlights(){
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
			String[] flights = flightReservationServer.getFlights();
			return flights;
		} catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
	
	public String[] getPassengerRecords(){
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
			String[] records = flightReservationServer.getPassengerRecords();
			return records;
		} catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
}
