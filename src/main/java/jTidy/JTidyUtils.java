package jTidy;

import org.w3c.tidy.Configuration;
import org.w3c.tidy.Tidy;
/**
 * 
 * Utility class for the JTidy-Framework: http://jtidy.sourceforge.net/. JTidy is used in this project for pretty-printing html-files
 * 
 * @author arnzel
 *
 */
public class JTidyUtils {
	
	/**
	 * 
	 * sets the character-encoding for a jTidy instance
	 * 
	 * @param charset the charset as a string. usually parsed from the header of an html-page
	 * @param tidy an instance of JTidy
	 */
	public static void setTidyCharset(String charset,Tidy tidy){
		if(charset.equals("iso-8859-1")){
			tidy.setCharEncoding(Configuration.LATIN1);
		}else if(charset.equals("UTF-8")){
			tidy.setCharEncoding(Configuration.UTF8);
		}
	}

}
