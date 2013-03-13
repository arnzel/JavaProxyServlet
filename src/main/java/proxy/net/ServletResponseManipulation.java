package proxy.net;

import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

/**
 * 
 * sets all header field of the request to the response
 * 
 * @author arnzel
 *
 */

public class ServletResponseManipulation {

	private static Logger rootLogger = Logger.getRootLogger();
	
	/**
	 * 
	 * sets alle properties of a given map to the response but not the key transfer encoding
	 * 
	 * @param response a given response object
	 * @param headerFields given header fields
	 */
	
	public static void setHeaderFields(HttpServletResponse response,
			Map headerFields)  {
		for (Iterator iterator = headerFields.keySet().iterator(); iterator
				.hasNext();) {
			String key = (String) iterator.next();
			if (key != null && !key.equals("Transfer-Encoding")) {
				rootLogger.info("Response Header Field " + key + " has ");
				
				List values = (List) headerFields.get(key);
				
				String value = (String) values.get(0);
				rootLogger.info("Value " + value);
				for (int i = 1; i < values.size(); i++) {
					Object o = values.get(i);
					rootLogger.info("Value " + (String)o);
					value += ";" + (String)o;
					
				}
				
				response.setHeader(key, value);
			}
		}
	}


}
