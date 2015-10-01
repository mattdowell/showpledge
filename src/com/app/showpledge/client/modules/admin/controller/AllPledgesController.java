package com.app.showpledge.client.modules.admin.controller;

import java.util.List;

import com.app.showpledge.client.ShowPledge;
import com.app.showpledge.client.modules.admin.view.AllPledgesView;
import com.app.showpledge.client.modules.admin.view.AllPledgesView.Context;
import com.app.showpledge.client.util.Callback;
import com.app.showpledge.client.util.LazyPledgeProvider;
import com.app.showpledge.client.util.stub.PledgeService;
import com.app.showpledge.client.util.stub.PledgeServiceAsync;
import com.app.showpledge.shared.entities.Pledge;
import com.google.gwt.core.client.GWT;
import com.google.gwt.view.client.AbstractDataProvider;

/**
 * ADMIN ONLY
 * 
 * @author mjdowell
 * 
 */
public class AllPledgesController {

	PledgeServiceAsync service = GWT.create(PledgeService.class);
	LazyPledgeProvider provider = new LazyPledgeProvider();
	AllPledgesView view = new AllPledgesView();
	int totalRows = 0;
	List<Pledge> myPledges = null;
	String msg = null;

	public void display() {
		if (ShowPledge.myUser.isAdmin()) {
			
			service.getAllPledges(new Callback<List<Pledge>>() {

				@Override
				public void onSuccess(List<Pledge> result) {
					myPledges = result;
					if (myPledges != null) {
						totalRows = myPledges.size();
					}
					view.display(buildContext());
				}
			});
		}
	}
	
	public void display(String inMsg) {
		msg = inMsg;
		display();
	}

	private Context buildContext() {
		return new Context() {

			@Override
			public int totalRows() {
				return totalRows;
			}

			@Override
			public boolean isSortable() {
				return false;
			}

			@Override
			public AbstractDataProvider<Pledge> getDataProvider() {
				return provider;
			}

			@Override
			public int getPageSize() {
				return 50;
			}

			@Override
			public String getMsg() {
				return msg;
			}
		};
	}
}
