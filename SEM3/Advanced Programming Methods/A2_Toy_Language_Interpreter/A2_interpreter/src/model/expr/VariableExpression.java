package model.expr;

import exceptions.MyException;
import model.utils.MyIDictionary;
import model.value.IValue;

public class VariableExpression implements IExpression {
    private final String varIdExp;

    public VariableExpression(String id)
    {
        this.varIdExp=id;
    }

    public String getId() {
        return varIdExp;
    }

    public IValue eval(MyIDictionary<String,IValue> symTbl)
    {
        return symTbl.lookup(varIdExp);
    }

    public IExpression deepcopy()
    {
        return new VariableExpression(varIdExp);
    }



    @Override
    public String toString() {
        return "VariableExpression{" +
                "varIdExp=" + varIdExp + '\'' +
                '}';
    }
}
