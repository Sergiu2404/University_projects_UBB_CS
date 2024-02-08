package repo;

import custom_exceptions.ArraySizeException;
import model.Vehicle;

import java.util.ArrayList;

public class Repository implements RepositoryInterface {
    ArrayList<Vehicle> vehicles;

    public Repository(){
        this.vehicles=new ArrayList<Vehicle>(8);
    }

    public Vehicle getVehicleById(int id) {
        for (Vehicle v:vehicles) {
            if (v.getId() == id) {
                return v;
            }
        }
        return null; // User not found
    }

    public ArrayList<Vehicle> getAllVehicles() {
        return vehicles;
    }

    public boolean addVehicle(Vehicle vehicle){
        if(getVehicleById(vehicle.getId())!=null)
            return false;
        else{
            vehicles.add(vehicle);
            return true;
        }
    }
    public int getIndexOfElement(Vehicle vehicle)
    {
        int index=0;

        if(getVehicleById(vehicle.getId())==null)
            return -1;
        for(Vehicle v: vehicles) {
            if (v.getId() == vehicle.getId())
                return index;
            index++;
        }
        return -1;
    }

    public boolean deleteVehicle(Vehicle vehicle){
        if(getVehicleById(vehicle.getId())==null)
            return false;
        else{
            int index=getIndexOfElement(vehicle);
            if(index==-1)
                return false;
            vehicles.remove(index);
            return true;
//            int index=-1;
////            vehicles.remove(vehicle);
////            return true;
//            for (int i=0;i<vehicles.size();i++)
//                if(vehicles.get(i).getId()==vehicle.getId() || index>=i) {
//                    vehicles.set(i, vehicles.get(i + 1));
//                    index=i;
//                }
//            return true;
        }
    }

}

