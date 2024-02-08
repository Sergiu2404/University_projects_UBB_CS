package model.utils;

import exceptions.HeapException;
import model.values.IValue;

import java.util.HashMap;
import java.util.Set;

public class HeapTable implements MyIHeap{
    HashMap<Integer, IValue> heap;
    Integer freeLocationValue;

    public HeapTable()
    {
        heap = new HashMap<>();
        freeLocationValue = 1;
    }

    public int newFreeLocation()
    {
        freeLocationValue++;
        while(freeLocationValue == 0 || heap.containsKey(freeLocationValue))
            freeLocationValue++;

        return freeLocationValue;
    }

    @Override
    public int getFirstFree() {
        return this.freeLocationValue;
    }

    @Override
    public HashMap<Integer, IValue> getContent() {
        return this.heap;
    }

    @Override
    public void setContent(HashMap<Integer, IValue> newHeap) {
        this.heap=newHeap;
    }

    @Override
    public int add(IValue val) {
        this.heap.put(freeLocationValue, val);
        Integer positionAdded = freeLocationValue;

        freeLocationValue = newFreeLocation();
        return positionAdded;
    }

    @Override
    public void update(Integer position, IValue val) throws HeapException{
        if(!heap.containsKey(position))
            throw new HeapException(String.format("%d position not present in the heap", position));

        this.heap.put(position, val);
    }

    @Override
    public IValue get(Integer position) throws HeapException{
        if(!heap.containsKey(position))
            throw new HeapException(String.format("%d position not present in the heap", position));

        return heap.get(position);
    }

    @Override
    public boolean containsKey(Integer position) {
        return heap.containsKey(position);
    }

    @Override
    public void remove(Integer key) throws HeapException{
        if(!heap.containsKey(key))
            throw new HeapException(String.format("%d position not present in the heap", key));

        freeLocationValue = key;
        this.heap.remove(key);
    }

    @Override
    public Set<Integer> keySet() {
        return this.heap.keySet();
    }

    @Override
    public String toString() {
        return "" + this.heap;
    }
}
