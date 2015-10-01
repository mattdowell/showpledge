package com.app.showpledge.client.util.stub;

import java.util.List;

import com.app.showpledge.shared.composite.ShowQueryResults;
import com.app.showpledge.shared.entities.Show;
import com.app.showpledge.shared.entities.ShowSearchParams;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("shows")
public interface ShowService extends RemoteService {

	public Long nominateShow(Show inShow);

	public Long acceptShow(Show inShow);

	public void update(Show inShow);
	
	public void delete(Show inShow);
	
	public void putShowInSession(Show inShow);

	public ShowQueryResults getNominatedShows(int inOffset, int inLimit);

	public ShowQueryResults getAcceptedShows(int inOffset, int inLimit);

	public List<Show> getAllShows();

	public List<Show> getPromoShows();

	List<Show> searchForShows(ShowSearchParams inParams);

	public String getBlobstoreUploadUrl();

	public List<String> getAllShowImages();

	public Show get(Long inShowKey);

	public int getShowCount(Show.SystemStatus status);
	
	public int getVisibleShowCount();
	
	public List<Show> getAllNominatedShows();
	
	public List<Show> getAllVisibleShows();	

	public List<Show> getVisibleShows(int inOffset, int inLimit);
	
	public List<Show> getShows(int inOffset, int inLimit, Show.SystemStatus status);
	
	public String getImageUrl(Show inShow);

}
