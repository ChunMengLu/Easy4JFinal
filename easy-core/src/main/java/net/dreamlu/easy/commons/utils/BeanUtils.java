package net.dreamlu.easy.commons.utils;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.jfinal.plugin.activerecord.CPI;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Record;

import net.sf.cglib.beans.BeanCopier;
import net.sf.cglib.beans.BeanGenerator;
import net.sf.cglib.beans.BeanMap;
import net.sf.cglib.core.Converter;

/**
 * Bean工具类，支持Bean，Model，Record
 * 
 * @author L.cm
 * email: 596392912@qq.com
 * site:http://www.dreamlu.net
 * @date 2015年4月26日下午5:10:42
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class BeanUtils {
    /**
     * 给一个Bean添加字段
     * @param superBean 父级Bean
     * @param props 新增属性
     * @return  {Object}
     */
    public static Object generator(Object superBean, BeanProperty... props) {
        Class<?> superclass = superBean.getClass();
        Object genBean = generator(superclass, props);
        BeanUtils.copy(superBean, genBean);
        return genBean;
    }
    
    /**
     * 给一个class添加字段
     * @param superclass 父级
     * @param props 新增属性
     * @return {Object}
     */
    public static Object generator(Class<?> superclass, BeanProperty... props) {
        BeanGenerator generator = new BeanGenerator();
        generator.setSuperclass(superclass);
        for (BeanProperty prop : props) {
            generator.addProperty(prop.getName(), prop.getType());
        }
        return generator.create();
    }
    
    /**
     * copy 对象属性到另一个对象，默认不使用Convert
     * @param src
     * @param clazz 类名
     * @return T
     */
    public static <T> T copy(Object src, Class<T> clazz) {
        BeanCopier copier = BeanCopier.create(src.getClass(), clazz, false);
        
        T to = ClassUtils.newInstance(clazz);
        copier.copy(src, to, null);
        return to;
    }
    
    /**
     * copy 对象属性到另一个对象
     * @param src 源对象
     * @param clazz 生成的对象Class
     * @param converter 自定义转换器
     * @return
     */
    public static <T> T copy(Object src, Class<T> clazz, Converter converter) {
        BeanCopier copier = BeanCopier.create(src.getClass(), clazz, true);
        
        T to = ClassUtils.newInstance(clazz);
        copier.copy(src, to, converter);
        return to;
    }
    
    /**
     * 拷贝对象
     * @param src 源对象
     * @param dist 需要赋值的对象
     */
    public static void copy(Object src, Object dist) {
        BeanCopier copier = BeanCopier
                .create(src.getClass(), dist.getClass(), false);
        
        copier.copy(src, dist, null);
    }
    
    /**
     * 拷贝对象
     * @param src 源对象
     * @param dist 需要赋值的对象
     * @param converter 自定义转换器
     * @return
     */
    public static void copy(Object src, Object dist, Converter converter) {
        BeanCopier copier = BeanCopier
                .create(src.getClass(), dist.getClass(), true);
        
        copier.copy(src, dist, converter);
    }
    
    /**
     * 将model转为 bean
     */
    public static <T> T toBean(Model<?> model, Class<T> valueType) {
        return toBean(CPI.getAttrs(model), valueType);
    }
    
    /**
     * 将record转为 bean
     */
    public static <T> T toBean(Record record, Class<T> valueType) {
        return toBean(record.getColumns(), valueType);
    }
    
    /**
     * 将map 转为 bean
     */
    public static <T> T toBean(Map<String, Object> beanMap, Class<T> valueType) {
        T bean = ClassUtils.newInstance(valueType);
        PropertyDescriptor[] beanPds = getPropertyDescriptors(bean);
        for (PropertyDescriptor propDescriptor : beanPds) {
            String propName = propDescriptor.getName();
            // 过滤class属性 
            if (propName.equals("class")) {
                continue;
            }
            if (beanMap.containsKey(propName)) { 
                Method writeMethod = propDescriptor.getWriteMethod();
                if (null == writeMethod) {
                    continue;
                }
                Object value = beanMap.get(propName);
                if (!writeMethod.isAccessible()) {
                    writeMethod.setAccessible(true);
                }
                try {
                    writeMethod.invoke(bean, value);
                } catch (Throwable e) {
                    throw new RuntimeException("Could not set property '" + propName + "' to bean", e);
                }
            } 
        }
        return bean;
    }
    
    /**
     * 将对象装成map形式
     * @param src 源对象
     * @return Map
     */
    public static Map toMap(Object src) {
        return BeanMap.create(src);
    }
    
    /**
     * copy 老model的属性到新model
     * @param src 源model
     * @param dist 新model
     */
    public static void copy(Model<?> src, Model<?> dist) {
        dist._setAttrs(CPI.getAttrs(src));
    }
    
    /**
     * copy 老model的属性到新Record
     * @param src 源model
     * @param dist 新Record
     */
    public static void copy(Model<?> src, Record dist) {
        dist.setColumns(src);
    }
    
    /**
     * copy 老Record的属性到新model
     * @param src 源Record
     * @param dist 新model
     */
    public static void copy(Record src, Model<?> dist) {
        dist._setAttrs(src.getColumns());
    }
    
    /**
     * copy java Bean到model
     * @param src
     * @param dist
     */
    public static void copy(Object src, Model<?> dist) {
        Map<String, Object> attrs = BeanUtils.toMap(src);
        dist._setAttrs(attrs);
    }
    
    /**
     * copy java Bean到record
     * @param src
     * @param dist
     */
    public static void copy(Object src, Record dist) {
        Map<String, Object> columns = BeanUtils.toMap(src);
        dist.setColumns(columns);
    }
    
    // 类属性缓存，空间换时间
    private static final ConcurrentMap<Class<?>, PropertyDescriptor[]> classPropCache =
            new ConcurrentHashMap<Class<?>, PropertyDescriptor[]>(64);
    
    /**
     * 获取Bean的属性
     * @param bean
     * @return
     */
    private static PropertyDescriptor[] getPropertyDescriptors(Object bean) {
        Class<?> beanClass = bean.getClass();
        PropertyDescriptor[] cachePds = classPropCache.get(beanClass);
        if (null != cachePds) {
            return cachePds;
        }
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(beanClass);
            cachePds = beanInfo.getPropertyDescriptors();
            classPropCache.put(beanClass, cachePds);
            return cachePds;
        } catch (IntrospectionException e) {
            throw new RuntimeException(e);
        }
    }
    
    /**
     * 获取Bean的属性
     * @param bean bean
     * @param propertyName 属性名
     * @return 属性值
     */
    public static Object getProperty(Object bean, String propertyName) {
        PropertyDescriptor[] beanPds = getPropertyDescriptors(bean);
        for (PropertyDescriptor propertyDescriptor : beanPds) {
            if (propertyDescriptor.getName().equals(propertyName)){
                Method readMethod = propertyDescriptor.getReadMethod();
                if (null == readMethod) {
                    continue;
                }
                if (!readMethod.isAccessible()) {
                    readMethod.setAccessible(true);
                }
                try {
                    return readMethod.invoke(bean);
                } catch (Throwable ex) {
                    throw new RuntimeException("Could not read property '" + propertyName + "' from bean", ex);
                }
            }
        }
        return null;
    }
    
    /**
     * 设置Bean属性
     * @param bean bean
     * @param propertyName 属性名
     * @param value 属性值
     */
    public static void setProperty(Object bean, String propertyName, Object value) {
        PropertyDescriptor[] beanPds = getPropertyDescriptors(bean);
        for (PropertyDescriptor propertyDescriptor : beanPds) {
            if (propertyDescriptor.getName().equals(propertyName)){
                Method writeMethod = propertyDescriptor.getWriteMethod();
                if (null == writeMethod) {
                    continue;
                }
                if (!writeMethod.isAccessible()) {
                    writeMethod.setAccessible(true);
                }
                try {
                    writeMethod.invoke(bean, value);
                } catch (Throwable ex) {
                    throw new RuntimeException("Could not set property '" + propertyName + "' to bean", ex);
                }
            }
        }
    }
    
}