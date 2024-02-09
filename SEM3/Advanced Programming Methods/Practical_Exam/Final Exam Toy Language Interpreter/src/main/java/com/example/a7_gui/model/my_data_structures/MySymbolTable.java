package com.example.a7_gui.model.my_data_structures;

import com.example.a7_gui.my_exceptions.DataStructureException;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class MySymbolTable<T, S> implements MyISymbolTable<T, S>{
    HashMap<T, S> symbolTable;

    public MySymbolTable() {
        this.symbolTable = new HashMap<>();
    }

    @Override
    public boolean isDefined(T key) {
        return this.symbolTable.containsKey(key);
    }

    @Override
    public S lookUp(T key) throws DataStructureException {
        if (!isDefined(key))
            throw new DataStructureException( key + " is not defined.");
        return this.symbolTable.get(key);
    }

    @Override
    public Collection<S> values() {
        return this.symbolTable.values();
    }


    @Override
    public Set<T> keySet() {
        return symbolTable.keySet();
    }

    @Override
    public Map<T, S> getContent() {
        return symbolTable;
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
        return this.symbolTable.toString();
    }

    @Override
    public void put(T key, S value) {
        this.symbolTable.put(key, value);
    }
}
