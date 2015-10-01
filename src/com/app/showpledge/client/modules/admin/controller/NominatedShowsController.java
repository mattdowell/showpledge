package com.app.showpledge.client.modules.admin.controller;

import com.app.showpledge.client.controller.AbstractDisplayShowsController;
import com.app.showpledge.client.modules.user.controller.EditShowController;
import com.app.showpledge.shared.entities.Show;
import com.app.showpledge.shared.entities.Show.SystemStatus;

public class NominatedShowsController extends AbstractDisplayShowsController {

	@Override
	public SystemStatus getSystemStatus() {
		return SystemStatus.NOMINATED;
	}

	@Override
	public int getPageSizeForDisplay() {
		return DEFAULT_PAGE_SIZE;
	}

	@Override
	public void handleRowSelect(Show inSelectedShow) {
		EditShowController c = new EditShowController(inSelectedShow);
		c.begin();
	}

}
