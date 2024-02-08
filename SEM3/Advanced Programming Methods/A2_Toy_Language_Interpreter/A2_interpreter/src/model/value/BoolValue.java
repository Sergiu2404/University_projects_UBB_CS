package model.value;

import model.type.BoolType;
import model.type.IType;
import model.type.IntType;

public class BoolValue implements IValue{
    private boolean val;
    public BoolValue(boolean v){val=v;}

    public boolean getValue(){return this.val;}

    public IType getType() { return new BoolType();}

    @Override
    public String toString() {
        return "BoolValue{" +
                "boolean val=" + val +
                '}';
    }
}
