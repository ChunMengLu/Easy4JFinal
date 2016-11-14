package net.dreamlu.easy.commons.type;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class TypeAliasRegistry {
	private final Map<String, Class<?>> TYPE_ALIASES = new HashMap<String, Class<?>>();

	public TypeAliasRegistry() {
		registerAlias("string", String.class);
	}

	public void registerAlias(String alias, Class<?> value) {
		if (alias == null) {
			throw new RuntimeException("The parameter alias cannot be null");
		}
		// issue #748
		String key = alias.toLowerCase(Locale.ENGLISH);
		if (TYPE_ALIASES.containsKey(key) && TYPE_ALIASES.get(key) != null && !TYPE_ALIASES.get(key).equals(value)) {
			throw new RuntimeException("The alias '" + alias + "' is already mapped to the value '"
					+ TYPE_ALIASES.get(key).getName() + "'.");
		}
		TYPE_ALIASES.put(key, value);
	}

}
