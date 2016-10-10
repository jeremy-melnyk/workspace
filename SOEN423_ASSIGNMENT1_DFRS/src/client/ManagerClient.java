package client;

import java.rmi.RemoteException;
import java.util.List;

import enums.FlightClass;
import enums.FlightDbOperation;
import models.Flight;
import models.FlightClassOperation;
import models.RecordOperation;
import server.IFlightReservationServer;

public class ManagerClient extends Client
{
	private String managerId;
	
	public ManagerClient(String baseUrl) {
		super(baseUrl);
		this.managerId = null;
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
				System.out.println("Logged in as: " + managerId);
				return true;
			}
			return false;
		} catch (RemoteException e)
		{
			System.out.println(e.getMessage());
			return false;
		} catch (Exception e)
		{
			System.out.println(e.getMessage());
			return false;
		}
	}
	
	public void logout(){
		System.out.println("Logged out of: " + this.managerId);
		this.managerId = null;
	}
	
	public String getBookedFlightCount(FlightClass flightClass)
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
	
	public boolean editFlightRecord(int recordId, FlightDbOperation operation, Flight flight){
		if(managerId == null){
			System.out.println("No manager is logged in!");
			return false;
		}
		
		try
		{
			String cityAcronym = this.managerId.substring(0, 3);
			IFlightReservationServer flightReservationServer = ServerLocator.locateServer(this.baseUrl + cityAcronym);
			RecordOperation recordOperation = new RecordOperation(this.managerId, recordId);
			return flightReservationServer.editFlightRecord(recordOperation, operation, flight);
		} catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}
	}
}
