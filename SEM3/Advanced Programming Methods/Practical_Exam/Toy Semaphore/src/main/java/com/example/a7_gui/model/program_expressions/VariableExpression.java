package com.example.a7_gui.model.program_expressions;

import com.example.a7_gui.exceptions.ADTException;
import com.example.a7_gui.exceptions.ExpressionEvaluationException;
import com.example.a7_gui.model.program_types.Type;
import com.example.a7_gui.model.my_utils.MyIDictionary;
import com.example.a7_gui.model.my_utils.MyIHeap;
import com.example.a7_gui.model.values.Value;

public class VariableExpression implements IExpression {
    String key;

    public VariableExpression(String key) {
        this.key = key;
    }

    @Override
    public Type typeCheck(MyIDictionary<String, Type> typeEnv) throws ExpressionEvaluationException, ADTException {
        return typeEnv.lookUp(key);
    }

    @Override
    public Value evaluateExpression(MyIDictionary<String, Value> table, MyIHeap heap) throws ADTException {
        return table.lookUp(key);
    }

    @Override
    public IExpression deepCopy() {
        return new VariableExpression(key);
    }

    @Override
    public String toString() {
        return key;
    }
}
