package net.dreamlu.easy.commons.type;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.beetl.ext.jfinal.BeetlRenderFactory;

import com.jfinal.json.FastJsonFactory;
import com.jfinal.json.JacksonFactory;

import net.dreamlu.easy.commons.session.RedisSessionManager;
import net.dreamlu.easy.commons.utils.ClassUtils;

/**
 * 类型别名注册表，参考mybatis
 * @author L.cm
 */
public class TypeAliasRegistry {
	private final Map<String, Class<?>> TYPE_ALIASES = new HashMap<String, Class<?>>();

	public TypeAliasRegistry() {
		registerAlias("fastjson", FastJsonFactory.class);
		registerAlias("jackson", JacksonFactory.class);
		registerAlias("beetl", BeetlRenderFactory.class);
		registerAlias("redis:session", RedisSessionManager.class);
	}

	public void registerAlias(String alias, Class<?> value) {
		if (alias == null) {
			throw new RuntimeException("The parameter alias cannot be null");
		}
		String key = alias.toLowerCase(Locale.ENGLISH);
		if (TYPE_ALIASES.containsKey(key) && TYPE_ALIASES.get(key) != null 
				&& !TYPE_ALIASES.get(key).equals(value)) {
			throw new RuntimeException("The alias '" + alias + "' is already mapped to the value '"
					+ TYPE_ALIASES.get(key).getName() + "'.");
		}
		TYPE_ALIASES.put(key, value);
	}

	@SuppressWarnings("unchecked")
	// throws class cast exception as well if types cannot be assigned
	public <T> Class<T> resolveAlias(String string) {
		if (string == null) {
			return null;
		}
		String key = string.toLowerCase(Locale.ENGLISH);
		if (TYPE_ALIASES.containsKey(key)) {
			return (Class<T>) TYPE_ALIASES.get(key);
		}
		return (Class<T>) ClassUtils.loadClass(string);
	}
}
