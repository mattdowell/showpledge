package com.app.showpledge.client.modules.admin.controller;

import java.util.List;

import com.app.showpledge.client.controller.DisplayStaticShowsController;
import com.app.showpledge.client.util.Callback;
import com.app.showpledge.client.util.stub.ShowService;
import com.app.showpledge.client.util.stub.ShowServiceAsync;
import com.app.showpledge.shared.entities.Show;
import com.google.gwt.core.client.GWT;

/**
 * Display all the promo shows in a list format.
 * 
 * @author mjdowell
 *
 */
public class PromoShowsListController extends DisplayStaticShowsController {

	final ShowServiceAsync service = GWT.create(ShowService.class);
	
	public PromoShowsListController(List<Show> inShows, boolean isAdmin) {
		super(inShows, isAdmin);
	}
	
	public PromoShowsListController() {
	}
	
	public PromoShowsListController(boolean inAdmin) {
		super(inAdmin);
	}
	
	public void begin() {
		service.getPromoShows(new Callback<List<Show>>() {

			@Override
			public void onSuccess(List<Show> result) {
				initProvider(result);
				show();				
			}
			
		});
	}

}
