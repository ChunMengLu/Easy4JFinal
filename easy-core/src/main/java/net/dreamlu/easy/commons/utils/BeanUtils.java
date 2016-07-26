package net.dreamlu.easy.commons.utils;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Record;
import net.sf.cglib.beans.BeanCopier;
import net.sf.cglib.beans.BeanMap;
import net.sf.cglib.core.Converter;

import java.util.Map;

/**
 * 实体工具类，目前copy不支持map、list和model
 * @author L.cm
 * email: 596392912@qq.com
 * site:http://www.dreamlu.net
 * @date 2015年4月26日下午5:10:42
 */
public class BeanUtils {
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
	 * 将对象装成map形式
	 * @param src 源对象
	 * @return Map
	 */
	@SuppressWarnings("rawtypes")
	public static Map toMap(Object src) {
		return BeanMap.create(src);
	}

	/**
	 * 将java Bean转成model
	 * @param src 源对象
	 * @param modelClass model类
	 * @return Model
	 */
	@SuppressWarnings("unchecked")
	public static <T extends Model<?>> T toModel(Object src, Class<T> modelClass) {
		Map<String, Object> attrs = BeanUtils.toMap(src);
		T to = ClassUtils.newInstance(modelClass);
		
		to._setAttrs(attrs);
		return to;
	}

	/**
	 * 将java bean转成Record
	 * @param src 源对象
	 * @return Record
	 */
	@SuppressWarnings("unchecked")
	public static Record toRecord(Object src) {
		Map<String, Object> columns = BeanUtils.toMap(src);
		Record to = ClassUtils.newInstance(Record.class);
		
		to.setColumns(columns);
		return to;
	}

}