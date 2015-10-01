package com.app.showpledge.server.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import com.app.showpledge.server.util.AppManagementUtil;

/**
 * 
 * @author mjdowell
 *
 */
public class PrePopulateDataServlet extends HttpServlet {
	private static final long serialVersionUID = 6845219668971414522L;

	@Override
	  public void init() throws ServletException {
	    AppManagementUtil.createManagementUsers();
	    AppManagementUtil.populateCache();
	  }

}
