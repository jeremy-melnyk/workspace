package client;

import org.omg.CORBA.ORB;

public abstract class Client
{
	protected ORB orb;
	protected String serverAcronym;

	public Client(ORB orb, String serverAcronym) {
		super();
		if (orb == null)
		{
			throw new IllegalArgumentException();
		}
		this.orb = orb;
		this.serverAcronym = serverAcronym;
	}

	public ORB getOrb() {
		return orb;
	}

	public void setOrb(ORB orb) {
		this.orb = orb;
	}

	public String getServerAcronym() {
		return serverAcronym;
	}

	public void setServerAcronym(String serverAcronym) {
		this.serverAcronym = serverAcronym;
	}
}
