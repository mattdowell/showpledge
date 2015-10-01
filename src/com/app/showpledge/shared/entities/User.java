package com.app.showpledge.shared.entities;

import java.util.Date;

import javax.persistence.Id;

import com.app.showpledge.shared.entities.iface.EntityIface;
import com.google.gwt.view.client.ProvidesKey;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Indexed;
import com.googlecode.objectify.annotation.Unindexed;

@Entity
@Unindexed
public class User implements EntityIface {

	private static final long serialVersionUID = 34457346761L;

	@Id
	private Long id;

	private Date createDate = null;

	@Indexed
	private String email;

	@Indexed
	private String password;

	private String firstName = null;

	private String lastName = null;

	private String screenName = null;

	@Indexed
	private boolean verified = false;

	// Should only be ONE or TWO admins
	private boolean admin = false;

	// Many publishers on the app?
	@Indexed
	private boolean publisher = false;

	// Locked or suspended
	private boolean locked = false;

	// Used to validate new users
	@Indexed
	private String validationCode;

	// How many failed validations
	private int failedValidations = 0;

	public User() {
		super();
	}

	public User(String email, String password) {
		super();
		this.email = email;
		this.password = password;
		this.createDate = new Date(System.currentTimeMillis());
	}

	/**
	 * This is used on client side data tables
	 */
	public static final ProvidesKey<User> KEY_PROVIDER = new ProvidesKey<User>() {
		public Object getKey(User item) {
			return item == null ? null : item.getId();
		}
	};

	@Override
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getScreenName() {
		return screenName;
	}

	public void setScreenName(String screenName) {
		this.screenName = screenName;
	}

	public boolean isVerified() {
		return verified;
	}

	public void setVerified(boolean verified) {
		this.verified = verified;
	}

	public boolean isAdmin() {
		return admin;
	}

	public void setAdmin(boolean admin) {
		this.admin = admin;
	}

	public boolean isPublisher() {
		return publisher;
	}

	public void setPublisher(boolean publisher) {
		this.publisher = publisher;
	}

	public int getFailedValidations() {
		return failedValidations;
	}

	public void incrementFailedValidations() {
		this.failedValidations++;
	}

	public String getValidationCode() {
		return validationCode;
	}

	public void setValidationCode(String validationCode) {
		this.validationCode = validationCode;
	}

	public boolean isLocked() {
		return locked;
	}

	public void setLocked(boolean locked) {
		this.locked = locked;
	}

	public String getDisplayName() {
		if (screenName != null) {
			return screenName;
		} else {
			return firstName + " " + lastName;
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + ((password == null) ? 0 : password.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (password == null) {
			if (other.password != null)
				return false;
		} else if (!password.equals(other.password))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("Email:").append(email).append("\n");
		sb.append("Pwd:").append(password).append("\n");
		sb.append("First:").append(firstName).append("\n");
		sb.append("Last:").append(lastName).append("\n");
		sb.append("Screen:").append(screenName).append("\n");
		sb.append("Verified:").append(verified).append("\n");
		return sb.toString();
	}

}
