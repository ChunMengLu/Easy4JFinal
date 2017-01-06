package net.dreamlu.easy.commons.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 控制器注解
 * @author L.cm
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE )
@Documented
@Inherited
public @interface Controller {
	String value() default "/";

	String viewPath() default "";
}