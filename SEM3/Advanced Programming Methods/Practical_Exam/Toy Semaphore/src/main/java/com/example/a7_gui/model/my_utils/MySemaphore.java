package com.example.a7_gui.model.my_utils;

import com.example.a7_gui.exceptions.ADTException;

import java.util.HashMap;
import java.util.List;

public class MySemaphore implements MyISemaphore{
    private HashMap<Integer, AditionalStructureTuple<Integer, List<Integer>, Integer>> semaphoreTable;
    private int freeLocation = 0;

    public MySemaphore() {
        this.semaphoreTable = new HashMap<>();
    }

    @Override
    public synchronized void put(int key, AditionalStructureTuple<Integer, List<Integer>, Integer> value) {
        semaphoreTable.put(key, value);
    }

    @Override
    public synchronized AditionalStructureTuple<Integer, List<Integer>, Integer> get(int key) throws ADTException {
        if (semaphoreTable.containsKey(key)) {
            return semaphoreTable.get(key);
        } else {
            throw new ADTException(String.format("Address %d not in semaphore table", key));
        }

    }

    @Override
    public synchronized boolean containsKey(int key) {
        return semaphoreTable.containsKey(key);
    }

    @Override
    public synchronized int getFreeAddress() {
        freeLocation++;
        return freeLocation;
    }

    @Override
    public synchronized void setFreeAddress(int freeAddress) {
        this.freeLocation = freeAddress;
    }

    @Override
    public synchronized HashMap<Integer, AditionalStructureTuple<Integer, List<Integer>, Integer>> getSemaphoreTable() {
        return semaphoreTable;
    }

    @Override
    public synchronized void setSemaphoreTable(HashMap<Integer, AditionalStructureTuple<Integer, List<Integer>, Integer>> newToySemaphoreTable) {
        this.semaphoreTable = newToySemaphoreTable;
    }

    @Override
    public String toString() {
        return semaphoreTable.toString();
    }
}
