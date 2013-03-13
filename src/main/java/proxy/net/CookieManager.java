package proxy.net;

import java.net.URL;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

/**
 * 
 * class for sets cookies manually
 * 
 * @author arnzel
 *
 */

public class CookieManager {

	static Set<UniqueCookie> cookieSet = new  HashSet<UniqueCookie>();
	
	/**
	 * 
	 * gets the cookies associated with a given url. iterates over all cookies 
	 * and checks if a cookie is in the domain of the url
	 * 
	 * @param url
	 * @return
	 */
	public static Set<UniqueCookie> getCookies(URL url) {
		Set<UniqueCookie> domainCookies = new HashSet<UniqueCookie>();
		for(UniqueCookie cookie: cookieSet){
			if(isInCookieDomain(url, cookie.getDomain())){
				domainCookies.add(cookie);
			}
		}
		return domainCookies;
	}
	
	/**
	 * 
	 * adds the cookie to cookieSet 
	 * 
	 * @param cookie
	 */
	
	public static void addCookie(UniqueCookie cookie) {
		cookieSet.add(cookie);
	}
	
	/**
	 * 
	 * checks if a url is in the domain of a given domain of a cookie
	 * 
	 * @param url an url as string
	 * @param cookieDomain the attribute domain of a cookie
	 * @return true if the domain starts with url(without protocol and "//www"),else false
	 */
	
	private static boolean isInCookieDomain(URL url,String cookieDomain){
		String urlString = url.toString();
		String domain = StringUtils.substringAfter(urlString,"//www");
		if( (domain).startsWith(cookieDomain)){
			return true;
		}else{
			return false;
		}
	}

}
