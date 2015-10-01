package com.app.showpledge.server;

import com.app.showpledge.client.util.stub.LoginService;
import com.app.showpledge.server.persistence.Obj;
import com.app.showpledge.shared.entities.User;
import com.googlecode.objectify.Objectify;

public class LoginServiceImpl extends AbstractRemoteServlet implements LoginService {

	private static final long serialVersionUID = 324190197874080573L;

	@Override
	public boolean login(String inUsername, String inPassword) {
		Objectify obj = Obj.begin();
		User u = obj.query(User.class).filter("email", inUsername).filter("password", inPassword).get();
		if (u == null) {
			return false;
		} else {
			setCurrentUser(u);
			return true;
		}
	}

	@Override
	public boolean isUserLoggedIn() {
		return super.isUserLoggedIn(getThreadLocalRequest());
	}
}
