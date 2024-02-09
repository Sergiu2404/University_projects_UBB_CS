package com.example.a7_gui.model.statements;

import com.example.a7_gui.model.expressions.IExpression;
import com.example.a7_gui.model.my_data_structures.MyIDictionary;
import com.example.a7_gui.model.my_data_structures.MyIHeap;
import com.example.a7_gui.model.my_data_structures.MyISemaphore;
import com.example.a7_gui.model.my_data_structures.MyISymbolTable;
import com.example.a7_gui.model.program_state.ProgramState;
import com.example.a7_gui.model.types.IntType;
import com.example.a7_gui.model.types.Type;
import com.example.a7_gui.model.values.IntValue;
import com.example.a7_gui.model.values.Value;
import com.example.a7_gui.my_exceptions.ArithmeticException;
import com.example.a7_gui.my_exceptions.DataStructureException;
import com.example.a7_gui.my_exceptions.ExpressionEvaluationException;
import com.example.a7_gui.my_exceptions.StatementExecutionException;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class CreateCountSemaphore implements IStatement{
    private final String variable;
    private final IExpression expression;
    private static final Lock lock = new ReentrantLock();

    public CreateCountSemaphore(String variable, IExpression expression) {
        this.variable = variable;
        this.expression = expression;
    }
    @Override
    public ProgramState executeStatement(ProgramState state) throws StatementExecutionException, ExpressionEvaluationException, ArithmeticException, DataStructureException, ArithmeticException {
        lock.lock();

        MyISymbolTable<String, Value> symbolTable = state.getSymbolTable();
        MyIHeap heap = state.getHeapTable();
        MyISemaphore semaphoreTable = state.getSemaphoreTable();

        IntValue nr = (IntValue) (expression.expressionEvaluation(symbolTable, heap));
        int number = nr.getValue();
        int freeAddress = semaphoreTable.getFreeAddress();

        semaphoreTable.put(freeAddress, new Pair<>(number, new ArrayList<>()));

        if (symbolTable.isDefined(variable) && symbolTable.lookUp(variable).getType().equals(new IntType()))
            symbolTable.put(variable, new IntValue(freeAddress));
        else
            throw new StatementExecutionException(String.format("CreateSemaphore: Variable %s not defined", variable));

        lock.unlock();

        return null;
    }

    @Override
    public MyIDictionary<String, Type> typeCheck(MyIDictionary<String, Type> typeEnv) throws StatementExecutionException, DataStructureException, ExpressionEvaluationException {

        if (typeEnv.lookUp(variable).equals(new IntType())) {
            if (expression.typeCheck(typeEnv).equals(new IntType()))
                return typeEnv;
            else
                throw new StatementExecutionException("Create Semaphore: Number must be of type int");
        } else {
            throw new StatementExecutionException(String.format("CreateSem: Variable %s must be of type int", variable));
        }

    }

    @Override
    public IStatement deepCopy() {
        return new CreateCountSemaphore(variable, expression.deepCopy());
    }

    @Override
    public String toString() {
        return String.format("createNewSem(%s, %s)", variable, expression);
    }
}
