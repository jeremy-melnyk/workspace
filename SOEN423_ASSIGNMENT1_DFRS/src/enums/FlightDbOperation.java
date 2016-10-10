package enums;

public enum FlightDbOperation
{
	ADD, REMOVE, EDIT;

	@Override
	public String toString()
	{
		switch (this)
		{
		case ADD:
			return "Added";
		case REMOVE:
			return "Remove";
		case EDIT:
			return "Edit";
		default:
			throw new IllegalArgumentException();
		}
	}
}
