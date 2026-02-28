/*
    Vehicle -  Blueprint class for attributes of a fleet unit, such as plate number, fuel type, etc
    enum - defines fixed role (only accepts Role.??? rather than any String)

*/
import java.time.LocalDate;

public class Vehicle {
    // fixed role(status), for easier categorize
    public enum VehicleType{
        MOTORCYCLE, VAN, TRUCK
    }
    public enum VehicleStatus{
        AVAILABLE, IN_USE, UNDER_MAINTENANCE
    }

    private final String vehicleID;
    private String plateNumber, assignedCourierID;
    private VehicleType  type;
    private VehicleStatus status;
    private double maxLoadKg;
    private LocalDate lastMaintenanceDate, nextMaintenanceDate;

    // Parameterized Constructor (receive 4 parameters)
    public Vehicle(String vehicleID, String plateNumber, VehicleType type, double maxLoadKg){
        this.vehicleID = vehicleID;
        this.plateNumber = plateNumber;
        this.type = type;
        this.maxLoadKg = maxLoadKg;
        this.status = VehicleStatus.AVAILABLE;
        this.lastMaintenanceDate = LocalDate.now().minusMonths(1);  
        this.nextMaintenanceDate = LocalDate.now().plusMonths(3);   
        this.assignedCourierID = null;  // null because not yet assign any courier
    }

    // Getter for "vehicleID" (No setter because it's final[constant])
    public String getVehicleID(){
        return vehicleID;
    }

    // Getter and Setter for "plateNumber"
    public String getPlateNumber(){
        return plateNumber;
    }

    public void setPlateNumber(String plateNumber){
        this.plateNumber = plateNumber;
    }

    // Getter for "type" (No setter because we don't want the Vehicle Type to be changed)
    public VehicleType getType(){
        return type;
    }

    // Getter and Setter for "status"
    public VehicleStatus getStatus(){
        return status;
    }

    public void setStatus(VehicleStatus status){
        this.status = status;
    }

    // Getter for "maxLoadKg" (No setter because the capacity is fixed by manufacturer)
    public double getMaxLoadKg(){
        return maxLoadKg;
    }

    // Getter for "lastMaintenanceDate"
    public LocalDate getLastMaintenanceDate(){
        return lastMaintenanceDate;
    }

    // Getter for "nextMaintenanceDate"
    public LocalDate getNextMaintenanceDate(){
        return nextMaintenanceDate;
    }

    // Getter and Setter for "assignedCourierId"
    public String getAssignedCourierID(){
        return assignedCourierID;
    }

    public void setAssignedCourierID(String assignedCourierID){
        this.assignedCourierID = assignedCourierID;
    }


    // METHODS, Check if the current date has reached or passed the maintenance date
    public boolean isMaintenanceDue(){
        return LocalDate.now().isAfter(nextMaintenanceDate) || LocalDate.now().isEqual(nextMaintenanceDate);
        // return true if passed or equal nextMaintenanceDate 
    }
    
    // METHODS, Check is the vehicle available or not
    public boolean isAvailable(){
        return status == VehicleStatus.AVAILABLE;
        // return true if the status is AVAILABLE
    }


    // METHODS, Changes the vehicle status
    public void scheduleMaintenance(){
        this.status = VehicleStatus.UNDER_MAINTENANCE;
        this.lastMaintenanceDate = LocalDate.now();
        this.nextMaintenanceDate = LocalDate.now().plusMonths(3);
        this.assignedCourierID = null;  // null because vehicle going maintenance, which means no courier assigned
        System.out.println("  [✓] Vehicle " + vehicleID + " scheduled for maintenance.");
    }

    public void completeMaintenance(){
        this.status = VehicleStatus.AVAILABLE;
        System.out.println("  [✓] Vehicle " + vehicleID + " maintenance complete - now AVAILABLE.");
    }

    // Display Info (For every details of that vehicle, e.g. picked a specific vehicle and making decision)
    public void displayInfo(){
        // ... : ... ? ... - ternary operator | result = (condition) ? valueIfTrue : valueIfFalse
        // printf %n - new line
        System.out.println("  ┌──────────────────────────────────────┐");
        System.out.printf ("  │  Vehicle ID  : %-21s│%n", vehicleID);
        System.out.printf ("  │  Plate       : %-21s│%n", plateNumber);
        System.out.printf ("  │  Type        : %-21s│%n", type);
        System.out.printf ("  │  Status      : %-21s│%n", status);
        System.out.printf ("  │  Max Load    : %-18.1f kg│%n", maxLoadKg);
        System.out.printf ("  │  Last Maint  : %-21s│%n", lastMaintenanceDate);
        System.out.printf ("  │  Next Maint  : %-21s│%n", nextMaintenanceDate);
        System.out.printf ("  │  Courier     : %-21s│%n", assignedCourierID != null ? assignedCourierID : "Unassigned");
        System.out.printf ("  │  Maint Due   : %-21s│%n", isMaintenanceDue() ? "*** YES ***" : "No");
        System.out.println("  └──────────────────────────────────────┘");
    }

    // toString method (Polymorphism)
    @Override   // Override 
    public String toString(){
        return String.format("  %-10s %-12s %-12s %-18s %s%n",
                vehicleID, plateNumber, type, status,
                isMaintenanceDue() ? "⚠ MAINT DUE" : "NONE");
    }
}
