package com.example.a7_gui.model.my_utils;

import com.example.a7_gui.exceptions.ADTException;

import java.util.HashMap;
import java.util.List;

public interface MyISemaphore {
    boolean containsKey(int key);
    void put(int key, AditionalStructureTuple<Integer, List<Integer>, Integer> value);

    AditionalStructureTuple<Integer, List<Integer>, Integer> get(int key) throws ADTException;


    int getFreeAddress();
    void setFreeAddress(int freeAddress);
    HashMap<Integer, AditionalStructureTuple<Integer, List<Integer>, Integer>> getSemaphoreTable();
    void setSemaphoreTable(HashMap<Integer, AditionalStructureTuple<Integer, List<Integer>, Integer>> newToySemaphoreTable);
}
