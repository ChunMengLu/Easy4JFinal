package net.dreamlu.easy.commons.utils;

import com.jfinal.kit.LogKit;

/**
 * 类工具集
 * @author L.cm
 */
public class ClassUtils {
	/**
	 * 实例化对象
	 * @param clazz 类
	 * @return 对象
	 */
	@SuppressWarnings("unchecked")
	public static <T> T newInstance(Class<?> clazz) {
		try {
			return (T) clazz.newInstance();
		} catch (InstantiationException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 实例化对象
	 * @param clazzStr 类名
	 * @return 对象
	 */
	public static <T> T newInstance(String clazzStr) {
		Class<?> clazz = ClassUtils.loadClass(clazzStr);
		return newInstance(clazz);
	}
	
	/**
	 * 获取当前线程
	 * @return 当前线程的class loader
	 */
	public static ClassLoader getContextClassLoader() {
		return Thread.currentThread().getContextClassLoader();
	}

	/**
	 * 获得class loader
	 * 若当前线程class loader不存在，取当前类的class loader
	 * @return 类加载器
	 */
	public static ClassLoader getClassLoader() {
		ClassLoader classLoader = getContextClassLoader();
		if(classLoader == null) {
			classLoader = ClassUtils.class.getClassLoader();
		}
		return classLoader;
	}

	/**
	 * 加载类
	 * @param className 类名
	 * @return Class
	 */
	public static Class<?> loadClass(String className) {
		Class<?> clazz = null;

		if (className == null) {
			return null;
		}
		
		try {
			return Class.forName(className);
		} catch (ClassNotFoundException e) {
			LogKit.error(className + " NotFound!");
		}

		ClassLoader ctxClassLoader = ClassUtils.getContextClassLoader();
		if (ctxClassLoader != null) {
			try {
				clazz = ctxClassLoader.loadClass(className);
			} catch (ClassNotFoundException e) {
				LogKit.error(className + " NotFound!");
			}
		}

		return clazz;
	}

}
