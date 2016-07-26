package net.dreamlu.easy.interceptor;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.core.Controller;
import com.jfinal.kit.LogKit;
import com.jfinal.kit.StrKit;
import net.dreamlu.easy.commons.utils.URLUtils;
import net.dreamlu.easy.commons.utils.WebUtils;
import net.dreamlu.easy.model.User;

import javax.servlet.http.HttpServletRequest;

/**
 * 登陆拦截器
 */
public class LoginInterceptor implements Interceptor {

	@Override
	public void intercept(Invocation inv) {
		Controller controller = inv.getController();
		User user = WebUtils.getCurrentUser(controller);

		// 如果用户是已经登陆的跳出
		if (null != user) {
			inv.invoke();
			return;
		}

		HttpServletRequest request = controller.getRequest();
		String queryString = request.getQueryString();
		// 被拦截前的请求URL
		String redirectUrl = request.getRequestURI();
		if (StrKit.notBlank(queryString)) {
			redirectUrl = redirectUrl.concat("?").concat(queryString);
		}
		redirectUrl = URLUtils.encode(redirectUrl);

		String targetPath = "/login?from=" + redirectUrl;
		// 日志记录
		if (LogKit.isDebugEnabled()) {
			LogKit.debug("LoginInterceptor[redirectUrl]:\t" + targetPath);
		}
		controller.redirect(targetPath);
	}

}
