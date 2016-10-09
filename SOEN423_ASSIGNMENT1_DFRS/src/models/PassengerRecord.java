package models;
import java.io.Serializable;
import java.util.Date;

public class PassengerRecord implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private Passenger passenger;
	private Flight flight;
	private Date bookingDate;

	public PassengerRecord(Passenger passenger, Flight flight, Date bookingDate) {
		super();
		this.passenger = passenger;
		this.flight = flight;
		this.bookingDate = bookingDate;
	}

	public Passenger getPassenger()
	{
		return passenger;
	}

	public void setPassenger(Passenger passenger)
	{
		this.passenger = passenger;
	}

	public Flight getFlight()
	{
		return flight;
	}

	public void setFlight(Flight flight)
	{
		this.flight = flight;
	}

	public Date getBookingDate()
	{
		return bookingDate;
	}

	public void setBookingDate(Date bookingDate)
	{
		this.bookingDate = bookingDate;
	}

	@Override
	public String toString()
	{
		return "PassengerRecord [passenger=" + passenger + ", flight=" + flight + ", bookingDate=" + bookingDate + "]";
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((bookingDate == null) ? 0 : bookingDate.hashCode());
		result = prime * result + ((flight == null) ? 0 : flight.hashCode());
		result = prime * result + ((passenger == null) ? 0 : passenger.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PassengerRecord other = (PassengerRecord) obj;
		if (bookingDate == null)
		{
			if (other.bookingDate != null)
				return false;
		} else if (!bookingDate.equals(other.bookingDate))
			return false;
		if (flight == null)
		{
			if (other.flight != null)
				return false;
		} else if (!flight.equals(other.flight))
			return false;
		if (passenger == null)
		{
			if (other.passenger != null)
				return false;
		} else if (!passenger.equals(other.passenger))
			return false;
		return true;
	}
}
