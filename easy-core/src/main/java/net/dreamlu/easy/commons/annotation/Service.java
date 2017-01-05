package net.dreamlu.easy.commons.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 服务层注解
 * @author L.cm
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
@Inherited
public @interface Service {
	String value();

	/**
	 * 是否开启事务，选填，默认false
	 * @return
	 */
	boolean tx() default false;
}