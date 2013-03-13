package cache;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Comparator;
import java.util.Date;
import java.util.TreeSet;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

public class FilePageCache extends PageCache {

	/**
	 * 
	 * Maximum Items in the cache
	 * 
	 */

	private int maximumItems;

	/**
	 * 
	 * Saves the requested html files sorted by the request time
	 * 
	 */

	private TreeSet<HtmlFile> writtenFiles = new TreeSet<HtmlFile>(
			new FileModificationDateComparator());

	/**
	 * 
	 * the directory, where the files are saved
	 * 
	 */

	private static final String DIRECTORY = "htmlFileCache/";

	/**
	 * 
	 * the logger for all errors,infos,etc..
	 * 
	 */

	private Logger rootLogger = Logger.getRootLogger();

	/**
	 * 
	 * Creates an instance with maximum file count DEFAULT_MAXIMUM_ITEMS
	 * 
	 */

	public FilePageCache() {
		this(DEFAULT_MAXIMUM_ITEMS);
	}

	/**
	 * 
	 * Creates an instance with a given number of maximum files in the cache
	 * 
	 * @param maximumItems
	 *            the number of maximum files in the cache
	 */

	public FilePageCache(int maximumItems) {
		this.maximumItems = maximumItems;
	}

	/**
	 * 
	 * saves an html String in the cache
	 * 
	 * @param url
	 *            a requested url like http://www.spiegel.de
	 * @param html
	 *            the html content to save
	 * 
	 */
	@Override
	public void saveItem(URL url, String html) {

		// replace backslashes to "_", because they are interpreted as new
		// directories
		String filename = url.toString().replace("/", "_");
		HtmlFile file = new HtmlFile(DIRECTORY + filename);
		try {
			boolean wasAdded = writtenFiles.add(file);
			if (wasAdded) {
				FileUtils.write(file, html);
				rootLogger.info("Added file " + filename);
			} else {
				rootLogger.info("Dont added file " + filename);
			}
			this.map.put(filename, html);
			urlTimeMap.put(filename, new Date());

		} catch (IOException e) {
			rootLogger.error("Could not write File " + file.getName());
			e.printStackTrace();
		}
		if (writtenFiles.size() > maximumItems) {
			boolean success = writtenFiles.last().delete();
			rootLogger.error("File " + filename + " was deleted:" + success);
			writtenFiles.remove(writtenFiles.last());
		}

	}

	/**
	 * 
	 * loads an html site from the cache.
	 * 
	 * @param url
	 *            a given url like htp://www.spiegel.de
	 * 
	 * @return the content of the url as string
	 * 
	 */
	@Override
	public String loadItem(URL url) {
		File file = new File(DIRECTORY + url.toString());
		try {
			String content = FileUtils.readFileToString(file);
			return content;
		} catch (IOException e) {
			rootLogger.info("Could not find an antry in the cache for url "
					+ url);
			return null;
		}
	}

	/**
	 * 
	 * @author arnzel
	 * 
	 *         class which sorts the savedFiles by modification date. file which
	 *         was saved earliest is the first one.
	 * 
	 */
	private class FileModificationDateComparator implements
			Comparator<HtmlFile> {

		public int compare(HtmlFile fileOne, HtmlFile fileTwo) {
			if (fileOne.getName().equals(fileTwo.getName())) {
				return 0;
			} else if (fileOne.lastModified() > fileTwo.lastModified()) {
				return 1;
			} else {
				return -1;
			}
		}

	}

	/**
	 * 
	 * subclass from file, which overrides the equals method: objects are the
	 * same, when the filenames are equal
	 * 
	 * @author arnzel
	 * 
	 */
	public class HtmlFile extends File {

		/**
		 * 
		 */
		private static final long serialVersionUID = 7794309768765592885L;

		public HtmlFile(String pathname) {
			super(pathname);

		}
		
		/**
		 * 
		 * two htmlfiles are equals if they have the same pathname
		 * 
		 */
		
		@Override
		public boolean equals(Object obj) {

			if (obj == null) {
				return false;
			}

			if (!(obj instanceof HtmlFile)) {
				return false;
			} else {
				HtmlFile thatFile = (HtmlFile) obj;
				if (thatFile.getAbsolutePath().equals(this.getAbsolutePath())) {
					return true;
				} else {
					return false;
				}
			}
		}
	}
	/**
	 * 
	 * get all written Files by the cahche
	 * 
	 * @return
	 */
	public TreeSet<HtmlFile> getWrittenFiles() {
		return writtenFiles;
	}

}
