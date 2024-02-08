package model.utils;

import exceptions.DictionaryException;
import exceptions.StatementExecutionException;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class MyDictionary<K,V> implements MyIDictionary<K,V>{
    HashMap<K,V> dictionary;

    public MyDictionary() {
        this.dictionary = new HashMap<>();
    }
    
    public boolean isDefined(K key) {
        return this.dictionary.containsKey(key);
    }

    
    public V lookUp(K key) throws DictionaryException {
        if (!isDefined(key))
            throw new DictionaryException( key + " is not defined.");
        return this.dictionary.get(key);
    }

    
    public void update(K key, V value) throws DictionaryException {
        if (!isDefined(key))
            throw new DictionaryException(key + " is not defined.");
        this.dictionary.put(key, value);
    }

    
    public Collection<V> values() {
        return this.dictionary.values();
    }


    public void remove(K key) throws StatementExecutionException{
        if (!isDefined(key))
            throw new StatementExecutionException(key + " is not defined.");
        this.dictionary.remove(key);
    }

    
    public Set<K> keySet() {
        return dictionary.keySet();
    }

    
    public Map<K, V> getContent() {
        return dictionary;
    }


    public MyIDictionary<K,V> deepcopy() throws StatementExecutionException {
        MyIDictionary<K,V> deepcopiedDictionary = new MyDictionary<>();
        for(K key:keySet())
            deepcopiedDictionary.put(key,lookup(key));
        return deepcopiedDictionary;
    }

    
    public String toString() {
        return this.dictionary.toString();
    }

    
    public void put(K key, V value) {
        this.dictionary.put(key, value);
    }

    @Override
    public V lookup(K key) {
        return this.dictionary.get(key);
    }
}
