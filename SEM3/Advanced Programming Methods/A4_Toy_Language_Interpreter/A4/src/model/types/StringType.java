package model.types;

import model.values.IValue;
import model.values.StringValue;

public class StringType implements IType{
    public boolean equals(Object other){return other instanceof StringType;}

    public String toString()
    {
        return "string";
    }

    @Override
    public IValue defaultValue()
    {
        return new StringValue("");
    }

    @Override
    public IType deepcopy() {
        return new StringType();
    }
}
