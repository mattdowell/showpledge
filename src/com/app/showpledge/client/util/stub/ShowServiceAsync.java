package com.app.showpledge.client.util.stub;

import java.util.List;

import com.app.showpledge.shared.composite.ShowQueryResults;
import com.app.showpledge.shared.entities.Show;
import com.app.showpledge.shared.entities.Show.SystemStatus;
import com.app.showpledge.shared.entities.ShowSearchParams;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ShowServiceAsync {

	void nominateShow(Show inShow, AsyncCallback<Long> callback);

	void getAllNominatedShows(AsyncCallback<List<Show>> callback);
	
	void getAllShows(AsyncCallback<List<Show>> callback);

	void searchForShows(ShowSearchParams inParams, AsyncCallback<List<Show>> callback);

	void acceptShow(Show inShow, AsyncCallback<Long> callback);

	void getBlobstoreUploadUrl(AsyncCallback<String> callback);

	void getAllShowImages(AsyncCallback<List<String>> callback);

	void getPromoShows(AsyncCallback<List<Show>> callback);

	void update(Show inShow, AsyncCallback<Void> callback);

	void get(Long inShowKey, AsyncCallback<Show> callback);

	void getNominatedShows(int inOffset, int inLimit, AsyncCallback<ShowQueryResults> callback);

	void getAcceptedShows(int inOffset, int inLimit, AsyncCallback<ShowQueryResults> callback);

	void getAllVisibleShows(AsyncCallback<List<Show>> callback);
	
	void getVisibleShows(int inOffset, int inLimit, AsyncCallback<List<Show>> callback);

	void getShowCount(SystemStatus status, AsyncCallback<Integer> callback);

	void getShows(int inOffset, int inLimit, SystemStatus status, AsyncCallback<List<Show>> callback);

	void putShowInSession(Show inShow, AsyncCallback<Void> callback);

	void getImageUrl(Show inShow, AsyncCallback<String> callback);

	void getVisibleShowCount(AsyncCallback<Integer> callback);

	void delete(Show inShow, AsyncCallback<Void> callback);

}
