package beans;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.bean.ManagedBean;

import proxy.servlets.ProxyServlet;
import cache.LRUMap;
import cache.MemoryPageCache;

/**
 * 
 * provides the data for the table-tag in the File MemoryCache.xhtml
 * 
 * @author arnzel
 *
 */

@ManagedBean
public class MemoryPageCacheAccessBean {

	MemoryPageCache memoryPageCache;
	
	LRUMap lruMap; 

	public MemoryPageCacheAccessBean() {
		
	}
	
	/**
	 * 
	 * provides all requested urls from ProxyServlet
	 * 
	 * @return all visited urls as string list
	 */
	
	public List<String> getUrls() {
		memoryPageCache = ProxyServlet.getMemoryPageCache();
		lruMap = memoryPageCache.getMap();
		List<String> ret = new ArrayList<String>();
		for (String url : lruMap.keySet())
			ret.add(url);
		return ret;
	}
	
	/**
	 * 
	 * provides the date of the last request for a url in the cache
	 * 
	 * @param url a url which was requested by ProxyServlet
	 * @return the date of the last request for this url
	 */
	
	public Date getDateFromUrl(String url){
		memoryPageCache = ProxyServlet.getMemoryPageCache();
		return memoryPageCache.getUrlTimeMap().get(url);
	}
	
}
