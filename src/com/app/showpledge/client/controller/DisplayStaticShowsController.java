package com.app.showpledge.client.controller;

import java.util.List;

import com.app.showpledge.client.modules.admin.controller.AdminEditShowController;
import com.app.showpledge.client.modules.visitor.controller.ShowDetailsController;
import com.app.showpledge.client.modules.visitor.view.PageableShowRows;
import com.app.showpledge.shared.entities.Show;
import com.google.gwt.view.client.AbstractDataProvider;
import com.google.gwt.view.client.ListDataProvider;

/**
 * This controller takes a static list of Shows and displays them
 * 
 * @author mjdowell
 * 
 */
public class DisplayStaticShowsController {

	private PageableShowRows screen = null;
	private PageableShowRows.Context myContext = null;
	private List<Show> myShows = null;
	private int totalRows;
	private ListDataProvider<Show> showListProvider;
	private boolean isAdmin = false;

	public DisplayStaticShowsController(List<Show> inShows) {
		initProvider(inShows);
	}

	public DisplayStaticShowsController(List<Show> inShows, boolean inIsAdmin) {
		initProvider(inShows);
		isAdmin = inIsAdmin;
	}

	public DisplayStaticShowsController(boolean inIsAdmin) {
		isAdmin = inIsAdmin;
	}

	public DisplayStaticShowsController() {
	}

	public void initProvider(List<Show> inShows) {
		myShows = inShows;
		if (myShows != null) {
			totalRows = myShows.size();
		} else {
			totalRows = 0;
		}
		showListProvider = new ListDataProvider<Show>(myShows);
	}

	/**
	 * What happens when someone selects a show!
	 * 
	 * @param inSelectedShow
	 */
	public void handleRowSelect(Show inSelectedShow) {

		if (isAdmin) {
			AdminEditShowController c = new AdminEditShowController(inSelectedShow);
			c.begin();
		} else {
			ShowDetailsController con = new ShowDetailsController(inSelectedShow);
			con.display();
		}
	}

	public void show() {
		myContext = buildContext();
		screen = new PageableShowRows(myContext);
		screen.display();
	}

	public PageableShowRows.Context buildContext() {
		return new PageableShowRows.Context() {

			@Override
			public int totalRows() {
				return totalRows;
			}

			@Override
			public void onRowClick(Show inShow) {
				handleRowSelect(inShow);
			}

			@Override
			public boolean isSortable() {
				return true;
			}

			@Override
			public AbstractDataProvider<Show> getDataProvider() {
				return showListProvider;
			}

			@Override
			public int getPageSize() {
				return 5;
			}
		};
	}

}
