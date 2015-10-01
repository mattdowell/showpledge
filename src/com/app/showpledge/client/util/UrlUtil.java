package com.app.showpledge.client.util;

public class UrlUtil {

	/**
	 * Should strip HTTP:// or HTTPS:// from the prefix of an URL if it exists
	 * 
	 * @param inFullUrl
	 * @return
	 */
	public static String stripHTTPPrefix(String inFullUrl) {
		if (inFullUrl.indexOf("://") > 0) {
			return inFullUrl.substring(inFullUrl.indexOf("://")+3, inFullUrl.length());
		}
		return inFullUrl;
	}

}
