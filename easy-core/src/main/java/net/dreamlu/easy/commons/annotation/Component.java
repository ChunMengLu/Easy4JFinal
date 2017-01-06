package net.dreamlu.easy.commons.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自定义的bean加载，可用于组件
 * @author L.cm
 *
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Component {
	/**
	 * 用于Handler和Interceptor的扫描
	 * The order value. Default is {@link Integer#MAX_VALUE}.
	 * @return order
	 */
	int order() default Integer.MAX_VALUE;
}