package server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

import database.IFlightDb;
import database.IPassengerRecordDb;
import enums.FlightClassEnum;
import models.PassengerRecord;

public class TransferReservationHandler implements Runnable
{
	private final DatagramSocket socket;
	private final DatagramPacket request;
	private final IPassengerRecordDb passengerRecordDb;
	private final IFlightDb flightDb;
	
	public TransferReservationHandler(DatagramSocket socket, DatagramPacket request, IPassengerRecordDb passengerRecordDb, IFlightDb flightDb) {
		super();
		this.socket = socket;
		this.request = request;
		this.passengerRecordDb = passengerRecordDb;
		this.flightDb = flightDb;
	}
	
	@Override
	public void run()
	{
		try
		{
			String passengerRecordAsString = new String(request.getData()).trim();
			PassengerRecord passengerRecord = PassengerRecord.toPassengerRecord(passengerRecordAsString);
			boolean flightAddResult = this.flightDb.addFlight(passengerRecord.getFlight());
			boolean result = false;
			if(flightAddResult){
				result = this.passengerRecordDb.addRecord(passengerRecord);
			}
			byte[] message = Boolean.toString(result).getBytes();
			DatagramPacket reply = new DatagramPacket(message, message.length, request.getAddress(), request.getPort());
			socket.send(reply);
		} catch (SocketException e)
		{
			e.printStackTrace();
		} catch (IOException e)
		{
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
