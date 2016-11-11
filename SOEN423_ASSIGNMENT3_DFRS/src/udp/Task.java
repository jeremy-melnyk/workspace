package udp;

import models.FlightServerAddress;

public abstract class Task implements Runnable {
	protected FlightServerAddress flightServerAddress;

	public Task(FlightServerAddress flightServerAddress) {
		super();
		this.flightServerAddress = flightServerAddress;
	}

	public FlightServerAddress getFlightServerAddress() {
		return flightServerAddress;
	}

	public void setFlightServerAddress(FlightServerAddress flightServerAddress) {
		this.flightServerAddress = flightServerAddress;
	}
}
