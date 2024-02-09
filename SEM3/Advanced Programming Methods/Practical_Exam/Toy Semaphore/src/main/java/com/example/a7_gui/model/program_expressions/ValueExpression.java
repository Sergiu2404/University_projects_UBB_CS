package com.example.a7_gui.model.program_expressions;

import com.example.a7_gui.exceptions.ExpressionEvaluationException;
import com.example.a7_gui.model.program_types.Type;
import com.example.a7_gui.model.my_utils.MyIDictionary;
import com.example.a7_gui.model.my_utils.MyIHeap;
import com.example.a7_gui.model.values.Value;

public class ValueExpression implements IExpression {
    Value value;

    public ValueExpression(Value value) {
        this.value = value;
    }

    @Override
    public Type typeCheck(MyIDictionary<String, Type> typeEnv) throws ExpressionEvaluationException {
        return value.getType();
    }

    @Override
    public Value evaluateExpression(MyIDictionary<String, Value> table, MyIHeap heap) {
        return this.value;
    }

    @Override
    public IExpression deepCopy() {
        return new ValueExpression(value);
    }

    @Override
    public String toString() {
        return this.value.toString();
    }
}

