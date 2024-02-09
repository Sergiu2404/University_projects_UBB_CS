package com.example.a7_gui.model.stmt;

import com.example.a7_gui.exceptions.DataStructureException;
import com.example.a7_gui.exceptions.ExecutionException;
import com.example.a7_gui.model.programState.ProgramState;
import com.example.a7_gui.model.types.IntType;
import com.example.a7_gui.model.types.Type;
import com.example.a7_gui.model.utils.MyIDictionary;
import com.example.a7_gui.model.utils.MyISemaphore;
import com.example.a7_gui.model.values.IntValue;
import com.example.a7_gui.model.values.Value;
import javafx.util.Pair;

import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ReleasePermitStatement implements IStatement{
    private final String variable;
    private static final Lock lock = new ReentrantLock();

    public ReleasePermitStatement(String variable) {
        this.variable = variable;
    }

    @Override
    public ProgramState execute(ProgramState state) throws ExecutionException, DataStructureException {
        lock.lock();

        MyIDictionary<String, Value> symTable = state.getSymbolTable();
        MyISemaphore semaphoreTable = state.getSemaphoreTable();

        if (symTable.isDefined(variable)) {

            if (symTable.lookUp(variable).getType().equals(new IntType())) {

                IntValue fi = (IntValue) symTable.lookUp(variable);
                int currentAddress = fi.getValue();

                if (semaphoreTable.getSemaphoreTable().containsKey(currentAddress)) {

                    Pair<Integer, List<Integer>> foundSemaphore = semaphoreTable.get(currentAddress);

                    if (foundSemaphore.getValue().contains(state.getId()))
                        foundSemaphore.getValue().remove((Integer) state.getId());
                    semaphoreTable.put(currentAddress, new Pair<>(foundSemaphore.getKey(), foundSemaphore.getValue()));

                } else {
                    throw new ExecutionException("Release: Index not in the semaphore table");
                }
            } else {
                throw new ExecutionException("Release: Index must be of int type");
            }
        } else {
            throw new ExecutionException("Release: Index not in symbol table");
        }
        lock.unlock();
        return null;
    }

    @Override
    public MyIDictionary<String, Type> typeCheck(MyIDictionary<String, Type> typeEnv) throws DataStructureException, ExecutionException {
        if (typeEnv.lookUp(variable).equals(new IntType())) {
            return typeEnv;
        } else {
            throw new ExecutionException(String.format("Release: %s is not int", variable));
        }
    }

    @Override
    public IStatement deepCopy() {
        return new ReleasePermitStatement(variable);
    }

    @Override
    public String toString() {
        return String.format("releasePermit(%s)", variable);
    }
}
