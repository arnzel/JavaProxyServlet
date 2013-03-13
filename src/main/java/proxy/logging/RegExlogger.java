package proxy.logging;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.regex.Matcher;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

public class RegExlogger {
	
	private static Logger rootlogger = Logger.getRootLogger();	
	
	public static void logMatcherResults(Matcher matcher,String name){
	

		int count = 0;
		while (matcher.find()){
		    count++;
		    for(int i = 1; i< matcher.groupCount();i++){
		    	rootlogger.info("Matcher " + name + " Group " + i + matcher.group(i));
		    }
		    
		}
		rootlogger.info("Matcher " + name + " has found " + count + " items");
		
	}
	
	public static void logMatcherResults(Matcher matcher,String name,String directory) {
		
		int count = 0;
		String string = "";
		while (matcher.find()){
		    count++;
		    for(int i = 1; i< matcher.groupCount();i++){
		    	string = string.concat("Matcher " + name + " Group " + i + " " + matcher.group(i) );
		    	string = string.concat("\n");
		    }
		    string = string.concat("\n");
		    string = string.concat("\n");
		    
		}
		string = string.concat("Matcher " + name + " has found " + count + " items");
		String fileName = directory + name + "-" + new Date().toString();
		try {
			FileUtils.write(new File(fileName),string);
		} catch (IOException e) {
			rootlogger.error("Cant write File " + fileName );
		}
	}

}
