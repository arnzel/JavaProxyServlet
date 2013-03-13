package application;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

@WebListener
public class WebStartListener implements ServletContextListener {
	
	/**
	 * Configures Log4j and sets the CookiePolicy
	 * 
	 */
	public void contextInitialized(ServletContextEvent sce) {
		ServletContext servletContext = sce.getServletContext();
        String log4jPath = servletContext.getRealPath("/") + "log4j.xml";
        DOMConfigurator.configure(log4jPath);
        Logger logger = LogManager.getRootLogger();
        logger.debug("Loaded: " + log4jPath);
        
        setCookiePolicy();

	}

	public void contextDestroyed(ServletContextEvent sce) {
		
		
	}
	
	/**
	 * 
	 * sets the cookie policy: accept all
	 * 
	 */
	
	
	private void setCookiePolicy(){
		
		CookieManager manager = new CookieManager();
		manager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
		CookieHandler.setDefault(manager);
		
	}

}
