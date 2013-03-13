package htmltransformation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.servlet.ServletContext;

import org.apache.log4j.Logger;

/**
 * 
 * parses a given File in the webapp folder.in each line there is given a key and a value written as key => value 
 * 
 * @author arnzel
 *
 */
public abstract class AbstractParser {
	
	
	/**
	 * 
	 * the servlet context. needed to get the root folder of the wepapp
	 * 
	 */
	ServletContext servletContext;
	
	Logger rootLogger = Logger.getRootLogger();
	
	public AbstractParser(ServletContext servletContext){
		this.servletContext = servletContext;
	}
	
	/**
	 * 
	 * read every line of the line and applies "parseLine"
	 * 
	 */
	public void applyTransformation() {

		BufferedReader bufferedReader;
		try {
			// "transformations.txt"
			bufferedReader = new BufferedReader(new FileReader(new File(servletContext.getRealPath("/") + getParsingFileName())));
			try {
				for (String line; (line = bufferedReader.readLine()) != null;) {
					parseLine(line);
				}
			} catch (IOException e) {
				rootLogger.error("IO-Exception while reading transformation.txt");
			}
		} catch (FileNotFoundException e1) {
			rootLogger.error("Cant find transformation.txt");
		}

	}
	
	/**
	 * 
	 * the filename which the parser should read
	 * 
	 * @return the filename
	 */
	
	public abstract String getParsingFileName();
	
	/**
	 * 
	 * the parsing function for a single line
	 * 
	 * @param line: a line in the loaded file
	 */
	
	public abstract void parseLine(String line);

}
