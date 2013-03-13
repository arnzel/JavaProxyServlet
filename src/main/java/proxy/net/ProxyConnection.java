package proxy.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookieStore;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import proxy.string.HtmlStringUtils;
import proxy.tagreplacement.TagContentReplacer;

/**
 * 
 * the class which manages an url connection used for the proxy. this class gets requests from ProxyServlet.java
 * and Cookies from the CookieManager.java. it opens an url-connection for the request and sets the cookies.
 * it gives the content of the request back to ProxyServelt 
 * 
 * 
 * @author arnzel
 *
 */

public class ProxyConnection {
	
	/**
	 * 
	 * default encoding if no on can be parsed from the request
	 * 
	 */
	
	public static final String DEFAULT_ENCODING = "ISO-8859-1";

	/**
	 * 
	 * default charset if no one can be parsed from the request
	 * 
	 * 
	 */
	
	public static final String DEFAULT_CHARSET = "charset=ISO-8859-1";
	
	/**
	 * 
	 * the url of the request as String
	 * 
	 */
	
	private String urlAsString;
	
	/**
	 * 
	 * the connection to the url given from ProxyServlet.java
	 * 
	 */
	
	private HttpURLConnection urlConnection;

	private Logger rootLogger = Logger.getRootLogger();
	
	/**
	 * 
	 * creates an ProxyConnection for building an website og snippets from other websites. no cookies are used. 
	 * 
	 * @param urlString
	 * @param method
	 * @throws IOException
	 */
	
	public ProxyConnection(String urlString, String method) throws IOException {
		this.urlAsString = urlString;
		URL url = new URL(urlString);
		urlConnection = (HttpURLConnection) url.openConnection();
	}
	
	/**
	 * 
	 * 
	 * creates an proxy Connection to a request from ProxyServlet.java
	 * 
	 * @param urlString the url ass tring
	 * @param request the Servlet-Request-Object
	 * @param requestCookies the ccokies stored in CookiesManager
	 * @throws MalformedURLException if the url is malformed
	 * @throws IOException if IO-exception occurs while reading the url
	 */
	public ProxyConnection(String urlString, HttpServletRequest request,
			Set<UniqueCookie> requestCookies) throws MalformedURLException,
			IOException {
		this.urlAsString = urlString;
		String method = request.getMethod();
		URL url = new URL(urlString);

		urlConnection = (HttpURLConnection) url.openConnection();
		// Set Cookies before open Connection
		setCookies(requestCookies);
		addRequestProperty("Connection", "keep-alive");
		

		urlConnection.setRequestMethod(method);

		if (method.equals("POST")) {
			writePostData(request);
		}
	}
	/**
	 * 
	 * adds a property to the request
	 * 
	 * @param key
	 * @param value
	 */
	public void addRequestProperty(String key, String value) {
		urlConnection.addRequestProperty(key, value);
	}

	private void setCookies(Set<UniqueCookie> requestCookies) {

		if (requestCookies != null) {
			for (UniqueCookie cookie : requestCookies) {
				String myCookie = cookie.getName() + "=" + cookie.getValue();
				rootLogger.info("Set cookie from CookieManager "
						+ myCookie);
				this.getUrlConnection().setRequestProperty("Cookie", myCookie);
			}
		} else {
			rootLogger
					.info("No Cookies found in the CookieManager for URL "
							+ urlConnection.getURL());
		}

		CookieManager cookieManager = (CookieManager) CookieHandler
				.getDefault();
		CookieStore cookieJar = cookieManager.getCookieStore();
		// set all necessary cookies
		for (HttpCookie cookie : cookieJar.getCookies()) {
			urlConnection.setRequestProperty("Cookie", cookie.toString());
			rootLogger.info("Set cookie from Java CookieManager "
					+ cookie);
		}

	}

