package model.expr;

import exceptions.DivisionByZero;
import exceptions.ExpressionEvaluationException;
import exceptions.MyException;
import model.utils.MyIDictionary;
import model.value.IValue;

public class ValueExpression implements IExpression {
    private final IValue valExp;

    public ValueExpression(IValue v)
    {
        this.valExp=v;
    }

    public IValue eval(MyIDictionary<String,IValue> symTbl){return valExp;}

    public IExpression deepcopy()
    {
        return new ValueExpression(valExp);
    }

    @Override
    public String toString() {
        return "ValueExpression{" +
                "value=" + valExp +
                '}';
    }
}
