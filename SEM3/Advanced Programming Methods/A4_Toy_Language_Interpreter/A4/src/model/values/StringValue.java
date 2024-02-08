package model.values;

import model.types.IType;
import model.types.IntType;
import model.types.StringType;

public class StringValue implements IValue{
    private final String val;

    public StringValue(String v)
    {
        this.val=v;
    }

    public boolean equals(Object another)
    {
        if(!(another instanceof StringValue))
            return false;
        StringValue stringCast = (StringValue) another;
        return this.val.equals(stringCast.getValue());
    }

    @Override
    public IValue deepcopy() {
        return new StringValue(val);
    }

    public String getValue(){return this.val;}
    public IType getType()
    {
        return new StringType();
    }

    public String toString()
    {
        return ""+this.val;
    }
}
