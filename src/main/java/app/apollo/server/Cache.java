package app.apollo.server;

import java.util.Collection;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;

/***
 * Cache<K,V> interface refers to a container that stores some fixed amount of items and allows for quick lookup.
 */
public interface Cache<K,V> {

    /***
     * Puts a key value map in cache contianer.
     * @param key Key that will be used to find element.
     * @param value Value of element.
     */
    public void put(K key, V value);

    /***
     * Returns an element that refers to given key. If element cannot be found then defaultValue will be returned.
     * @param key Key of some element that might be found in cache.
     * @return Optionally returns found element.
     */
    public Optional<V> get(K key);

    /***
     * Removes element from cache that is coupled with given key.
     * @param key Key that refers to some element in cache.
     */
    public void remove(K key);

    /***
     * Checks if cache is empty.
     * @return Flag that tells if container is empty.
     */
    public boolean isEmpty();

    /***
     * Creates set of entires stored in cache.
     * @return Set of entries stored in cache.
     */
    public Set<Entry<K,V>> entrySet();

    /***
     * Creates collection of values.
     * @return Collection of values.
     */
    public Collection<V> values();

    /***
     * Clears all elements stored in cache.
     */
    public void clear();

}
