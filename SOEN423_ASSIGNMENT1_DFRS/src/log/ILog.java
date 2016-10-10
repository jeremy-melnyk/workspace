package log;

public interface ILog
{
	public boolean write(String tag, String message);
	public String readContent(String tag);
	public boolean clear(String tag);
}
