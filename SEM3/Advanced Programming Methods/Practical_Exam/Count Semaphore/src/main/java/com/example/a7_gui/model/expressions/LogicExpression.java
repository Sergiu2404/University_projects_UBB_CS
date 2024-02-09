package com.example.a7_gui.model.expressions;


import com.example.a7_gui.exceptions.DataStructureException;
import com.example.a7_gui.exceptions.ArithmeticException;
import com.example.a7_gui.exceptions.EvaluationException;
import com.example.a7_gui.model.types.BoolType;
import com.example.a7_gui.model.types.Type;
import com.example.a7_gui.model.utils.MyIDictionary;
import com.example.a7_gui.model.utils.MyIHeap;
import com.example.a7_gui.model.values.BoolValue;
import com.example.a7_gui.model.values.Value;

import java.util.Objects;

public class LogicExpression implements IExpression {
    IExpression expression1;
    IExpression expression2;
    String operation;

    public LogicExpression(String operation, IExpression expression1, IExpression expression2) {
        this.expression1 = expression1;
        this.expression2 = expression2;
        this.operation = operation;
    }

    @Override
    public Type typeCheck(MyIDictionary<String, Type> typeEnv) throws EvaluationException, DataStructureException {
        Type type1, type2;
        type1 = expression1.typeCheck(typeEnv);
        type2 = expression2.typeCheck(typeEnv);
        if (type1.equals(new BoolType())) {
            if (type2.equals(new BoolType())) {
                return new BoolType();
            } else
                throw new EvaluationException("Second operand is not a boolean.");
        } else
            throw new EvaluationException("First operand is not a boolean.");

    }

    @Override
    public Value evaluateExpression(MyIDictionary<String, Value> table, MyIHeap heap) throws EvaluationException, DataStructureException, ArithmeticException {
        Value value1, value2;
        value1 = this.expression1.evaluateExpression(table, heap);
        if (value1.getType().equals(new BoolType())) {
            value2 = this.expression2.evaluateExpression(table, heap);
            if (value2.getType().equals(new BoolType())) {
                BoolValue bool1 = (BoolValue) value1;
                BoolValue bool2 = (BoolValue) value2;
                boolean b1, b2;
                b1 = bool1.getValue();
                b2 = bool2.getValue();
                if (Objects.equals(this.operation, "&&")) {
                    return new BoolValue(b1 && b2);
                } else if (Objects.equals(this.operation, "||")) {
                    return new BoolValue(b1 || b2);
                }
            } else {
                throw new EvaluationException("Second operand is not a boolean.");
            }
        } else {
            throw new EvaluationException("First operand is not a boolean.");
        }
        return null;
    }

    @Override
    public IExpression deepCopy() {
        return new LogicExpression(operation, expression1.deepCopy(), expression2.deepCopy());
    }
}


