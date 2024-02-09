package com.example.a7_gui.model.utils;

import com.example.a7_gui.exceptions.DataStructureException;

import java.util.ArrayList;
import java.util.List;

public class MyList<T> implements MyIList<T>{
    List<T> list;

    public MyList() {
        this.list = new ArrayList<>();
    }

    @Override
    public void add(T elem) {
        this.list.add(elem);
    }

    @Override
    public T pop() throws DataStructureException {
        if (list.isEmpty())
            throw new DataStructureException("The list is empty!");
        return this.list.remove(0);
    }

    @Override
    public boolean isEmpty() {
        return this.list.isEmpty();
    }

    @Override
    public List<T> getList() {
        return list;
    }

    @Override
    public String toString() {
        return this.list.toString();
    }
}