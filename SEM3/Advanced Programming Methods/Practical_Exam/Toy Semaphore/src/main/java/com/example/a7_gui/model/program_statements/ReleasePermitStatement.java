package com.example.a7_gui.model.program_statements;

import com.example.a7_gui.exceptions.ADTException;
import com.example.a7_gui.exceptions.StatementExecutionException;
import com.example.a7_gui.model.programState.ProgramState;
import com.example.a7_gui.model.program_types.IntType;
import com.example.a7_gui.model.program_types.Type;
import com.example.a7_gui.model.my_utils.AditionalStructureTuple;
import com.example.a7_gui.model.my_utils.MyIDictionary;
import com.example.a7_gui.model.my_utils.MyISemaphore;
import com.example.a7_gui.model.values.IntValue;
import com.example.a7_gui.model.values.Value;

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
    public ProgramState execute(ProgramState state) throws StatementExecutionException, ADTException {
        lock.lock();

        MyIDictionary<String, Value> symTable = state.getSymbolTable();
        MyISemaphore semaphoreTable = state.getSemaphore();

        if (symTable.isDefined(variable)) {

            if (symTable.lookUp(variable).getType().equals(new IntType())) {
                IntValue fi = (IntValue) symTable.lookUp(variable);
                int address = fi.getValue();

                if (semaphoreTable.getSemaphoreTable().containsKey(address)) {

                    AditionalStructureTuple<Integer, List<Integer>, Integer> currentSemaphore = semaphoreTable.get(address);

                    if (currentSemaphore.getElement2().contains(state.getId()))
                        currentSemaphore.getElement2().remove((Integer) state.getId());

                    semaphoreTable.put(address, new AditionalStructureTuple<>(currentSemaphore.getElement1(), currentSemaphore.getElement2(), currentSemaphore.getElement3()));
                } else {
                    throw new StatementExecutionException("Release: Index not in the semaphore table");
                }
            } else {
                throw new StatementExecutionException("Release: Index must be of int type!");
            }
        } else {
            throw new StatementExecutionException("Release: Index not in symbol table!");
        }
        lock.unlock();
        return null;
    }

    @Override
    public MyIDictionary<String, Type> typeCheck(MyIDictionary<String, Type> typeEnv) throws ADTException, StatementExecutionException {
        return typeEnv;
    }

    @Override
    public IStatement deepCopy() {
        return new ReleasePermitStatement(variable);
    }

    @Override
    public String toString() {
        return String.format("release(%s)", variable);
    }
}
