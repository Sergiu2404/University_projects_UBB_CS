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

public class AcquirePermitStatement implements IStatement{
    private final String variable;
    private static final Lock lock = new ReentrantLock();

    public AcquirePermitStatement(String variable) {
        this.variable = variable;
    }

    @Override
    public ProgramState execute(ProgramState state) throws StatementExecutionException, ADTException {
        lock.lock();

        MyIDictionary<String, Value> symTable = state.getSymbolTable();
        MyISemaphore semaphoreTable = state.getSemaphore();

        if (symTable.isDefined(variable)) {

            if (symTable.lookUp(variable).getType().equals(new IntType())){
                IntValue fi = (IntValue) symTable.lookUp(variable);
                int address = fi.getValue();

                if (semaphoreTable.getSemaphoreTable().containsKey(address)) {

                    AditionalStructureTuple<Integer, List<Integer>, Integer> currentSemaphore = semaphoreTable.get(address);
                    int NL = currentSemaphore.getElement2().size();
                    int N1 = currentSemaphore.getElement1();
                    int N2 = currentSemaphore.getElement3();

                    if (N1-N2 > NL) {
                        if (!currentSemaphore.getElement2().contains(state.getId())) {
                            currentSemaphore.getElement2().add(state.getId());
                            semaphoreTable.put(address, new AditionalStructureTuple<>(N1, currentSemaphore.getElement2(), N2));
                        }
                    } else {
                        state.getExecutionStack().push(this);
                    }
                } else {
                    throw new StatementExecutionException("Acquire: Index not an address in the semaphore table");
                }
            } else {
                throw new StatementExecutionException("Acquire: Index must be of int type!");
            }
        } else {
            throw new StatementExecutionException("Acquire: Index not in symbol table!");
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
        return new AcquirePermitStatement(variable);
    }

    @Override
    public String toString() {
        return String.format("acquire(%s)", variable);
    }
}
