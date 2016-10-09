package enums;

public enum FlightClass
{
	FIRST, BUSINESS, ECONOMY;

	@Override
	public String toString()
	{
		switch (this)
		{
		case FIRST:
			return "First";
		case BUSINESS:
			return "Business";
		case ECONOMY:
			return "Economy";
		default:
			throw new IllegalArgumentException();
		}
	}
	
	public static FlightClass toFlightClass(String string){
		switch (string)
		{
		case "FIRST":
			return FIRST;
		case "BUSINESS":
			return BUSINESS;
		case "ECONOMY":
			return ECONOMY;
		default:
			throw new IllegalArgumentException();
		}
	}
}
