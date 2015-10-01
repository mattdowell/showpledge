package com.app.showpledge.shared.exception;

/**
 * Thrown when a user account is not found when a user tries to login
 * with their email and password
 * 
 * @author mjdowell
 *
 */
public class InvalidLoginException extends RuntimeException {

	public InvalidLoginException() {
	}

	public InvalidLoginException(String arg0) {
		super(arg0);
	}

	public InvalidLoginException(Throwable arg0) {
		super(arg0);
	}

	public InvalidLoginException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

}
