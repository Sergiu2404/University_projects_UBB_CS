package com.example.a7_gui.model.expressions;

import com.example.a7_gui.exceptions.ADTException;
import com.example.a7_gui.exceptions.DivisionByZero;
import com.example.a7_gui.exceptions.ExpressionEvaluationException;
import com.example.a7_gui.model.types.Type;
import com.example.a7_gui.model.utils.MyIDictionary;
import com.example.a7_gui.model.utils.MyIHeap;
import com.example.a7_gui.model.values.Value;


public interface IExpression {
    Type typeCheck(MyIDictionary<String, Type> typeEnv) throws ExpressionEvaluationException, ADTException;
    Value evaluate(MyIDictionary<String, Value> table, MyIHeap heap) throws ExpressionEvaluationException, ADTException, DivisionByZero;
    IExpression deepCopy();
}
