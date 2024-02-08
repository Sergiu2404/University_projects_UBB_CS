package com.example.a7_gui.model.utils;

import com.example.a7_gui.exceptions.ADTException;
import javafx.util.Pair;

import java.util.HashMap;
import java.util.List;

public class MyBarrierTable implements MyIBarrierTable{
    private HashMap<Integer, Pair<Integer, List<Integer>>> barrierTable;
    private int freeAddress;

    public MyBarrierTable()
    {
        this.barrierTable = new HashMap<>();
        freeAddress = 0;
    }

    @Override
    public boolean containsKey(int key) {
        synchronized (this)
        {
            return barrierTable.containsKey(key);
        }
    }

    @Override
    public int getFreeAddress() {
        synchronized (this)
        {
            freeAddress++;
            return freeAddress;
        }
    }

    @Override
    public void setFreeAddress(int freeAddress) {
        synchronized (this)
        {
            this.freeAddress = freeAddress;
        }
    }

    @Override
    public HashMap<Integer, Pair<Integer, List<Integer>>> getBarrierTable() {
        synchronized (this)
        {
            return this.barrierTable;
        }
    }

    @Override
    public void setBarrierTable(HashMap<Integer, Pair<Integer, List<Integer>>> newBarrierTable) {
        synchronized (this)
        {
            this.barrierTable = newBarrierTable;
        }
    }

    @Override
    public void put(int key, Pair<Integer, List<Integer>> value) {
        synchronized (this)
        {
            this.barrierTable.put(key, value);
        }
    }

    @Override
    public Pair<Integer, List<Integer>> get(int key) throws ADTException {
        return this.barrierTable.get(key);
    }

    @Override
    public String toString() {
        return this.barrierTable.toString();
    }
}
