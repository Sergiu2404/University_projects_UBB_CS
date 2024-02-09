package com.example.a7_gui.model.my_utils;

import com.example.a7_gui.exceptions.ADTException;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class MyFileTable<T1, T2> implements MyIFileTable<T1, T2> {
    HashMap<T1, T2> fileTable;

    public MyFileTable() {
        this.fileTable = new HashMap<>();
    }

    @Override
    public boolean isDefined(T1 key) {
        return this.fileTable.containsKey(key);
    }

    @Override
    public T2 lookUp(T1 key) throws ADTException {
        if (!isDefined(key))
            throw new ADTException( key + " is not defined.");
        return this.fileTable.get(key);
    }

    @Override
    public void update(T1 key, T2 value) throws ADTException {
        if (!isDefined(key))
            throw new ADTException(key + " is not defined.");
        this.fileTable.put(key, value);
    }

    @Override
    public Collection<T2> values() {
        return this.fileTable.values();
    }

    @Override
    public void remove(T1 key) throws ADTException {
        if (!isDefined(key))
            throw new ADTException(key + " is not defined.");
        this.fileTable.remove(key);
    }

    @Override
    public Set<T1> keySet() {
        return fileTable.keySet();
    }

    @Override
    public Map<T1, T2> getContent() {
        return fileTable;
    }

    @Override
    public MyIFileTable<T1, T2> deepCopy() throws ADTException {
        MyIFileTable<T1, T2> toReturn = new MyFileTable<>();
        for (T1 key: keySet())
            toReturn.put(key, lookUp(key));
        return toReturn;
    }

    @Override
    public String toString() {
        return this.fileTable.toString();
    }

    @Override
    public void put(T1 key, T2 value) {
        this.fileTable.put(key, value);
    }

}
