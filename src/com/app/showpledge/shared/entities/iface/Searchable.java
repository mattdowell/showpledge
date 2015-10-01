package com.app.showpledge.shared.entities.iface;

import java.util.Set;

/**
 * All searchable entities should implement this interface
 * @author mjdowell
 *
 */
public interface Searchable {
	
	String getSearchableContent();
	
	void setFts(Set<String> inFts);
	
	Set<String> getFts();

}
