package com.app.showpledge.server.search;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.app.showpledge.server.persistence.Obj;
import com.app.showpledge.shared.entities.Show;
import com.googlecode.objectify.Query;

/**
 * http://googleappengine.blogspot.com/2010/04/making-your-app-searchable-using-self.html?utm_source=feedburner&utm_medium=feed&utm_campaign=Feed:+GoogleAppEngineBlog+(Google+App+Engine+Blog
 * 
 * @author mjdowell
 * 
 */
public class SearchEngine {

	public static final int MAXIMUM_NUMBER_OF_WORDS_TO_SEARCH = 5;
	
	/* Full Text SEarch Field Name */
	public static final String FTS_FIELD_NAME = "fts";

	
	/**
	 * 
	 * @param queryString
	 * @return
	 */
	public static List<Show> searchShows(String queryString) {

		Query<Show> query = Obj.begin().query(Show.class);

		Set<String> queryTokens = SearchJanitorUtils.getTokensForIndexingOrQuery(queryString, MAXIMUM_NUMBER_OF_WORDS_TO_SEARCH);

		for (String token : queryTokens) {
			query.filter(FTS_FIELD_NAME, token);
		}

		List<Show> theReturn = new ArrayList<Show>();
		for (Show s : query) {
			theReturn.add(s);
		}
		return theReturn;
	}
}