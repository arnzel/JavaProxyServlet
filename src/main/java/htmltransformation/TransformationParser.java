package htmltransformation;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;

import org.apache.log4j.Logger;

/**
 * 
 * class for applay the transformations given in the file transformations.txt
 * 
 * 
 * @author arnzel
 *
 */


public class TransformationParser extends AbstractParser {

	Logger rootLogger = Logger.getRootLogger();
	
	/**
	 * 
	 * the html to which the transformations are to apply
	 * 
	 */
	
	String html;
	

	/**
	 * 
	 * the class which applies the transformations
	 * 
	 */
	
	JerryAccess jerryAccess;
	
	/**
	 * 
	 * lines in transformations.txt, which begins with this string are ignored
	 * 
	 */
	
	public static final String ONE_LINE_COMMENT_STRING = "//";
	
	public TransformationParser(ServletContext servletContext,String html) {
		super(servletContext);
		this.html = html;
		jerryAccess = new JerryAccess(html);
	}
	
	/**
	 * 
	 * splits the line by the string "=>" and applies an action depending on the second substring, which is an jquery-function
	 * 
	 */
	
	@Override
	public void parseLine(String line) {
		
		if(line.startsWith(ONE_LINE_COMMENT_STRING)){
			return;
		}
		
		String splits[] = line.split("->");

		String selector = splits[0].trim();
		String operation = splits[1].trim();

		String firstParameter = splits.length > 2  ? splits[2].trim() : null;
		String secondParameter = splits.length > 3 ? splits[3].trim() : null;

		if (operation.equals("remove")) {
			jerryAccess.remove(selector);
		}
		if (operation.equals("addCss")) {
			jerryAccess.addCss(selector, firstParameter, secondParameter);
		}
		if (operation.equals("append")) {
			jerryAccess.append(selector, firstParameter);
		}
		if (operation.equals("changeAttribute")) {
			jerryAccess.changeAttribute(selector, firstParameter,
					secondParameter);
		}
		if(operation.equals("addClasses")){
			List<String> classes = new ArrayList<String>();
			for (int i = 2; i < splits.length; i++) {
				String clazz = splits[i];
				classes.add(clazz);
				
			}
			jerryAccess.addClasses(selector, classes.toArray(new String[classes.size()]));
		}
		if(operation.equals("insertBefore")){
			jerryAccess.insertBefore(selector, firstParameter);
		}
		if(operation.equals("changeAttribute")){
			jerryAccess.changeAttribute(selector, firstParameter, secondParameter);
		}
	}
	
	/**
	 * 
	 * returns the transformated html
	 * 
	 * @return
	 */
	
	public String getHtml(){
		return jerryAccess.getHtml();
	}

	/**
	 * 
	 * return the filename in which the transformations are saved
	 */
	
	@Override
	public String getParsingFileName() {
		return "transformations.txt";
	}

}
