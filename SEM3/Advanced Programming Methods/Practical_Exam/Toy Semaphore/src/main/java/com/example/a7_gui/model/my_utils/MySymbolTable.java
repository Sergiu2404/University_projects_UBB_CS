package com.example.a7_gui.model.my_utils;

import com.example.a7_gui.exceptions.ADTException;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class MySymbolTable<T1, T2> implements MyISymbolTable<T1, T2> {
    HashMap<T1, T2> symbolTable;

    public MySymbolTable() {
        this.symbolTable = new HashMap<>();
    }

    @Override
    public boolean isDefined(T1 key) {
        return this.symbolTable.containsKey(key);
    }

    @Override
    public T2 lookUp(T1 key) throws ADTException {
        if (!isDefined(key))
            throw new ADTException( key + " is not defined.");
        return this.symbolTable.get(key);
    }


    @Override
    public Collection<T2> values() {
        return this.symbolTable.values();
    }

    @Override
    public void remove(T1 key) throws ADTException {
        if (!isDefined(key))
            throw new ADTException(key + " is not defined.");
        this.symbolTable.remove(key);
    }

    @Override
    public Set<T1> keySet() {
        return symbolTable.keySet();
    }

    @Override
    public Map<T1, T2> getContent() {
        return symbolTable;
    }

    @Override
    public MyISymbolTable<T1, T2> deepCopy() throws ADTException {
        MyISymbolTable<T1, T2> toReturn = new MySymbolTable<>();
        for (T1 key: keySet())
            toReturn.put(key, lookUp(key));
        return toReturn;
    }

    @Override
    public String toString() {
        return this.symbolTable.toString();
    }

    @Override
    public void put(T1 key, T2 value) {
        this.symbolTable.put(key, value);
    }

}
