package model.types;

import model.values.BoolValue;
import model.values.IValue;

public class BoolType implements IType{
    public boolean equals(Object other){return other instanceof BoolType;}

    public String toString()
    {
        return "boolean";
    }

    public IValue defaultValue()
    {
        return new BoolValue(false);
    }
}
