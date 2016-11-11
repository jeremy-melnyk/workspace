package udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.concurrent.Callable;

import enums.FlightClass;
import enums.UdpRequestType;
import models.FlightServerAddress;

public class BookedFlightCountTask implements Callable<String> {
	private final String DELIMITER = "|";
	private final int BUFFER_SIZE = 1000;
	private final FlightServerAddress flightServerAddress;
	private final FlightClass flightClass;

	public BookedFlightCountTask(FlightServerAddress flightServerAddress, FlightClass flightClass) {
		this.flightServerAddress = flightServerAddress;
		this.flightClass = flightClass;
	}

	@Override
	public String call() {
		return getBookedFlightCount();
	}

	private String getBookedFlightCount() {
		DatagramSocket socket = null;
		try {
			socket = new DatagramSocket();
			String messageToSend = UdpRequestType.BOOKED_FLIGHTCOUNT + DELIMITER + flightClass.name();
			byte[] message = messageToSend.getBytes();
			InetAddress host = InetAddress.getByName(flightServerAddress.getHost());
			int serverPort = flightServerAddress.getPort();
			DatagramPacket request = new DatagramPacket(message, message.length, host, serverPort);
			socket.send(request);
			byte[] buffer = new byte[BUFFER_SIZE];
			DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
			socket.receive(reply);
			String replyAsString = new String(reply.getData()).trim();
			int flightCount = Integer.parseInt(replyAsString);
			return flightServerAddress.getCity() + " " + flightCount;
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			socket.close();
		}
		return "Error";
	}
}