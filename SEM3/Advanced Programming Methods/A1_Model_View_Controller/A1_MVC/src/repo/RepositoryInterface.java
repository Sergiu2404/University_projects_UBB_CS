package repo;

import model.Vehicle;

import java.util.ArrayList;

public interface RepositoryInterface {
    Vehicle getVehicleById(int id);

    boolean addVehicle(Vehicle vehicle);
    boolean deleteVehicle(Vehicle vehicle);

    ArrayList<Vehicle> getAllVehicles();
}

