package net.dreamlu.easy.commons.interceptors;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;

import net.dreamlu.easy.commons.plugin.ioc.InjectUtils;

/**
 * 控制器上 {@Inject} 注解处理的拦截器
 * @author L.cm
 *
 */
public class InjectInterceptor implements Interceptor {

	@Override
	public void intercept(Invocation inv) {
		Object target = inv.getTarget();
		InjectUtils.inject(target);
		inv.invoke();
	}

}
