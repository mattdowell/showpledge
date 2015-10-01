package com.app.showpledge.client.modules.visitor.controller;

import com.app.showpledge.client.controller.panel.ContentContainer;
import com.app.showpledge.client.modules.visitor.view.AboutUsScreen;

public class AboutUsController {
	
	private final AboutUsScreen screen = new AboutUsScreen();

	public AboutUsController() {
		super();
	}
	
	public void show() {
		ContentContainer.setContent(screen);
	}

}
