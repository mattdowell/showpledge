package com.app.showpledge.client.modules.user.controller;

import java.util.List;

import com.app.showpledge.client.controller.panel.ContentContainer;
import com.app.showpledge.client.modules.user.view.UserPledgesScreen;
import com.app.showpledge.client.util.Callback;
import com.app.showpledge.client.util.stub.PledgeService;
import com.app.showpledge.client.util.stub.PledgeServiceAsync;
import com.app.showpledge.client.util.stub.UserService;
import com.app.showpledge.client.util.stub.UserServiceAsync;
import com.app.showpledge.shared.entities.Pledge;
import com.app.showpledge.shared.entities.User;
import com.google.gwt.core.client.GWT;

/**
 * Display a users pledges
 * 
 * @author mjdowell
 * 
 */
public class DisplayPledgesController {

	private final PledgeServiceAsync pledgeService = GWT.create(PledgeService.class);
	private final UserServiceAsync service = GWT.create(UserService.class);
	List<Pledge> myPledges;
	User myUser;
	UserPledgesScreen myScreen;

	public DisplayPledgesController(User inUser) {
		super();
		myUser = inUser;
	}

	public DisplayPledgesController() {
		super();
		Callback<User> callback = new Callback<User>() {

			public void onSuccess(User result) {
				myUser = result;
			}
		};
		service.getLoggedInUser(callback);
	}

	public void display() {
		display(null);
	}

	public void display(final String inMsg) {
		Callback<List<Pledge>> callback = new Callback<List<Pledge>>() {
			@Override
			public void onSuccess(List<Pledge> result) {
				myPledges = result;
				myScreen = new UserPledgesScreen(new UserPledgesScreen.Context() {

					@Override
					public List<Pledge> getPledges() {
						return myPledges;
					}
				});
				myScreen.show();

				if (inMsg != null) {
					ContentContainer.showInfo(inMsg);
				}
			}
		};

		pledgeService.getPledgesForCurrentUser(callback);
	}
}
