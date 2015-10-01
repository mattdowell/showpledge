package com.app.showpledge.client.util.stub;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface LoginServiceAsync {

	void login(String inUsername, String inPassword, AsyncCallback<Boolean> callback);

	void isUserLoggedIn(AsyncCallback<Boolean> callback);

}
