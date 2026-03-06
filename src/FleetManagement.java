/*
    FleetManagement - Handles all fleet management UI and also the methods
*/
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class FleetManagement{
    //
    private FleetManager fleetManager;
    private Scanner input;  // shared in constructor (shared with Main.java)
    private int vehicleIdCounter = 500; // id start with 500 e.g. VHE501, VHE502

    // Constructor
    public FleetManagement(FleetManager fleetManager, Scanner input){
        this.fleetManager = fleetManager;
        this.input = input;
    }

    // Display Portal (MAIN)
    public void show(){
        boolean inFleetMenu = true;
        while(inFleetMenu){
            try{
                displayMenu();
                int choice = input.nextInt();
                input.nextLine(); // clear input buffer

                switch(choice){
                    case 1 -> addVehicle(); 
                    case 2 -> viewAllVehicles();
                    case 3 -> viewVehicleDetails(); 
                    case 4 -> updateVehicleInfo(); 
                    case 5 -> scheduleMaintenance(); 
                    case 6 -> completeMaintenance();
                    case 7 -> removeVehicle(); 
                    case 8 -> viewMaintenanceDue();
                    case 0 -> {
                        System.out.println("  [i] Returning to Admin Portal...");
                        inFleetMenu = false;
                    }
                    default -> System.out.println("  [!] Invalid option. Please try again.");
                }
            } catch(InputMismatchException e){
                System.out.println("  [!] Invalid input! Please enter a number.");
                input.nextLine(); // clear the wrong input
            }
        }
    }


    // OPERATIONS
    private void addVehicle(){
        try{
            System.out.println("\n  ╔═══════════════════════════════════════╗");
            System.out.println("  ║            ADD NEW VEHICLE            ║");
            System.out.println("  ╚═══════════════════════════════════════╝");

            // PRE-INCREMENT, increment first then use
            String id = "VHE" + (++vehicleIdCounter);

            System.out.print("  Plate Number (e.g. WHL999) -> ");
            String plateNum = input.next();

            // Default vehicle is VAN
            System.out.println("\n  Vehicle Type:");
            System.out.println("    1. MOTORCYCLE (Max: 50kg)");
            System.out.println("    2. VAN (Max: 500kg)");
            System.out.println("    3. TRUCK (Max: 2000kg)");
            System.out.print("  Choice -> ");
            int typeChoice = input.nextInt();
            input.nextLine(); // clear input buffer
            
            Vehicle.VehicleType type;
            double maxLoad;
            switch(typeChoice){
                case 1 -> {
                    type = Vehicle.VehicleType.MOTORCYCLE;
                    maxLoad = 50;
                }
                case 3 -> {
                    type = Vehicle.VehicleType.TRUCK;
                    maxLoad = 2000;
                }
                default -> {
                    type = Vehicle.VehicleType.VAN;
                    maxLoad = 500;
                }
            }
            
            Vehicle vehicle = new Vehicle(id, plateNum, type, maxLoad);
            fleetManager.addVehicle(vehicle);   // add this new vehicle to fleetManager VEHICLE data storage

            System.out.println("  [✓] Vehicle ID \"" + id + "\" added successfully.");

        } catch(InputMismatchException e){
            System.out.println("  [!] Invalid input! Please enter valid data.");
            input.nextLine(); // clear buffer to reset input
        }
    }

    private void viewAllVehicles(){
        fleetManager.displayAll();
    }

    private void viewVehicleDetails(){
        System.out.print("  Enter Vehicle ID -> ");
        String vehicleID = input.next();
        input.nextLine();

        // get the vehicle object
        Vehicle v = fleetManager.findByID(vehicleID);
        if(v == null){  // if v = nothing then error
            System.out.println("  [!] Vehicle not found: " + vehicleID);
        }
        else{
            System.out.println();
            v.displayInfo();
        }
    }

    private void updateVehicleInfo(){
        try{
            System.out.print("  Enter Vehicle ID -> ");
            String vehicleID = input.next();

            Vehicle v = fleetManager.findByID(vehicleID);
            if(v == null){
                System.out.println("  [!] Vehicle not found: " + vehicleID);
                return; // Stop the operation immediately
            }

            System.out.println("\n  Current Information: ");
            v.displayInfo();

            System.out.println("\n  What would you like to update?");
            System.out.println("  1. Plate Number");
            System.out.println("  2. Status");
            System.out.println("  0. Cancel");
            System.out.print("  Choice -> ");
            int choice = input.nextInt();
            input.nextLine();

            switch(choice){
                case 1 -> {
                    System.out.print("\n  New Plate Number (e.g. WHL999) -> ");
                    String newPlate = input.next();
                    input.nextLine();
                    v.setPlateNumber(newPlate);
                    System.out.println("  [✓] Plate number updated to -> " + newPlate);
                }
                case 2 -> {
                    System.out.println(" \n  Status Options:");
                    System.out.println("  1. AVAILABLE");
                    System.out.println("  2. IN_USE");
                    System.out.println("  3. UNDER_MAINTENANCE");
                    System.out.print("  Choice -> ");
                    int statusChoice = input.nextInt();
                    input.nextLine();

                    // ???.values() return array of all enum constants
                    // make it to array so can get enum (VehicleStatus 1,2,3)
                    Vehicle.VehicleStatus[] statuses = Vehicle.VehicleStatus.values();
                    switch(statusChoice){
                        case 1: // fall-through switch case, cause case 1 and case 2 also same code as 3
                        case 2: // if case 1 it will fall till case 3 because no break
                        case 3: // so don't need to repeatedly write the same code
                            v.setStatus(statuses[statusChoice - 1]);
                            System.out.println("  [✓] Status updated to -> " + statuses[statusChoice - 1]);
                            break;
                        default:
                            System.out.println("  [!] Invalid status choice.");
                    }
                }
                case 0 -> {
                    System.out.println("\n  [i] Update cancelled.");
                }
                default -> System.out.println("  [!] Invalid option. Please try again.");
            }
        } catch(InputMismatchException e){
            System.out.println("  [!] Invalid input!");
            input.nextLine(); // reset buffer (clear input)
        }
    }

    private void scheduleMaintenance(){
        System.out.print("  Enter Vehicle ID -> ");
        String vehicleID = input.next();

        Vehicle v = fleetManager.findByID(vehicleID);
        if(v == null){
            System.out.println("  [!] Vehicle not found: " + vehicleID);
            return; // Stop the operation immediately
        }

        // if Status = IN_USE, stop operation
        if(v.getStatus() == Vehicle.VehicleStatus.IN_USE){
            System.out.println("  [!] Cannot inputhedule maintenance - vehicle is currently in use.");
            return;
        }

        v.scheduleMaintenance();
    }

    private void completeMaintenance(){
        System.out.print("  Enter Vehicle ID -> ");
        String vehicleID = input.next();

        Vehicle v = fleetManager.findByID(vehicleID);
        if(v == null){
            System.out.println("  [!] Vehicle not found: " + vehicleID);
            return; // Stop the operation immediately
        }

        // if Status not equal (!=) UNDER_MAINTENANCE, don't continue operation
        if(v.getStatus() != Vehicle.VehicleStatus.UNDER_MAINTENANCE){
            System.out.println("\n  [!] Vehicle is not under maintenance!");
            System.out.println("  >>> Current Status: " + v.getStatus());
        }

        v.completeMaintenance();
    }

    private void removeVehicle(){
        try{
            System.out.print("  Enter Vehicle ID -> ");
            String vehicleID = input.next();

            Vehicle v = fleetManager.findByID(vehicleID);
            if(v == null){
                System.out.println("  [!] Vehicle not found: " + vehicleID);
                return; // Stop the operation immediately
            }

            System.out.println("\n  Vehicle to be removed:");
            v.displayInfo();

            System.out.print("  Are you sure to confirm delete the vehicle? (Y/N) -> ");
            String confirm = input.next();
            input.nextLine();

            if(confirm.equalsIgnoreCase("y")){
                fleetManager.removeVehicle(vehicleID);
            }
            else{
                System.out.println("  [i] Remove operation cancelled.");
            }
        }catch(VehicleNotFoundException e){
            // e.getMessage() = Retrive and print the exception message
            System.out.println("  [!] " + e.getMessage());
        }catch(IllegalStateException e){
            System.out.println("  [!] " + e.getMessage());
        }
    }

    private void viewMaintenanceDue(){
        List<Vehicle> due = fleetManager.getVehiclesNeedingMaintenance();

        if(due.isEmpty()){
            System.out.println("\n  [✓] No vehicles need maintenance attention.");
            return;
        }

        System.out.println("\n  ╔════════════════════════════════════════════════════════╗");
        System.out.println("  ║               VEHICLES NEEDING MAINTENANCE             ║");
        System.out.println("  ╚════════════════════════════════════════════════════════╝");
        System.out.println("  Total: " + due.size() + " vehicle(s)");
        System.out.println();
        System.out.printf("  %-10s %-12s %-12s %-20s %-15s %-15s%n", "ID", "Plate", "Type", "Status", "Next Maint", "Action");        
        System.out.println("  " + "─".repeat(90));

        for (Vehicle v : due){
            // do .toString() because need to display it out so convert to String
            String statusDisplay = v.getStatus().toString();
            String maintenanceDisplay = v.getNextMaintenanceDate().toString();

            String actionNeeded = "";
            if(v.getStatus() == Vehicle.VehicleStatus.UNDER_MAINTENANCE){
                actionNeeded = "In Progress";
            }
            else if(v.isMaintenanceDue()){
                actionNeeded = "Schedule Now";
            }

        System.out.printf("  %-10s %-12s %-12s %-20s %-15s %-18s%n", v.getVehicleID(), v.getPlateNumber(),
            v.getType(), statusDisplay, maintenanceDisplay, actionNeeded);
        }
        System.out.println();
    }


    // Menu Design
    private void displayMenu(){
        System.out.println("\n  ╔══════════════════════════════════════╗");
        System.out.println("  ║         FLEET MANAGEMENT MENU        ║");
        System.out.println("  ╠══════════════════════════════════════╣");
        System.out.println("  ║  1.  Add Vehicle                     ║");
        System.out.println("  ║  2.  View All Vehicles               ║");
        System.out.println("  ║  3.  View Vehicle Details            ║");
        System.out.println("  ║  4.  Update Vehicle Info             ║");
        System.out.println("  ║  5.  Schedule Maintenance            ║");
        System.out.println("  ║  6.  Complete Maintenance            ║");
        System.out.println("  ║  7.  Remove Vehicle                  ║");
        System.out.println("  ║  8.  View Vehicles Due for Maint     ║");
        System.out.println("  ║  0.  Return to Admin Portal          ║");
        System.out.println("  ╚══════════════════════════════════════╝");
        System.out.print("  Choice -> ");
    }
}