package net.dreamlu.example.common;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;

import net.dreamlu.easy.commons.annotation.Component;

@Component
public class GlobalInterceptor implements Interceptor {

	@Override
	public void intercept(Invocation inv) {
		System.out.println("全局拦截器 Before invoking " + inv.getMethodName());
		inv.invoke();
		System.out.println("全局拦截器 After invoking " + inv.getMethodName());
	}

}
