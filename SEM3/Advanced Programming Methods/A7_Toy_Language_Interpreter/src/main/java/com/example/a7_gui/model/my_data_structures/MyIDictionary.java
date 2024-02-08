package com.example.a7_gui.model.my_data_structures;

import com.example.a7_gui.my_exceptions.DataStructureException;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public interface MyIDictionary<T1,T2> {
    boolean isDefined(T1 key);
    void put(T1 key, T2 value);
    T2 lookUp(T1 key) throws DataStructureException;
    Collection<T2> values();
    Set<T1> keySet();
    Map<T1, T2> getContent();
    MyIDictionary<T1, T2> deepCopy() throws DataStructureException;
}