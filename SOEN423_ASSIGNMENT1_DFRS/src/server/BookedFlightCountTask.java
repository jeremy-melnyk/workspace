package server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.concurrent.Callable;

import enums.FlightClass;
import enums.LogOperation;
import models.FlightCountResult;
import models.FlightServerAddress;
import server.IFlightReservationServer;

public class BookedFlightCountTask implements Callable<FlightCountResult>
{
	private final int BUFFER_SIZE = 1000;
	private final FlightServerAddress flightServerAddress;
	private final FlightClass flightClass;
	
	public BookedFlightCountTask(FlightClass flightClass, FlightServerAddress flightServerAddress){
		this.flightClass = flightClass;
		this.flightServerAddress = flightServerAddress;
	}

	@Override
	public FlightCountResult call() throws Exception
	{
		return getBookedFlightCount(this.flightClass, this.flightServerAddress);
	}

	private FlightCountResult getBookedFlightCount(FlightClass flightClass, FlightServerAddress flightServerAddress) throws Exception{
		DatagramSocket socket = null;
		try{
			socket = new DatagramSocket();
			byte[] message = flightClass.name().getBytes();
			InetAddress host = InetAddress.getByName(flightServerAddress.getHost());
			int serverPort = flightServerAddress.getUdpPort();
			DatagramPacket request = new DatagramPacket(message, message.length, host, serverPort);
			socket.send(request);
			byte[] buffer = new byte[BUFFER_SIZE];
			DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
			socket.receive(reply);
			String replyAsString = new String(reply.getData()).trim();
			int flightCount = Integer.parseInt(replyAsString);
			return new FlightCountResult(flightServerAddress.getName(), flightCount);
		} catch (SocketException e)
		{
			throw e;
		} catch (IOException e)
		{
			throw e;
		}finally {
			if (socket != null){
				socket.close();
			}
		}
	}
}
