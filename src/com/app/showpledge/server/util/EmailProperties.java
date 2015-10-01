package com.app.showpledge.server.util;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

public class EmailProperties extends Properties {

	private static final long serialVersionUID = -8850210629321557727L;

	private static EmailProperties INSTANCE = new EmailProperties();

	private EmailProperties() {
		try {
			load(new FileInputStream(new File("WEB-INF/email.properties")));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static EmailProperties getInstance() {
		return INSTANCE;
	}
}
