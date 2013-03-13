package cache;

import java.util.LinkedHashMap;

public class LRUMap extends LinkedHashMap<String,String>{
	
	private static final long serialVersionUID = -4062759810368067691L;
	
	
	/**
	 * 
	 * the maximum size of the map
	 * 
	 */
	private int maximumSize;
	/**
	 * 
	 * creates a LinkedHashmap with size 1,loadfactor 0.75 and accessing order 
	 * 
	 * @param maximumSize the maximum size of the map
	 */
	public LRUMap(int maximumSize){
		super(maximumSize+1,0.75f,true);
		this.maximumSize = maximumSize;
	}
	
	
	
	
	/**
	 * 
	 * deletes the eldest entry if maximumsize +1 is reached
	 * 
	 * @return true if maximum size has reached, else false
	 * 
	 */
	@Override
	protected boolean removeEldestEntry(java.util.Map.Entry<String, String> eldest) {
		return size() > maximumSize;
	}


}
