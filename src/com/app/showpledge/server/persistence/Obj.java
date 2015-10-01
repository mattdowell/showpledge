package com.app.showpledge.server.persistence;

import com.app.showpledge.shared.entities.Pledge;
import com.app.showpledge.shared.entities.Show;
import com.app.showpledge.shared.entities.User;
import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyOpts;
import com.googlecode.objectify.ObjectifyService;

/**
 * Simple singleton wrapper class for persistence layer
 * @author mjdowell
 *
 */
public class Obj {

	// List of all entities
	static {
		ObjectifyService.register(User.class);
		ObjectifyService.register(Show.class);
		ObjectifyService.register(Pledge.class);
	}

	/**
	 * http://code.google.com/p/objectify-appengine/wiki/IntroductionToObjectify#Caching
	 * 
	 * @return
	 */
	public static Objectify begin() {
		ObjectifyOpts opts = new ObjectifyOpts().setSessionCache(true);
		return ObjectifyService.begin(opts);
	}

}
