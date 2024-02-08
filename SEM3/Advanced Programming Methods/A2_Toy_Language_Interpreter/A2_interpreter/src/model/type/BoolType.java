package model.type;

public class BoolType implements IType{
    public boolean equals(Object another){
        return another instanceof BoolType; //true or false
    }

    public String toString() { return "boolean";}
}
