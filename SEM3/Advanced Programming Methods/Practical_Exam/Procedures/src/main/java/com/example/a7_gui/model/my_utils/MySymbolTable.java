package com.example.a7_gui.model.my_utils;

import com.example.a7_gui.exceptions.DataStructureException;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class MySymbolTable<T, S> implements MyISymbolTable<T, S>{
    HashMap<T, S> dictionary;

    public MySymbolTable() {
        this.dictionary = new HashMap<>();
    }

    @Override
    public boolean isDefined(T key) {
        return this.dictionary.containsKey(key);
    }

    @Override
    public S lookUp(T key) throws DataStructureException {
        if (!isDefined(key))
            throw new DataStructureException( key + " is not defined.");
        return this.dictionary.get(key);
    }


    @Override
    public Collection<S> values() {
        return this.dictionary.values();
    }

    @Override
    public void remove(T key) throws DataStructureException {
        if (!isDefined(key))
            throw new DataStructureException(key + " is not defined.");
        this.dictionary.remove(key);
    }

    @Override
    public Set<T> keySet() {
        return dictionary.keySet();
    }

    @Override
    public Map<T, S> getContent() {
        return dictionary;
    }

    @Override
    public MyISymbolTable<T, S> deepCopy() throws DataStructureException {
        MyISymbolTable<T, S> toReturn = new MySymbolTable<>();
        for (T key: keySet())
            toReturn.put(key, lookUp(key));
        return toReturn;
    }

    @Override
    public String toString() {
        return this.dictionary.toString();
    }

    @Override
    public void put(T key, S value) {
        this.dictionary.put(key, value);
    }
}
