import java.util.HashMap;
import java.util.List;

import client.Client;
import models.City;
import models.PassengerRecord;

public class ManagerClient extends Client {
	public String getBookedFlightCount(List<PassengerRecord> passengerRecords){
		HashMap<String, Integer> flightCounts =  this.computeFlightCounts(passengerRecords);
		// TODO: Implement formatting for returned string with StringBuilder
		return null;		
	}
	
	private HashMap<String, Integer> computeFlightCounts(List<PassengerRecord> passengerRecords){
		HashMap<String, Integer> flightCounts = new HashMap<String, Integer>();	
		for (PassengerRecord passengerRecord : passengerRecords){
			City city = passengerRecord.getDestination();
			String cityAcronym = city.getAcronym();
			if (flightCounts.containsKey(cityAcronym)){
				flightCounts.put(cityAcronym, 1);
			} else {
				Integer count = flightCounts.get(cityAcronym);
				++count;
			}
		}		
		return flightCounts;
	}
}
