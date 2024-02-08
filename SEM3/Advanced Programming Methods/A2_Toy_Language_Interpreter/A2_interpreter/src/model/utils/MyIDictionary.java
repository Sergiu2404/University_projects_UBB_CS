package model.utils;

public interface MyIDictionary<K,V> {
    V lookup(K key);

    void put(K key, V val);

    boolean isDefined(K key);

    void update(K key, V val);

}
