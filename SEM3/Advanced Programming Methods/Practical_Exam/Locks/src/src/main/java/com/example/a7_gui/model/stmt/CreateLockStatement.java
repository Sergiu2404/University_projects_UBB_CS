package com.example.a7_gui.model.stmt;

import com.example.a7_gui.exceptions.ADTException;
import com.example.a7_gui.exceptions.DivisionByZero;
import com.example.a7_gui.exceptions.ExpressionEvaluationException;
import com.example.a7_gui.exceptions.StatementExecutionException;
import com.example.a7_gui.model.programState.ProgramState;
import com.example.a7_gui.model.types.IntType;
import com.example.a7_gui.model.types.Type;
import com.example.a7_gui.model.utils.MyIDictionary;
import com.example.a7_gui.model.utils.MyILockTable;
import com.example.a7_gui.model.utils.MyISymbolTable;
import com.example.a7_gui.model.values.IntValue;
import com.example.a7_gui.model.values.Value;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class CreateLockStatement implements IStatement{
    private final String variable;
    private static final Lock lock = new ReentrantLock();


    public CreateLockStatement(String variable)
    {
        this.variable = variable;
        //lock = new ReentrantLock();
    }


    @Override
    public ProgramState execute(ProgramState state) throws StatementExecutionException, ExpressionEvaluationException, ADTException, DivisionByZero {
        lock.lock();

        MyILockTable lockTable = state.getLockTable();
        MyISymbolTable<String, Value> symbolTable = state.getSymTable();

        int freeAddress = lockTable.getFreeAddress();
        lockTable.put(lockTable.getFreeAddress(), -1);

        if(symbolTable.isDefined(variable) && symbolTable.lookUp(variable).getType().equals(new IntType()))
            symbolTable.put(variable, new IntValue(freeAddress));
        else throw new StatementExecutionException(String.format("CreateLock: Variable %s not declared in symbol table", variable));

        lock.unlock();
        return null;
    }

    @Override
    public MyIDictionary<String, Type> typeCheck(MyIDictionary<String, Type> typeEnv) throws StatementExecutionException, ExpressionEvaluationException, ADTException {
        if (typeEnv.lookUp(variable).equals(new IntType()))
            return typeEnv;
        else throw new StatementExecutionException(String.format("CreateLock: Variable %s should be of type int", variable));
    }

    @Override
    public IStatement deepCopy() {
        return new CreateLockStatement(variable);
    }

    @Override
    public String toString() {
        return String.format("createLock(%s)", variable);
    }
}
