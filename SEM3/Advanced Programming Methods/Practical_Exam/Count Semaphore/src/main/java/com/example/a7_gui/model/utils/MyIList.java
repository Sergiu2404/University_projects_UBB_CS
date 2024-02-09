package com.example.a7_gui.model.utils;

import com.example.a7_gui.exceptions.DataStructureException;

import java.util.List;

public interface MyIList<T> {
    void add(T elem);
    T pop() throws DataStructureException;
    boolean isEmpty();
    List<T> getList();
}