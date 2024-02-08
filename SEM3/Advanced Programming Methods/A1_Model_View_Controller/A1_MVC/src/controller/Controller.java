package controller;

import custom_exceptions.ArraySizeException;
import custom_exceptions.InvalidObject;
import custom_exceptions.InvalidType;
import model.Bike;
import model.Car;
import model.Motorcycle;
import model.Vehicle;
import repo.Repository;
import repo.RepositoryInterface;

import java.util.ArrayList;

public class Controller {
    private RepositoryInterface repository;

    public Controller(RepositoryInterface repo)
    {
        this.repository=repo;
    }

    public void initController()
    {
        Vehicle bike1=new Bike(1,"bycicle","red");
        Vehicle car1=new Car(2,"car","blue");
        Vehicle motorcycle1=new Motorcycle(3,"motorcycle","green");
        Vehicle bike2=new Bike(4,"bycicle","black");
        Vehicle car2=new Car(5,"car","white");
        Vehicle motorcycle2=new Motorcycle(6,"motorcycle","grey");

        this.repository.addVehicle(bike1);
        this.repository.addVehicle(car1);
        this.repository.addVehicle(motorcycle1);
        this.repository.addVehicle(bike2);
        this.repository.addVehicle(car2);
        this.repository.addVehicle(motorcycle2);

    }

    public ArrayList<Vehicle> getAllVehicles(){return this.repository.getAllVehicles();}
    public ArrayList<Vehicle> getAllGivenColorVehicles(String color){
        ArrayList<Vehicle> colorVehicles=new ArrayList<Vehicle>();

        for(Vehicle v: this.repository.getAllVehicles())
            if(v.getColor().equals(color))
                colorVehicles.add(v);

        return colorVehicles;
    }

    public boolean addVehicle(int id,String type,String color) throws ArraySizeException,InvalidType,InvalidObject
    {
        //int size=this.repository.getAllVehicles().size()+1;
        Vehicle vehicle=null;

        //try {
            if(this.repository.getAllVehicles().size()>=8)
                throw new ArraySizeException("Can't add any new vehicle. Number exceeded!");


            if (type.toLowerCase().equals("car"))
                vehicle = new Car(id, type, color);

            else if (type.toLowerCase().equals("bike") || type.toLowerCase().equals("bicycle"))
                vehicle = new Bike(id, type, color);

            else if (type.toLowerCase().equals("motorbike") || type.toLowerCase().equals("motorcycle"))
                vehicle = new Motorcycle(id, type, color);

            else
                throw new InvalidType("Try using another type!");
        //}catch(InvalidType exception1)
        //{
            //System.out.println(exception1.getMessage());
        //}catch(ArraySizeException ase)
        //{
            //System.out.println(ase.getMessage());
        //}

        //try {
            if (vehicle == null)
                throw new InvalidObject("Vehicle needs arguments!");
        //}catch(InvalidObject exception2)
        //{
        //    System.out.println(exception2.getMessage());
        //}
        return this.repository.addVehicle(vehicle);
    }

    public boolean deleteVehicle(int id,String type,String color)
    {
        Vehicle vehicle=new Vehicle(id,type,color);

//        try {
//            if (type.toLowerCase().equals("car"))
//                vehicle = new Car(id, type, color);
//
//            else if (type.toLowerCase().equals("bike") || type.toLowerCase().equals("bicycle"))
//                vehicle = new Bike(id, type, color);
//
//            else if (type.toLowerCase().equals("motorbike") || type.toLowerCase().equals("motorcycle"))
//                vehicle = new Motorcycle(id, type, color);
//
//            else
//                throw new InvalidType("Try using another type!");
//        }catch(InvalidType exception1)
//        {
//            System.out.println(exception1.getMessage());
//        }

//        try {
//            if (vehicle == null)
//                throw new InvalidObject("Vehicle needs arguments!");
//        }catch(InvalidObject exception2)
//        {
//            System.out.println(exception2.getMessage());
//        }
        return this.repository.deleteVehicle(vehicle);
    }

}
