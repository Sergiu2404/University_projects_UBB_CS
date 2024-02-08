package model.values;

import model.types.IType;
import model.types.RefType;

public class RefValue implements IValue{
    private final int address;
    private final IType locationType;

    public RefValue(int addr, IType t)
    {
        address = addr;
        locationType = t;
    }

    public int getAddress() {
        return address;
    }

    public IType getLocationType() {
        return locationType;
    }

    public IType getType()
    {
        return new RefType(locationType);
    }

    public IValue deepcopy()
    {
        return new RefValue(address, locationType.deepcopy());
    }

    @Override
    public String toString() {
        return "RefValue{" +
                "address=" + address +
                ", locationType=" + locationType +
                '}';
    }
}
