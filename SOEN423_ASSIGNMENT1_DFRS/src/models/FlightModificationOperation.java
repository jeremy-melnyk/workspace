package models;

import enums.FlightClassEnum;
import enums.FlightParameter;

public class FlightModificationOperation {
	private FlightParameter flightParameter;
	private FlightClassEnum flightClass;
	
	public FlightModificationOperation(FlightParameter flightParameter, FlightClassEnum flightClass) {
		super();
		this.flightParameter = flightParameter;
		this.flightClass = flightClass;
	}

	public FlightParameter getFlightParameter() {
		return flightParameter;
	}

	public void setFlightParameter(FlightParameter flightParameter) {
		this.flightParameter = flightParameter;
	}

	public FlightClassEnum getFlightClass() {
		return flightClass;
	}

	public void setFlightClass(FlightClassEnum flightClass) {
		this.flightClass = flightClass;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((flightClass == null) ? 0 : flightClass.hashCode());
		result = prime * result + ((flightParameter == null) ? 0 : flightParameter.hashCode());
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
		FlightModificationOperation other = (FlightModificationOperation) obj;
		if (flightClass != other.flightClass)
			return false;
		if (flightParameter != other.flightParameter)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "FlightModificationOperation [flightParameter=" + flightParameter + ", flightClass=" + flightClass + "]";
	}
}
