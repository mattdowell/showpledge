package com.app.showpledge.client.util;

import java.util.List;

import com.app.showpledge.client.util.stub.ShowService;
import com.app.showpledge.client.util.stub.ShowServiceAsync;
import com.app.showpledge.shared.entities.Show;
import com.app.showpledge.shared.entities.Show.SystemStatus;
import com.google.gwt.core.client.GWT;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.Range;

/**
 * {@link #onRangeChanged(HasData)} is called when the table requests a new range of data. You can push data back to the displays using
 * {@link #updateRowData(int, List)}.
 * 
 * int start = range.getStart(); int length = range.getLength(); List<String> newData = new ArrayList<String>(); for (int i = start; i <
 * start + length; i++) { newData.add("Item " + i); }
 * 
 * // Push the data to the displays. AsyncDataProvider will only update // displays that are within range of the data. updateRowData(start,
 * newData);
 */
public class LazyShowProvider extends AsyncDataProvider<Show> {

	private ShowServiceAsync showService = GWT.create(ShowService.class);

	private SystemStatus status = null;

	private int totalRowCount;

	public LazyShowProvider(SystemStatus inStat, int inRowCount) {
		status = inStat;
		totalRowCount = inRowCount;
	}

	@Override
	protected void onRangeChanged(HasData<Show> display) {
		final Range range = display.getVisibleRange();
		final int offset = range.getStart();
		final int limit = range.getLength();

		if (status != null) {
			showService.getShows(offset, limit, status, new Callback<List<Show>>() {

				@Override
				public void onSuccess(List<Show> result) {
					updateRowData(offset, result);
					updateRowCount(totalRowCount, true);
				}
			});
		} else {
			showService.getVisibleShows(offset, limit, new Callback<List<Show>>() {

				@Override
				public void onSuccess(List<Show> result) {
					updateRowData(offset, result);
					updateRowCount(totalRowCount, true);
				}
			});
		}
	}
}
