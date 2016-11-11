package udp;

import java.net.DatagramSocket;
import java.net.InetAddress;

import databases.DatabaseRepository;

public abstract class RequestHandler {
	protected final InetAddress address;
	protected final int port;
	protected final String requestData;
	protected final DatagramSocket socket;
	protected final DatabaseRepository databaseRepository;

	public RequestHandler(InetAddress address, int port, String requestData, DatagramSocket socket,
			DatabaseRepository databaseRepository) {
		super();
		this.address = address;
		this.port = port;
		this.requestData = requestData;
		this.socket = socket;
		this.databaseRepository = databaseRepository;
	}

	public abstract void execute();
}
