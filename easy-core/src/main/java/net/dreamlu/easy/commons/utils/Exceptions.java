package net.dreamlu.easy.commons.utils;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * 异常处理工具类，让异常不再杂乱无章
 * @author L.cm
 */
public abstract class Exceptions {
	/**
	 * 将CheckedException转换为UncheckedException.
	 */
	public static RuntimeException unchecked(Throwable e) {
		if (e instanceof RuntimeException) {
			return (RuntimeException) e;
		} else {
			return new RuntimeException(e);
		}
	}
	
	/**
	 * 将ErrorStack转化为String.
	 */
	public static String getStackTraceAsString(Throwable ex) {
		StringWriter stringWriter = new StringWriter();
		ex.printStackTrace(new PrintWriter(stringWriter));
		return stringWriter.toString();
	}
}
