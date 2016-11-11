package server;

import java.util.HashMap;
import java.util.Map.Entry;

import javax.xml.ws.Endpoint;

public class PublishingServer {

	public static void main(String[] args) {
		DistributedServer server = new DistributedServer();
		HashMap<String, FlightServer> flightServers = server.init();
		for(Entry<String, FlightServer> flightServerEntry : flightServers.entrySet()){
			String key = flightServerEntry.getKey();
			FlightServer flightServer = flightServerEntry.getValue();
			String endpointName = "http://localhost:8080/" + key;
			Endpoint endpoint = Endpoint.publish(endpointName, flightServer);
			System.out.println(endpointName + " isPublished: " + endpoint.isPublished());	
		}
	}
}
