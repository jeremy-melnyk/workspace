package enums;

public enum FlightDbOperation {
	ADD, REMOVE, EDIT;

	public static FlightDbOperation toFlightDbOperation(String input) {
		switch (input) {
		case "ADD":
			return FlightDbOperation.ADD;
		case "REMOVE":
			return FlightDbOperation.REMOVE;
		case "EDIT":
			return FlightDbOperation.EDIT;
		default:
			return FlightDbOperation.ADD;
		}
	}
}
