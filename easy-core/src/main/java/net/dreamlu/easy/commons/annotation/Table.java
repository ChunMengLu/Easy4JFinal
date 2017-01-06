package net.dreamlu.easy.commons.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 表注解，备用
 * @author L.cm
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE )
@Documented
public @interface Table {
	/**
	 * 对应的表名
	 * @return {String}
	 */
	String value() default "";
	/**
	 * 表主键
	 * @return {String}
	 */
	String ids() default "id";
	/**
	 * 数据源名称
	 * @return {String}
	 */
	String dbName() default "main";
}
