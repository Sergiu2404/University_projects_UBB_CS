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

public class AcquirePermitsStatement implements IStatement{
    private final String variable;
    private static final Lock lock = new ReentrantLock();

    public AcquirePermitsStatement(String variable) {
        this.variable = variable;
    }

    @Override
    public ProgramState executeStatement(ProgramState state) throws DataStructureException, StatementExecutionException {
        lock.lock();

        MyISymbolTable<String, Value> symboltable = state.getSymbolTable();
        MyISemaphore semaphoreTable = state.getSemaphoreTable();

        if (symboltable.isDefined(variable)) {

            if (symboltable.lookUp(variable).getType().equals(new IntType())){

                IntValue fi = (IntValue) symboltable.lookUp(variable);
                int foundIndex = fi.getValue();

                if (semaphoreTable.getSemaphoreTable().containsKey(foundIndex)) {
                    Pair<Integer, List<Integer>> foundSemaphore = semaphoreTable.get(foundIndex);

                    int NL = foundSemaphore.getValue().size();
                    int N1 = foundSemaphore.getKey();

                    if (N1 > NL) {

                        if (!foundSemaphore.getValue().contains(state.getId())) {
                            foundSemaphore.getValue().add(state.getId());
                            semaphoreTable.put(foundIndex, new Pair<>(N1, foundSemaphore.getValue()));
                        }

                    } else {
                        state.getExecutionStack().push(this);
                    }
                } else {
                    throw new StatementExecutionException("Acquire: Address must be a key in the semaphore table");
                }
            } else {
                throw new StatementExecutionException("Acquire: Address must be of int type");
            }
        } else {
            throw new StatementExecutionException("Acquire: Address is not defined in symbol table");
        }
        lock.unlock();
        return null;
    }

    @Override
    public MyIDictionary<String, Type> typeCheck(MyIDictionary<String, Type> typeEnv) throws DataStructureException, StatementExecutionException {
        if (typeEnv.lookUp(variable).equals(new IntType())) {
            return typeEnv;
        } else {
            throw new StatementExecutionException("Acquire: SymbAdressol not found in the symbol table");
        }
    }

    @Override
    public IStatement deepCopy() {
        return new AcquirePermitsStatement(variable);
    }

    @Override
    public String toString() {
        return String.format("acquirePermit(%s)", variable);
    }
}
