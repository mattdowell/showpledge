package com.app.showpledge.client.util;

import java.util.List;

import com.app.showpledge.client.util.stub.PledgeService;
import com.app.showpledge.client.util.stub.PledgeServiceAsync;
import com.app.showpledge.shared.entities.Pledge;
import com.google.gwt.core.client.GWT;
import com.google.gwt.view.client.AbstractDataProvider;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.Range;

public class LazyPledgeProvider extends AbstractDataProvider<Pledge> {

	PledgeServiceAsync service = GWT.create(PledgeService.class);
	
	@Override
	protected void onRangeChanged(HasData<Pledge> display) {
		
		final Range range = display.getVisibleRange();
		final int offset = range.getStart();
		final int limit = range.getLength();

		service.getPledges(offset, limit, new Callback<List<Pledge>>() {

			@Override
			public void onSuccess(List<Pledge> result) {
				updateRowData(offset, result);				
			}
		});		
	}

}
