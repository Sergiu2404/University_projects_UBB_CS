package model.values;

import model.types.BoolType;
import model.types.IType;
import model.types.IntType;

public class BoolValue implements IValue{
    private final boolean val;

    public BoolValue(boolean v)
    {
        this.val=v;
    }

    public boolean getValue()
    {
        return this.val;
    }

    public boolean equals(Object another)
    {
        if(!(another instanceof BoolValue))
            return false;
        BoolValue castBool = (BoolValue) another;
        return this.val == castBool.getValue();
    }
    public IType getType()
    {
        return new BoolType();
    }

    public String toString()
    {
        return "" + this.val;
    }
}
