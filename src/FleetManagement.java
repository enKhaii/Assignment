/*
    FleetManagement - Handles all fleet management UI and also the methods
*/

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class FleetManagement{
    //
    private FleetManager fleetManager;
    private Scanner input;  // shared in constructor (shared with Main.java)
    private int vehicleIdCounter = 502; // id start with 502 e.g. VHE503, VHE504, 501 and 502 are sample vehicle

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
                    case 9 -> assignVehicleToCourier();
                    case 10 -> releaseVehicleFromCourier();
                    case 0 -> {
                        System.out.println("  [i] Returning to Admin Portal...");
                        inFleetMenu = false;
                    }
                    default -> System.out.println("  [!] Invalid option. Please try again.");
                }
            } catch(InputMismatchException e){
                System.out.println("\n  [!] Invalid input! Please enter a number.");
                input.nextLine(); // clear the wrong input
            }
        }
    }


    // OPERATIONS
    private boolean isValidPlateNumber(String plate){ // For checking Plate Number only
        // check length (6-8), example ABC123, ABC1234
        if(plate.length() < 6 || plate.length() >= 8){
            return false;
        }

        // boolean for checking contains digits or letters
        boolean hasLetter = false;
        boolean hasDigit = false;
        for(char c : plate.toCharArray()){  // toCharArray = convert String into new character array
            if(!Character.isLetterOrDigit(c)){
                return false;   // if there's symbols(\ , . - +), return false,
            }
            if(Character.isLetter(c)){   // to use isDigit/Letter, need to use Character.?????
                hasLetter = true;
            }
            if(Character.isDigit(c)){
                hasDigit = true;
            }
        }

        return hasLetter && hasDigit; // return true if hasLetter and hasDigit = true
    }

    private void addVehicle(){
        try{
            System.out.println("\n  ╔═══════════════════════════════════════╗");
            System.out.println("  ║            ADD NEW VEHICLE            ║");
            System.out.println("  ╚═══════════════════════════════════════╝");

            
            System.out.print("  Plate Number (e.g. WHL999) -> ");
            String plateNum = input.next();
            
            // if isValidPlateNumber = false, error and return(stop operation)
            if(!isValidPlateNumber(plateNum)){
                System.out.println("\n  [!] Invalid Plate Number format!");
                System.out.println("  [i] Must be 6-8 characters with letters and numbers only.");
                return;
            }

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
            
            // PRE-INCREMENT, increment first then use
            String id = "VHE" + (++vehicleIdCounter);
            Vehicle vehicle = new Vehicle(id, plateNum, type, maxLoad);
            fleetManager.addVehicle(vehicle);   // add this new vehicle to fleetManager VEHICLE data storage

            System.out.println("\n  [DONE] Success: " + vehicle.getVehicleID() + " | Registered to Plate: " + vehicle.getPlateNumber());

        } catch(InputMismatchException e){
            System.out.println("\n  [!] Invalid input! Please enter a number.");
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
            System.out.println("\n  [!] Vehicle not found: " + vehicleID);
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
                System.out.println("\n  [!] Vehicle not found: " + vehicleID);
                return; // Stop the operation immediately
            }

            System.out.println("\n  Current Information: ");
            v.displayInfo();

            System.out.println("\n  What would you like to update?");
            System.out.println("   1. Plate Number");
            System.out.println("   2. Status");
            System.out.println("   0. Cancel");
            System.out.print("  Choice -> ");
            int choice = input.nextInt();
            input.nextLine();

            switch(choice){
                case 1 -> {
                    System.out.print("\n  New Plate Number (e.g. WHL999) -> ");
                    String newPlate = input.next();
                    input.nextLine();
                    v.setPlateNumber(newPlate);
                    System.out.println("\n  [DONE] Plate number updated to -> " + newPlate);
                }
                case 2 -> {
                    System.out.println(" \n  Status Options:");
                    System.out.println("   1. AVAILABLE");
                    System.out.println("   2. IN_USE");
                    System.out.println("   3. UNDER_MAINTENANCE");
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
                            System.out.println("\n  [DONE] Status updated to -> " + statuses[statusChoice - 1]);
                            break;
                        default:
                            System.out.println("\n  [!] Invalid status choice.");
                    }
                }
                case 0 -> {
                    System.out.println("\n  [i] Update cancelled.");
                }
                default -> System.out.println("  [!] Invalid option. Please try again.");
            }
        } catch(InputMismatchException e){
            System.out.println("\n  [!] Invalid input!");
            input.nextLine(); // reset buffer (clear input)
        }
    }

    private void scheduleMaintenance(){
        System.out.print("  Enter Vehicle ID -> ");
        String vehicleID = input.next();

        Vehicle v = fleetManager.findByID(vehicleID);
        if(v == null){
            System.out.println("\n  [!] Vehicle not found: " + vehicleID);
            return; // Stop the operation immediately
        }

        // if Status = IN_USE, stop operation
        if(v.getStatus() == Vehicle.VehicleStatus.IN_USE){
            System.out.println("\n  [!] Cannot schedule maintenance - vehicle is currently in use.");
            return;
        }

        // if Status = UNDER_MAINTENANCE, stop operation bc already in maintenance
        if(v.getStatus() == Vehicle.VehicleStatus.UNDER_MAINTENANCE){
            System.out.println("\n  [!] Cannot schedule maintenance - vehicle already under maintenance.");
            return;
        }

        v.scheduleMaintenance();
    }

    private void completeMaintenance(){
        System.out.print("  Enter Vehicle ID -> ");
        String vehicleID = input.next();

        Vehicle v = fleetManager.findByID(vehicleID);
        if(v == null){
            System.out.println("\n  [!] Vehicle not found: " + vehicleID);
            return; // Stop the operation immediately
        }

        // if Status not equal (!=) UNDER_MAINTENANCE, don't continue operation
        if(v.getStatus() != Vehicle.VehicleStatus.UNDER_MAINTENANCE){
            System.out.println("\n  [!] Vehicle is not under maintenance!");
            System.out.println("  >>> Current Status: " + v.getStatus());
            return; // if UNDER_MAINTENANCE, stop the operation
        }

        v.completeMaintenance();
    }

    private void removeVehicle(){
        try{
            System.out.print("  Enter Vehicle ID -> ");
            String vehicleID = input.next();

            Vehicle v = fleetManager.findByID(vehicleID);
            if(v == null){
                System.out.println("\n  [!] Vehicle not found: " + vehicleID);
                return; // Stop the operation immediately
            }

            System.out.println("\n  Vehicle to be removed:");
            v.displayInfo();

            if(v.getStatus() == Vehicle.VehicleStatus.IN_USE){
                System.out.println("\n  [!] Cannot remove this vehicle - currently in use!");
                System.out.println("  [i] Please release the vehicle from courier assignment first.");
                return;
            }

            System.out.print("  Are you sure to confirm delete the vehicle? (Y/N) -> ");
            String confirm = input.next();
            input.nextLine();

            if(confirm.equalsIgnoreCase("y")){
                fleetManager.removeVehicle(vehicleID);
                System.out.println("\n  [DONE] Vehicle " + vehicleID + " removed from fleet.");

            }
            else{
                System.out.println("\n  [ℹ] Remove operation cancelled.");
            }
        }catch(VehicleNotFoundException e){
            // e.getMessage() = Retrive and print the exception message
            System.out.println("\n  [!] " + e.getMessage());
        }catch(IllegalStateException e){
            System.out.println("\n  [!] " + e.getMessage());
        }
    }

    private void viewMaintenanceDue(){
        List<Vehicle> due = fleetManager.getVehiclesNeedingMaintenance();

        if(due.isEmpty()){
            System.out.println("\n  [i] No vehicles under maintenance or need maintenance.");
            return;
        }

        System.out.println("\n  ╔════════════════════════════════════════════════════════╗");
        System.out.println("  ║                   VEHICLES MAINTENANCE                 ║");
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

    private void assignVehicleToCourier(){
        System.out.println("\n  ╔══════════════════════════════════════════╗");
        System.out.println("  ║        ASSIGN VEHICLE TO COURIER         ║");
        System.out.println("  ╚══════════════════════════════════════════╝");

        try{
            // Show available vehicle (status = AVAILABLE)
            List<Vehicle> available = fleetManager.getAvailableVehicles();

            if(available.isEmpty()){
                System.out.println("\n  [!] No available vehicles.");
                System.out.println("  [i] All vehicles are either \"IN_USE\" or \"UNDER_MAINTENANCE\" ");
                return;
            }

            System.out.println("\n  Available Vehicles:\n");
            System.out.printf("  %-10s %-12s %-12s %-10s%n", "ID", "Plate", "Type", "Max Load");
            System.out.println("  " + "─".repeat(60));
        
            for (Vehicle v : available){
                System.out.printf("  %-10s %-12s %-12s %.1f kg%n", v.getVehicleID(), v.getPlateNumber(), v.getType(), v.getMaxLoadKg());
            }

            // Check if available
            System.out.print("\n  Enter Vehicle ID -> ");
            String vehicleID = input.next();
            input.nextLine(); // clear buffer

            Vehicle v = fleetManager.findByID(vehicleID);

            if(v == null){
                System.out.println("\n  [!] Vehicle \"" + vehicleID + "\" not found.");
                return;
            }

            if(!v.isAvailable()){
                System.out.println("\n  [!] Vehicle \"" + vehicleID + "\" is not available.");
                System.out.println("  [i] Current Status -> " + v.getStatus());
                return;
            }

            // Get available couriers
            ArrayList<Courier> couriers = UserRegistry.getAllCouriers();

            if(couriers.isEmpty()){
                System.out.println("\n  [!] No couriers available in the system.");
                return;
            }

            System.out.println("\n\n  Available Couriers:\n");
            System.out.printf("  %-10s %-32s %-12s %-15s%n", "ID", "Name", "Status", "Current Vehicle");
            System.out.println("  " + "─".repeat(72));
            
            for (Courier c : couriers) {
                System.out.printf("  %-10s %-32s %-12s %-15s%n", c.getPersonID(), c.getName(), c.isOnDuty() ? "ON DUTY" : "OFF DUTY", c.getAssignedVehicleID() != null ? c.getAssignedVehicleID() : "None");
            }

            System.out.print("\n  Enter Courier ID -> ");
            String courierID = input.next();
            input.nextLine();

            Courier courier = UserRegistry.getCourierById(courierID);

            if(courier == null){
                System.out.println("\n  [!] Courier \"" + courierID + "\" not found.");
                return;
            }

            // if assigned vehicle ID = ???, means already got vehicle assigned
            if(courier.getAssignedVehicleID() != null){
                System.out.println("\n  [!] Courier already has a vehicle assigned, ID -> \"" + courier.getAssignedVehicleID() + "\"");
                System.out.print("  Replace with " + vehicleID + "? (Y/N) -> ");
                String confirm = input.next();
                input.nextLine();

                if(!confirm.equalsIgnoreCase("Y")){
                    System.out.println("\n  [i] Assignment cancelled.");
                    return;
                }
            }
            
            
            // Summary and Confirmation
            System.out.println("\n  ─────── Assignment Summary ───────");
            System.out.println("  Vehicle : " + " [" + v.getVehicleID() + "] " + v.getPlateNumber());
            System.out.println("  Courier : " + courier.getName() + " (" + courierID + ")");
            System.out.print("\n  Confirm assignment? (Y/N) -> ");
            String confirm = input.next();
            input.nextLine();

            if(confirm.equalsIgnoreCase("Y")){
                // Call FleetManager to assign (will update both vehicle and courier)
                // Release old vehicle first before assigning
                try{
                    fleetManager.releaseVehicle(courier.getAssignedVehicleID());
                } catch(VehicleNotFoundException e){
                    // Nothing, cause if not found, continue anyway
                }

                fleetManager.assignToCourier(vehicleID, courierID);

                System.out.println("\n  ╔═══════════════════════════════════════╗");
                System.out.println("  ║     VEHICLE ASSIGNED SUCCESSFULLY     ║");
                System.out.println("  ╚═══════════════════════════════════════╝");
                System.out.println("\n  [i] " + courier.getName() + " can now use " + vehicleID + ".\n");
            }
            else{
                System.out.println("\n  [i] Assignment cancelled.");
            }
            } catch (VehicleNotFoundException e){
                System.out.println("\n  [!] " + e.getMessage());
            } catch (IllegalStateException e){
                System.out.println("\n  [!] " + e.getMessage());
            } catch (InputMismatchException e){
                System.out.println("\n  [!] Invalid input!");
                input.nextLine();
            }
    }

    private void releaseVehicleFromCourier(){
        try{
            System.out.println("\n  ╔══════════════════════════════════════════╗");
            System.out.println("  ║       RELEASE VEHICLE FROM COURIER       ║");
            System.out.println("  ╚══════════════════════════════════════════╝");

            List<Vehicle> inUse = fleetManager.getVehiclesByStatus(Vehicle.VehicleStatus.IN_USE);

            if(inUse.isEmpty()){
                System.out.println("\n  [!] No vehicles currently assigned to couriers.");
            }

            System.out.println("\n  Vehicles Currently Assigned:");
            System.out.printf("  %-12s %-12s %-12s %-15s%n", "Vehicle ID", "Plate", "Type", "Assigned To");
            System.out.println("  " + "─".repeat(60));

            for(Vehicle v : inUse){
                String courierName = "Unknown";
                if(v.getAssignedCourierID() != null){
                    Courier c = UserRegistry.getCourierById(v.getAssignedCourierID());
                    if(c != null){
                        courierName = c.getName();
                    }
                }

                System.out.printf("  %-12s %-12s %-12s %-15s%n", v.getVehicleID(), v.getPlateNumber(), v.getType(), courierName);
            }

            // Get vehicleID to release
            System.out.print("\n  Enter Vehicle ID to release -> ");
            String vehicleID = input.next();
            input.nextLine();

            Vehicle v = fleetManager.findByID(vehicleID);

            if(v == null){
                System.out.println("\n  [!] Vehicle \"" + vehicleID + "\" not found." );
                return;
            }

            // Check if vehicle is already assigned
            if(v.getStatus() != Vehicle.VehicleStatus.IN_USE){
                System.out.println("\n  [!] Vehicle is not currently assigned to a courier.");
                System.out.println("  [i] Current Status -> " + v.getStatus());
                return;
            }

            // Courier info for display
            String courierInfo = "Unknown";
            if(v.getAssignedCourierID() != null){
                Courier c = UserRegistry.getCourierById(v.getAssignedCourierID());

                if(c != null){
                    courierInfo = c.getName() + " (" + c.getPersonID() + ")";
                }
            }

            // Show summary and confirmation
            System.out.println("\n  ─────── Release Summary ───────");
            System.out.println("  Vehicle : " + vehicleID + " (" + v.getType() + ")");
            System.out.println("  Currently assigned to : " + courierInfo);
            System.out.print("\n  Confirm release? (Y/N) -> ");
            String confirm = input.next();
            input.nextLine();

            if(confirm.equalsIgnoreCase("Y")){
                // Call FleetManager to release (will update both Vehicle and Courier)
                fleetManager.releaseVehicle(vehicleID);
                
                System.out.println("\n  ╔═════════════════════════════════════════╗");
                System.out.println("  ║      VEHICLE RELEASED SUCCESSFULLY      ║");
                System.out.println("  ╚═════════════════════════════════════════╝");
                System.out.println("\n  [i] Vehicle \"" + vehicleID + "\" is now AVAILABLE.");
            } else {
                System.out.println("\n  [i] Release cancelled.");
            }
        } catch(VehicleNotFoundException e){
                System.out.println("\n  [!] " + e.getMessage());
        } catch (InputMismatchException e){
            System.out.println("\n  [!] Invalid input!");
            input.nextLine();
        }
        
    }

    // Menu Design
    private void displayMenu(){
        System.out.println("\n  ╔══════════════════════════════════════╗");
        System.out.println("  ║         FLEET MANAGEMENT MENU        ║");
        System.out.println("  ╠══════════════════════════════════════╣");
        System.out.println("  ║  1.   Add Vehicle                    ║");
        System.out.println("  ║  2.   View All Vehicles              ║");
        System.out.println("  ║  3.   View Vehicle Details           ║");
        System.out.println("  ║  4.   Update Vehicle Info            ║");
        System.out.println("  ║  5.   Schedule Maintenance           ║");
        System.out.println("  ║  6.   Complete Maintenance           ║");
        System.out.println("  ║  7.   Remove Vehicle                 ║");
        System.out.println("  ║  8.   View Vehicles Maint Status     ║");
        System.out.println("  ║  9.   Assign Vehicle to Courier      ║");
        System.out.println("  ║  10.  Release Vehicle From Courier   ║");
        System.out.println("  ║  0.   Return to Admin Portal         ║");
        System.out.println("  ╚══════════════════════════════════════╝");
        System.out.print("  Choice -> ");
    }
}