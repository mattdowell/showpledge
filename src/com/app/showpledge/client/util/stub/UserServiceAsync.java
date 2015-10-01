package com.app.showpledge.client.util.stub;

import java.util.List;

import com.app.showpledge.shared.composite.Feedback;
import com.app.showpledge.shared.composite.LoginResults;
import com.app.showpledge.shared.entities.User;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface UserServiceAsync {

	void login(String inUsername, String inPassword, AsyncCallback<LoginResults> callback);

	void createUser(User inUser, int inCaptcha, AsyncCallback<Long> callback);

	void getAllUsers(AsyncCallback<List<User>> callback);

	void isUserLoggedIn(AsyncCallback<Boolean> callback);

	void doesEmailAlreadyExist(String inEmail, AsyncCallback<Boolean> callback);

	void logout(AsyncCallback<Void> callback);

	void getCaptchaNumber(int inCeiling, AsyncCallback<Integer> callback);

	void update(User inUser, AsyncCallback<Void> callback);

	void getLoggedInUser(AsyncCallback<User> callback);

	void suspendUser(User u, AsyncCallback<Void> callback);

	void getUsers(int inOffset, int inLimit, AsyncCallback<List<User>> callback);

	void getTotalUserCount(AsyncCallback<Integer> callback);

	void submitFeedback(Feedback inFeed, AsyncCallback<Void> callback);

	void userForgotPassowrd(String inEmail, AsyncCallback<Void> callback);

	void delete(User inUser, AsyncCallback<Void> callback);

	void validateUser(String userId, String validationCode, AsyncCallback<Void> callback);

	void activateUser(User inUser, AsyncCallback<Void> buildVoidThenRefreshCallback);

}
