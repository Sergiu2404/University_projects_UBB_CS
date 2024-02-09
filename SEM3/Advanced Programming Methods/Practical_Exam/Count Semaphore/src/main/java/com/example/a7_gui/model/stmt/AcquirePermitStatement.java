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

public class AcquirePermitStatement implements IStatement{
    private final String variable;
    private static final Lock lock = new ReentrantLock();

    public AcquirePermitStatement(String variable) {
        this.variable = variable;
    }

    @Override
    public ProgramState execute(ProgramState state) throws ExecutionException, DataStructureException {
        lock.lock();
        MyIDictionary<String, Value> symTable = state.getSymbolTable();
        MyISemaphore semaphoreTable = state.getSemaphoreTable();

        if (symTable.isDefined(variable)) {

            if (symTable.lookUp(variable).getType().equals(new IntType())){

                IntValue fi = (IntValue) symTable.lookUp(variable);
                int address = fi.getValue();

                if (semaphoreTable.getSemaphoreTable().containsKey(address)) {
                    Pair<Integer, List<Integer>> foundSemaphore = semaphoreTable.get(address);

                    int NL = foundSemaphore.getValue().size();
                    int N1 = foundSemaphore.getKey();

                    if (N1 > NL) {

                        if (!foundSemaphore.getValue().contains(state.getId())) {
                            foundSemaphore.getValue().add(state.getId());
                            semaphoreTable.put(address, new Pair<>(N1, foundSemaphore.getValue()));
                        }

                    } else {
                        state.getExecutionStack().push(this);
                    }
                } else {
                    throw new ExecutionException("Acquire: Index not a key in the semaphore table");
                }
            } else {
                throw new ExecutionException("Acquire: Index must be of int type!");
            }
        } else {
            throw new ExecutionException("Acquire: Index not in symbol table!");
        }
        lock.unlock();
        return null;
    }

    @Override
    public MyIDictionary<String, Type> typeCheck(MyIDictionary<String, Type> typeEnv) throws DataStructureException, ExecutionException {
        if (typeEnv.lookUp(variable).equals(new IntType())) {
            return typeEnv;
        } else {
            throw new ExecutionException(String.format("Acquire: %s is not int", variable));
        }
    }

    @Override
    public IStatement deepCopy() {
        return new AcquirePermitStatement(variable);
    }

    @Override
    public String toString() {
        return String.format("acquire(%s)", variable);
    }
}
