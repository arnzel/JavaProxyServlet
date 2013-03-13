package proxy.string;

import jTidy.JTidyUtils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.w3c.tidy.Tidy;

import proxy.net.ProxyConnection;

public class HtmlStringUtils {
	
	/**
	 * applies pretty printing to an html file via JTidy
	 * 
	 * @param htmlString the html-file to format
	 * @param encoding the encoding of the html-file
	 * @return an formatted html-file as string
	 * @throws UnsupportedEncodingException if  parameter encoding is not valid
	 */
	
	public static String formatHtmlString(String htmlString, String encoding)
			throws UnsupportedEncodingException {
		byte[] bytes = htmlString.getBytes(encoding);
		InputStream inputStream = new ByteArrayInputStream(bytes);
		Tidy tidy = new Tidy();
		JTidyUtils.setTidyCharset(encoding, tidy);
		tidy.setShowWarnings(false);
		tidy.setQuiet(true);
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		tidy.parse(inputStream, byteArrayOutputStream);
		return byteArrayOutputStream.toString();
	}
	
	/**
	 * gets the encoding of an html page by parsing the content type
	 * 
	 * @param contentType a given contentype-string of an html-file
	 * @return the encoding as string or default encoding if an error occurs
	 */
	
	public static String getEncoding(String contentType) {
		String[] contentTypeSplit = contentType.split(";");
		try {
			String charSet = contentTypeSplit[1];
			String[] charSetSplit = charSet.split("=");
			String encoding = charSetSplit[1];
			return encoding;
		} catch (Exception e) {
			return ProxyConnection.DEFAULT_ENCODING;
		}
	}

}
