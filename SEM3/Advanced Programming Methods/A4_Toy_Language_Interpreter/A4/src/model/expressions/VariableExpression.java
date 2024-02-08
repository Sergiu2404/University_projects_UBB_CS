package model.expressions;

import model.utils.MyIDictionary;
import model.utils.MyIHeap;
import model.values.IValue;

public class VariableExpression implements IExpression{
    private final String variableExp;

    public VariableExpression(String v)
    {
        this.variableExp=v;
    }

    public IValue eval(MyIDictionary<String, IValue> symTbl, MyIHeap heap)
    {
        return symTbl.lookup(variableExp);
    }

    public IExpression deepcopy()
    {
        return new VariableExpression(variableExp);
    }

    @Override
    public String toString() {
        return "VariableExpression{" +
                "variableExp='" + variableExp + '\'' +
                '}';
    }
}
