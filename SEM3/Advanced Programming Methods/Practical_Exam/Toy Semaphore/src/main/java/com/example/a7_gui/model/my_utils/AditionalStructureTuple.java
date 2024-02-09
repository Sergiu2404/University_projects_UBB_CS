package com.example.a7_gui.model.my_utils;

public class AditionalStructureTuple<T, S, U> {
    private final T element1;
    private final S element2;
    private final U element3;

    public AditionalStructureTuple(T e1, S e2, U e3) {
        this.element1 = e1;
        this.element2 = e2;
        this.element3 = e3;
    }

    public T getElement1() {
        return element1;
    }

    public S getElement2() {
        return element2;
    }

    public U getElement3() {
        return element3;
    }
}
