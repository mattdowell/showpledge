package com.app.showpledge.client.controller;

import com.app.showpledge.client.modules.visitor.view.PageableShowRows;
import com.app.showpledge.client.util.Callback;
import com.app.showpledge.client.util.LazyShowProvider;
import com.app.showpledge.client.util.stub.ShowService;
import com.app.showpledge.client.util.stub.ShowServiceAsync;
import com.app.showpledge.shared.entities.Show;
import com.app.showpledge.shared.entities.Show.SystemStatus;
import com.google.gwt.core.client.GWT;
import com.google.gwt.view.client.AbstractDataProvider;

/**
 * This controller is abstract, with concrete implementations that have their own SystemStatus
 * 
 * @author mjdowell
 * 
 */
public abstract class AbstractDisplayShowsController {

	private PageableShowRows screen = null;
	private LazyShowProvider provider = null;
	private int totalRows;
	private ShowServiceAsync showService = GWT.create(ShowService.class);
	private PageableShowRows.Context myContext = null;
	public static final int DEFAULT_PAGE_SIZE = 5;

	public AbstractDisplayShowsController() {
	}

	public abstract SystemStatus getSystemStatus();

	public abstract int getPageSizeForDisplay();

	public abstract void handleRowSelect(Show inSelectedShow);

	public void show() {
		getTotalRows();
	}

	private void begin() {
		myContext = buildContext();
		screen = new PageableShowRows(myContext);
		screen.display();
	}

	public void getTotalRows() {
		showService.getShowCount(getSystemStatus(), new Callback<Integer>() {

			@Override
			public void onSuccess(Integer result) {
				totalRows = result;
				provider = new LazyShowProvider(getSystemStatus(), totalRows);
				begin();
			}
		});
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
				return getPageSizeForDisplay();
			}
		};
	}
}
