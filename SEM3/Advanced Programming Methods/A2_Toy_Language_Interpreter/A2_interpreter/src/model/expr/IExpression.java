package model.expr;

import exceptions.DivisionByZero;
import exceptions.ExpressionEvaluationException;
import exceptions.MyException;
import model.utils.MyIDictionary;
import model.value.IValue;

public interface IExpression {
    IValue eval(MyIDictionary<String, IValue> symT) throws ExpressionEvaluationException, DivisionByZero;

    IExpression deepcopy();
}
