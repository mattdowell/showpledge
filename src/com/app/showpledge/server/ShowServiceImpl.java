package com.app.showpledge.server;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.app.showpledge.client.util.stub.ShowService;
import com.app.showpledge.server.persistence.Obj;
import com.app.showpledge.server.persistence.dao.ShowDao;
import com.app.showpledge.server.search.SearchEngine;
import com.app.showpledge.server.search.SearchJanitorUtils;
import com.app.showpledge.shared.composite.ShowQueryResults;
import com.app.showpledge.shared.entities.Show;
import com.app.showpledge.shared.entities.Show.SystemStatus;
import com.app.showpledge.shared.entities.ShowSearchParams;
import com.app.showpledge.shared.entities.User;
import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.images.ImagesService;
import com.google.appengine.api.images.ImagesServiceFactory;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.Objectify;

/**
 * 
 * @author mjdowell
 * 
 */
public class ShowServiceImpl extends AbstractRemoteServlet implements ShowService {

	private static final ShowDao DAO = new ShowDao();
	private static final long serialVersionUID = -594503498668072081L;
	private static final BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
	private static final ImagesService imagesService = ImagesServiceFactory.getImagesService();
	public static final List<Show.SystemStatus> visibleShowStati = new ArrayList<Show.SystemStatus>(2);
	static {

		visibleShowStati.add(Show.SystemStatus.ACCEPTED);
		visibleShowStati.add(Show.SystemStatus.NOMINATED);
	}




	/**
	 * This is the first method that is called when a show is being created.
	 */
	@Override
	public Long nominateShow(Show inShow) {
		performLoginCheck();

		User currentUser = getCurrentUser();

		Key<User> userKey = new Key<User>(User.class, currentUser.getId());

		inShow.setNominationUser(userKey);
		inShow.setSystemStatus(SystemStatus.NOMINATED);
		inShow.setNominationDate(new Date());

		update(inShow, false);

		putShowInSession(inShow);

		return inShow.getId();
	}

	/**
	 * This show has been vetted, and now is being accepted in to the system.
	 */
	@Override
	public Long acceptShow(Show inShow) {

		User currentUser = super.getCurrentUser();

		performLoginCheck();

		Key<User> userKey = new Key<User>(User.class, currentUser.getId());

		inShow = get(inShow.getId());

		inShow.setSystemStatus(SystemStatus.ACCEPTED);
		inShow.setPromotionUser(userKey);
		inShow.setAcceptedDate(new Date());
		update(inShow);

		return inShow.getId();
	}

	@Override
	public List<Show> getAllNominatedShows() {
		Objectify obj = Obj.begin();
		return obj.query(Show.class).filter("systemStatus", Show.SystemStatus.NOMINATED).list();
	}

	/**
	 * Filters all shows that are nominated or promoted
	 */
	@Override
	public List<Show> getAllVisibleShows() {
		Objectify obj = Obj.begin();
		return obj.query(Show.class).filter("systemStatus in", visibleShowStati).list();
	}

	@Override
	public List<Show> getVisibleShows(int inOffset, int inLimit) {
		Objectify obj = Obj.begin();
		return obj.query(Show.class).order("name").filter("systemStatus in", visibleShowStati).offset(inOffset).limit(inLimit).list();

	}




	/**
	 * TODO: We should only be searching for ACCEPTED shows
	 */
	@Override
	public List<Show> searchForShows(ShowSearchParams inParams) {

		if (inParams.showStartWith != null) {
			Objectify obj = Obj.begin();
			List<Show> results = obj.query(Show.class).filter("name >=", inParams.showStartWith)
					.filter("name <", inParams.showStartWith + "\uFFFD").list();
			return results;
		} else {
			return SearchEngine.searchShows(inParams.ftsSearchString);
		}
	}

	@Override
	public List<Show> getAllShows() {
		Objectify obj = Obj.begin();

		// TODO: Do we need to populate each user?
		return obj.query(Show.class).order("name").list();
	}

	/**
	 * Return an accurate count of the given shows and their status
	 * 
	 * @param status
	 * @return
	 */
	public int getShowCount(Show.SystemStatus status) {
		Objectify obj = Obj.begin();
		if (status == null) {
			return obj.query(Show.class).count();
		} else {
			return obj.query(Show.class).filter("systemStatus", status).count();
		}
	}

