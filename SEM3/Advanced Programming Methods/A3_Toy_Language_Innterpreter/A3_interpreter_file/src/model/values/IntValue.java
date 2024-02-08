package model.values;

import model.types.IType;
import model.types.IntType;

public class IntValue implements IValue{
    private final int val;

    public IntValue(int v)
    {
        this.val=v;
    }

    public boolean equals(Object another)
    {
        if(!(another instanceof IntValue))
            return false;
        IntValue castBool = (IntValue) another;
        return this.val == castBool.getValue();
    }

    public int getValue()
    {
        return this.val;
    }

    public IType getType()
    {
        return new IntType();
    }

    public String toString()
    {
        return ""+this.val;
    }

}
