package com.app.showpledge.shared.exception;

public class UserSuspendedException extends IllegalStateException {

	public UserSuspendedException() {
		super();
	}

	public UserSuspendedException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public UserSuspendedException(String arg0) {
		super(arg0);
	}

	public UserSuspendedException(Throwable arg0) {
		super(arg0);
	}

}
