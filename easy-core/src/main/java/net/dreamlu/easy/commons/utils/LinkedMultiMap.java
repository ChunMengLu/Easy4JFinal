package net.dreamlu.easy.commons.utils;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * 参看Spring mvc并改造原有的ArrayListMultimap
 * @author L.cm
 * 
 * @param <K> 泛型标记
 * @param <V> 泛型标记
 */
public class LinkedMultiMap<K, V> implements MultiMap<K, V> {
    private static final long serialVersionUID = -8643982901171995929L;

    private final Map<K, List<V>> targetMap;

    /**
     * Create a new LinkedMultiMap that wraps a {@link LinkedHashMap}.
     */
    public LinkedMultiMap() {
        this.targetMap = new LinkedHashMap<K, List<V>>();
    }

    /**
     * Create a new LinkedMultiValueMap that wraps a {@link LinkedHashMap}
     * with the given initial capacity.
     * @param initialCapacity the initial capacity
     */
    public LinkedMultiMap(int initialCapacity) {
        this.targetMap = new LinkedHashMap<K, List<V>>(initialCapacity);
    }

    /**
     * 使用其他类型的map进行初始化
     */
    public LinkedMultiMap(Map<K, List<V>> otherMap) {
        this.targetMap = otherMap;
    }

    @Override
    public boolean put(K key, V value) {
        List<V> list = targetMap.get(key);
        if (list == null) {
            list = new LinkedList<V>();
            if (list.add(value)) {
                targetMap.put(key, list);
                return true;
            } else {
                throw new AssertionError("New list violated the list spec");
            }
        } else if (list.add(value)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public List<V> get(Object key) {
        return targetMap.get(key);
    }

    @Override
    public void clear() {
        targetMap.clear();
    }

    @Override
    public boolean isEmpty() {
        return targetMap.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return targetMap.containsKey(key);
    }

    @Override
    public List<V> remove(Object key) {
        return this.targetMap.remove(key);
    }

    @Override
    public Set<Entry<K, List<V>>> entrySet() {
        return this.targetMap.entrySet();
    }
    
    @Override
    public boolean equals(Object obj) {
        return this.targetMap.equals(obj);
    }

    @Override
    public int hashCode() {
        return this.targetMap.hashCode();
    }

    @Override
    public String toString() {
        return this.targetMap.toString();
    }

}
