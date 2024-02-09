package com.example.a7_gui.model.my_data_structures;

import com.example.a7_gui.my_exceptions.DataStructureException;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class MyFileTable<T, S> implements MyIFileTable<T, S>{
    HashMap<T, S> fileTable;

    public MyFileTable() {
        this.fileTable = new HashMap<>();
    }

    @Override
    public boolean isDefined(T key) {
        return this.fileTable.containsKey(key);
    }

    @Override
    public S lookUp(T key) throws DataStructureException {
        if (!isDefined(key))
            throw new DataStructureException( key + " is not defined.");
        return this.fileTable.get(key);
    }


    @Override
    public Collection<S> values() {
        return this.fileTable.values();
    }

    @Override
    public void remove(T key) throws DataStructureException {
        if (!isDefined(key))
            throw new DataStructureException(key + " is not defined.");
        this.fileTable.remove(key);
    }

    @Override
    public Set<T> keySet() {
        return fileTable.keySet();
    }

    @Override
    public Map<T, S> getContent() {
        return fileTable;
    }

    @Override
    public MyIFileTable<T, S> deepCopy() throws DataStructureException {
        MyIFileTable<T, S> toReturn = new MyFileTable<>();
        for (T key: keySet())
            toReturn.put(key, lookUp(key));
        return toReturn;
    }

    @Override
    public String toString() {
        return this.fileTable.toString();
    }

    @Override
    public void put(T key, S value) {
        this.fileTable.put(key, value);
    }
}
