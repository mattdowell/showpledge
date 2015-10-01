package com.app.showpledge.client.modules.admin.controller;

import com.app.showpledge.client.modules.user.controller.EditShowController;
import com.app.showpledge.shared.entities.Show;

/**
 * 
 * @author mjdowell
 * 
 */
public class AdminEditShowController extends EditShowController {

	public AdminEditShowController() {
		super();
	}

	public AdminEditShowController(Show inShow) {
		super(inShow, true, true);
	}

	/**
	 * Admins/Pro's get to edit an Show with admin control
	 */
	public void begin() {
		//myForm = new ManageShowForm(buildContext());
		super.begin();
	}

}
