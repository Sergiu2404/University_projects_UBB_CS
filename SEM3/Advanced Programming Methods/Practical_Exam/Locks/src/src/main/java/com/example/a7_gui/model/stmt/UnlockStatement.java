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

public class UnlockStatement implements IStatement{
    private final String variable;
    private static final Lock lock = new ReentrantLock();


    public UnlockStatement(String var) {
        this.variable = var;
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
                int freeAddress = fi.getValue();

                if (lockTable.containsKey(freeAddress)) {

                    if (lockTable.get(freeAddress) == state.getId())
                        lockTable.put(freeAddress, -1);

                } else {
                    throw new StatementExecutionException(String.format("Unlock: Address %d not in the lock table", freeAddress));
                }
            } else {
                throw new StatementExecutionException(String.format("Unlock: Variable %s is not of int type", variable));
            }
        } else {
            throw new StatementExecutionException(String.format("Unlock: Variable %s is not defined", variable));
        }

        lock.unlock();
        return null;
    }

    @Override
    public MyIDictionary<String, Type> typeCheck(MyIDictionary<String, Type> typeEnv) throws StatementExecutionException, ExpressionEvaluationException, ADTException {
        if (typeEnv.lookUp(variable).equals(new IntType()))
            return typeEnv;
        else
            throw new StatementExecutionException(String.format("Lock: Var %s is not of type int!", variable));
    }

    @Override
    public IStatement deepCopy() {
        return new UnlockStatement(variable);
    }

    @Override
    public String toString() {
        return String.format("Unlock(%s)", variable);
    }
}
