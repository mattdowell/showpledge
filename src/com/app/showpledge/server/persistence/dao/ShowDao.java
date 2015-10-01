package com.app.showpledge.server.persistence.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.app.showpledge.server.ShowServiceImpl;
import com.app.showpledge.server.persistence.Obj;
import com.app.showpledge.shared.entities.Show;
import com.googlecode.objectify.Objectify;

/**
 * TODO: Refactor to get rid of biz logic in here.. belongs in service
 * 
 * @author mjdowell
 *
 */
public class ShowDao extends ObjectifyGenericDao<Show> {
	
	// Cache's
	private static final Map<String, List<Show>> SHOW_CACHE = new HashMap<String, List<Show>>();
	private static final Map<String, Integer> COUNT_CACHE = new HashMap<String, Integer>();
	
	// Cache labels
	private static final String VISIBLE_SHOW_COUNT_LABLE = "visibleShowCount";
	private static final String VISIBLE_SHOW_LABLE = "visibleShows";
	private static final String PROMO_SHOW_LABLE = "promoShows";
	
	public void delete(Show inShow) {
		super.delete(inShow);
	}

	/**
	 * 
	 */
	public void clearVisibleShowCount() {
		SHOW_CACHE.put(VISIBLE_SHOW_LABLE, null);
	}
	/**
	 * 
	 */
	public void clearPromoShowsCache() {
		SHOW_CACHE.put(PROMO_SHOW_LABLE, null);
	}
	
	/**
	 * Get promoted shows. First check the cache. 
	 * @return
	 */
	public List<Show> getPromoShows() {

		List<Show> theReturn = SHOW_CACHE.get(PROMO_SHOW_LABLE);
		
		if (theReturn == null) {
			theReturn = Obj.begin().query(Show.class).filter("promoted", true).list();
			if (theReturn == null) {
				theReturn = new ArrayList<Show>(0);
			}
			SHOW_CACHE.put(PROMO_SHOW_LABLE, theReturn);
		}

		return theReturn;
	}	
	
	/**
	 * Return the count from the cache if exists
	 */
	public int getVisibleShowCount() {

		if (COUNT_CACHE.get(VISIBLE_SHOW_COUNT_LABLE) != null) {
			return COUNT_CACHE.get(VISIBLE_SHOW_COUNT_LABLE);
		} else {
			Objectify obj = Obj.begin();
			int visibleCount = obj.query(Show.class).filter("systemStatus in", ShowServiceImpl.visibleShowStati).count();
			COUNT_CACHE.put(VISIBLE_SHOW_COUNT_LABLE, visibleCount);
			return visibleCount;
		}
	}	
	
}
