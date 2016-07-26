package net.dreamlu.easy.commons.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class ServletContextHolderKit {
	private static final ThreadLocal<ServletContextHolder> holder = new ThreadLocal<ServletContextHolder>();
	
	static void set(ServletContextHolder value) {
		holder.set(value);
	}
	
	static void remove() {
		holder.remove();
	}
	
	/**
	 * 获取ServletContextHolder
	 * @return ServletContextHolder
	 */
	public static ServletContextHolder get() {
		return holder.get();
	}
	
	public static HttpServletRequest getRequest() {
		ServletContextHolder servletContextHolder = ServletContextHolderKit.get();
		return null == servletContextHolder ? null : servletContextHolder.getRequest();
	}
	
	public static HttpSession getSession() {
		return ServletContextHolderKit.getSession(true);
	}
	
	public static HttpSession getSession(boolean create) {
		HttpServletRequest request = ServletContextHolderKit.getRequest();
		return null == request ? null : request.getSession(create);
	}
}
