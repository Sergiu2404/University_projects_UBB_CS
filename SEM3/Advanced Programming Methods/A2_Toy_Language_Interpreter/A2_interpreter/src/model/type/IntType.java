package model.type;

public class IntType implements IType {
    public boolean equals(Object another){
        return another instanceof IntType; //true or false
    }

    public String toString() { return "int";}
}
