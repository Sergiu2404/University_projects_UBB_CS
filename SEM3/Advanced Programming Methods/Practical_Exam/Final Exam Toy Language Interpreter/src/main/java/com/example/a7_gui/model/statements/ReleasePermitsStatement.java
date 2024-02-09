package com.example.a7_gui.model.statements;

import com.example.a7_gui.model.my_data_structures.MyIDictionary;
import com.example.a7_gui.model.my_data_structures.MyISemaphore;
import com.example.a7_gui.model.my_data_structures.MyISymbolTable;
import com.example.a7_gui.model.program_state.ProgramState;
import com.example.a7_gui.model.types.IntType;
import com.example.a7_gui.model.types.Type;
import com.example.a7_gui.model.values.IntValue;
import com.example.a7_gui.model.values.Value;
import com.example.a7_gui.my_exceptions.DataStructureException;
import com.example.a7_gui.my_exceptions.StatementExecutionException;
import javafx.util.Pair;

import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ReleasePermitsStatement implements IStatement{
    private final String variable;
    private static final Lock lock = new ReentrantLock();

    public ReleasePermitsStatement(String variable) {
        this.variable = variable;
    }

    @Override
    public ProgramState executeStatement(ProgramState state) throws StatementExecutionException, DataStructureException {
        lock.lock();

        MyISymbolTable<String, Value> symbolTabel = state.getSymbolTable();
        MyISemaphore semaphoreTable = state.getSemaphoreTable();

        if (symbolTabel.isDefined(variable)) {

            if (symbolTabel.lookUp(variable).getType().equals(new IntType())) {

                IntValue foundIndex = (IntValue) symbolTabel.lookUp(variable);
                int currentAddress = foundIndex.getValue();

                if (semaphoreTable.getSemaphoreTable().containsKey(currentAddress)) {

                    Pair<Integer, List<Integer>> foundSemaphore = semaphoreTable.get(currentAddress);

                    if (foundSemaphore.getValue().contains(state.getId()))
                        foundSemaphore.getValue().remove((Integer) state.getId());
                    semaphoreTable.put(currentAddress, new Pair<>(foundSemaphore.getKey(), foundSemaphore.getValue()));

                } else {
                    throw new StatementExecutionException("Release: Index not in the semaphore table");
                }
            } else {
                throw new StatementExecutionException("Release: Index must be of int type");
            }
        } else {
            throw new StatementExecutionException("Release: Index not in symbol table");
        }
        lock.unlock();
        return null;
    }

    @Override
    public MyIDictionary<String, Type> typeCheck(MyIDictionary<String, Type> typeEnv) throws DataStructureException, StatementExecutionException {
        if (typeEnv.lookUp(variable).equals(new IntType())) {
            return typeEnv;
        } else {
            throw new StatementExecutionException(String.format("Release: %s is not int", variable));
        }
    }

    @Override
    public IStatement deepCopy() {
        return new ReleasePermitsStatement(variable);
    }

    @Override
    public String toString() {
        return String.format("releasePermit(%s)", variable);
    }
}
