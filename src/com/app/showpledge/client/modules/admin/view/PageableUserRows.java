package com.app.showpledge.client.modules.admin.view;

import com.app.showpledge.client.controller.panel.ContentContainer;
import com.app.showpledge.shared.entities.User;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.view.client.AbstractDataProvider;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;

public class PageableUserRows {
	
	public interface Context {
		AbstractDataProvider<User> getDataProvider();

		void onRowClick(User inShow);

		int totalRows();

		int getPageSize();
		
		String getTitle();
	}
	
	private VerticalPanel mainPanel = null;
	private CellTable<User> usersTable = null;
	private SimplePager myPager = null;
	private Context myContext = null;
	private Label title = null;

	public PageableUserRows(Context inCtx) {
		myContext = inCtx;
	}	
	
	/**
	 * Loop through all the users and show them in a table
	 * 
	 * @param inUsers
	 */
	public void display() {

		mainPanel = new VerticalPanel();
		
		if (myContext.getTitle() != null) {
			title = new Label(myContext.getTitle());
			title.setStyleName("page-title");
			mainPanel.add(title);
		}

		// Build the table
		usersTable = buildCellTable();

		// Add the table to the data provider
		myContext.getDataProvider().addDataDisplay(usersTable);

		mainPanel.add(usersTable);

		// Build the pager
		myPager = buildPager(usersTable);
		mainPanel.add(myPager);

		// Set the content
		ContentContainer.setContent(mainPanel);
	}	
	
	/**
	 * Create a Pager to control the table.
	 * 
	 * @param inTable
	 * @return
	 */
	private SimplePager buildPager(CellTable<User> inTable) {
		SimplePager.Resources pagerResources = GWT.create(SimplePager.Resources.class);
		SimplePager pager = new SimplePager(TextLocation.CENTER, pagerResources, false, 0, true);
		pager.setDisplay(inTable);
		pager.setPageSize(myContext.getPageSize());
		return pager;
	}	
	
	/**
	 * Build a pageable table
	 * 		
	 * @return
	 */
	private CellTable<User> buildCellTable() {
		
		CellTable<User> table = new CellTable<User>();

		// Create name column.
		TextColumn<User> uidCol = new TextColumn<User>() {
			@Override
			public String getValue(User inUser) {
				return inUser.getId().toString();
			}
		};
		
		TextColumn<User> usernameCol = new TextColumn<User>() {
			@Override
			public String getValue(User inUser) {
				return inUser.getEmail();
			}
		};
		
		// Temporary
		TextColumn<User> pwdCol = new TextColumn<User>() {
			@Override
			public String getValue(User inUser) {
				return inUser.getPassword();
			}
		};
		
		TextColumn<User> firstNameCol = new TextColumn<User>() {
			@Override
			public String getValue(User inUser) {
				return inUser.getFirstName();
			}
		};		
		TextColumn<User> lastNameCol = new TextColumn<User>() {
			@Override
			public String getValue(User inUser) {
				return inUser.getLastName();
			}
		};
		TextColumn<User> screenNameCol = new TextColumn<User>() {
			@Override
			public String getValue(User inUser) {
				return inUser.getDisplayName();
			}
		};				
		TextColumn<User> validatedCol = new TextColumn<User>() {
			@Override
			public String getValue(User inUser) {
				return String.valueOf(inUser.isVerified());
			}
		};		
		TextColumn<User> suspendedCol = new TextColumn<User>() {
			@Override
			public String getValue(User inUser) {
				return String.valueOf(inUser.isLocked());
			}
		};				
		TextColumn<User> failedActCol = new TextColumn<User>() {
			@Override
			public String getValue(User inUser) {
				return String.valueOf(inUser.getFailedValidations());
			}
		};		

		// Add the columns.
		table.addColumn(uidCol, "ID");
		table.addColumn(usernameCol, "Username");
		table.addColumn(pwdCol, "Pwd");
		table.addColumn(firstNameCol, "First");
		table.addColumn(lastNameCol, "Last");
		table.addColumn(screenNameCol, "Screen");
		table.addColumn(validatedCol, "Validated?");
		table.addColumn(suspendedCol, "Suspended?");
		table.addColumn(failedActCol, "Failed Actvs.");
		

		// Add a selection model so we can select cells.
		final SingleSelectionModel<User> selectionModel = new SingleSelectionModel<User>(User.KEY_PROVIDER);
		table.setSelectionModel(selectionModel);
		selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
			public void onSelectionChange(SelectionChangeEvent event) {
				myContext.onRowClick(selectionModel.getSelectedObject());
			}
		});

		return table;
	}	

}
