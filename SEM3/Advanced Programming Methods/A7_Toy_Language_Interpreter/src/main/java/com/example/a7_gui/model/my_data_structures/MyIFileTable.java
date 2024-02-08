package com.example.a7_gui.model.my_data_structures;

import com.example.a7_gui.my_exceptions.DataStructureException;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public interface MyIFileTable<T, S> {
    boolean isDefined(T key);
    void put(T key, S value);
    S lookUp(T key) throws DataStructureException;
    Collection<S> values();
    void remove(T key) throws DataStructureException;
    Set<T> keySet();
    Map<T, S> getContent();
    MyIFileTable<T, S> deepCopy() throws DataStructureException;
}
