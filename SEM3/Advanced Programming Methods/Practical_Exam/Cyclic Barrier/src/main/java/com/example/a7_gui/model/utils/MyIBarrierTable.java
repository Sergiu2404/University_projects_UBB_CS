package com.example.a7_gui.model.utils;

import com.example.a7_gui.exceptions.ADTException;
import javafx.util.Pair;

import java.util.HashMap;
import java.util.List;

public interface MyIBarrierTable {
    boolean containsKey(int key);
    int getFreeAddress();
    void setFreeAddress(int freeAddress);
    HashMap<Integer, Pair<Integer, List<Integer>>> getBarrierTable();
    void setBarrierTable(HashMap<Integer, Pair<Integer, List<Integer>>> newBarrierTable);

    void put(int key, Pair<Integer, List<Integer>> value);
    Pair<Integer, List<Integer>> get(int key) throws ADTException;
}
