package server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DatagramServer implements Runnable {
	private final int BUFFER_SIZE = 1000;
	private final int THREAD_POOL_SIZE = 16;
    private final ExecutorService threadPool;
    
	DatagramSocket socket;
	int port;
	
	public DatagramServer(int port){
		this.threadPool = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
		this.socket = null;
		this.port = port;
	}
	
    public void run() {
    	try{
    		socket = new DatagramSocket(port);
            while (true) {
            	byte[] buffer = new byte[BUFFER_SIZE];
				DatagramPacket request = new DatagramPacket(buffer, buffer.length);
                socket.receive(request);
                threadPool.execute(new BookedFlightCountHandler(socket, request, passengerRecordDb));
                new Thread(new Responder(socket, packet)).start();
            }
    	} catch (IOException e){
    		
    	}
    }
}