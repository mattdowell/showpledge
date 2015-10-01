package com.app.showpledge.shared.exception;

/**
 * Throw this if the user is trying to perform an action that requires them to
 * be logged in, but they are not currently logged in.
 * 
 * @author mjdowell
 *
 */
public class UserNotLoggedInException extends IllegalStateException {

	public UserNotLoggedInException() {
		super();
	}

	public UserNotLoggedInException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public UserNotLoggedInException(String arg0) {
		super(arg0);
	}

	public UserNotLoggedInException(Throwable arg0) {
		super(arg0);
	}
	

}
