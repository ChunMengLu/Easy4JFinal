package net.dreamlu.easy.commons.plugin.ioc;

import java.util.Collection;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.jfinal.aop.Enhancer;
import com.jfinal.config.Routes;
import com.jfinal.kit.StrKit;
import com.jfinal.log.Log;
import com.jfinal.plugin.IPlugin;

import net.dreamlu.easy.commons.annotation.Component;
import net.dreamlu.easy.commons.annotation.Controller;
import net.dreamlu.easy.commons.annotation.Service;
import net.dreamlu.easy.commons.searcher.ClassSearcher;

/**
 * Ioc容器用来装载Bean
 * @author L.cm
 *
 */
@SuppressWarnings("unchecked")
public class IocPlugin implements IPlugin {
	private static Log log = Log.getLog(IocPlugin.class);
	
	private static final ConcurrentHashMap<String, Object> iocBeanMap = new ConcurrentHashMap<String, Object>();
	// 待扫描的包
	private final Routes routes;
	private final String[] pkgs;
	
	public IocPlugin(Routes routes, String[] pkgs) {
		this.routes = routes;
		this.pkgs = StrKit.notBlank(pkgs) ? pkgs : null;
	}

	@Override
	public boolean start() {
		// 扫描控制器、服务和组件
		Set<Class<?>> clzzSet = ClassSearcher.getClasses(pkgs, 
				Controller.class, Service.class, Component.class); // , Table.class
		// 装载bean
		for (Class<?> clazz : clzzSet) {
			// 如果是控制器
			if (com.jfinal.core.Controller.class.isAssignableFrom(clazz)) {
				initController(clazz);
				continue;
			}
			// table处理，待开发
			
			String beanName = clazz.getName();
			// 增强bean，使bean具备处理@Befor
			Object enhanceBean = Enhancer.enhance(clazz);
			if (iocBeanMap.containsKey(beanName)) {
				log.warn("bean:" + beanName + " reloading!");
			}
			iocBeanMap.put(beanName, enhanceBean);
		}
		IocKit.init(iocBeanMap);
		// 处理Bean的相互@Inject
		Collection<Object> beanColl = iocBeanMap.values();
		for (Object object : beanColl) {
			Class<?> superclass = object.getClass().getSuperclass();
			InjectUtils.inject(superclass, object);
		}
		return true;
	}

	@Override
	public boolean stop() {
		iocBeanMap.clear();
		return true;
	}

	/**
	 * 注入控制器路由
	 * @param clazz 控制器类
	 */
	private void initController(Class<?> clazz) {
		Controller controller = clazz.getAnnotation(Controller.class);
		String viewPath = controller.viewPath();
		if (StrKit.isBlank(viewPath)) {
			viewPath = controller.value();
		}
		routes.add(controller.value(), (Class<? extends com.jfinal.core.Controller>) clazz, viewPath);
	}

}
