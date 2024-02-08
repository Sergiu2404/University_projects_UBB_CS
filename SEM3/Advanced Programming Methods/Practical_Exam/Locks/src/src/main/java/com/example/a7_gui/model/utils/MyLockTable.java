package com.example.a7_gui.model.utils;

import com.example.a7_gui.exceptions.ADTException;

import java.util.HashMap;
import java.util.Set;

public class MyLockTable implements MyILockTable{
    private HashMap<Integer, Integer> lockTable;
    private int freeAddress;

    public MyLockTable()
    {
        this.lockTable = new HashMap<>();
        this.freeAddress = 0;
    }
    @Override
    public boolean containsKey(int key) {
        synchronized (this)
        {
            return this.lockTable.containsKey(key);
        }
    }

    @Override
    public int getFreeAddress() {
        synchronized (this)
        {
            this.freeAddress++;
            return freeAddress;
        }
    }

    @Override
    public HashMap<Integer, Integer> getContent() {
        synchronized (this)
        {
            return this.lockTable;
        }
    }

    @Override
    public int get(int position) throws ADTException {
        synchronized (this)
        {
            if(!lockTable.containsKey(position))
                throw new ADTException(String.format("Position %d is not in the lock table"));
            return lockTable.get(position);
        }
    }

    @Override
    public Set<Integer> keySet() {
        synchronized (this)
        {
            return lockTable.keySet();
        }
    }

    @Override
    public void setContent(HashMap<Integer, Integer> newLockTbl) {
        synchronized (this)
        {
            this.lockTable = newLockTbl;
        }
    }

    @Override
    public void put(int key, int value) throws ADTException {
        synchronized (this)
        {
            lockTable.put(key, value);
        }
    }
}
