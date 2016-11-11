package udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

import databases.DatabaseRepository;
import databases.FlightReservationDb;
import enums.FlightClass;

public class TransferReservationHandler extends RequestHandler {

	public TransferReservationHandler(InetAddress address, int port, String requestData, DatagramSocket socket,
			DatabaseRepository databaseRepository) {
		super(address, port, requestData, socket, databaseRepository);
	}

	@Override
	public void execute() {
		try {
			// TODO : Implement transfer reservation protocol
			FlightClass flightClass = FlightClass.valueOf(requestData);
			FlightReservationDb flightReservationDb = databaseRepository.getFlightReservationDb();
			int bookedFlightCount = flightReservationDb.getFlightReservationCount(flightClass);
			String bookedFlightCountAsString = Integer.toString(bookedFlightCount);
			byte[] message = bookedFlightCountAsString.getBytes();
			DatagramPacket reply = new DatagramPacket(message, message.length, address, port);
			socket.send(reply);
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}