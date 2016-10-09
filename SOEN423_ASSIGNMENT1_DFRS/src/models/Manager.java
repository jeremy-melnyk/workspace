package models;
public class Manager extends Person {
	private static final long serialVersionUID = 1L;
	private String managerId;
	
	public Manager(String firstName, String lastName) {
		super(firstName, lastName);
	}

	public String getManagerId()
	{
		return managerId;
	}

	public void setManagerId(String managerId)
	{
		this.managerId = managerId;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((managerId == null) ? 0 : managerId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		Manager other = (Manager) obj;
		if (managerId == null)
		{
			if (other.managerId != null)
				return false;
		} else if (!managerId.equals(other.managerId))
			return false;
		return true;
	}
}
