package model.expressions;

import model.utils.MyIDictionary;
import model.values.IValue;

public class ValueExpression implements IExpression{
    private final IValue value;

    public ValueExpression(IValue value)
    {
        this.value=value;
    }

    public IValue eval(MyIDictionary<String,IValue> symtbl)
    {
        return value;
    }

    public IExpression deepcopy()
    {
        return new ValueExpression(value);
    }

    @Override
    public String toString() {
        return "ValueExpression{" +
                "value=" + value +
                '}';
    }
}
