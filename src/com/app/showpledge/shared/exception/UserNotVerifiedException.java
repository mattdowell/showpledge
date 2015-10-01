package com.app.showpledge.shared.exception;

public class UserNotVerifiedException extends IllegalStateException {

	public UserNotVerifiedException() {
		super();
	}

	public UserNotVerifiedException(String message, Throwable cause) {
		super(message, cause);
	}

	public UserNotVerifiedException(String s) {
		super(s);
	}

	public UserNotVerifiedException(Throwable cause) {
		super(cause);
	}

}
