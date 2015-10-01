package com.app.showpledge.server.util;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

public final class SPProperties extends Properties {
	
	private static final long serialVersionUID = -8850210629321557727L;

	private static SPProperties INSTANCE = new SPProperties();
	
	private static Properties myProperties = null;
	private SPProperties() {
		myProperties = new Properties();
		try {
		myProperties.load(new FileInputStream(new File("WEB-INF/system.properties")));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public Properties getProperties() {
		return myProperties;
	}
	
	public static SPProperties getInstance() {
		return INSTANCE;
	}

}
