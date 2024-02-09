package com.example.a7_gui.model.my_data_structures;

import com.example.a7_gui.my_exceptions.DataStructureException;
import com.example.a7_gui.model.values.Value;

import java.util.HashMap;
import java.util.Set;

public interface MyIHeap{
    HashMap<Integer, Value> getContent();
    void setContent(HashMap<Integer, Value> newMap);
    int add(Value value);
    void update(Integer position, Value value) throws DataStructureException;
    Value get(Integer position) throws DataStructureException;
    boolean containsKey(Integer position);
    Set<Integer> keySet();
}