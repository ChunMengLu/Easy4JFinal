package net.dreamlu.easy.commons.annotation;

/**
 * 表
 * @author L.cm
 */
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
}
