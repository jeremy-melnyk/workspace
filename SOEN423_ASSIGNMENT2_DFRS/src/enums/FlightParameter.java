package enums;

public enum FlightParameter {
	FIRST_CLASS_SEATS, BUSINESS_CLASS_SEATS, ECONOMY_CLASS_SEATS, DESTINATION, DATE, NONE;
	
	public static FlightParameter toFlightParameter(String input) {
		switch (input) {
		case "FIRST_CLASS_SEATS":
			return FlightParameter.FIRST_CLASS_SEATS;
		case "BUSINESS_CLASS_SEATS":
			return FlightParameter.BUSINESS_CLASS_SEATS;
		case "ECONOMY_CLASS_SEATS":
			return FlightParameter.ECONOMY_CLASS_SEATS;
		case "DESTINATION":
			return FlightParameter.DESTINATION;
		case "DATE":
			return FlightParameter.DATE;
		case "NONE":
			return FlightParameter.NONE;
		default:
			return FlightParameter.NONE;
		}
	}
}
