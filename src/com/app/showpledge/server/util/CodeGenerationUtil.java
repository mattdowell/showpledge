package com.app.showpledge.server.util;

/**
 * http://stackoverflow.com/questions/41107/how-to-generate-a-random-alpha-numeric-string-in-java
 * 
 * @author mjdowell
 *
 */
public class CodeGenerationUtil {
	
	/**
	 * Generates a unique string.
	 * 
	 * @return
	 */
	public static String generate() {
		return Long.toHexString(Double.doubleToLongBits(Math.random()));
	}

}
