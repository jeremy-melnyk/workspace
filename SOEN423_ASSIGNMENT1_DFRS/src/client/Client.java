package client;

public abstract class Client
{
	protected String baseUrl;

	public Client(String baseUrl) {
		super();
		if (baseUrl == null)
		{
			throw new IllegalArgumentException();
		}
		this.baseUrl = baseUrl;
	}

	public String getBaseUrl()
	{
		return baseUrl;
	}

	public void setBaseUrl(String baseUrl)
	{
		this.baseUrl = baseUrl;
	}
}
