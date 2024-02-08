package com.example.a7_gui.model.my_utils;

import com.example.a7_gui.exceptions.DataStructureException;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public interface MyISymbolTable <T, S>{
    boolean isDefined(T key);
    void put(T key, S value);
    S lookUp(T key) throws DataStructureException;
    Collection<S> values();
    void remove(T key) throws DataStructureException;
    Set<T> keySet();
    Map<T, S> getContent();
    MyISymbolTable<T, S> deepCopy() throws DataStructureException;
}
