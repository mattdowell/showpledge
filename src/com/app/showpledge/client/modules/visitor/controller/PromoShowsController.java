package com.app.showpledge.client.modules.visitor.controller;

import java.util.List;

import com.app.showpledge.client.controller.panel.ContentContainer;
import com.app.showpledge.client.modules.visitor.view.PromoScreen;
import com.app.showpledge.client.util.Callback;
import com.app.showpledge.client.util.stub.ShowService;
import com.app.showpledge.client.util.stub.ShowServiceAsync;
import com.app.showpledge.shared.entities.Show;
import com.google.gwt.core.client.GWT;

/**
 * Controlls the display of the Promotional shows. (usually 8 or so)
 * 
 * @author mjdowell
 * 
 */
public class PromoShowsController {

	private static final ShowServiceAsync showService = GWT.create(ShowService.class);
	private PromoScreen screen = null;
	private static final int ROWS = 4;
	private List<Show> myShows = null;
	private String myText = null;

	public PromoShowsController() {
		super();
	}

	public void show() {
		show(null);
	}
	public void show(String inTxt) {
		ContentContainer.showLoading();
		myText = inTxt;

		showService.getPromoShows(new Callback<List<Show>>() {

			@Override
			public void onSuccess(List<Show> result) {
				myShows = result;
				displayScreen();
			}

		});
	}

	private void displayScreen() {

		screen = new PromoScreen(new PromoScreen.Context() {

			@Override
			public List<Show> getShows() {
				return myShows;
			}

			@Override
			public int getRows() {
				return ROWS;
			}

			@Override
			public String getPromoText() {
				return myText;
			}
		});

		screen.displayPromotionShows();
		ContentContainer.hideLoading();

	}

}
