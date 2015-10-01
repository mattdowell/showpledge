package com.app.showpledge.client.util;

import com.app.showpledge.client.controller.panel.ContentContainer;
import com.app.showpledge.client.modules.visitor.controller.LoginController;
import com.app.showpledge.shared.exception.UserNotVerifiedException;
import com.app.showpledge.shared.exception.UserSuspendedException;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Concrete implementation of AsyncCallback that handles the errors in a unified format.
 * 
 * @author mjdowell
 * 
 * @param <T>
 */
public abstract class Callback<T> implements AsyncCallback<T> {

	@Override
	public void onFailure(Throwable caught) {
		caught.printStackTrace();

		if (caught instanceof UserNotVerifiedException) {
			ContentContainer.showError("Your user account has not been verified yet. Please check your email (or spam) inbox.");
			showLogin();
		} else if (caught instanceof UserSuspendedException) {
			ContentContainer.showError("Your user account has been suspended. Please email the administrator if you have any questions.");
			showLogin();
		} else {
			ContentContainer.showError("There has been an unknown error. " + caught.getMessage() + " Support has been notified.");
			showLogin();
		}
	}
	
	private void showLogin() {
		LoginController c = new LoginController();
		c.begin();		
		ContentContainer.hideLoading();
	}
}
