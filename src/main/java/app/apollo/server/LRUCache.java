package app.apollo.server;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;

public class LRUCache<K, V> implements Cache<K, V> {

    private final Integer capacity;
    private final Map<K, V> map;
    private final LinkedList<K> usageList;

    public LRUCache(Integer capacity) {
        this.capacity = capacity;
        this.map = new HashMap<>();
        this.usageList = new LinkedList<>();
    }

    @Override
    public void put(K key, V value) {
        if (map.containsKey(key)) {
            usageList.remove(key);
        } else if (map.size() >= capacity) {
            K lruKey = usageList.removeLast();
            map.remove(lruKey);
        }
        usageList.addFirst(key);
        map.put(key, value);
    }

    @Override
    public Optional<V> get(K key) {
        if (!map.containsKey(key)) {
            return Optional.empty();
        }
        usageList.remove(key);
        usageList.addFirst(key);
        return Optional.of(map.get(key));
    }

    @Override
    public void remove(K key) {
        if (map.containsKey(key)) {
            map.remove(key);
            usageList.remove(key);
        }
    }

    @Override
    public boolean isEmpty() {
        return map.isEmpty();
    }

    @Override
    public void clear() {
        map.clear();
        usageList.clear();
    }

    @Override
    public Collection<V> values() {
        return map.values();
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        return map.entrySet();
    }

}
