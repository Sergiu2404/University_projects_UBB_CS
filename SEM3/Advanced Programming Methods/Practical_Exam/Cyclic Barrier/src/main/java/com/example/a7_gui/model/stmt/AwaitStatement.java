package com.example.a7_gui.model.stmt;

import com.example.a7_gui.exceptions.ADTException;
import com.example.a7_gui.exceptions.DivisionByZero;
import com.example.a7_gui.exceptions.ExpressionEvaluationException;
import com.example.a7_gui.exceptions.StatementExecutionException;
import com.example.a7_gui.model.programState.ProgramState;
import com.example.a7_gui.model.types.IntType;
import com.example.a7_gui.model.types.Type;
import com.example.a7_gui.model.utils.MyIBarrierTable;
import com.example.a7_gui.model.utils.MyIDictionary;
import com.example.a7_gui.model.utils.MyISymbolTable;
import com.example.a7_gui.model.values.IntValue;
import com.example.a7_gui.model.values.Value;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class AwaitStatement implements IStatement{
    private final String variable;
    private static final Lock lock = new ReentrantLock();

    public AwaitStatement(String var) {
        this.variable = var;
    }


    @Override
    public ProgramState execute(ProgramState state) throws StatementExecutionException, ExpressionEvaluationException, ADTException, DivisionByZero {
        lock.lock();

        MyISymbolTable<String, Value> symTable = state.getSymTable();
        MyIBarrierTable barrierTable = state.getBarrierTable();

        if (symTable.isDefined(variable)) {

            IntValue f = (IntValue) symTable.lookUp(variable);
            int foundIndex = f.getValue();

            if (barrierTable.containsKey(foundIndex)) {

                Pair<Integer, List<Integer>> currentBarriers = barrierTable.get(foundIndex);
                ArrayList<Integer> list = (ArrayList<Integer>) currentBarriers.getValue();

                int length = currentBarriers.getValue().size();
                int currentKey = currentBarriers.getKey();

                if (currentKey > length) {

                    if (list.contains(state.getId()))
                        state.getExeStack().push(this);
                    else {

                        list.add(state.getId());
                        barrierTable.put(foundIndex, new Pair<>(currentKey, list));
                        state.setBarrierTable(barrierTable);

                    }
                }
            } else {
                throw new StatementExecutionException("Await: Address not in Barrier Table");
            }
        } else {
            throw new StatementExecutionException("Await: Variable not in symbol table");
        }
        lock.unlock();
        return null;
    }

    @Override
    public MyIDictionary<String, Type> typeCheck(MyIDictionary<String, Type> typeEnv) throws StatementExecutionException, ExpressionEvaluationException, ADTException {
        if (typeEnv.lookUp(variable).equals(new IntType()))
            return typeEnv;
        else
            throw new StatementExecutionException("Var is not of type int!");
    }

    @Override
    public IStatement deepCopy() {
        return new AwaitStatement(variable);
    }

    @Override
    public String toString() {
        return String.format("barrierAwait(%s)", variable);
    }
}
