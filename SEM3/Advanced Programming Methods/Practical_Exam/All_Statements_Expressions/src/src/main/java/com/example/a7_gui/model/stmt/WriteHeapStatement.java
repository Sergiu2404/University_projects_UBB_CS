package com.example.a7_gui.model.stmt;

import com.example.a7_gui.exceptions.ADTException;
import com.example.a7_gui.exceptions.DivisionByZero;
import com.example.a7_gui.exceptions.ExpressionEvaluationException;
import com.example.a7_gui.exceptions.StatementExecutionException;
import com.example.a7_gui.model.expressions.IExpression;
import com.example.a7_gui.model.programState.ProgramState;
import com.example.a7_gui.model.types.RefType;
import com.example.a7_gui.model.types.Type;
import com.example.a7_gui.model.utils.MyIDictionary;
import com.example.a7_gui.model.utils.MyIHeap;
import com.example.a7_gui.model.values.RefValue;
import com.example.a7_gui.model.values.Value;

public class WriteHeapStatement implements IStatement{
    private final String varName;
    private final IExpression expression;

    public WriteHeapStatement(String varName, IExpression expression) {
        this.varName = varName;
        this.expression = expression;
    }

    @Override
    public ProgramState execute(ProgramState state) throws StatementExecutionException, ExpressionEvaluationException, ADTException, DivisionByZero {
        MyIDictionary<String, Value> symTable = state.getSymTable();
        MyIHeap heap = state.getHeap();
        if (symTable.isDefined(varName)) {
            Value value = symTable.lookUp(varName);
            if (value.getType() instanceof RefType) {
                RefValue refValue = (RefValue) value;
                if (heap.containsKey(refValue.getAddress())) {
                    Value evaluated = expression.evaluate(symTable, heap);
                    if (evaluated.getType().equals(refValue.getLocationType())) {
                        heap.update(refValue.getAddress(), evaluated);
                        state.setHeap(heap);
                    } else
                        throw new StatementExecutionException(String.format("Write heap (expression %s type does not match %s type)", evaluated, refValue.getLocationType()));
                } else
                    throw new StatementExecutionException(String.format("Write heap (Ref value %s not defined)", value));
            } else
                throw new StatementExecutionException(String.format("Write heap (%s must be RefType)", value));
        } else
            throw new StatementExecutionException(String.format("Write heap (%s not present in the heap table)", varName));
        return null;
    }

    @Override
    public MyIDictionary<String, Type> typeCheck(MyIDictionary<String, Type> typeEnv) throws StatementExecutionException, ExpressionEvaluationException, ADTException {
        if (typeEnv.lookUp(varName).equals(new RefType(expression.typeCheck(typeEnv))))
            return typeEnv;
        else
            throw new StatementExecutionException("WriteHeap: right hand side and left hand side have different types.");
    }

    @Override
    public IStatement deepCopy() {
        return new WriteHeapStatement(varName, expression.deepCopy());
    }

    @Override
    public String toString() {
        return String.format("WriteHeap(%s, %s)", varName, expression);
    }
}