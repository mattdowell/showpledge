package com.app.showpledge.server;

import java.text.MessageFormat;
import java.util.List;

import com.app.showpledge.client.util.stub.UserService;
import com.app.showpledge.server.persistence.Obj;
import com.app.showpledge.server.persistence.dao.UserDao;
import com.app.showpledge.server.util.CodeGenerationUtil;
import com.app.showpledge.server.util.EmailProperties;
import com.app.showpledge.server.util.EmailUtil;
import com.app.showpledge.shared.composite.Feedback;
import com.app.showpledge.shared.composite.LoginResults;
import com.app.showpledge.shared.entities.User;
import com.google.gwt.gen2.logging.shared.Log;
import com.googlecode.objectify.Objectify;

/**
 * Implementation of the UserService
 * 
 */
public class UserServiceImpl extends AbstractRemoteServlet implements UserService {

	private static final long serialVersionUID = 324190197874080573L;

	private static final UserDao userDao = new UserDao();

	public static String APP_ADMIN_EMAIL = "admin@showpledge.com";

	@Override
	public LoginResults login(String inUsername, String inPassword) {
		LoginResults res = new LoginResults();
		User u = userDao.getUser(inUsername, inPassword);
		res.setUser(u);

		if (res.isSucess()) {
			setCurrentUser(u);
		}
		return res;
	}

	@Override
	public void logout() {
		setCurrentUser(null);
		putShowInSession(null);
	}

	@Override
	public void update(User inUser) {
		Objectify obj = Obj.begin();
		obj.put(inUser);
	}

	/**
	 * Create a new user
	 */
	@Override
	public long createUser(User inUser, int inCaptchaChoice) {

		if (inCaptchaChoice != getCaptchaNum()) {
			return UserService.UserError.BAD_CAPTCHA.getNumber();
		}
		if (!doesEmailAlreadyExist(inUser.getEmail())) {

			String code = CodeGenerationUtil.generate();

			// Add the validation code
			inUser.setValidationCode(code);

			// Save the user
			Objectify obj = Obj.begin();
			obj.put(inUser);

			// Send a confirmation email
			try {
				
				// Format the activation email
				String emailBody = EmailProperties.getInstance().getProperty("activation");
				MessageFormat form = new MessageFormat(emailBody);
				Object[] args = {inUser.getFirstName(), inUser.getId().toString(), code  };
				String formattedBody = form.format(args);
				
				EmailUtil.send(inUser.getEmail(), inUser.getDisplayName(), "Welcome to Showpledge.com", formattedBody);
				
			} catch (Exception e) {
				e.printStackTrace();
				Log.severe("Could not send an email. Error: " + e.toString() + " to this user: " + inUser);
				
				// Delete the user object
				obj.delete(inUser);
				
				return UserService.UserError.EMAIL_FAIL.getNumber();
			}

			// Log in the user
			return inUser.getId();

		} else {

			// Email already exists
			return UserService.UserError.EMAIL_EXISTS.getNumber();
		}
	}

	public User getLoggedInUser() {
		return getCurrentUser();
	}

	@Override
	public boolean isUserLoggedIn() {
		return super.isUserLoggedIn(getThreadLocalRequest());
	}

	@Override
	public boolean doesEmailAlreadyExist(String inEmail) {
		return userDao.doesEmailAlreadyExist(inEmail);
	}

	@Override
	public List<User> getAllUsers() {
		return userDao.getAllUsers();
	}

	@Override
	public void suspendUser(User u) {
		userDao.suspendUser(u);
	}

	@Override
	public void activateUser(User u) {
		userDao.activateUser(u);
	}

	@Override
	public int getCaptchaNumber(int in) {
		int randomInt = userDao.getCaptchaNumber(in);
		setCaptchaNum(randomInt);
		return randomInt;
	}

	@Override
	public List<User> getUsers(int inOffset, int inLimit) {
		return userDao.getUsers(inOffset, inLimit);
	}

	@Override
	public int getTotalUserCount() {
		return userDao.getTotalUserCount();
	}

	@Override
	public void submitFeedback(Feedback inFeed) {
		User user = super.getCurrentUser();

		try {
			EmailUtil.send(APP_ADMIN_EMAIL, "Matt Dowell", user.getEmail(), user.getDisplayName(), "ShowPledge Feedback", inFeed.getText());
		} catch (Exception e) {
			e.printStackTrace();
			Log.severe("Could not send FEEDBACK: " + e.getLocalizedMessage());
		}
	}

	/**
	 * TODO: Move properties reading to utils class
	 */
	@Override
	public void userForgotPassowrd(String inEmail) {

		try {
			User user = userDao.getUserByEmail(inEmail);

			String emailBody = EmailProperties.getInstance().getProperty("forgotpassword");
			MessageFormat form = new MessageFormat(emailBody);

			Object[] args = { user.getFirstName(), user.getPassword() };
			String formattedBody = form.format(args);

			EmailUtil.send(inEmail, user.getDisplayName(), APP_ADMIN_EMAIL, "Showpledge.com", "Forgotten Password", formattedBody);
		} catch (Exception e) {
			e.printStackTrace();
			Log.severe("Error sending forgot password email: " + e.getLocalizedMessage());
		} 

	}

	/**
	 * Only allow if current user is an Admin
	 */
	@Override
	public void delete(User inUser) {
		
		// Todo: Do we need to look for all "shows" where this user is associated with and change them?
		
		User u = getCurrentUser();
		if (u.isAdmin()) {
			userDao.delete(inUser);
		}
	}

	@Override
	public void validateUser(String userIdCd, String validationCd) {
		try {

			Log.info("Raw Parameters are, uid: " + userIdCd + " cd: " + validationCd);

			Long uid = new Long(userIdCd.toString());
			String cd = String.valueOf(validationCd);

			User u = userDao.getById(uid);

			Log.info("Got user id and code: " + userIdCd + " Code: " + validationCd);

			if (u == null) {
				Log.info("No user found for uid: " + uid);
				throw new RuntimeException("There was no user found for that ID. Please contact support.");
			} else if (cd != null && cd.equals(u.getValidationCode())) {

				u.setVerified(true);
				userDao.put(u);
				
			} else {

				Log.severe("The validation codes did not match: " + cd + " user code: " + u.getValidationCode());
				u.incrementFailedValidations();
				userDao.put(u);

				// Show failed validation screen
				throw new RuntimeException("There validation code was not valid");
			}

		} catch (Exception e) {
			Log.severe("Exception: " + e.toString());

			// Redirect and show a negative message
			throw new RuntimeException("There was an unknown error. Please contact support.");
		}		
	}

}
