package net.dreamlu.example.blog;


import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;

/**
 * BlogInterceptor
 * 此拦截器仅做为示例展示，在本 demo 中并不需要
 */
public class BlogInterceptor implements Interceptor {
	
	public void intercept(Invocation inv) {
		System.out.println("Before invoking " + inv.getMethodName());
		inv.invoke();
		System.out.println("After invoking " + inv.getMethodName());
	}
}
