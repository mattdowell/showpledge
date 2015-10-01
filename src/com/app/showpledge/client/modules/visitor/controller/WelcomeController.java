package com.app.showpledge.client.modules.visitor.controller;

import com.app.showpledge.client.controller.panel.ContentContainer;
import com.app.showpledge.client.modules.visitor.view.WelcomeScreen;

public class WelcomeController {

	private final WelcomeScreen screen = new WelcomeScreen();

	public WelcomeController() {
		super();
	}
	
	public void show() {
		ContentContainer.setContent(screen);
	}	
}
