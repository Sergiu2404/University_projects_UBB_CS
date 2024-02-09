package com.example.a7_gui.model.stmt;

import com.example.a7_gui.exceptions.DataStructureException;
import com.example.a7_gui.exceptions.ArithmeticException;
import com.example.a7_gui.exceptions.EvaluationException;
import com.example.a7_gui.exceptions.ExecutionException;
import com.example.a7_gui.model.expressions.IExpression;
import com.example.a7_gui.model.programState.ProgramState;
import com.example.a7_gui.model.types.IntType;
import com.example.a7_gui.model.types.Type;
import com.example.a7_gui.model.utils.MyIDictionary;
import com.example.a7_gui.model.utils.MyIHeap;
import com.example.a7_gui.model.utils.MyISemaphore;
import com.example.a7_gui.model.values.IntValue;
import com.example.a7_gui.model.values.Value;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class CreateSemaphoreStatement implements IStatement{
    private final String var;
    private final IExpression expression;
    private static final Lock lock = new ReentrantLock();

    public CreateSemaphoreStatement(String var, IExpression expression) {
        this.var = var;
        this.expression = expression;
    }
    @Override
    public ProgramState execute(ProgramState state) throws ExecutionException, EvaluationException, ArithmeticException, DataStructureException {
        lock.lock();

        MyIDictionary<String, Value> symbolTable = state.getSymbolTable();
        MyIHeap heap = state.getHeapTable();
        MyISemaphore semaphoreTable = state.getSemaphoreTable();

        IntValue nr = (IntValue) (expression.evaluateExpression(symbolTable, heap));
        int number = nr.getValue();
        int freeAddress = semaphoreTable.getFreeAddress();

        semaphoreTable.put(freeAddress, new Pair<>(number, new ArrayList<>()));

        if (symbolTable.isDefined(var) && symbolTable.lookUp(var).getType().equals(new IntType()))
            symbolTable.put(var, new IntValue(freeAddress));
        else
            throw new ExecutionException(String.format("CreateSem: Error for variable %s: not defined/does not have int type", var));
        lock.unlock();
        return null;
    }

    @Override
    public MyIDictionary<String, Type> typeCheck(MyIDictionary<String, Type> typeEnv) throws ExecutionException, DataStructureException, EvaluationException {
        if (typeEnv.lookUp(var).equals(new IntType())) {
            if (expression.typeCheck(typeEnv).equals(new IntType()))
                return typeEnv;
            else
                throw new ExecutionException("Expression is not of int type!");
        } else {
            throw new ExecutionException(String.format("Create Sem: %s is not of type int", var));
        }
    }

    @Override
    public IStatement deepCopy() {
        return new CreateSemaphoreStatement(var, expression.deepCopy());
    }

    @Override
    public String toString() {
        return String.format("createSem(%s, %s)", var, expression);
    }
}
