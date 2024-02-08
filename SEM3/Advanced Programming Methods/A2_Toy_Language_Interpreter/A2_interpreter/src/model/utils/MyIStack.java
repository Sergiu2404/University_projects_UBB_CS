package model.utils;

import java.util.List;


//Execution stack: stack that contains statements
public interface MyIStack<T> {

    void push(T elem);

    T pop();

    boolean isEmpty();

    List<T> getReverse();
}
