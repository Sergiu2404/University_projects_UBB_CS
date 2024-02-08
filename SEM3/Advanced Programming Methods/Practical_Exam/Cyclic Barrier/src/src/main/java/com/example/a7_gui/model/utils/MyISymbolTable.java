package com.example.a7_gui.model.utils;

import com.example.a7_gui.exceptions.ADTException;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public interface MyISymbolTable<T, S> {
    boolean isDefined(T key);
    void put(T key, S value);
    S lookUp(T key) throws ADTException;
    void update(T key, S value) throws ADTException;
    Collection<S> values();
    void remove(T key) throws ADTException;
    Set<T> keySet();
    Map<T, S> getContent();
    MyISymbolTable<T, S> deepCopy() throws ADTException;
}
