package com.example.a7_gui.model.my_utils;

import com.example.a7_gui.exceptions.DataStructureException;
import com.example.a7_gui.model.program_values.Value;

import java.util.HashMap;
import java.util.Set;

public interface MyIHeap{
    int getFreeValue();
    HashMap<Integer, Value> getContent();
    void setContent(HashMap<Integer, Value> newMap);
    int add(Value value);
    void update(Integer position, Value value) throws DataStructureException;
    Value get(Integer position) throws DataStructureException;
    boolean containsKey(Integer position);
    void remove(Integer key) throws DataStructureException;
    Set<Integer> keySet();
}