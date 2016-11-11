package manager_requests;

public class TransferReservationRequest {
	private final String DELIMITER = "|";
	private String managerId;
	private String flightReservation;

	public TransferReservationRequest(String managerId, String flightReservation) {
		super();
		this.managerId = managerId;
		this.flightReservation = flightReservation;
	}

	@Override
	public String toString() {
		return managerId + DELIMITER + flightReservation;
	}

}
