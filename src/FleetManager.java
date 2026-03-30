/*  
    FleetManager a.k.a VehicleRegistry
    FleetManager - Handles logic and data storage related to "Fleet"
    Handles: Data storage(ArrayList)
    Does not Handle: User input or menus (FleetManagement job)
*/

// ADD SAMPLE VEHICLE VHE501 and VHE502 AS IT"S ALREADY ASSIGNED TO COURIERS

/*
 * ════════════════════════════════════════════════════════════════════════════
 *                   FLEET MANAGER - TODO LIST
 *            Methods to Add for Courier Integration
 * ════════════════════════════════════════════════════════════════════════════
 * 
 * PRIORITY 1: NEW METHOD NEEDED ⭐
 * ────────────────────────────────────────────────────────────────────────────
 * [ ] Add method: findByAssignedCourier()
 * 
 *     /**
 *      * Find a vehicle assigned to a specific courier.
 *      * Used by: Courier Portal to show courier's vehicle
 *      *          Admin to check if courier has vehicle before removal
 *      *
 *      * @param courierId The courier ID to search for
 *      * @return Vehicle object if found, null if courier has no vehicle
 *      *
 *     public Vehicle findByAssignedCourier(String courierId) {
 *         for (Vehicle v : vehicles) {
 *             if (courierId.equals(v.getAssignedCourierID())) {
 *                 return v;  // Found the vehicle assigned to this courier
 *             }
 *         }
 *         return null;  // Courier has no assigned vehicle
 *     }
 * 
 * ────────────────────────────────────────────────────────────────────────────
 * PRIORITY 2: UPDATE EXISTING METHODS
 * ────────────────────────────────────────────────────────────────────────────
 * [ ] Update displayAll() to show assigned courier:
 *     - Add "Assigned To" column
 *     - Show courier ID or "Unassigned"
 *     - Format: %-15s for column width
 * 
 *     Example addition:
 *     System.out.printf("  %-10s %-12s %-12s %-22s %-18s %-15s%n",
 *             "ID", "Plate", "Type", "Status", "Maintenance", "Assigned To");
 * 
 *     for (Vehicle v : vehicles) {
 *         String assignedTo = v.getAssignedCourierID() != null 
 *                            ? v.getAssignedCourierID() 
 *                            : "Unassigned";
 *         // ... add assignedTo to printf
 *     }
 * 
 * ────────────────────────────────────────────────────────────────────────────
 * PRIORITY 3: OPTIONAL HELPER METHODS
 * ────────────────────────────────────────────────────────────────────────────
 * [ ] Optional: getVehiclesInUse()
 *     - Returns List<Vehicle> where status == IN_USE
 *     - Used by: Admin to see which vehicles are assigned
 * 
 * [ ] Optional: getUnassignedVehicles()
 *     - Returns List<Vehicle> where assignedCourierID == null AND status == AVAILABLE
 *     - Used by: Admin when assigning vehicle to courier
 * 
 * ════════════════════════════════════════════════════════════════════════════
 * EXISTING METHODS (Already Implemented - No Changes Needed) ✅
 * ════════════════════════════════════════════════════════════════════════════
 * ✅ assignToCourier(vehicleId, courierId) - assigns vehicle to courier
 * ✅ releaseVehicle(vehicleId) - releases vehicle from courier
 * ✅ findById(vehicleId) - finds vehicle by ID
 * ✅ getAvailableVehicles() - gets vehicles with status AVAILABLE
 * ✅ addVehicle(), removeVehicle() - CRUD operations
 * ✅ saveToFile(), loadFromFile() - CSV storage (already saves assignedCourierID)
 * 
 * ════════════════════════════════════════════════════════════════════════════
 */

import java.util.List;
import java.util.ArrayList;

public class FleetManager {
    // Data storage to store all vehicles in "Fleet"
    // List - stores item in order, can add, remove, search
    // List<Vehicle> - can only hold "Vehicle" object, does not accept other like "String"
    private final List<Vehicle> vehicles;
    
    // Constructor
    public FleetManager(){
        this.vehicles = new ArrayList<>();
    }


