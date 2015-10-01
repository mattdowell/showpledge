package com.app.showpledge.client.modules.visitor.controller;

import com.app.showpledge.client.controller.AbstractDisplayShowsController;
import com.app.showpledge.client.modules.visitor.view.PageableShowRows;
import com.app.showpledge.client.util.Callback;
import com.app.showpledge.client.util.LazyShowProvider;
import com.app.showpledge.client.util.stub.ShowService;
import com.app.showpledge.client.util.stub.ShowServiceAsync;
import com.app.showpledge.shared.entities.Show;
import com.google.gwt.core.client.GWT;
import com.google.gwt.view.client.AbstractDataProvider;

/**
 * Searches and displays all VISIBLE shows.
 * 
 * @author mjdowell
 *
 */
public class VisibleShowsController {

	private PageableShowRows screen = null;
	private LazyShowProvider provider = null;
	private int totalRows;
	private ShowServiceAsync showService = GWT.create(ShowService.class);
	private PageableShowRows.Context myContext = null;
	public static final int DEFAULT_PAGE_SIZE = 5;

	public VisibleShowsController() {
	}

	public void show() {
		getTotalRows();
	}

	private void begin() {
		myContext = buildContext();
		screen = new PageableShowRows(myContext);
		screen.display();
	}

	public void getTotalRows() {
		showService.getVisibleShowCount(new Callback<Integer>() {

			@Override
			public void onSuccess(Integer result) {
				totalRows = result;
				provider = new LazyShowProvider(null, totalRows);
				begin();
			}
		});
	}
	
	void handleRowSelect(Show inSelectedShow) {
		ShowDetailsController c = new ShowDetailsController(inSelectedShow);
		c.display();
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
				return false;
			}

			@Override
			public AbstractDataProvider<Show> getDataProvider() {
				return provider;
			}

			@Override
			public int getPageSize() {
				return AbstractDisplayShowsController.DEFAULT_PAGE_SIZE;
			}
		};
	}
}
