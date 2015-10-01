package com.app.showpledge.client.util.stub;

import java.util.List;

import com.app.showpledge.shared.composite.Feedback;
import com.app.showpledge.shared.composite.LoginResults;
import com.app.showpledge.shared.entities.User;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("user")
public interface UserService extends RemoteService {
	
	/**
	 * 
	 * @author mjdowell
	 *
	 */
	public enum UserError {
		
		EMAIL_EXISTS(-300, "Sorry that email is already in use"),
		EMAIL_FAIL(-100, "Sorry could not send email to that address"),
		BAD_CAPTCHA(-2, "The slider does not match the captcha number. Bad Robot!");
		
		int number;
		String text;
		
		UserError(int inNum, String inTxt) {
			number = inNum;
			text = inTxt;
		}

		public int getNumber() {
			return number;
		}

		public String getText() {
			return text;
		}
		
		public static UserError getErrorFromNumber(int inNum) {
			for (UserError ue : UserError.values()) {
				if (ue.getNumber() == inNum) {
					return ue;
				}
			}
			return null;
		}
	}

	public boolean isUserLoggedIn();

	public LoginResults login(String inUsername, String inPassword);

	public User getLoggedInUser();

	public long createUser(User inUser, int inCaptchaChoice);

	public void update(User inUser);
	
	public void delete(User inUser);

	public boolean doesEmailAlreadyExist(String inEmail);

	public List<User> getAllUsers();

	public void suspendUser(User u);
	
	public void userForgotPassowrd(String inEmail);

	public void logout();

	public int getCaptchaNumber(int in);
	
	public List<User> getUsers(int inOffset, int inLimit);
	
	public int getTotalUserCount();
	
	public void submitFeedback(Feedback inFeed);
	
	public void validateUser(String userId, String validationCode);

	void activateUser(User inUser);

}
