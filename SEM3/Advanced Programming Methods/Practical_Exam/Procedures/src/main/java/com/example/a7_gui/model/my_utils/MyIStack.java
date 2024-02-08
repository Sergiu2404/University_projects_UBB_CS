package com.example.a7_gui.model.my_utils;

import com.example.a7_gui.exceptions.DataStructureException;

import java.util.List;

public interface MyIStack<T> {
    T pop() throws DataStructureException;
    void push(T element);
    T peek();
    boolean isEmpty();
    List<T> getReversed();
    MyIStack<T> clone();
}