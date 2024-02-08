package com.example.a7_gui.model.my_data_structures;

import com.example.a7_gui.my_exceptions.DataStructureException;

import java.util.List;

public interface MyIList<T> {
    void add(T elem);
    boolean isEmpty();
    List<T> getList();
}