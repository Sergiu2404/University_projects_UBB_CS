package com.example.a7_gui.model.stmt;

import com.example.a7_gui.exceptions.ADTException;
import com.example.a7_gui.exceptions.DivisionByZero;
import com.example.a7_gui.exceptions.ExpressionEvaluationException;
import com.example.a7_gui.exceptions.StatementExecutionException;
import com.example.a7_gui.model.expressions.IExpression;
import com.example.a7_gui.model.programState.ProgramState;
import com.example.a7_gui.model.types.IntType;
import com.example.a7_gui.model.types.Type;
import com.example.a7_gui.model.utils.MyIBarrierTable;
import com.example.a7_gui.model.utils.MyIDictionary;
import com.example.a7_gui.model.utils.MyIHeap;
import com.example.a7_gui.model.utils.MyISymbolTable;
import com.example.a7_gui.model.values.IntValue;
import com.example.a7_gui.model.values.Value;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class CreateBarrierStatement implements IStatement{
    private final IExpression expression;
    private final String variable;
    private static final Lock lock = new ReentrantLock();

    public CreateBarrierStatement(IExpression expression, String var) {
        this.variable = var;
        this.expression = expression;
    }
    @Override
    public ProgramState execute(ProgramState state) throws StatementExecutionException, ExpressionEvaluationException, DivisionByZero, ADTException {
        lock.lock();
        MyISymbolTable<String, Value> symTable = state.getSymTable();
        MyIHeap heap = state.getHeap();
        MyIBarrierTable barrierTable = state.getBarrierTable();
        
        IntValue waitingThreads = (IntValue) (expression.eval(symTable, heap));

        int nr = waitingThreads.getValue();
        int freeAddress = barrierTable.getFreeAddress();

        barrierTable.put(freeAddress, new Pair<>(nr, new ArrayList<>()));

        if (symTable.isDefined(variable))
            symTable.update(variable, new IntValue(freeAddress));
        else
            throw new StatementExecutionException(String.format("NewBarrier: Variable %s is not defined in the symbol table", variable));


        lock.unlock();
        return null;
    }

    @Override
    public MyIDictionary<String, Type> typeCheck(MyIDictionary<String, Type> typeEnv) throws ExpressionEvaluationException, ADTException, StatementExecutionException {
        if (typeEnv.lookUp(variable).equals(new IntType()))
            if (expression.typeCheck(typeEnv).equals(new IntType()))
                return typeEnv;
            else
                throw new StatementExecutionException("Expression is not of type int");
        else
            throw new StatementExecutionException("Variable is not of type int!");
    }

    @Override
    public IStatement deepCopy() {
        return new CreateBarrierStatement(expression.deepCopy(), variable);
    }

    @Override
    public String toString() {
        return String.format("createBarrier(%s, %s)", expression, variable);
    }
}