	private void writePostData(HttpServletRequest request) throws IOException {
		urlConnection.setDoOutput(true);
		OutputStream outputStream = urlConnection.getOutputStream();
		OutputStreamWriter writer = new OutputStreamWriter(outputStream);

		Map<String, String[]> parameters = request.getParameterMap();
		Iterator<String> i = parameters.keySet().iterator();

		StringBuilder stringBuilder = new StringBuilder();
		while (i.hasNext()) {

			String key = (String) i.next();
			String value = ((String[]) parameters.get(key))[0];

			if (!key.equals("") && !value.equals("")) {
				stringBuilder.append(key).append("=").append(value);
				// check if it is not the last parameter
				if (i.hasNext()) {
					stringBuilder.append("&");
				}
			}

		}
		String parameterString = stringBuilder.toString();
		writer.write(parameterString);
		writer.flush();
	}

	public URLConnection getUrlConnection() {
		return this.urlConnection;
	}

	public String getHtml() throws IOException {
		InputStreamReader inputStreamReader;
		try {
			String encoding = getEncoding();
			inputStreamReader = new InputStreamReader(
					urlConnection.getInputStream(), encoding);
		} catch (UnsupportedEncodingException e) {
			inputStreamReader = new InputStreamReader(
					urlConnection.getInputStream(), DEFAULT_ENCODING);
		}
		BufferedReader in = new BufferedReader(inputStreamReader);

		StringBuilder response = new StringBuilder();

		String inputLine = "";

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
		return response.toString();
	}
	
	/**
	 *
	 * replaces the tags in the url, so that all links in the html-file are related tpo the proxy.
	 * also relates resources to the right path (example /icons/icon.png => http://spiegel.de/icons/icon.png)
	 * 
	 * @return the html-content with all replaced tags
	 * @throws IOException
	 */
	
	public String replaceTags() throws IOException {
		String html = getHtml();
		URL url = new URL(urlAsString);
		TagContentReplacer patternReplacer = new TagContentReplacer(html, url);
		patternReplacer.replaceAll(url);
		return patternReplacer.getString();
	}
	
	/**
	 * 
	 * gets all header field of the urlConnection
	 * 
	 * @return a Map of all properties in the header of the current request
	 */
	
	public Map<String, List<String>> getHeaderFields() {
		Map<String, List<String>> responseMap = urlConnection.getHeaderFields();
		return responseMap;
	}
	
	/**
	 * 
	 * parses the header of the request to get the content type
	 * 
	 * @return the content type or "application/octet-stream;charset=UTF-8" if any error occurs while parsing
	 */
	
	public String getContentType()  {
		try {
			Map<String, List<String>> responseMap = urlConnection
					.getHeaderFields();
			List<String> values = responseMap.get("Content-Type");
			String contentType = (String) values.get(0);
			return contentType;
		} catch (Exception e) {
			return "application/octet-stream;charset=UTF-8";
		}
	}
	
	/**
	 * 
	 * parses the header to get the encoding of the current request or DEFAULT_ENCODING if any error occurs
	 * 
	 * @return the encoding of the request
	 */
	
	public String getEncoding() {
		return HtmlStringUtils.getEncoding(getContentType());
	}

	public void addCookiesToCookieManager() {
		Map<String, List<String>> headerFields = urlConnection
				.getHeaderFields();
		for (Iterator iterator = headerFields.keySet().iterator(); iterator
				.hasNext();) {
			String key = (String) iterator.next();

			if (key != null && !key.equals("Transfer-Encoding")) {

				if (key.equalsIgnoreCase("Set-Cookie")) {

					List values = (List) headerFields.get(key);

					for (Object value : values) {
						String cookieField = (String) value;
						String[] parts = cookieField.split(";");
						try {
							String cookieName = parts[0].split("=")[0];
							String cookieValue = parts[0].split("=")[1];
							String domain = parts[2].split("=")[1];
							rootLogger
									.info("Get Cookie from Response. Name:"
											+ cookieName + ",Value:"
											+ cookieValue + ",Domain:" + domain
											+ " from Url " + urlAsString);
							UniqueCookie cookie = new UniqueCookie(cookieName,
									cookieValue);
							cookie.setDomain(domain);
							proxy.net.CookieManager.addCookie(cookie);
						} catch (ArrayIndexOutOfBoundsException e) {
							rootLogger.error("Error while get cookie",e);
						}

					}
				}
			}
		}

	}

	public static String getDomain(URL targetUrl) {
		return targetUrl.getProtocol() + "://" + targetUrl.getHost();
	}

}
