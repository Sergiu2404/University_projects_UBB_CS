package model;

public class Motorcycle extends Vehicle {
    protected int id;
    protected String type,color;

    public Motorcycle(){}
    public Motorcycle(int id, String type, String color)
    {
        super(id,type,color);
    }

    @Override
    public int getId(){
        return super.id;
    }
    @Override
    public String getType(){
        return super.type;
    }
    @Override
    public String getColor(){
        return super.color;
    }

    @Override
    public String representation(){
        return super.id+". This " +super.type+" is "+super.color;
    }
}