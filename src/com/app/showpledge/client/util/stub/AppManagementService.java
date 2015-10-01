package com.app.showpledge.client.util.stub;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("appManagement")
public interface AppManagementService extends RemoteService {
	
	public void createManagementUsers();

}
