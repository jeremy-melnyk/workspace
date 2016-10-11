package server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

import database.IPassengerRecordDb;
import enums.FlightClass;
import log.ILogger;

public class BookedFlightCountHandler implements Runnable
{
	private final DatagramSocket socket;
	private final DatagramPacket request;
	private final IPassengerRecordDb passengerRecordDb;
	
	public BookedFlightCountHandler(DatagramSocket socket, DatagramPacket request, IPassengerRecordDb passengerRecordDb) {
		super();
		this.socket = socket;
		this.request = request;
		this.passengerRecordDb = passengerRecordDb;
	}
	
	@Override
	public void run()
	{
		try
		{
			String flightClassAsString = new String(request.getData()).trim();
			FlightClass flightClass = FlightClass.toFlightClass(flightClassAsString);
			int bookedFlightCount = this.passengerRecordDb.numberOfRecords(flightClass);
			String bookedFlightCountAsString = Integer.toString(bookedFlightCount);
			byte[] message = bookedFlightCountAsString.getBytes();
			DatagramPacket reply = new DatagramPacket(message, message.length, request.getAddress(), request.getPort());
			socket.send(reply);
		} catch (SocketException e)
		{
			e.printStackTrace();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}
