package net.dreamlu.easy.commons.plugin.event;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.jfinal.log.Log;
import com.jfinal.plugin.IPlugin;

import net.dreamlu.easy.commons.plugin.event.core.ApplicationListener;
import net.dreamlu.easy.commons.plugin.event.core.Listener;
import net.dreamlu.easy.commons.searcher.ClassSearcher;
import net.dreamlu.easy.commons.utils.ClassUtils;
import net.dreamlu.easy.commons.utils.LinkedMultiMap;

/**
 * 模拟spring的消息机制插件
 * @author L.cm
 * email: 596392912@qq.com
 * site:http://www.dreamlu.net
 * date 2015年4月26日下午10:25:04
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class EventPlugin implements IPlugin {
    private static Log log = Log.getLog(EventPlugin.class);

    // 线程池
    private static ExecutorService pool = null;
    // 重复key的map，使用监听的type，取出所有的监听器
    private static LinkedMultiMap<EventType, ListenerHelper> map = null;

    // 默认扫描所有的包
    private final String[] scanPackage;

    /**
     * 构造EventPlugin
     * @param scanPackage 扫描的包名
     */
    public EventPlugin() {
        this.scanPackage = new String[]{""};
    }
    
    /**
     * 构造EventPlugin
     * @param scanPackage 扫描的包名
     */
    public EventPlugin(String... scanPackage) {
        this.scanPackage = scanPackage;
    }

    /**
     * 异步，默认创建3个线程
     * @param count 线程池的容量，不传或小于1时默认为3
     * @return EventPlugin
     */
    public EventPlugin threads(int count) {
        int threadCount = count  < 1 ? 3 : count;
        threadPool(Executors.newFixedThreadPool(threadCount));
        return this;
    }

    /**
     * 自定义线程池
     * @param pool 线程池
     * @return
     */
    public EventPlugin threadPool(ExecutorService pool) {
        EventPlugin.pool = pool;
        return this;
    }

    @Override
    public boolean start() {
        create();
        EventKit.init(map, pool);
        return true;
    }

    /**
     * 构造
     */
    private void create() {
        if (null != map) {
            return;
        }
        // 扫描注解 {@code Listener}
        Set<Class<?>> clazzSet = ClassSearcher.getClasses(scanPackage, Listener.class);
        if (clazzSet.isEmpty()) {
            log.error("Listener is empty! Please check it!");
        }

        List<Class<? extends ApplicationListener>> allListeners = new ArrayList<Class<? extends ApplicationListener>>();
        // 装载所有 {@code ApplicationListener} 的子类
        Class superClass;
        for (Class<?> clazz : clazzSet) {
            superClass = ApplicationListener.class;
            if (superClass.isAssignableFrom(clazz) && !superClass.equals(clazz)) {
                allListeners.add((Class<? extends ApplicationListener>) clazz);
            }
        }
        if (allListeners.isEmpty()) {
            log.error("Listener is empty! Please check @Listener is right?");
        }

        // 监听器排序
        sortListeners(allListeners);

        // 重复key的map，使用监听的type，取出所有的监听器
        map = new LinkedMultiMap<EventType, ListenerHelper>();

        Type type;
        ApplicationListener listener;
        for (Class<? extends ApplicationListener> clazz : allListeners) {
            // 获取监听器上的泛型信息
            type = ((ParameterizedType) clazz.getGenericInterfaces()[0]).getActualTypeArguments()[0];
            // 实例化监听器
            listener = ClassUtils.newInstance(clazz);

            // 监听器上的注解
            Listener annotation = clazz.getAnnotation(Listener.class);
            boolean enableAsync = annotation.enableAsync();
            String tag = annotation.tag();
            
            EventType eventType = new EventType(tag, type);
            map.put(eventType, new ListenerHelper(listener, enableAsync));
            if (log.isDebugEnabled()) {
                log.debug(clazz + " init~");
            }
        }

    }

    /**
     * 对所有的监听器进行排序
     */
    private void sortListeners(List<Class<? extends ApplicationListener>> listeners) {
        Collections.sort(listeners, new Comparator<Class<? extends ApplicationListener>>() {

            @Override
            public int compare(Class<? extends ApplicationListener> o1,
                    Class<? extends ApplicationListener> o2) {

                int x = o1.getAnnotation(Listener.class).order();
                int y = o2.getAnnotation(Listener.class).order();
                return (x < y) ? -1 : ((x == y) ? 0 : 1);
            }
        });
    }

    @Override
    public boolean stop() {
        if (null != pool) {
            pool.shutdown();
            pool = null;
        }
        if (null != map) {
            map.clear();
            map = null;
        }
        return true;
    }

}
