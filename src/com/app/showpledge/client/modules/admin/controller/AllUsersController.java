package com.app.showpledge.client.modules.admin.controller;

import com.app.showpledge.client.modules.admin.view.PageableUserRows;
import com.app.showpledge.client.modules.user.controller.EditUserController;
import com.app.showpledge.client.util.Callback;
import com.app.showpledge.client.util.LazyUserProvider;
import com.app.showpledge.client.util.stub.UserService;
import com.app.showpledge.client.util.stub.UserServiceAsync;
import com.app.showpledge.shared.entities.User;
import com.google.gwt.core.client.GWT;
import com.google.gwt.view.client.AbstractDataProvider;

/**
 * 
 * @author mjdowell
 *
 */
public class AllUsersController {
	
	private PageableUserRows screen = null;
	private LazyUserProvider dataProvider = new LazyUserProvider();
	private final UserServiceAsync userService = GWT.create(UserService.class);
	private int totalUsers;
	private String message = null;
	
	public AllUsersController() {
		screen = new PageableUserRows(buildContext());
	}
	
	public AllUsersController(String inMsg) {
		message = inMsg;
		screen = new PageableUserRows(buildContext());
	}
	
	private void handleUserClick(User inUser) {
		EditUserController euc = new EditUserController(inUser, "Editing user: " + inUser.getDisplayName());
		euc.startEditUser();		
	}
	
	public void begin() {
		getTotalUsers();
	}
	
	private void displayScreen() {
		screen.display();
	}
	private PageableUserRows.Context buildContext() {
		return new PageableUserRows.Context() {

			@Override
			public AbstractDataProvider<User> getDataProvider() {
				return dataProvider;
			}

			@Override
			public void onRowClick(User inUser) {
				handleUserClick(inUser);
			}

			@Override
			public int totalRows() {
				return totalUsers;
			}

			@Override
			public int getPageSize() {
				return 5;
			}

			@Override
			public String getTitle() {
				return message;
			}
			
		};
	}
	
	private void getTotalUsers() {
		userService.getTotalUserCount(new Callback<Integer>() {

			@Override
			public void onSuccess(Integer result) {
				totalUsers = result;
				displayScreen();
			}
			
		});
	}
	
		

}
