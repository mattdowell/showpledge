package com.app.showpledge.client.modules.admin.view;

import java.util.List;

import com.app.showpledge.client.controller.panel.ContentContainer;
import com.app.showpledge.client.modules.user.controller.EditUserController;
import com.app.showpledge.client.util.stub.UserService;
import com.app.showpledge.client.util.stub.UserServiceAsync;
import com.app.showpledge.shared.entities.User;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.VerticalPanel;

public class AllUsers {

	private VerticalPanel mainPanel = null;
	private FlexTable usersTable = null;
	private List<User> myUsers = null;
	private final UserServiceAsync userService = GWT.create(UserService.class);

	/**
	 * Loop through all the users and show them in a table
	 * 
	 * @param inUsers
	 */
	public void showAllUsers(List<User> inUsers) {
		mainPanel = new VerticalPanel();
		usersTable = new FlexTable();
		myUsers = inUsers;
		buildTable();
		mainPanel.add(usersTable);
		ContentContainer.setContent(mainPanel);
	}

	private void buildTable() {

		usersTable.clear();
		int i = 0;
		usersTable.setText(0, i++, "UID");
		usersTable.setText(0, i++, "Username");
		usersTable.setText(0, i++, "Password");
		usersTable.setText(0, i++, "First");
		usersTable.setText(0, i++, "Last");
		usersTable.setText(0, i++, "Screen Name");
		usersTable.setText(0, i++, "Validated?");
		usersTable.setText(0, i++, "Suspended?");
		usersTable.setText(0, i++, "Delete");
		usersTable.setText(0, i++, "Suspend");
		usersTable.setText(0, i++, "Activate");
		usersTable.setText(0, i++, "Edit");
		usersTable.setText(0, i++, "Failed Actv8s.");

		for (int index = 0; index < myUsers.size(); index++) {
			addUser(myUsers.get(index), index + 1);
		}

	}

	/**
	 * 
	 * @param inUser
	 * @param inRowId
	 */
	private void addUser(final User inUser, final int inRowId) {
		System.out.println("Row: " + inRowId + " Username: " + inUser.getEmail());

		// Add a button to remove this stock from the table.
		Button removeUserButton = new Button("Del.");
		removeUserButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				try {
					System.out.println("Removing: " + inUser);
					myUsers.remove(inUser);
					usersTable.removeRow(inRowId);
					buildTable();
				} catch (Exception e) {
					ContentContainer.showError(e.toString());
				}
			}
		});

		Button suspendUserButton = new Button("Susp.");
		suspendUserButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				try {
					System.out.println("Suspending: " + inUser);
					userService.suspendUser(inUser, buildVoidThenRefreshCallback(inUser.getId()));
				} catch (Exception e) {
					ContentContainer.showError(e.toString());
				}
			}
		});

		Button activateUserButton = new Button("Actv8.");
		activateUserButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				try {
					System.out.println("Suspending: " + inUser);
					userService.activateUser(inUser, buildVoidThenRefreshCallback(inUser.getId()));
				} catch (Exception e) {
					ContentContainer.showError(e.toString());
				}
			}
		});

		Button editUserButton = new Button("Edit");
		editUserButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				try {

					System.out.println("Editing: " + inUser);
					EditUserController euc = new EditUserController(inUser, "Editing user: " + inUser.getDisplayName());
					euc.startEditUser();

				} catch (Exception e) {
					ContentContainer.showError(e.toString());
				}
			}
		});

		int t = 0;
		usersTable.setText(inRowId, t++, inUser.getId().toString());
		usersTable.setText(inRowId, t++, inUser.getEmail());
		usersTable.setText(inRowId, t++, inUser.getPassword());
		usersTable.setText(inRowId, t++, inUser.getFirstName());
		usersTable.setText(inRowId, t++, inUser.getLastName());
		usersTable.setText(inRowId, t++, inUser.getScreenName());
		usersTable.setText(inRowId, t++, String.valueOf(inUser.isVerified()));
		usersTable.setText(inRowId, t++, String.valueOf(inUser.isLocked()));
		usersTable.setWidget(inRowId, t++, removeUserButton);
		usersTable.setWidget(inRowId, t++, suspendUserButton);
		usersTable.setWidget(inRowId, t++, activateUserButton);
		usersTable.setWidget(inRowId, t++, editUserButton);
		usersTable.setText(inRowId, t++, String.valueOf(inUser.getFailedValidations()));

	}

	/**
	 * 
	 * @param inUserId
	 * @return
	 */
	private AsyncCallback<Boolean> buildValidateCallback(Long inUserId) {

		// Set up the callback object.
		AsyncCallback<Boolean> callback = new AsyncCallback<Boolean>() {
			@Override
			public void onFailure(Throwable caught) {
				ContentContainer.showError(caught);
			}

			@Override
			public void onSuccess(Boolean result) {
				refreshUsersList();
			}
		};

		return callback;
	}

	private AsyncCallback<Void> buildVoidThenRefreshCallback(Long inUserId) {

		// Set up the callback object.
		AsyncCallback<Void> callback = new AsyncCallback<Void>() {
			public void onFailure(Throwable caught) {
				ContentContainer.showError(caught);
			}

			public void onSuccess(Void result) {
				refreshUsersList();
			}
		};

		return callback;
	}

	/**
	 * 
	 */
	private void refreshUsersList() {

		// Set up the callback object.
		AsyncCallback<List<User>> callback = new AsyncCallback<List<User>>() {
			@Override
			public void onFailure(Throwable caught) {
				ContentContainer.showError(caught);
			}

			@Override
			public void onSuccess(List<User> inUsers) {
				myUsers = inUsers;
				buildTable();
			}
		};

		userService.getAllUsers(callback);
	}

}
