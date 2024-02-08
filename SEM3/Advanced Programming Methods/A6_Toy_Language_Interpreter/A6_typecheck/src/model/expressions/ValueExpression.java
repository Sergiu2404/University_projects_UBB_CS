package model.expressions;

import exceptions.InvalidTypeException;
import model.types.IType;
import model.utils.MyIDictionary;
import model.utils.MyIHeap;
import model.values.IValue;

public class ValueExpression implements IExpression{
    private final IValue value;

    public ValueExpression(IValue value)
    {
        this.value=value;
    }

    @Override
    public IType typecheck(MyIDictionary<String, IType> typeEnv) throws InvalidTypeException {
        return value.getType();
    }

    public IValue eval(MyIDictionary<String,IValue> symtbl, MyIHeap heap)
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
