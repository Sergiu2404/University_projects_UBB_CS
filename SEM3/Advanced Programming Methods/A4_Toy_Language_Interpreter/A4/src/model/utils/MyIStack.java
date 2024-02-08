package model.utils;

import model.stmt.IStmt;

import java.util.List;

public interface MyIStack<Stmt> {
    void push(Stmt st);

    Stmt pop();


    boolean isEmpty();

    List<Stmt> getReverse();
}
