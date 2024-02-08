package com.example.a7_gui.model.my_data_structures;

import com.example.a7_gui.my_exceptions.DataStructureException;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public interface MyISymbolTable<T, S> {
    boolean isDefined(T key);
    void put(T key, S value);
    S lookUp(T key) throws DataStructureException;
    Collection<S> values();
    Set<T> keySet();
    Map<T, S> getContent();
    MyISymbolTable<T, S> deepCopy() throws DataStructureException;
}
