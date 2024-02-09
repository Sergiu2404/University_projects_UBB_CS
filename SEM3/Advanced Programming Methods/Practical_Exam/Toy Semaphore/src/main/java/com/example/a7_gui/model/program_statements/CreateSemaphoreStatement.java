package com.example.a7_gui.model.program_statements;

import com.example.a7_gui.exceptions.ADTException;
import com.example.a7_gui.exceptions.DivisionByZero;
import com.example.a7_gui.exceptions.ExpressionEvaluationException;
import com.example.a7_gui.exceptions.StatementExecutionException;
import com.example.a7_gui.model.program_expressions.IExpression;
import com.example.a7_gui.model.programState.ProgramState;
import com.example.a7_gui.model.program_types.IntType;
import com.example.a7_gui.model.program_types.Type;
import com.example.a7_gui.model.my_utils.AditionalStructureTuple;
import com.example.a7_gui.model.my_utils.MyIDictionary;
import com.example.a7_gui.model.my_utils.MyIHeap;
import com.example.a7_gui.model.my_utils.MyISemaphore;
import com.example.a7_gui.model.values.IntValue;
import com.example.a7_gui.model.values.Value;

import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class CreateSemaphoreStatement implements IStatement{
    private final String variable;
    private final IExpression expression1;
    private final IExpression expression2;
    private static final Lock lock = new ReentrantLock();

    public CreateSemaphoreStatement(String variable, IExpression expression1, IExpression expression2) {
        this.variable = variable;
        this.expression1 = expression1;
        this.expression2 = expression2;
    }
    @Override
    public ProgramState execute(ProgramState state) throws StatementExecutionException, ExpressionEvaluationException, DivisionByZero, ADTException {
        lock.lock();

        MyIDictionary<String, Value> symTable = state.getSymbolTable();
        MyIHeap heap = state.getHeapMemory();
        MyISemaphore semaphoreTable = state.getSemaphore();

        IntValue initValue1 = (IntValue) (expression1.evaluateExpression(symTable, heap));
        IntValue initValue2 = (IntValue) (expression2.evaluateExpression(symTable, heap));

        int number1 = initValue1.getValue();
        int number2 = initValue2.getValue();
        int freeAddress = semaphoreTable.getFreeAddress();

        semaphoreTable.put(freeAddress, new AditionalStructureTuple<>(number1, new ArrayList<>(), number2));

        if (symTable.isDefined(variable) && symTable.lookUp(variable).getType().equals(new IntType()))
            symTable.put(variable, new IntValue(freeAddress));
        else
            throw new StatementExecutionException(String.format("CreateSemaphore: Variable %s in not defined in the symbol table", variable));

        lock.unlock();
        return null;
    }

    @Override
    public MyIDictionary<String, Type> typeCheck(MyIDictionary<String, Type> typeEnv) throws StatementExecutionException, ADTException, ExpressionEvaluationException {
        return typeEnv;
    }

    @Override
    public IStatement deepCopy() {
        return new CreateSemaphoreStatement(variable, expression1.deepCopy(), expression2.deepCopy());
    }

    @Override
    public String toString() {
        return String.format("createSemaphore(%s, %s, %s)", variable, expression1, expression2);
    }
}
