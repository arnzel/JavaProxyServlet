package proxy.servlets;

import htmltransformation.WebsiteManufacturingParser;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 
 * servlet for building a website from snippets of other websites.
 * the content of the snippets is described via jquery selectors.
 * 
 * @author arnzel
 *
 */

@WebServlet("/websitebuilding")
public class WebsiteBuilldingServlet extends HttpServlet {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4423941434141873424L;
	
	/**
	 * 
	 * building a website from snippets defined in websiteManufacture.txt
	 * 
	 */
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		WebsiteManufacturingParser websiteManufacturingParser = new WebsiteManufacturingParser(req.getServletContext());
		websiteManufacturingParser.applyTransformation();
		resp.getWriter().write(websiteManufacturingParser.getHtml());
	}
	
	

}
