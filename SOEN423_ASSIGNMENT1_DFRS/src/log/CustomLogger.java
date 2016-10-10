package log;

public class CustomLogger implements ILogger {

	ILog log;
	
	public CustomLogger(ILog log) {
		super();
		this.log = log;
	}

	@Override
	public boolean log(String tag, String operation, String message)
	{
		String logMessage = String.format("%s : %s : %s" + System.lineSeparator(), tag, operation, message);
		String fileName = tag + ".txt";
		return this.log.write(fileName, logMessage);
		
	}

	@Override
	public boolean clearLog(String tag)
	{
		return this.log.clear(tag);
	}

}
