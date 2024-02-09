package com.example.a7_gui.model.expressions;

import com.example.a7_gui.exceptions.DataStructureException;
import com.example.a7_gui.exceptions.EvaluationException;
import com.example.a7_gui.model.types.Type;
import com.example.a7_gui.model.utils.MyIDictionary;
import com.example.a7_gui.model.utils.MyIHeap;
import com.example.a7_gui.model.values.Value;

public class VariableExpression implements IExpression {
    String key;

    public VariableExpression(String key) {
        this.key = key;
    }

    @Override
    public Type typeCheck(MyIDictionary<String, Type> typeEnv) throws EvaluationException, DataStructureException {
        return typeEnv.lookUp(key);
    }

    @Override
    public Value evaluateExpression(MyIDictionary<String, Value> table, MyIHeap heap) throws DataStructureException {
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
