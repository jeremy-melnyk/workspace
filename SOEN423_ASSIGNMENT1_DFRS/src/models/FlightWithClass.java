package models;

import enums.FlightClassEnum;

public class FlightWithClass {
	private Flight flight;
	private FlightClassEnum flightClassEnum;
	
	public FlightWithClass(Flight flight, FlightClassEnum flightClassEnum) {
		super();
		this.flight = flight;
		this.flightClassEnum = flightClassEnum;
	}

	public Flight getFlight() {
		return flight;
	}

	public void setFlight(Flight flight) {
		this.flight = flight;
	}

	public FlightClassEnum getFlightClassEnum() {
		return flightClassEnum;
	}

	public void setFlightClassEnum(FlightClassEnum flightClassEnum) {
		this.flightClassEnum = flightClassEnum;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((flight == null) ? 0 : flight.hashCode());
		result = prime * result + ((flightClassEnum == null) ? 0 : flightClassEnum.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FlightWithClass other = (FlightWithClass) obj;
		if (flight == null) {
			if (other.flight != null)
				return false;
		} else if (!flight.equals(other.flight))
			return false;
		if (flightClassEnum != other.flightClassEnum)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "FlightWithClass [flight=" + flight + ", flightClassEnum=" + flightClassEnum + "]";
	}
}
