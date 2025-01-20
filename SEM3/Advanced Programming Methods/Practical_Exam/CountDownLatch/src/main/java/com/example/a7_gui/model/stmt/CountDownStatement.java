package com.example.a7_gui.model.stmt;

import com.example.a7_gui.exceptions.ADTException;
import com.example.a7_gui.exceptions.DivisionByZero;
import com.example.a7_gui.exceptions.ExpressionEvaluationException;
import com.example.a7_gui.exceptions.StatementExecutionException;
import com.example.a7_gui.model.expressions.ValueExpression;
import com.example.a7_gui.model.programState.ProgramState;
import com.example.a7_gui.model.types.IntType;
import com.example.a7_gui.model.types.Type;
import com.example.a7_gui.model.utils.MyICountdownLatchTable;
import com.example.a7_gui.model.utils.MyIDictionary;
import com.example.a7_gui.model.values.IntValue;
import com.example.a7_gui.model.values.Value;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class CountDownStatement implements IStatement{
    private final String variable;
    private static final Lock lock = new ReentrantLock();

    public CountDownStatement(String var)
    {
        this.variable = var;
    }

    @Override
    public ProgramState execute(ProgramState state) throws StatementExecutionException, ExpressionEvaluationException, ADTException, DivisionByZero {
        lock.lock();
        MyIDictionary<String, Value> symTbl = state.getSymTable();
        MyICountdownLatchTable countdownLatchTable = state.getCountdownLatchTable();

        if(symTbl.isDefined(variable))
        {
            IntValue ind = (IntValue) symTbl.lookUp(variable);
            int index = ind.getValue();

            if(countdownLatchTable.containsKey(index))
            {
                if(countdownLatchTable.get(index) > 0) //update latch values
                    countdownLatchTable.put(index, countdownLatchTable.get(index) - 1);
                state.getExeStack().push(new PrintStatement(new ValueExpression(new IntValue(state.getId()))));
            } else throw new StatementExecutionException("Countdown: Index not found in the latch table");
        } else throw new StatementExecutionException(String.format("Countdown: Variable %s not defined in symbol table", variable));

        lock.unlock();

        return null;
    }

    @Override
    public MyIDictionary<String, Type> typeCheck(MyIDictionary<String, Type> typeEnv) throws StatementExecutionException, ExpressionEvaluationException, ADTException {
        if(typeEnv.lookUp(variable).equals(new IntType()))
            return typeEnv;
        else throw new StatementExecutionException(String.format("Countdown: %s shoul be of type int", variable));
    }

    @Override
    public IStatement deepCopy() {
        return new CountDownStatement(variable);
    }

    public String toString()
    {
        return String.format("countdown(%s)", variable);
    }
}
