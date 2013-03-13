package beans;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.bean.ManagedBean;

import proxy.servlets.ProxyServlet;
import cache.FilePageCache;
import cache.LRUMap;

/**
 * 
 * class which provides the data used in the table-tag of FileCache.xhtml
 * 
 * @author arnzel
 *
 */

@ManagedBean
public class FilePageCacheAccessBean  {
	
	FilePageCache filePageCache;
	
	LRUMap lruMap; 
	
	public FilePageCacheAccessBean(){
		
	}
	
	/**
	 * 
	 * provides the filenames used in the first column of the table-tag
	 * 
	 * @return
	 */
	
	public List<String> getFileNames(){

		filePageCache = ProxyServlet.getFilePageCache();
		lruMap = filePageCache.getMap();
		List<String> ret = new ArrayList<String>();
		for (String url : lruMap.keySet())
			ret.add(url);
		return ret;
		
	}
	
	/**
	 * 
	 * provides the date of the last request for a file in the cache
	 * 
	 * @param filename the name of a file saved in the cache
	 * @return the date of the last request for the url associated for this file
	 */
	
	public Date getDateFromFileName(String filename){
		filePageCache = ProxyServlet.getFilePageCache();
		return filePageCache.getUrlTimeMap().get(filename);
	}

}
