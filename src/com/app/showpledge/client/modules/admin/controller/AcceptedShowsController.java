package com.app.showpledge.client.modules.admin.controller;

import com.app.showpledge.client.controller.AbstractDisplayShowsController;
import com.app.showpledge.client.modules.visitor.controller.ShowDetailsController;
import com.app.showpledge.shared.entities.Show;
import com.app.showpledge.shared.entities.Show.SystemStatus;

/**
 * Admin / publisher module
 * @author mjdowell
 *
 */
public class AcceptedShowsController extends AbstractDisplayShowsController {

	@Override
	public SystemStatus getSystemStatus() {
		return SystemStatus.ACCEPTED;
	}

	@Override
	public int getPageSizeForDisplay() {
		return DEFAULT_PAGE_SIZE;
	}

	@Override
	public void handleRowSelect(Show inSelectedShow) {
		ShowDetailsController c = new ShowDetailsController(inSelectedShow);
		c.display();
	}

}
