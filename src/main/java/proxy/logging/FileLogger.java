package proxy.logging;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import org.apache.commons.io.FileUtils;

public class FileLogger {
	
	public static void logStringBuilder(String directory,StringBuilder stringBuilder) throws IOException{
		logString(directory, stringBuilder.toString());
	}
	
	public static void logString(String directory,String html) throws IOException{
		FileUtils.write(new File(directory + new Date().toString()),html);
	}

}
