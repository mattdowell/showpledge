package com.app.showpledge.shared.composite;

import java.io.Serializable;
import java.util.List;

import com.app.showpledge.shared.entities.Show;

/**
 * This class is a composite of results needed for the UI to accurately
 * display the results in a "performant" manner.
 * 
 *  The results might be a sub-section of the total results
 *  
 *  The totalCount will always hold the total # of results found in the store
 * 
 */
public class ShowQueryResults implements Serializable {
	
	private static final long serialVersionUID = 5112580658780026525L;
	
	private List<Show> shows;
	private int totalCount;
	
	
	public ShowQueryResults(List<Show> shows, int totalCount) {
		super();
		this.shows = shows;
		this.totalCount = totalCount;
	}
	
	public ShowQueryResults() {
		super();
	}



	public List<Show> getShows() {
		return shows;
	}
	
	public int getTotalCount() {
		return totalCount;
	}	

}
