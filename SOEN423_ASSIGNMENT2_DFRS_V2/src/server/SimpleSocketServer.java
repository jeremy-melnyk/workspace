package server;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.ServerSocket;

public class SimpleSocketServer implements Runnable {
	private DatagramSocket datagramSocket;
	private int port;
	private boolean running = false;

	public void startServer() {
		try {
			datagramSocket = new DatagramSocket(port);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void stopServer() {
		running = false;
	}

	@Override
	public void run() {
        running = true;
        while(running)
        {
            try
            {
                System.out.println( "Listening for a connection" );

                // Call accept() to receive the next connection
                Datagram socket = datagramSocket.receive(arg0);

                // Pass the socket to the RequestHandler thread for processing
                RequestHandler requestHandler = new RequestHandler( socket );
                requestHandler.start();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
	}

}
