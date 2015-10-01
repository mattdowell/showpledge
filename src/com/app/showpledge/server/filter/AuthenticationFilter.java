package com.app.showpledge.server.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public class AuthenticationFilter implements Filter {

	private FilterConfig filterConfig = null;
	
	@Override
	public void destroy() {
	}

	@Override
	public void doFilter(ServletRequest inReq, ServletResponse inResp, FilterChain inChain) throws IOException, ServletException {
	}

	@Override
	public void init(FilterConfig inConfig) throws ServletException {
		filterConfig = inConfig;
	}

}
