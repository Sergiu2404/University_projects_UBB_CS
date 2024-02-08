package com.example.a7_gui.model.utils;

import com.example.a7_gui.exceptions.ADTException;

import java.util.HashMap;

public class MyCountdownLatchTable implements MyICountdownLatchTable{
    private HashMap<Integer, Integer> countdownLatchTable;
    private int freeLocation;

    public MyCountdownLatchTable()
    {
        countdownLatchTable = new HashMap<>();
    }

    @Override
    public int get(int key) throws ADTException {
        synchronized (this)
        {
            if(countdownLatchTable.containsKey(key))
                return countdownLatchTable.get(key);
            else throw new ADTException(String.format("Latch tbl doesnt cotain key %d", key));
        }
    }

    @Override
    public int getFreeAddress() {
        synchronized (this)
        {
            this.freeLocation++;
            return freeLocation;
        }
    }

    @Override
    public boolean containsKey(int key) {
        synchronized (this)
        {
            return countdownLatchTable.containsKey(key);
        }
    }

    ///DIFFERNT
    @Override
    public void put(int key, int value) throws ADTException {
        synchronized (this){
            countdownLatchTable.put(key, value);
        }
    }

    @Override
    public void setFreeAddress(int freeAddress) {
        synchronized (this)
        {
            this.freeLocation = freeAddress;
        }
    }

    @Override
    public HashMap<Integer, Integer> getCountdownLatchTable() {
        synchronized (this)
        {
            return countdownLatchTable;
        }
    }

    @Override
    public void setCountdownLatchTable(HashMap<Integer, Integer> newTable) {
        synchronized (this)
        {
            this.countdownLatchTable = newTable;
        }
    }

    public String toString()
    {
        return countdownLatchTable.toString();
    }
}
