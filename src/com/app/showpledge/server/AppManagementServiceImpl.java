package com.app.showpledge.server;

import com.app.showpledge.client.util.stub.AppManagementService;
import com.app.showpledge.server.util.AppManagementUtil;

public class AppManagementServiceImpl extends AbstractRemoteServlet implements AppManagementService {
	
	
	/**
	 * Checks to see if Admin user: mdowell@gmail.com has been created
	 * and if not, creates him.
	 * 
	 */
	@Override
	public void createManagementUsers() {
		AppManagementUtil.createManagementUsers();
	}

}
