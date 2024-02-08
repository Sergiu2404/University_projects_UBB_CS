package model.value;

import model.type.IType;
import model.type.IntType;

public class IntValue implements IValue{
    private int val;

    public IntValue(int v){val=v;}

    public int getValue(){return this.val;}
    public IType getType() { return new IntType();}

    @Override
    public String toString() {
        return "IntValue{" +
                "integer val=" + val +
                '}';
    }

}
