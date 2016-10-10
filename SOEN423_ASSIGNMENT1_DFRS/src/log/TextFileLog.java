package log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
public class TextFileLog implements ILog {
	
	@Override
	public boolean write(String tag, String message)
	{
		if (tag == null)
		{
			return false;
		}
		if (message == null)
		{
			return false;
		}

		File file = new File(tag + ".txt");
		FileWriter fileWriter = null;
		try
		{
			fileWriter = new FileWriter(file, true);
			fileWriter.write(tag + " : " + message + " at " + new Date() + System.lineSeparator());
		} catch (IOException e)
		{
			e.printStackTrace();
		} finally
		{
			try
			{
				fileWriter.close();
				return true;
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		return false;
	}

	@Override
	public String readContent(String tag)
	{
		if(tag == null){
			return null;
		}	

		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(tag + ".txt"));
		    StringBuilder sb = new StringBuilder();
		    String line = reader.readLine();
		    while (line != null) {
		        sb.append(line);
		        sb.append(System.lineSeparator());
		        line = reader.readLine();
		    }
		    return sb.toString();
		} catch (IOException e)
		{
			e.printStackTrace();
			return null;
		} finally {
			if(reader != null){
			    try
				{
					reader.close();
				} catch (IOException e)
				{
					e.printStackTrace();
					return null;
				}	
			}
		}
	}

	@Override
	public boolean clear(String tag)
	{
		if (tag == null)
		{
			return false;
		}

		File file = new File(tag + ".txt");
		return file.delete();
	}

}
