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

public class LockStatement implements IStatement{
    private final String variable;
    private static final Lock lock = new ReentrantLock();

    public LockStatement(String variable)
    {
        this.variable = variable;
        //lock = new ReentrantLock();
    }

    @Override
    public ProgramState execute(ProgramState state) throws StatementExecutionException, ExpressionEvaluationException, ADTException, DivisionByZero {
        lock.lock();

        MyISymbolTable<String, Value> symTable = state.getSymTable();
        MyILockTable lockTable = state.getLockTable();

        if (symTable.isDefined(variable)) {

            if (symTable.lookUp(variable).getType().equals(new IntType())) {
                IntValue fi = (IntValue) symTable.lookUp(variable);
                int address = fi.getValue();

                if (lockTable.containsKey(address)) {

                    if (lockTable.get(address) == -1) {

                        lockTable.put(address, state.getId());
                        state.setLockTable(lockTable);

                    } else {
                        state.getExeStack().push(this);
                    }
                } else {
                    throw new StatementExecutionException(String.format("Lock: Address %s is not in the lock table", address));
                }
            } else {
                throw new StatementExecutionException(String.format("Lock: Variable %s should be of type int", variable));
            }
        } else {
            throw new StatementExecutionException(String.format("Lock: Variable %s not defined", variable));
        }
        lock.unlock();
        return null;
    }

    @Override
    public MyIDictionary<String, Type> typeCheck(MyIDictionary<String, Type> typeEnv) throws StatementExecutionException, ExpressionEvaluationException, ADTException {
        if (typeEnv.lookUp(variable).equals(new IntType())) {
            return typeEnv;
        } else {
            throw new StatementExecutionException(String.format("Lock: Variable %s should be of type int", variable));
        }
    }

    @Override
    public IStatement deepCopy() {
        return new LockStatement(variable);
    }

    @Override
    public String toString() {
        return String.format("lock (%s)", variable);
    }
}
