package net.dreamlu.easy.commons.utils;

import java.io.Serializable;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

/**
 * 多value的Map,value为list
 * @author L.cm
 */
public interface MultiMap<K, V> extends Serializable {

    /**
     * Add the given single value to the current list of values for the given key.
     * @param key the key
     * @param value the value to be added
     * @return boolean
     */
    boolean put(K key, V value);

    List<V> get(Object key);

    /**
     * clear
     */
    void clear();

    boolean isEmpty();

    boolean containsKey(Object key);

    List<V> remove(Object key);

    Set<Entry<K, List<V>>> entrySet();

}
