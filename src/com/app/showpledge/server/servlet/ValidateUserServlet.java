package com.app.showpledge.server.servlet;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.app.showpledge.server.persistence.Obj;
import com.app.showpledge.shared.entities.User;
import com.googlecode.objectify.Objectify;

/**
 * This servlet's responsibilities are to take two parameters from the URL and perform a validation.
 * 
 * UID <-- This is the system User ID CD <-- This is the system generated code that is also saved on this Users record.
 * 
 * 1) Look up the user using the UID 2) If the user is already validated a) Redirect to a page that show the user already validated 3) If
 * the user is not already validated a) Compare the given code with the code saved on the user record b) If they match 1) Validate the user
 * c) If they don't match 1) Do not validate the user 2) Increment the failed validation count
 * 
 * 
 * @author mjdowell
 * 
 */
public class ValidateUserServlet extends HttpServlet {

	private static final long serialVersionUID = 505291776201819156L;

	private static final String UID_ATTRIB = "uid";
	private static final String CD_ATTRIB = "cd";

	private static final Logger Log = Logger.getLogger(ValidateUserServlet.class.getName());


	public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		doGet(req, res);
	}
	

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		Log.info("Starting validation..");

		Object uidObj = req.getParameter(UID_ATTRIB);
		Object cdObj = req.getParameter(CD_ATTRIB);

		try {

			Log.info("Raw Parameters are, uid: " + uidObj + " cd: " + cdObj);

			Long uid = new Long(uidObj.toString());
			String cd = String.valueOf(cdObj);

			User u = getUser(uid);

			Log.info("Got user id and code: " + uidObj + " Code: " + cdObj);

			if (u == null) {
				Log.info("No user found for uid: " + uid);
				getServletContext().getRequestDispatcher("/?err="+encode("There was no user found for that ID. Please contact support.")).forward(req, resp);
			} else if (cd != null && cd.equals(u.getValidationCode())) {

				u.setVerified(true);
				saveUser(u);
				
				// Now redirect and show a positive message
				getServletContext().getRequestDispatcher("/?msg="+encode("Your account is now validated. Welcome!")).forward(req, resp);

			} else {

				Log.severe("The validation codes did not match: " + cd + " user code: " + u.getValidationCode());
				u.incrementFailedValidations();
				saveUser(u);

				// Show failed validation screen
				getServletContext().getRequestDispatcher("/?err="+encode("There validation code was not valid")).forward(req, resp);
			}

		} catch (Exception e) {
			Log.severe("Exception: " + e.toString());

			// Redirect and show a negative message
			getServletContext().getRequestDispatcher("/?err="+encode("There was an unknown error. Please contact support.")).forward(req, resp);
		}
	}
	
	private String encode(String in) {
		try {
			return URLEncoder.encode(in, "ISO-8859-1");
		} catch (UnsupportedEncodingException e) {
			Log.severe(e.toString());
		}
		return in;
	}
	private User getUser(Long inId) {
		Objectify obj = Obj.begin();
		return obj.query(User.class).filter("id", inId).get();		
	}
	
	private void saveUser(User in) {
		Objectify obj = Obj.begin();
		obj.put(in);
	}
}
