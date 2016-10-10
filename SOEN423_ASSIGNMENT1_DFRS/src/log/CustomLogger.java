package log;

public class CustomLogger implements ILogger {

	ILog log;
	
	public CustomLogger(ILog log) {
		super();
		this.log = log;
	}

	@Override
	public boolean log(String tag, String message)
	{
		return this.log.write(tag, message);
		
	}

	@Override
	public boolean clearLog(String tag)
	{
		return this.log.clear(tag);
	}

}
