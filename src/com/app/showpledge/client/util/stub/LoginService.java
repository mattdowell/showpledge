package com.app.showpledge.client.util.stub;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("login")
public interface LoginService extends RemoteService {
	
	public boolean isUserLoggedIn();
	
	public boolean login(String inUsername, String inPassword);
	
}
