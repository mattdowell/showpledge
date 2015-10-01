package com.app.showpledge.client.modules.admin.controller;

import com.app.showpledge.client.controller.AbstractDisplayShowsController;
import com.app.showpledge.client.modules.user.controller.EditShowController;
import com.app.showpledge.shared.entities.Show;
import com.app.showpledge.shared.entities.Show.SystemStatus;

public class AllShowsController extends AbstractDisplayShowsController {

	private boolean isAdmin = false;

	public AllShowsController(boolean isAdmin) {
		super();
		this.isAdmin = isAdmin;
	}

	public AllShowsController() {
		super();
		this.isAdmin = false;
	}

	@Override
	public SystemStatus getSystemStatus() {
		// Null makes the service search for all
		return null;
	}

	@Override
	public int getPageSizeForDisplay() {
		return DEFAULT_PAGE_SIZE;
	}

	@Override
	public void handleRowSelect(Show inSelectedShow) {
		
		EditShowController c = null;
		
		if (isAdmin) {
			c = new AdminEditShowController(inSelectedShow);
		} else {
			c = new EditShowController(inSelectedShow);

		}
		c.begin();
	}

}
