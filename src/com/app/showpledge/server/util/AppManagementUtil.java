package com.app.showpledge.server.util;

import com.app.showpledge.server.persistence.dao.ShowDao;
import com.app.showpledge.server.persistence.dao.UserDao;
import com.app.showpledge.shared.entities.User;

public class AppManagementUtil {

	private static final String ADMIN_EMAIL = "mdowell@gmail.com";

	private static final UserDao userDao = new UserDao();
	private static final ShowDao showDao = new ShowDao();
	
	/**
	 * Checks to see if Admin user: mdowell@gmail.com has been created
	 * and if not, creates him.
	 * 
	 */
	public static void createManagementUsers() {

			User admin = userDao.getUser(ADMIN_EMAIL, "power55");
			
			if (admin == null) {
				admin = new User();
				admin.setAdmin(true);
				admin.setEmail(ADMIN_EMAIL);
				admin.setPassword("power55");
				admin.setFirstName("Matt");
				admin.setLastName("Dowell");
				admin.setLocked(false);
				admin.setScreenName("Admin");
				admin.setPublisher(true);
				admin.setVerified(true);
				
				userDao.put(admin);						
			}			
	}	
	
	public static void populateCache() {
		showDao.getPromoShows();
	}
}
