package htmltransformation;

import java.io.IOException;

import javax.servlet.ServletContext;

import org.apache.log4j.Logger;

import proxy.net.ProxyConnection;

/**
 * 
 * builds an website from snippets of other website(a website-snippet  is created applying a jquery-selector to a site)
 * 
 * @author arnzel
 *
 */

public class WebsiteManufacturingParser extends AbstractParser {
	
	JerryAccess jerryAccess;

	Logger logger = Logger.getRootLogger();
	
	StringBuilder stringBuilder = new StringBuilder();
	
	public WebsiteManufacturingParser(ServletContext servletContext){
		super(servletContext);

	}
	
	/**
	 * 
	 * splits the line by the seperator "=>". the first split is the jquery-selector an the second the site , to which this selector is applied
	 * 
	 */
	
	@Override
	public void parseLine(String line) {
		
		String splits[] = line.split("->");

		String selector = splits[0].trim();
		String website = splits[1].trim();
		
		try {
			ProxyConnection proxyConnection = new ProxyConnection(website,"GET");
			website= proxyConnection.replaceTags();
			jerryAccess = new JerryAccess(website);
			String selectorHtml = jerryAccess.getSelectorHtml(selector);
			stringBuilder.append(selectorHtml);
		} catch (IOException e) {
			logger.error("IO-Exception while opening a connection to website " + website);
		}
		
		
	}
	
	/**
	 * 
	 * return the site, in which the jquery-selectors and the websites are given
	 * 
	 */

	@Override
	public String getParsingFileName() {
		return "websiteManufacture.txt";
	}
	
	/**
	 * 
	 * returns all snippets together as a html site
	 * 
	 * @return the website as a string
	 */
	
	public String getHtml(){
		return stringBuilder.toString();
	}

}
