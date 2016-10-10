package log;

public interface ILogger {
	public boolean log(String tag, String message);
	public boolean clearLog(String tag);
}
