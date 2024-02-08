package com.example.a7_gui.model.utils;

import com.example.a7_gui.exceptions.ADTException;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class MyFileTable<T, S> implements MyIFileTable<T, S>{
    HashMap<T, S> dictionary;

    public MyFileTable() {
        this.dictionary = new HashMap<>();
    }

    @Override
    public boolean isDefined(T key) {
        return this.dictionary.containsKey(key);
    }

    @Override
    public S lookUp(T key) throws ADTException {
        if (!isDefined(key))
            throw new ADTException( key + " is not defined.");
        return this.dictionary.get(key);
    }

    @Override
    public void update(T key, S value) throws ADTException {
        if (!isDefined(key))
            throw new ADTException(key + " is not defined.");
        this.dictionary.put(key, value);
    }

    @Override
    public Collection<S> values() {
        return this.dictionary.values();
    }

    @Override
    public void remove(T key) throws ADTException {
        if (!isDefined(key))
            throw new ADTException(key + " is not defined.");
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
    public MyIFileTable<T, S> deepCopy() throws ADTException {
        MyIFileTable<T, S> toReturn = new MyFileTable<>();
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
