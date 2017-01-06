package net.dreamlu.easy.commons.plugin.ioc;

import java.util.concurrent.ConcurrentMap;

/**
 * Ioc工具类
 * @author L.cm
 *
 */
@SuppressWarnings("unchecked")
public class IocKit {
	private static ConcurrentMap<String, Object> beanMap;
	
	static void init(ConcurrentMap<String, Object> beanMap) {
		IocKit.beanMap = beanMap;
	}
	
	static ConcurrentMap<String, Object> getBeanMap() {
		return beanMap;
	}
	
	public static <T> T getBean(Class<?> clazz) {
		String beanName = clazz.getName();
		return (T) beanMap.get(beanName);
	}

}
