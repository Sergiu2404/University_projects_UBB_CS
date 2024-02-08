package model.utils;

import exceptions.DictionaryException;

import java.util.Set;

public interface MyIDictionary<K,V> {
    void put(K key, V value);

    V lookup(K key);

    boolean isDefined(K key);

    void update(K key, V value);

    Set<K> keySet();

    void remove(K key) throws DictionaryException;
}
