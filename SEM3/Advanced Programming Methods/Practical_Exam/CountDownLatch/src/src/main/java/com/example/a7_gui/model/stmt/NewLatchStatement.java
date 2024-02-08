package com.example.a7_gui.model.stmt;

import com.example.a7_gui.exceptions.*;
import com.example.a7_gui.model.expressions.IExpression;
import com.example.a7_gui.model.programState.ProgramState;
import com.example.a7_gui.model.types.IntType;
import com.example.a7_gui.model.types.Type;
import com.example.a7_gui.model.utils.MyICountdownLatchTable;
import com.example.a7_gui.model.utils.MyIDictionary;
import com.example.a7_gui.model.utils.MyIHeap;
import com.example.a7_gui.model.values.IntValue;
import com.example.a7_gui.model.values.Value;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class NewLatchStatement implements IStatement{
    private final String variable;
    private final IExpression expr;
    private static final Lock lock = new ReentrantLock();

    public NewLatchStatement(String var, IExpression e)
    {
        this.variable = var;
        this.expr = e;
    }

    @Override
    public ProgramState execute(ProgramState state) throws StatementExecutionException, ExpressionEvaluationException, ADTException, DivisionByZero {
        lock.lock();
        MyIDictionary<String, Value> symTbl = state.getSymTable();
        MyIHeap heap = state.getHeap();
        MyICountdownLatchTable countdownLatchTable = state.getCountdownLatchTable();

        IntValue innerNr = (IntValue) (expr.eval(symTbl, heap));
        int innerNumber = innerNr.getValue();
        int freeAddr = countdownLatchTable.getFreeAddress();
        countdownLatchTable.put(freeAddr, innerNumber);

        if(symTbl.isDefined(variable))
            symTbl.put(variable, new IntValue(freeAddr));
        else throw new StatementExecutionException(String.format("NewLatchStatement: %s is not defined in sym table", variable));

        lock.unlock();
        return null;
    }

    @Override
    public MyIDictionary<String, Type> typeCheck(MyIDictionary<String, Type> typeEnv) throws StatementExecutionException, ExpressionEvaluationException, ADTException {
        if(typeEnv.lookUp(variable).equals(new IntType()))
        {
            if (expr.typeCheck(typeEnv).equals(new IntType()))
                return typeEnv;
            else throw new StatementExecutionException("Countdown: Expression must have int type");
        } else throw new StatementExecutionException(String.format("Countdown: %s is not of type int", variable));
    }

    @Override
    public IStatement deepCopy() {
        return new NewLatchStatement(variable, expr.deepCopy());
    }

    @Override
    public String toString() {
        return String.format("newLatch(%s, %s)", variable, expr);
    }
}
