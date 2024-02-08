package model.utils;

import exceptions.StatementExecutionException;
import model.values.IValue;

import java.util.Collection;
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

    public Map<K, V> getContent(){return this.dictionary;}
    public Collection<V> values()
    {
        return this.dictionary.values();
    }

    public MyIDictionary<K,V> deepcopy() throws StatementExecutionException {
        MyIDictionary<K,V> deepcopiedSymbolTable = new SymbolTable<>();
        for(K key:keySet())
            deepcopiedSymbolTable.put(key,lookup(key));
        return deepcopiedSymbolTable;
    }
}
