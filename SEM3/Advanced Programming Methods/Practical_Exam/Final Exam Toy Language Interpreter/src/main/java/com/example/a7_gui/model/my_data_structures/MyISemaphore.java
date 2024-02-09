package com.example.a7_gui.model.my_data_structures;

import com.example.a7_gui.my_exceptions.DataStructureException;
import javafx.util.Pair;

import java.util.HashMap;
import java.util.List;

public interface MyISemaphore {
    void put(int key, Pair<Integer, List<Integer>> value);
    Pair<Integer, List<Integer>> get(int key) throws DataStructureException;
    int getFreeAddress();
    HashMap<Integer, Pair<Integer, List<Integer>>> getSemaphoreTable();
}
