package cache;

import java.net.URL;
import java.util.Date;


/**
 * 
 * saves requested html pages in the memory
 * 
 * @author arnzel
 *
 */
public class MemoryPageCache extends PageCache {
	
	/**
	 * 
	 * creates an instance with a default size
	 * 
	 */
	
	public MemoryPageCache(){
		this(DEFAULT_MAXIMUM_ITEMS);
	}
	
	/**
	 * 
	 * 
	 * creates an instance with a given size
	 * 
	 * @param maximumItems
	 */
	
	public MemoryPageCache(int maximumItems){
		this.map = new LRUMap(maximumItems);

	}
	
	/**
	 * 
	 * saves the item in the cache. saves the timestamp for this operation
	 * 
	 */
	
	@Override
	public void saveItem(URL url,String html) {
		this.map.put(url.toString(), html);
		urlTimeMap.put(url.toString(), new Date());
	}
	
	/**
	 * 
	 * loads the item from the cache
	 * 
	 */
	
	@Override
	public String loadItem(URL url) {
		return this.map.get(url.toString());
	}



	


}
