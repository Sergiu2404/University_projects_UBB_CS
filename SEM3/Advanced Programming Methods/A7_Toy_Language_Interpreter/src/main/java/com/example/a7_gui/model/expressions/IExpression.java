package com.example.a7_gui.model.expressions;

import com.example.a7_gui.my_exceptions.DataStructureException;
import com.example.a7_gui.my_exceptions.DivisionByZero;
import com.example.a7_gui.my_exceptions.ExpressionEvaluationException;
import com.example.a7_gui.model.types.Type;
import com.example.a7_gui.model.my_data_structures.MyIDictionary;
import com.example.a7_gui.model.my_data_structures.MyIHeap;
import com.example.a7_gui.model.my_data_structures.MyISymbolTable;
import com.example.a7_gui.model.values.Value;


public interface IExpression {
    Type typeCheck(MyIDictionary<String, Type> typeEnv) throws ExpressionEvaluationException, DataStructureException;
    Value eval(MyISymbolTable<String, Value> table, MyIHeap heap) throws ExpressionEvaluationException, DataStructureException, DivisionByZero;
    IExpression deepCopy();
}
