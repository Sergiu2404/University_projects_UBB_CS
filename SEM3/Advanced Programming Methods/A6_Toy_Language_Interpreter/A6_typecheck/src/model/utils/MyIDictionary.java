package model.utils;

import exceptions.DictionaryException;
import exceptions.StatementExecutionException;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

public interface MyIDictionary<K,V> {
    void put(K key, V value);

    V lookup(K key);

    boolean isDefined(K key);

    void update(K key, V value) throws DictionaryException;

    Set<K> keySet();

    void remove(K key) throws StatementExecutionException;

    Map<K, V> getContent();
    Collection<V> values();

    MyIDictionary<K,V> deepcopy() throws StatementExecutionException;
}
