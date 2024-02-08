package model.types;

import model.values.BoolValue;
import model.values.IValue;
import model.values.IntValue;

public class IntType implements IType{
    public boolean equals(Object other){return other instanceof IntType;}

    public String toString()
    {
        return "integer";
    }

    public IValue defaultValue()
    {
        return new IntValue(0);
    }

    @Override
    public IType deepcopy() {
        return new IntType();
    }
}
