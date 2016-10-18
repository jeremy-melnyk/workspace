package enums;

public enum FlightClassEnum
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
	
	public static FlightClassEnum toFlightClass(String string){
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
