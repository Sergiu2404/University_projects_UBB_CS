package com.example.a7_gui.model.expressions;

import com.example.a7_gui.my_exceptions.DataStructureException;
import com.example.a7_gui.my_exceptions.ArithmeticException;
import com.example.a7_gui.my_exceptions.ExpressionEvaluationException;
import com.example.a7_gui.model.types.RefType;
import com.example.a7_gui.model.types.Type;
import com.example.a7_gui.model.my_data_structures.MyIDictionary;
import com.example.a7_gui.model.my_data_structures.MyIHeap;
import com.example.a7_gui.model.my_data_structures.MyISymbolTable;
import com.example.a7_gui.model.values.RefValue;
import com.example.a7_gui.model.values.Value;

public class ReadHeapExpression implements IExpression{
    private final IExpression expression;

    public ReadHeapExpression(IExpression expression) {
        this.expression = expression;
    }

    @Override
    public Type typeCheck(MyIDictionary<String, Type> typeEnv) throws ExpressionEvaluationException, DataStructureException {
        Type type = expression.typeCheck(typeEnv);
        if (type instanceof RefType) {
            RefType refType = (RefType) type;
            return refType.getInner();
        } else
            throw new ExpressionEvaluationException("ReadHeap: Argument must be of type ref");

    }

    @Override
    public Value expressionEvaluation(MyISymbolTable<String, Value> symTable, MyIHeap heap) throws ExpressionEvaluationException, DataStructureException, ArithmeticException {
        Value value = expression.expressionEvaluation(symTable, heap);
        if (value instanceof RefValue) {
            RefValue refValue = (RefValue) value;
            if (heap.containsKey(refValue.getAddress()))
                return heap.get(refValue.getAddress());
            else
                throw new ExpressionEvaluationException("ReadH: The address is not defined on the heap!");
        } else
            throw new ExpressionEvaluationException(String.format("ReadHeap: %s not of RefType", value));
    }

    @Override
    public IExpression deepCopy() {
        return new ReadHeapExpression(expression.deepCopy());
    }

    @Override
    public String toString() {
        return String.format("rHeap(%s)", expression);
    }
}