package com.app.showpledge.shared.composite;

import java.io.Serializable;

public class Feedback implements Serializable {
	private static final long serialVersionUID = -666630915988600557L;
	
	private String text;

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
	
	
}
