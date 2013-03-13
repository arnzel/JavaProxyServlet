package cache;

import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 
 * abstract class for all chaches, which saves html files for requested urls
 * 
 * @author arnzel
 *
 */
public abstract class PageCache {
	
	/**
	 * 
	 * default cache size, is no other cache is setted to the constructor
	 * 
	 */
	
	public PageCache(){
		this.map = new LRUMap(DEFAULT_MAXIMUM_ITEMS);
	}
	
	/**
	 * 
	 * a map for save the time of action "saveItem"
	 * 
	 */
	
	protected Map<String,Date> urlTimeMap = new HashMap<String, Date>();
	
	protected LRUMap map;
	
	protected static final int DEFAULT_MAXIMUM_ITEMS = 10;
	
	public abstract void saveItem(URL url,String html);
	
	public abstract String loadItem(URL url);
	
	public Map<String, Date> getUrlTimeMap() {
		return urlTimeMap;
	}
	
	public LRUMap getMap() {
		return map;
	}


}
