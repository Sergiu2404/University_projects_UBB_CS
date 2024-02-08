package model.expressions;

import exceptions.DivisionByZero;
import exceptions.ExpressionEvaluationException;
import exceptions.HeapException;
import model.programState.ProgramState;
import model.utils.MyIDictionary;
import model.utils.MyIHeap;
import model.values.IValue;

public interface IExpression{
    IValue eval(MyIDictionary<String, IValue> symTbl, MyIHeap heap) throws ExpressionEvaluationException, DivisionByZero, HeapException;
    IExpression deepcopy();
}
