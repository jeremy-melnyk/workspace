package client;

import java.rmi.RemoteException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import enums.FlightClassEnum;
import enums.FlightDbOperation;
import enums.LogOperation;
import log.CustomLogger;
import log.ILogger;
import log.TextFileLog;
import models.Flight;
import models.FlightClassOperation;
import models.FlightModificationOperation;
import models.FlightParameterValues;
import models.FlightRecordOperation;
import server.IFlightReservationServer;

public class ManagerClient extends Client
{
	private String managerId;
	ILogger logger;
	
	public ManagerClient(String baseUrl) {
		super(baseUrl);
		this.managerId = null;
		this.logger = new CustomLogger(new TextFileLog());
	}
	
	public boolean login(String managerId){
		String cityAcronym = managerId.substring(0, 3);
		IFlightReservationServer flightReservationServer;
		try
		{
			flightReservationServer = ServerLocator.locateServer(this.baseUrl + cityAcronym);
			List<String> managerIds = flightReservationServer.getManagerIds();
			if (managerIds.contains(managerId)){
				this.managerId = managerId;
				this.logger.log(managerId, LogOperation.LOGIN.name(), managerId);
				return true;
			}
			return false;
		} catch (RemoteException e)
		{
			this.logger.log(managerId, LogOperation.REMOTE_EXCEPTION.name(), e.getMessage());	
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
			IFlightReservationServer flightReservationServer = ServerLocator.locateServer(this.baseUrl + cityAcronym);
			FlightClassOperation flightClassOperation = new FlightClassOperation(this.managerId, flightClass);
			return flightReservationServer.getBookedFlightCount(flightClassOperation);
		} catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
	
	public boolean editFlightRecord(int recordId, FlightDbOperation operation, FlightModificationOperation flightOperationModification, FlightParameterValues flightParameters){
		if(managerId == null){
			this.logger.log(managerId, LogOperation.BAD_LOGIN.name(), "No ManagerId was set.");
			return false;
		}
		
		if(operation == null){
			this.logger.log(managerId, LogOperation.ILLEGAL_ARGUMENT_EXCEPTION.name(), "No FlightDbOperation was set.");
			return false;
		}
		
		if(flightParameters == null){
			this.logger.log(managerId, LogOperation.ILLEGAL_ARGUMENT_EXCEPTION.name(), "No FlightParameters were set.");
			return false;
		}
		
		try
		{
			String cityAcronym = this.managerId.substring(0, 3);
			IFlightReservationServer flightReservationServer = ServerLocator.locateServer(this.baseUrl + cityAcronym);
			FlightRecordOperation recordOperation = new FlightRecordOperation(this.managerId, recordId, operation);
			switch(operation){
			case ADD:
				try{;
					boolean result = flightReservationServer.editFlightRecord(recordOperation, flightOperationModification, flightParameters);
					if(result){
						this.logger.log(managerId, LogOperation.ADD_FLIGHT.name() + " [SUCCESS]", flightParameters.toString());	
					} else {
						this.logger.log(managerId, LogOperation.ADD_FLIGHT.name() + " [FAIL]", flightParameters.toString());	
					}
					return result;
				}catch(RemoteException e){
					this.logger.log(managerId, LogOperation.REMOTE_EXCEPTION.name(), e.getMessage());	
				}
			case EDIT:
				try{
					boolean result = flightReservationServer.editFlightRecord(recordOperation, flightOperationModification, flightParameters);
					if(result){
						this.logger.log(managerId, LogOperation.EDIT_FLIGHT.name() + " [SUCCESS]", recordOperation.getRecordId() + " : " + flightParameters.toString());	
					} else {
						this.logger.log(managerId, LogOperation.EDIT_FLIGHT.name() + " [FAIL]", recordOperation.getRecordId() + " : " + flightParameters.toString());	
					}
					return result;
				}catch(RemoteException e){
					this.logger.log(managerId, LogOperation.REMOTE_EXCEPTION.name(), e.getMessage());	
				}
			case REMOVE:
				try{
					boolean result = flightReservationServer.editFlightRecord(recordOperation, flightOperationModification, flightParameters);
					if(result){
						this.logger.log(managerId, LogOperation.REMOVE_FLIGHT.name() + " [SUCCESS]", recordOperation.getRecordId() + " : " + flightParameters.toString());	
					} else {
						this.logger.log(managerId, LogOperation.REMOVE_FLIGHT.name() + " [FAIL]", recordOperation.getRecordId() + " : " + flightParameters.toString());	
					}
					return result;
				}catch(RemoteException e){
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
	
	public void displayFlights(List<Flight> flights){
		if(flights == null){
			return;
		}
		
		for(Flight flight : flights){
			System.out.println(flight);
		}
	}
	
	public List<Flight> getFlights(){
		try
		{
			String cityAcronym = this.managerId.substring(0, 3);
			final ExecutorService executorService = Executors.newFixedThreadPool(3);
			final Future<List<Flight>> flights = executorService.submit(new GetFlightsTask(this.baseUrl + cityAcronym));
			return flights.get();
		} catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
}
