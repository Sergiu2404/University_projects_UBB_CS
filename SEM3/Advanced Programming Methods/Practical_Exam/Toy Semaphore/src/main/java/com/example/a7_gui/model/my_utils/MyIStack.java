package com.example.a7_gui.model.my_utils;

import com.example.a7_gui.exceptions.ADTException;

import java.util.List;

public interface MyIStack<T> {
    T pop() throws ADTException;
    void push(T element);
    T peek();
    boolean isEmpty();
    List<T> getReversed();
}