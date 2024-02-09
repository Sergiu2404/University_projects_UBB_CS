package com.example.a7_gui.model.my_utils;

import com.example.a7_gui.exceptions.ADTException;

import java.util.List;

public interface MyIList<T> {
    void add(T elem);
    T pop() throws ADTException;
    boolean isEmpty();
    List<T> getList();
}