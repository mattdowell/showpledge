package com.app.showpledge.client.util;

import java.util.List;

import com.app.showpledge.client.util.stub.UserService;
import com.app.showpledge.client.util.stub.UserServiceAsync;
import com.app.showpledge.shared.entities.User;
import com.google.gwt.core.client.GWT;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.Range;

/**
 * Lazylily page through  ALL USERS
 *  
 * @author mjdowell
 *
 */
public class LazyUserProvider extends AsyncDataProvider<User> {

	private final UserServiceAsync userService = GWT.create(UserService.class);

	@Override
	protected void onRangeChanged(HasData<User> display) {

		final Range range = display.getVisibleRange();
		final int offset = range.getStart();
		final int limit = range.getLength();

		userService.getUsers(offset, limit, new Callback<List<User>>() {

			@Override
			public void onSuccess(List<User> result) {
				updateRowData(offset, result);				
			}
		});

	}

}
