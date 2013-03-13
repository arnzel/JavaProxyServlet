package proxy.tagreplacement;

import java.io.IOException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import proxy.net.ProxyConnection;
import proxy.servlets.ProxyServlet;


/**
 * 
 * redirects the links of the page to links managed by the proxy. for an example: 
 * http://www.spiegel.de/auto/ => http://localhost:8080/webproxy/test?targetUrl=http://www.spiegel.de/auto/
 * 
 * @author arnzel
 *
 */

public class TagContentReplacer {

	String html;
	// TODO: Read servletpattern from annotation of ProxyServlet.java or
	// from web.xml
	private static final String PROXYURL = "http://localhost:8080/webproxy/test?" + ProxyServlet.TARGETURLPARAMETER + "=";
	
	private String domain; 

	public TagContentReplacer(String html,URL targetUrl) {
		this.html = html;
		domain = ProxyConnection.getDomain(targetUrl);
	}

	public String getString() {
		return html;
	}

	/**
	 * replaces all absolute links in forms a links for an example http://www.spiegel.de
	 * 
	 * @param targetUrl
	 * @throws IOException
	 */
	public void replaceAllAbsolute(URL targetUrl) {
		replaceAbsolute("form","action",targetUrl);
		replaceAbsolute("a","href", targetUrl);
	}
	
	/**
	 * 
	 * replaces a single absolute link
	 * 
	 * @param tags
	 * @param attribute
	 * @param url
	 */
	
	public void replaceAbsolute(String tags, String attribute, URL url) {
		String patternString = "(<[^>]*[tags][^>]*attribute=['\"])(http[s]?[^'\"]*)(['\"][^>]*>)";	
		applyPattern(patternString, tags, attribute, PROXYURL);

	}
	
	/**
	 * 
	 * replaces a single relative link
	 * 
	 * @param tags
	 * @param attribute
	 * @param targetUrl
	 */
	
	public void replaceSameFolder(String tags, String attribute, URL targetUrl) {
		String patternString = "(<[^>]*[tags][^>]*attribute=['\"])([^/][^':\"]*)(['\"][^>]*>)";
		applyPattern(patternString, tags, attribute,targetUrl.toString());
	}
	
	/**
	 * 
	 * replaces the strings "tags" and "attribute" to real tags an attributes and "injects" the proxyUrl at the beginning of the attribute string.
	 * 
	 * 
	 * @param patternString
	 * @param tags
	 * @param attribute
	 * @param replacement
	 */
	
	public void applyPattern(String patternString,String tags,String attribute,String replacement){
		patternString = patternString.replace("tags", tags);
		patternString = patternString.replace("attribute", attribute);
		Pattern pattern = Pattern.compile(patternString);
		Matcher matcher = pattern.matcher(html);
		html = matcher.replaceAll("$1" + replacement + "$2" + "$3");
	}
	/**
	 * 
	 * replaces links to resources at the same folder for an example href="favicon.ico"
	 * 
	 * @param targetUrl
	 */
	public void replaceAllSameFolder(URL targetUrl) {
		replaceSameFolder("link","href", targetUrl);
		replaceSameFolder("img|script|iframe","src", targetUrl);
		replaceSameFolder("form","action", targetUrl);
	}
	/**
	 * 
	 * replaces a link to a resource to the same domain, for an example 
	 * <link href="/static/sys/v9/icons/touch-icon-iphone.png" ..> = > <link href="http://www.spiegel.de/static/sys/v9/icons/touch-icon-iphone.png"..>
	 * <a href="/panorama/leute/" ...> => <a href="localhost:8080/webproxy/test?targetUrl="www.spiegel.de/panorama/leute/" ...>
	 * 
	 * 
	 * @param tags
	 * @param attribute
	 * @param targetUrl
	 * @param viaProxy
	 */
	public void replaceDomain(String tags, String attribute, URL targetUrl,boolean viaProxy) {
		String patternString = "(<[^>]*[tags][^>]*attribute=['\"])(/[^'\"]*)(['\"][^>]*>)";
		if(!viaProxy){
			applyPattern(patternString, tags, attribute, domain);
		}else{
			applyPattern(patternString, tags, attribute, PROXYURL + domain);
		}
		
	}
	
	/**
	 * 
	 * replaces all links and forms reffered to the same domain
	 * 
	 * @param targetUrl
	 * @throws IOException
	 */
	
	public void replaceLinksAndFormsIntern(URL targetUrl)  {
		replaceDomain("a","href", targetUrl,true);
		replaceDomain("form","action", targetUrl,true);
		
	}
	
	/**
	 * 
	 * replaces all links to images,scripts iframes
	 * 
	 * @param targetUrl
	 */

	public void replaceAllDomainRessources(URL targetUrl) {
		replaceDomain("link","href", targetUrl,false);
		replaceDomain("img|script|iframe","src", targetUrl, false);
	}
	
	/**
	 * 
	 * replaces the references to resources in Css-Element "background"
	 * 
	 */
	
	public void replaceStylesheetBackgrounds(){
		String patternString = "(background[^:]*:url\\()([^)]*)\\)";
		Pattern pattern = Pattern.compile(patternString);
		Matcher matcher = pattern.matcher(html);
		html = matcher.replaceAll("$1" + domain + "$2" + ")");
		
	}
	
	/**
	 * 
	 * applies all nessesary  replacements in the tags
	 * 
	 * @param targetUrl the url for which content the replacement is to apply
	 */
	
	public void replaceAll(URL targetUrl)  {
		replaceAllAbsolute(targetUrl);
		replaceLinksAndFormsIntern(targetUrl);
		replaceAllSameFolder(targetUrl);
		replaceAllDomainRessources(targetUrl);
		replaceStylesheetBackgrounds();
	}

}
