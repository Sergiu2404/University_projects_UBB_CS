package model.utils;

import exceptions.HeapException;
import model.values.IValue;

import java.util.HashMap;
import java.util.Set;

public interface MyIHeap {
    int getFirstFree();
    HashMap<Integer, IValue> getContent();

    void setContent(HashMap<Integer, IValue> newHeap);
    int add(IValue val);
    void update(Integer position, IValue val) throws HeapException;
    IValue get(Integer position) throws HeapException;

    boolean containsKey(Integer position);
    void remove(Integer key) throws HeapException;

    Set<Integer> keySet();
}
