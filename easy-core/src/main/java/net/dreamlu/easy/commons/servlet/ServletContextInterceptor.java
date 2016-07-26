package net.dreamlu.easy.commons.servlet;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.core.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 仿照spring mvc，将request、response暂存到ThreadLocal
 * @author L.cm
 */
public class ServletContextInterceptor implements Interceptor {
	
	@Override
	public void intercept(Invocation inv) {
		Controller controller = inv.getController();
		HttpServletRequest request = controller.getRequest();
		HttpServletResponse response = controller.getResponse();
		try {
			ServletContextHolderKit.set(new ServletContextHolder(request, response));
			inv.invoke();
		} finally {
			ServletContextHolderKit.remove();
		}
	}

}
