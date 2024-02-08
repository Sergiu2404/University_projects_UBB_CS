package model.utils;

import java.util.Map;
import java.util.HashMap;
import java.util.Set;

public class SymbolTable<K,V> implements MyIDictionary<K,V>{
    Map<K,V> dictionary;

    public SymbolTable()
    {
        this.dictionary=new HashMap<>();
    }

    public void put(K key,V val)
    {
        this.dictionary.put(key,val);
    }

    public void update(K key, V val)
    {
        this.dictionary.put(key, val);
    }

    public boolean isDefined(K key)
    {
        return this.dictionary.containsKey(key);
    }

    public V lookup(K key)
    {
        return this.dictionary.get(key);
    }

    public String toString()
    {
        return ""+this.dictionary;
    }

    public Set<K> keySet()
    {
        return this.dictionary.keySet();
    }

    public void remove(K key)
    {
        this.dictionary.remove(key);
    }
}
