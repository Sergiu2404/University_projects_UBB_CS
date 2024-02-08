package model.expressions;

import exceptions.ADTException;
import exceptions.DivisionByZero;
import exceptions.ExpressionEvaluationException;
import exceptions.HeapException;
import model.programState.ProgramState;
import model.types.Type;
import model.utils.MyIDictionary;
import model.utils.MyIHeap;
import model.values.Value;


public interface IExpression {
    Type typeCheck(MyIDictionary<String, Type> typeEnv) throws ExpressionEvaluationException, ADTException;
    Value eval(MyIDictionary<String, Value> table, MyIHeap heap) throws ExpressionEvaluationException, ADTException, DivisionByZero;
    IExpression deepCopy();
}
