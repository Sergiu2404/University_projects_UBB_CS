package model.utils;

import model.stmt.IStmt;

import java.util.*;

public class ExeStack<T> implements MyIStack<T>{
    Stack<T> exeStack;

    public ExeStack()
    {
        this.exeStack=new Stack<T>();
    }

    public void push(T elem)
    {
        this.exeStack.push(elem);
    }

    public T pop()
    {
        return this.exeStack.pop();
    }

    public boolean isEmpty()
    {
        return exeStack.isEmpty();
    }

    public List<T> getReverse()
    {
          List<T> stackToList = new ArrayList<T>(exeStack);
          Collections.reverse(stackToList);
          return stackToList;
    }

    public String toString()
    {
        return ""+this.getReverse();
    }

}
