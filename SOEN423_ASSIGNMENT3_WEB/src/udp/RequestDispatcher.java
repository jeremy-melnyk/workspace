package udp;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

import databases.DatabaseRepository;
import enums.UdpRequestType;

public class RequestDispatcher implements Runnable {
	private final String DELIMITER = "\\|";
	private final DatagramSocket socket;
	private final DatagramPacket packet;
	private final DatabaseRepository databaseRepository;

	public RequestDispatcher(DatagramSocket socket, DatagramPacket packet, DatabaseRepository databaseRepository) {
		super();
		this.socket = socket;
		this.packet = packet;
		this.databaseRepository = databaseRepository;
	}

	@Override
	public void run() {
		handleRequest();
	}

	private void handleRequest() {
		String message = new String(packet.getData());
		Request request = parseMessage(message);
		String requestData = request.getData().trim();
		UdpRequestType requestType = request.getRequestType();
		switch (requestType) {
		case BOOKED_FLIGHTCOUNT:
			new BookedFlightCountHandler(packet.getAddress(), packet.getPort(), requestData, socket, databaseRepository).execute();
			break;
		case TRANSFER_RESERVATION:
			new TransferReservationHandler(packet.getAddress(), packet.getPort(), requestData, socket, databaseRepository).execute();
			break;
		}
	}

	private Request parseMessage(String message) {
		String[] tokens = message.split(DELIMITER);
		UdpRequestType requestType = UdpRequestType.valueOf(tokens[0]);
		String data = tokens[1];
		return new Request(requestType, data);
	}
}