    // CRUD OPERATIONS
    // ---------- CREATE, READ(GET DATA) OPERATIONS METHODS -----------
    public void addVehicle(Vehicle vehicle){
        for(Vehicle v : vehicles){
            // Check if the vehicleID already exists or not, if exists, throw exception
            if(v.getVehicleID().equalsIgnoreCase(vehicle.getVehicleID())){
                throw new IllegalArgumentException("  [!] Vehicle ID " + vehicle.getVehicleID() + " already exists.");
            }
        }
        // If vehicleID didn't exists, add it into arrayList<> vehicles
        vehicles.add(vehicle);
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

    public List<Vehicle> getVehiclesNeedingMaintenance(){
        List<Vehicle> dueList = new ArrayList<>();

        for(Vehicle v : vehicles){
            if(v.isMaintenanceDue() || v.getStatus() == Vehicle.VehicleStatus.UNDER_MAINTENANCE){
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
            throw new IllegalStateException("  [!] Vehicle " + vehicleID + " is not available. (Status: " + v.getStatus());
        }

        // if all good, then assign
        v.setAssignedCourierID(courierID);
        v.setStatus(Vehicle.VehicleStatus.IN_USE);

        // 
        Courier courier = UserRegistry.getCourierById(courierID);
        if(courier != null){
            courier.setAssignedVehicleID(vehicleID);
        }

        System.out.println("  [DONE] Vehicle " + vehicleID + " assigned to courier " + courierID + ".");
        return true; 
    }

    // - release vehicle from vehicle assignment, status = available again
    public boolean releaseVehicle(String vehicleID) throws VehicleNotFoundException{
        Vehicle v = findByID(vehicleID);

        if(v == null){
            throw new VehicleNotFoundException(vehicleID);
        }

        // Remove vehicle from Courier object
        String courierID = v.getAssignedCourierID();
        if(courierID != null){
            Courier courier = UserRegistry.getCourierById(courierID);
            if(courier != null){
                courier.setAssignedVehicleID(null);
            }
        }

        // make CourierID null, which means vehicle not assigned
        v.setAssignedCourierID(null);

        // make VehicleStatus = AVAILABLE again
        v.setStatus(Vehicle.VehicleStatus.AVAILABLE);

        System.out.println("  [DONE] Vehicle " + vehicleID + " released and now AVAILABLE.");
        return true;
    }

    
    // ---------- DELETE OPERATIONS METHOD ----------
    // remove a car from the list
    public boolean removeVehicle(String vehicleID) throws VehicleNotFoundException{
        Vehicle v = findByID(vehicleID);

        if(v == null){
            throw new VehicleNotFoundException(vehicleID);
        }

        // Check if vehicle in_use or not, if in_use, stop the operation
        if (v.getStatus() == Vehicle.VehicleStatus.IN_USE){
            throw new IllegalStateException("\n  [!] Cannot remove a Vehicle currently in use.");
        }

        // If no problem then remove
        // ???.remove() = remove elements from array list
        vehicles.remove(v);

        return true;
    }

    // Display
    public void displayAll(){
        if(vehicles.isEmpty()){
            System.out.println("\n  [!] No vehicles in the fleet.");
            return; // Exit the method early
        }

        System.out.println("\n  ╔═══════════════════════════════════════════════════════╗");
        System.out.println("  ║                    FLEET OVERVIEW                     ║");
        System.out.println("  ╚═══════════════════════════════════════════════════════╝");

        System.out.println("  Total Vehicles:  " + vehicles.size() + "\n");

        System.out.printf("  %-10s %-12s %-12s %-20s %-15s%n", "ID", "Plate", "Type", "Status", "Maintenance");
        System.out.println("  " + "─".repeat(70));

        // Loop through all the elements and display each vehicle
        for(Vehicle v: vehicles){
            String maintenanceStatus;

            if(v.getStatus() == Vehicle.VehicleStatus.UNDER_MAINTENANCE){
                maintenanceStatus = "In Progress";
            }
            else if(v.isMaintenanceDue()){
                maintenanceStatus = "Overdue";
            }
            else{
                maintenanceStatus = "Up To Date";
            }

        System.out.printf("  %-10s %-12s %-12s %-20s %-18s%n", v.getVehicleID(), v.getPlateNumber(), v.getType(),
                v.getStatus(), maintenanceStatus);        
        }
    }

    // Vehicle Sample for VHE501 and VHE502, ALREADY ASSIGNED
    public void initializeData(){
        Vehicle v1 = new Vehicle("VHL501", "WKL1234", Vehicle.VehicleType.VAN, 500.0);
        v1.setStatus(Vehicle.VehicleStatus.IN_USE);
        v1.setAssignedCourierID("CR001");
        vehicles.add(v1);
        
        Vehicle v2 = new Vehicle("VHL502", "WKL5678", Vehicle.VehicleType.MOTORCYCLE, 50.0);
        v2.setAssignedCourierID("CR002");
        v2.setStatus(Vehicle.VehicleStatus.IN_USE);
        vehicles.add(v2);
    }
}