package model.types;

import model.values.IValue;
import model.values.RefValue;

public class RefType implements IType{
    private final IType innerType;

    public RefType(IType t)
    {
        innerType = t;
    }

    public IType getInnerType()
    {
        return innerType;
    }

    public boolean equals(Object anotherType)
    {
        if(anotherType instanceof RefType)
            return innerType.equals(((RefType) anotherType).getInnerType());
        else return false;
    }

    public IType deepcopy()
    {
        return new RefType(innerType.deepcopy());
    }

    @Override
    public IValue defaultValue() {
        return new RefValue(0, innerType);
    }

    @Override
    public String toString() {
        return "RefType{" +
                "innerType=" + innerType +
                '}';
    }
}
