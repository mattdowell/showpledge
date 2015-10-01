package com.app.showpledge.client.modules.user.controller;

import com.app.showpledge.client.ShowPledge;
import com.app.showpledge.client.modules.visitor.controller.PromoShowsController;
import com.app.showpledge.client.util.Callback;
import com.app.showpledge.client.util.stub.UserService;
import com.app.showpledge.client.util.stub.UserServiceAsync;
import com.google.gwt.core.client.GWT;

public class LogoutController {
	
	private static final UserServiceAsync service = GWT.create(UserService.class);
	
	public void begin() {
		// Set up the callback object.
		Callback<Void> callback = new Callback<Void>() {
			public void onSuccess(Void inVoid) {
				// Server side complete, handle client
				handleClientLogout();
			}
		};

		service.logout(callback);

	}
	
	private void handleClientLogout() {
		ShowPledge.isUserLoggedIn = false;
		ShowPledge.myUser = null;
		ShowPledge.buildMenu(false);
		PromoShowsController c = new PromoShowsController();
		c.show("You are now logged out");		
	}
}
