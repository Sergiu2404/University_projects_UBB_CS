package com.example.a7_gui.model.utils;

import com.example.a7_gui.exceptions.ADTException;

import java.util.HashMap;

public interface MyICountdownLatchTable {
    int get(int key) throws ADTException;
    int getFreeAddress();
    boolean containsKey(int key);

    void put(int key, int value) throws ADTException;
    void setFreeAddress(int freeAddress);

    HashMap<Integer, Integer> getCountdownLatchTable();
    void setCountdownLatchTable(HashMap<Integer, Integer> newTable);
}
