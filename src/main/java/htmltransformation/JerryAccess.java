package htmltransformation;

import static jodd.jerry.Jerry.jerry;
import jodd.lagarto.dom.Node;

/**
 * 
 * class for apply changes in the html of the request
 * 
 * @author arnzel
 *
 */
public class JerryAccess {
	
	jodd.jerry.Jerry doc;
	
	public JerryAccess(String html){
		 doc = jerry(html);
	}

	public void remove(String selector){
		doc.$(selector).remove();
	}
	
	public void addCss(String selector,String cssKey,String cssValue){
		doc.$(selector).css(cssKey, cssValue);
	}
	
	public void addClasses(String selector,String... classes){
		doc.$(selector).addClass(classes);
	}
	
	public void append(String selector,String newElement){
		doc.$(selector).append(newElement);
	}
	
	public void insertBefore(String selector,String html){
		doc.$(selector).before(html);
	}
	
	public void changeAttribute(String selector,String attributeName,String attributeValue){
		doc.$(selector).attr(attributeName,attributeValue);
	}
	
	public void removeAttribute(String selector,String attributeName){
		doc.$(selector).removeAttr(attributeName);
	}
	
	public String getSelectorHtml(String selector){
		StringBuilder stringBuilder = new StringBuilder();
		Node[] nodes = doc.$(selector).get();
		for (Node node : nodes) {
			stringBuilder.append(node.getHtml());
		}
		return stringBuilder.toString();
	}
	
	public String getHtml(){
		return doc.html();
	}
	
	

}
