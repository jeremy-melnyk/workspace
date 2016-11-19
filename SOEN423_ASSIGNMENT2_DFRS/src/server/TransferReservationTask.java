package server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.concurrent.Callable;

import enums.FlightClassEnum;
import models.FlightCountResult;
import models.FlightServerAddress;
import models.PassengerRecord;

public class TransferReservationTask implements Callable<Boolean>
{
	private final int BUFFER_SIZE = 5000;
	private final FlightServerAddress flightServerAddress;
	private final PassengerRecord passengerRecord;
	
	public TransferReservationTask(PassengerRecord passengerRecord, FlightServerAddress flightServerAddress){
		this.passengerRecord = passengerRecord;
		this.flightServerAddress = flightServerAddress;
	}

	@Override
	public Boolean call() throws Exception
	{
		return transferReservation(this.passengerRecord, this.flightServerAddress);
	}

	private boolean transferReservation(PassengerRecord passengerRecord, FlightServerAddress flightServerAddress) throws Exception{
		DatagramSocket socket = null;
		try{
			socket = new DatagramSocket();
			byte[] message = passengerRecord.formatToString().getBytes();
			InetAddress host = InetAddress.getByName(flightServerAddress.getHost());
			int serverPort = flightServerAddress.getUdpPort();
			DatagramPacket request = new DatagramPacket(message, message.length, host, serverPort);
			socket.send(request);
			byte[] buffer = new byte[BUFFER_SIZE];
			DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
			socket.receive(reply);
			String replyAsString = new String(reply.getData()).trim();
			boolean isSuccess = Boolean.parseBoolean(replyAsString);
			return isSuccess;
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
