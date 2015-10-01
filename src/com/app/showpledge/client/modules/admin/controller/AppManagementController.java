package com.app.showpledge.client.modules.admin.controller;

import com.app.showpledge.client.controller.panel.ContentContainer;
import com.app.showpledge.client.modules.admin.view.AppManagement;

public class AppManagementController  {

	private final AppManagement screen = new AppManagement();
	
	public void show() {
		ContentContainer.setContent(screen);
	}

}