	/**
	 * Returns a paging result of accepted shows
	 * 
	 * @param inOffset
	 * @param inLimit
	 * @return
	 */
	@Override
	public List<Show> getShows(int inOffset, int inLimit, Show.SystemStatus statuses) {
		Objectify obj = Obj.begin();
		if (statuses == null) {
			return obj.query(Show.class).order("name").offset(inOffset).limit(inLimit).list();
		} else {
			return obj.query(Show.class).order("name").filter("systemStatus", statuses).offset(inOffset).limit(inLimit).list();
		}
	}

	/**
	 * 
	 * @param inOffset
	 * @param inLimit
	 * @return
	 */
	public ShowQueryResults getNominatedShows(int inOffset, int inLimit) {
		int count = getShowCount(Show.SystemStatus.NOMINATED);
		List<Show> shows = getShows(inOffset, inLimit, Show.SystemStatus.NOMINATED);
		return new ShowQueryResults(shows, count);
	}

	/**
	 * 
	 * @param inOffset
	 * @param inLimit
	 * @return
	 */
	public ShowQueryResults getAcceptedShows(int inOffset, int inLimit) {
		int count = getShowCount(Show.SystemStatus.ACCEPTED);
		List<Show> shows = getShows(inOffset, inLimit, Show.SystemStatus.ACCEPTED);
		return new ShowQueryResults(shows, count);
	}

	@Override
	public String getBlobstoreUploadUrl() {
		performLoginCheck();
		return blobstoreService.createUploadUrl("/showpledge/upload");
	}

	/**
	 * TODO: Abstract
	 * 
	 * @param inShow
	 */
	private void updateSearchParameters(final Show inShow) {
		SearchJanitorUtils.updateFtsForSearchable(inShow);
	}

	/**
	 * 
	 * @return a list of URL's
	 */
	@Override
	public List<String> getAllShowImages() {
		List<Show> shows = getAllShows();
		List<String> urls = new ArrayList<String>();
		for (Show s : shows) {
			if (s != null) {
				if (s.getBlobKeys() != null) {
					for (String blobKey : s.getBlobKeys()) {
						String url = imagesService.getServingUrl(new BlobKey(blobKey));

						// NOTE@!!! FOR LOCAL DEBUGGING ONLY
						url = replaceLocalIP(url);

						urls.add(url);
					}
				}
			}
		}
		return urls;

	}

	private String replaceLocalIP(String inUrl) {
		return inUrl.replace(new StringBuffer("0.0.0.0"), new StringBuffer("127.0.0.1"));
	}

	/**
	 * 
	 * @return
	 */
	@Override
	public List<Show> getPromoShows() {
		return DAO.getPromoShows();
	}



	@Override
	public void update(final Show inShow) {
		update(inShow, true);
	}

	/**
	 * UPDATE A SHOW
	 * 
	 * @param inShow
	 * @param shouldMakeSearchable
	 */
	public void update(final Show inShow, boolean shouldMakeSearchable) {
		performLoginCheck();
		Objectify obj = Obj.begin();

		if (shouldMakeSearchable) {
			updateSearchParameters(inShow);
		}

		// We want to use the IMAGE properties that are in the Show that is stored in the Session.
		if (inShow.getPrimaryBlobKey() == null) {
			Show sessionShow = getShowInSession();
			inShow.setPrimaryBlobKey(sessionShow.getPrimaryBlobKey());
			inShow.setPrimaryImageUrl(sessionShow.getPrimaryImageUrl());
		}

		// Clear the caches
		if (inShow.isPromoted()) {
			DAO.clearPromoShowsCache();
			DAO.clearVisibleShowCount();
			// TODO: Spawn a thread to load the promo shows
		}

		// Persist the show
		obj.put(inShow);
	}

	@Override
	public Show get(Long inShowId) {
		if (inShowId == null) {
			return null;
		}
		Objectify obj = Obj.begin();
		Show show = obj.get(new Key<Show>(Show.class, inShowId));
		populateUsers(show);
		return show;
	}

	private void populateUsers(final Show inShow) {
		if (inShow.getNominationUser() != null) {
			Objectify obj = Obj.begin();
			User mgr = obj.get(inShow.getNominationUser());

			// Sets the transient campaign manager
			inShow.setCampaignManager(mgr);
		}
	}

	/**
	 * Refreshes the latest image url
	 */
	@Override
	public String getImageUrl(Show inShow) {
		return get(inShow.getId()).getPrimaryImageUrl();
	}

	@Override
	public void delete(Show inShow) {
		DAO.delete(inShow);
	}

	@Override
	public int getVisibleShowCount() {
		return DAO.getVisibleShowCount();
	}
}
