package com.example.a7_gui.model.program_expressions;

import com.example.a7_gui.exceptions.DataStructureException;
import com.example.a7_gui.exceptions.ArithmeticException;
import com.example.a7_gui.exceptions.EvaluationException;
import com.example.a7_gui.model.program_types.Type;
import com.example.a7_gui.model.my_utils.MyIDictionary;
import com.example.a7_gui.model.my_utils.MyIHeap;
import com.example.a7_gui.model.program_values.Value;


public interface IExpression {
    Type typeCheck(MyIDictionary<String, Type> typeEnv) throws EvaluationException, DataStructureException;
    Value evaluateExpression(MyIDictionary<String, Value> table, MyIHeap heap) throws EvaluationException, DataStructureException, ArithmeticException;
    IExpression deepCopy();
}
