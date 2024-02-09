package com.example.a7_gui.model.utils;

import com.example.a7_gui.exceptions.DataStructureException;

import java.util.List;

public interface MyIStack<T> {
    T pop() throws DataStructureException;
    void push(T element);
    boolean isEmpty();
    List<T> getReversed();
}