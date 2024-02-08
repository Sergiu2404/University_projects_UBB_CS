package model.expressions;

import exceptions.DivisionByZero;
import exceptions.ExpressionEvaluationException;
import model.programState.ProgramState;
import model.utils.MyIDictionary;
import model.values.IValue;

public interface IExpression{
    IValue eval(MyIDictionary<String, IValue> symTbl) throws ExpressionEvaluationException, DivisionByZero;
    IExpression deepcopy();
}
