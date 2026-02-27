/*
    FleetManager - Handles logic and data storage related to "Fleet"
    Handles: Data storage(ArrayList)
    Does not Handle: User input or menus (FleetManagement job)
*/
import java.util.List;
import java.util.ArrayList;

public class FleetManager {
    // Data storage to store all vehicles in "Fleet"
    // List - stores item in order, can add, remove, search
    // List<Vehicle> - can only hold "Vehicle" object, does not accept other like "String"
    private final List<Vehicle> vehicles = new ArrayList<>();
    
    // Constructor
    public FleetManager(){
        this.vehicles = new ArrayList<>();
    }

    // ---------- CREATE, READ(GET DATA) OPERATIONS METHODS -----------
    public void addVehicle(Vehicle vehicle){
        for(Vehicle v : vehicles){
            // Check if the vehicleID already exists or not, if exists, throw exception
            if(v.getVehicleID().equalsIgnoreCase(v.getVehicleID())){
                throw new IllegalArgumentException("  [!] Vehicle ID " + vehicle.getVehicleID() + " already exists.");
            }
        }
        // If vehicleID didn't exists, add it into arrayList<> vehicles
        vehicles.add(vehicle);

        System.out.println("  [✓] Vehicle added: " + vehicle.getVehicleID() + " (" + vehicle.getPlateNumber() + ")");
    }

    public Vehicle findByID(String vehicleID){
        for(Vehicle v : vehicles){
            // return entire vehicle object if ID matches
            if(v.getVehicleID().equalsIgnoreCase(vehicleID)){
                return v;
            }
        }
        // return nothing (null) if no matching ID
        return null;
    }

    public List<Vehicle> getAvailableVehicles(){
        // new list to store the results
        List<Vehicle> availableList = new ArrayList<>();

        for(Vehicle v : vehicles){
            if(v.isAvailable()){
                availableList.add(v);
            }
        }
        return availableList;
    }

    public List<Vehicle> getVehiclesDueForMaintenance(){
        List<Vehicle> dueList = new ArrayList<>();

        for(Vehicle v : vehicles){
            if(v.isMaintenanceDue()){
                dueList.add(v);
            }
        }
        return dueList;
    }

    // Get the total number of vehicles (int)
    public int getTotalVehicles(){
        // ArrayList.size() - returns how many items are in the list
        return vehicles.size();
    }


    // ---------- UPDATE OPERATIONS METHOD ----------
    // throws - warn callers this method might fail
    // throw  - trigger the exception
    public boolean assignToCourier(String vehicleID, String courierID) throws VehicleNotFoundException{
        Vehicle v = findByID(vehicleID);

        // if vehicleID not exists
        if(v == null){
            throw new VehicleNotFoundException(vehicleID);
        }

        // if vehicle is going MAINTENANCE or IN_USE
        if(!v.isAvailable()){
            throw new IllegalStateException("  [!] Vehicle " + vehicleID + " is not available (Status: " + v.getStatus());
        }

        // if all good, then assign
        v.setAssignedCourierID(courierID);
        v.setStatus(Vehicle.VehicleStatus.IN_USE);

        System.out.println("  [✓] Vehicle " + vehicleID + " assigned to courier " + courierID);
        return true; 
    }

    
}