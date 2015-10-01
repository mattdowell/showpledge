package com.app.showpledge.client.modules.visitor.controller;

import java.util.List;

import com.app.showpledge.client.controller.DisplayStaticShowsController;
import com.app.showpledge.client.modules.visitor.view.SearchForm;
import com.app.showpledge.client.util.Callback;
import com.app.showpledge.client.util.stub.ShowService;
import com.app.showpledge.client.util.stub.ShowServiceAsync;
import com.app.showpledge.shared.entities.Show;
import com.app.showpledge.shared.entities.ShowSearchParams;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;

public class SearchShowsController {

	final ShowServiceAsync service = GWT.create(ShowService.class);
	SearchForm myForm = new SearchForm();
	String resultsText = null;

	public SearchShowsController() {
		super();
	}

	/**
	 * 
	 * @return
	 */
	private SearchForm.Context buildContext() {
		return new SearchForm.Context() {

			@Override
			public void onClick(ClickEvent event) {
				callSearchService();
			}

			@Override
			public String getResultsText() {
				return resultsText;
			}

		};
	}

	public void startNewSearch() {
		myForm.build(buildContext());
	}

	/**
	 * 
	 */
	void callSearchService() {
		ShowSearchParams p = new ShowSearchParams();
		p.ftsSearchString = myForm.getSearchString();

		Callback<List<Show>> callback = new Callback<List<Show>>() {

			@Override
			public void onSuccess(List<Show> results) {

				if (results == null || results.isEmpty()) {
					resultsText = "Sorry, no results found for search: " + myForm.getSearchString();
					myForm.build(buildContext());
				} else {
					DisplayStaticShowsController c = new DisplayStaticShowsController(results);
					c.show();
				}
			}
		};

		service.searchForShows(p, callback);

	}

}
