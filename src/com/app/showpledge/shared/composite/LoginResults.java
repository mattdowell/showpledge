package com.app.showpledge.shared.composite;

import java.io.Serializable;

import com.app.showpledge.shared.entities.User;

/**
 * Composite class containing all the use cases for a login event.
 * 
 * @author mjdowell
 *
 */
public class LoginResults  implements Serializable {

	private static final long serialVersionUID = -1427949681414466416L;
	
	private User user;
	private boolean sucess = false;

	public static enum Status {
		SUCCESS, FAILED, NOT_VERIFIED, SUSPENDED;
	}

	private Status status;

	public LoginResults() {
	}
	
	/**
	 * Determines the system status (usability for this user) based upon
	 * the user that was passed in.
	 */
	private void determineStatus() {
		if (user == null) {
			status = Status.FAILED;
		} else {
			if (user.isLocked()) {
				status = Status.SUSPENDED;
			} else if (! user.isVerified()) {
				status = Status.NOT_VERIFIED;
			} else {
				// All good
				status = Status.SUCCESS;
				sucess = true;
			}
		}
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
		determineStatus();
	}

	public boolean isSucess() {
		return sucess;
	}

	public void setSucess(boolean sucess) {
		this.sucess = sucess;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

}
