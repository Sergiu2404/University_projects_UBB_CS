package model;

public class Vehicle {
    protected int id;
    protected String type,color;



    public Vehicle(){}


    public Vehicle(int id,String type,String color)
    {
        this.id=id;
        this.type=type;
        this.color=color;
    }

    public int getId() {
        return id;
    }
    public String getColor() {
        return color;
    }
    public String getType(){
        return type;
    }

    public String representation(){
        return "This "+type+"is "+color;
    }
}
