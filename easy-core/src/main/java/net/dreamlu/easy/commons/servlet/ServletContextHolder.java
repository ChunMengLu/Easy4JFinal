package net.dreamlu.easy.commons.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ServletContextHolder {
	private final HttpServletRequest request;
	private HttpServletResponse response;
	
	/**
	 * Create a new ServletContextHolder instance for the given request.
	 * @param request current HTTP request
	 */
	public ServletContextHolder(HttpServletRequest request) {
		this.request = request;
	}
	
	/**
	 * Create a new ServletContextHolder instance for the given request.
	 * @param request current HTTP request
	 * @param response current HTTP response (for optional exposure)
	 */
	public ServletContextHolder(HttpServletRequest request, HttpServletResponse response) {
		this(request);
		this.response = response;
	}
	
	public HttpServletResponse getResponse() {
		return response;
	}
	
	public void setResponse(HttpServletResponse response) {
		this.response = response;
	}
	
	public HttpServletRequest getRequest() {
		return request;
	}
}
