package com.app.showpledge.server;

import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import com.app.showpledge.shared.entities.Show;
import com.app.showpledge.shared.entities.User;
import com.app.showpledge.shared.exception.UserNotLoggedInException;
import com.app.showpledge.shared.exception.UserNotVerifiedException;
import com.app.showpledge.shared.exception.UserSuspendedException;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * Parent Servlet.
 * 
 * @author Matt
 * 
 */
public abstract class AbstractRemoteServlet extends RemoteServiceServlet {

	private static final long serialVersionUID = 2377307356029136L;

	public static final String USER_SESSION_KEY = "user_session_key";
	public static final String USERS_CURRENT_SHOW_FOR_EDIT = "user_nom_show_key";
	public static final String USERS_CAPTCHA_NUM = "user_num_captcha";
	
	static final Logger log = Logger.getLogger(AbstractRemoteServlet.class.getName());

	/**
	 * Checks to see if there is a User object in this threads Session
	 * 
	 * @param inRequest
	 * @return
	 */
	boolean isUserLoggedIn(HttpServletRequest inRequest) {
		User sessionUser = getCurrentUser();
		if (sessionUser == null) {
			return false;
		}
		return true;
	}

	User getCurrentUser() {
		return (User) getThreadLocalRequest().getSession().getAttribute(USER_SESSION_KEY);
	}

	void performLoginCheck() {
		User u = getCurrentUser();

		if (u == null) {
			throw new UserNotLoggedInException("You are not logged in.");
		}

		if (u.isLocked()) {
			throw new UserSuspendedException("This account is currently locked.");
		}

		if (!u.isVerified()) {
			throw new UserNotVerifiedException("This account has not been verified yet.");
		}

	}

	void setCurrentUser(User inUser) {
		getThreadLocalRequest().getSession().setAttribute(USER_SESSION_KEY, inUser);
	}

	public void putShowInSession(Show inShow) {
		getThreadLocalRequest().getSession().setAttribute(USERS_CURRENT_SHOW_FOR_EDIT, inShow);
	}

	public static Show getShowInSession(HttpServletRequest req) {
		return (Show) req.getSession().getAttribute(AbstractRemoteServlet.USERS_CURRENT_SHOW_FOR_EDIT);
	}

	public Show getShowInSession() {
		return getShowInSession(getThreadLocalRequest());
	}

	Show getLastNominatedShow() {
		return (Show) getThreadLocalRequest().getSession().getAttribute(USERS_CURRENT_SHOW_FOR_EDIT);
	}

	int getCaptchaNum() {
		return (Integer) getThreadLocalRequest().getSession().getAttribute(USERS_CAPTCHA_NUM);
	}

	void setCaptchaNum(int in) {
		getThreadLocalRequest().getSession().setAttribute(USERS_CAPTCHA_NUM, in);
	}
}
