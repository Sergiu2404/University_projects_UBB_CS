package com.example.a7_gui.model.stmt;

import com.example.a7_gui.exceptions.ADTException;
import com.example.a7_gui.exceptions.DivisionByZero;
import com.example.a7_gui.exceptions.ExpressionEvaluationException;
import com.example.a7_gui.exceptions.StatementExecutionException;
import com.example.a7_gui.model.programState.ProgramState;
import com.example.a7_gui.model.types.IntType;
import com.example.a7_gui.model.types.Type;
import com.example.a7_gui.model.utils.MyICountdownLatchTable;
import com.example.a7_gui.model.utils.MyIDictionary;
import com.example.a7_gui.model.values.IntValue;
import com.example.a7_gui.model.values.Value;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class AwaitStatement implements IStatement{
    private final String variable;
    private static final Lock lock = new ReentrantLock();

    public AwaitStatement(String var)
    {
        this.variable = var;
    }

    @Override
    public ProgramState execute(ProgramState state) throws StatementExecutionException, ExpressionEvaluationException, ADTException, DivisionByZero {
        lock.lock();
        MyIDictionary<String, Value> symTable = state.getSymTable();
        MyICountdownLatchTable countdownLatchTable = state.getCountdownLatchTable();

        if(symTable.isDefined(variable))
        {
            IntValue ind = (IntValue) symTable.lookUp(variable);
            int index = ind.getValue();

            if(countdownLatchTable.containsKey(index))
            {
                if(countdownLatchTable.get(index) != 0)
                    state.getExeStack().push(this);
            } else throw new StatementExecutionException("Await: Index not found in the latches table");
        }else throw new StatementExecutionException(String.format("Await: Variable %s is not defined", variable));

        lock.unlock();

        return null;
    }

    @Override
    public MyIDictionary<String, Type> typeCheck(MyIDictionary<String, Type> typeEnv) throws StatementExecutionException, ExpressionEvaluationException, ADTException {
        if(typeEnv.lookUp(variable).equals(new IntType()))
            return typeEnv;
        else throw new StatementExecutionException(String.format("Await: variable %s should be of type int", variable));
    }

    @Override
    public IStatement deepCopy() {
        return new AwaitStatement(variable);
    }

    public String toString()
    {
        return String.format("await(%s)", variable);
    }
}
