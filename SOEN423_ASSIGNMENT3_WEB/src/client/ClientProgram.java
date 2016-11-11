package client;

import enums.City;

public class ClientProgram {

	public static void main(String[] args) {
		ManagerClient managerMtl = new ManagerClient(City.MTL, "Moose", "Daniel", 1111);
		ManagerClient managerWst = new ManagerClient(City.WST, "Crook", "Peter", 1111);
		ManagerClient managerNdl = new ManagerClient(City.NDL, "Adams", "Samuel", 1111);
		
		PassengerClient passengerMtl = new PassengerClient(City.MTL, "Doe", "John", "1000 Somewhere Land|Montreal|QC|H1B 5U0|Canada", "514-123-4567");
		passengerMtl.bookFlight("WST", "5-Jun-2016", "FIRST");
		
		PassengerClient passengerWst = new PassengerClient(City.WST, "Bell", "Graham", "5000 Donald Trump|Washington|DC|12345|United States", "648-123-4567");
		passengerWst.bookFlight("MTL", "5-Jun-2016", "FIRST");
			
		String[] passengerRecords = managerMtl.getPassengerRecords();
		for (String passengerRecord : passengerRecords){
			System.out.println(passengerRecord);
		}
		
		managerMtl.getBookedFlightCount("FIRST");
		managerMtl.editFlightRecord("EDIT|0", "ORIGIN", "WST");
		managerMtl.editFlightRecord("EDIT|0", "DESTINATION", "MTL");
		managerMtl.editFlightRecord("EDIT|0", "DATE", "10-Jul-2017");
		managerMtl.editFlightRecord("EDIT|0", "SEATS", "FIRST|30");
		managerMtl.editFlightRecord("EDIT|0", "SEATS", "FIRST|10");
		managerMtl.editFlightRecord("ADD|0", "NONE", "MTL|NDL|15-Jul-2017|5|5|5");
		managerMtl.editFlightRecord("REMOVE|0", "NONE", "");
		managerMtl.getBookedFlightCount("FIRST");
	}
}
