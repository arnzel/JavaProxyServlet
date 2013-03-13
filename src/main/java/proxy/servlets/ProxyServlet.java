package proxy.servlets;

import htmltransformation.TransformationParser;

import java.io.IOException;
import java.net.URL;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import proxy.net.CookieManager;
import proxy.net.ProxyConnection;
import proxy.net.ServletResponseManipulation;
import proxy.net.UniqueCookie;
import cache.FilePageCache;
import cache.MemoryPageCache;

@WebServlet("/test")
public class ProxyServlet extends HttpServlet {

	private static final long serialVersionUID = -4474447847598477832L;

	public static final String TARGETURLPARAMETER = "targetUrl";

	private static MemoryPageCache memoryPageCache = new MemoryPageCache(10);
	
	private static FilePageCache filePageCache = new FilePageCache(10);

	private Logger rootLogger = Logger.getRootLogger();
	
	public static java.net.CookieManager cm;

	public ProxyServlet() {

		
	}

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String webpage = handleRequest(request, response);
		response.getWriter().write(webpage);
	}

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String webpage = handleRequest(request, response);
		response.getWriter().write(webpage);
	}

	public String handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		String urlAsString = request.getParameter(TARGETURLPARAMETER);
		URL targetUrl = new URL(urlAsString);
		
		rootLogger.info("Visit Url "
				+ targetUrl);
		
	
		// add existing Cookies to response
		Set<UniqueCookie> requestCookies = CookieManager
				.getCookies(targetUrl);
		
		for (UniqueCookie uniqueCookie : requestCookies) {
			response.addCookie(uniqueCookie);
		}
		

		ProxyConnection proxyConnection = new ProxyConnection(urlAsString,
				request,requestCookies);


		String contentType = proxyConnection.getContentType();
		response.setContentType(contentType);

		String encoding = proxyConnection.getEncoding();
		response.setCharacterEncoding(encoding);
		response.setContentType(proxyConnection.getContentType());

		ServletResponseManipulation.setHeaderFields(response,
				proxyConnection.getHeaderFields());

		proxyConnection.addCookiesToCookieManager();

		String webpage = proxyConnection.replaceTags();

		// webpage = HtmlStringUtils.formatHtmlString(webpage, encoding);
		
		TransformationParser transformationParser = new TransformationParser(this.getServletContext(),webpage);
		transformationParser.applyTransformation();
		

		webpage = transformationParser.getHtml();
		memoryPageCache.saveItem(targetUrl, webpage);
		filePageCache.saveItem(targetUrl, webpage);
		return webpage;
	}
	
	public static MemoryPageCache getMemoryPageCache(){
		return memoryPageCache;
	}

	public static FilePageCache getFilePageCache() {
		return filePageCache;
	}

}
