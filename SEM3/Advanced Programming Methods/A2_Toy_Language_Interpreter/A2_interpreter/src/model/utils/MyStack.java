package model.utils;

import java.util.*;

public class MyStack<T> implements MyIStack<T>{

    Stack<T> stack;

    public MyStack(){stack=new Stack<T>();}

    @Override
    public void push(T elem) {
        stack.push(elem);
    }

    @Override
    public T pop() {
        return stack.pop();
    }

    @Override
    public boolean isEmpty() {
        return stack.isEmpty();
    }

    @Override
    public List<T> getReverse() {
        List<T> stackToList= new ArrayList<T>(stack);
        Collections.reverse(stackToList);
        return stackToList;
//        List<T> auxList= Arrays.asList((T[])stack.toArray());
//        Collections.reverse(auxList);
//        return auxList;
    }

    @Override
    public String toString()
    {
        return ""+this.getReverse();
    }
}
