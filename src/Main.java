import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;

public class Main {
    // GLOBAL VARIABLES
    static Scanner input = new Scanner(System.in);

    static FleetManager fleetManager = new FleetManager();
    static FleetManagement fleetManagement = new FleetManagement(fleetManager, input);

    static ShipmentRegistry shipmentRegistry = new ShipmentRegistry();
    static ShipmentManagement shipmentManagement = new ShipmentManagement(shipmentRegistry, input);

    static QuickTrack quickTrack = new QuickTrack(shipmentRegistry, input);

    public static void main(String[] args){
        fleetManager.initializeData();      // Initialize fleet sample data
        shipmentRegistry.initializeData();  // Initialize shipments sample data
        UserRegistry.initializeData();      // Initialize login credentials data
        UserRegistry.assignSampleShipments(shipmentRegistry);   // Assigned sample shipments to Courier
        
        displayLogo();
        System.out.println("\n  Welcome to Courier & Logistics Management System!");

        boolean running = true;
        while(running){
            displayMainMenu();
            System.out.print("  Select portal -> ");
            int choice = input.nextInt();
            switch(choice){
                case 1 -> senderPortal();
                case 2 -> courierPortal();
                case 3 -> adminLogin();
                case 4 -> quickTrack.track();
                case 0 -> {
                    System.out.println("\n  Thank you for using CourierPro. Goodbye!\n");
                    running = false;
                }
                default -> System.out.println("  [!] Invalid option. Please try again.");
            }
        }
        input.close();
    }

    // SENDER PORTAL (1)
    public static void senderPortal(){
        System.out.println("Sender");
    }

    // COURIER(DRIVER) PORTAL (2)
    public static void courierPortal(){
        System.out.println("\n  ╔══════════════════════════════════════════╗");
        System.out.println("  ║         COURIER (DRIVER) LOGIN           ║");
        System.out.println("  ╚══════════════════════════════════════════╝");
        System.out.print("  Enter Login ID -> ");
        String idInput = input.next();
 
        if (UserRegistry.checkCourierIdExists(idInput)) {
            System.out.print("  Enter Password -> ");
            String passInput = input.next();
 
            Courier user = UserRegistry.checkCourier(idInput, passInput);
 
            if (user != null) {
                System.out.println("  Access Granted! Welcome, " + user.getName());
                // Create CourierPortal with the logged-in courier + shared scanner
                CourierPortal portal = new CourierPortal(user, input);
                portal.show();
            } else {
                System.out.println("\n  [!] Invalid credentials, please try again.");
            }
        } else {
            System.out.println("\n  >> Error: Login ID does not exist.");
        }
    }

    // ADMIN PORTAL (3) & LOGIN
    public static void adminLogin(){
        System.out.println("\n\n  ╔══════════════════════════════════════════╗");
        System.out.println("  ║               ADMIN LOGIN                ║");
        System.out.println("  ╚══════════════════════════════════════════╝");
        System.out.print("  Enter Login ID -> ");
        String idInput = input.next();

        // Check if ID exists or not
        if(UserRegistry.checkAdminIdExists(idInput)){
            System.out.print("  Enter Password -> ");
            String passInput = input.next();
    
            // Search the admins list(registry) for matching credentials, return specific object if found, or 'null' if no matches
            // bc checkAdmin returns entire "Admin Object", it also requires Admin data type
            Admin user = UserRegistry.checkAdmin(idInput, passInput);
            
            if(user != null){
                System.out.println("  Access Granted! Welcome " + user.getName());
                adminPortal(user);
            }
            else{
                System.out.println("\n  [!] Invalid credentials, please try again.");
            }
        }
        else{
            System.out.println("\n  >> Error: Login ID does not exist.");
        }
    }

    public static void adminPortal(Admin user){
        // Admin (data type) parameter to access the "Admin Objects" to display details

        boolean active = true;
        while(active){
            System.out.println("\n\n  ╔══════════════════════════════════════════╗");
            System.out.println("  ║               ADMIN PORTAL               ║");
            System.out.printf("  ║ %-40s ║\n", "Welcome, " + user.getName());
            System.out.printf("  ║ %-40s ║\n", "Role -> " + user.getAdminRole());
            System.out.println("  ╚══════════════════════════════════════════╝");
            System.out.println("\n  [ SHIPMENT CONTROL ]");
            System.out.println("   1. Assign Driver to Shipment");    
            System.out.println("   2. Update Shipment Status");       
            System.out.println("   3. Calculate/Review Shipping Fees");
            System.out.println("   4. Handle Delivery Failures");     
            System.out.println("\n  [ FLEET & LOGISTICS ]");
            System.out.println("   5. View All Shipments");
            System.out.println("   6. Manage Fleet & Maintenance");   
            System.out.println("   7. View Driver Delivery Lists");    
            System.out.println("\n  [ SYSTEM ]");
            System.out.println("   8. View My Profile");
            System.out.println("   0. Back to Main Menu (Logout)");
            System.out.println("  ──────────────────────────────────────────");

            System.out.print("  Choice -> ");
            int choice = input.nextInt();
            switch(choice){
                case 1 -> assignDriverToShipment();
                case 2 -> shipmentManagement.show();
                case 3 -> shipmentManagement.show();
                case 4 -> handleDeliveryFailure();
                case 5 -> shipmentManagement.show();
                case 6 -> fleetManagement.show();
                case 7 -> displayDriverWorkload();
                case 8 -> viewAdminProfile();
                case 0 -> {
                    System.out.println("\n  [i] Logging out...");
                    active = false;
                }
                default -> System.out.println("\n  [!] Invalid option. Please enter a number.");
            }
        }
    }


    // ADMIN METHODS
    // OPTION 1
    static void assignDriverToShipment(){
        System.out.println("\n  ╔════════════════════════════════════════════════════════╗");
        System.out.println("  ║               ASSIGN DRIVER TO SHIPMENT                ║");
        System.out.println("  ╚════════════════════════════════════════════════════════╝");

        // Get list of all avaiable couriers
        ArrayList<Courier> availableCouriers = UserRegistry.getAllCouriers();

        if(availableCouriers.isEmpty()){
            System.out.println("\n  [i] No couriers available in the system.");
            return;
        }
        
        System.out.println("\n  AVAILABLE COURIERS:\n");
        System.out.printf("  %-8s %-32s %-12s %-15s %-10s%n", "ID", "Name", "Status", "Vehicle", "Assigned");
        System.out.println("  " + "─".repeat(80));

        for (Courier c : availableCouriers){
            System.out.printf("  %-8s %-32s %-12s %-15s %-10d%n", c.getPersonID(), c.getName(), 
            c.isOnDuty() ? "ON DUTY" : "OFF DUTY", 
            // if vehicleID != null, use vehicleID, else use "None"
            c.getAssignedVehicleID() != null ? c.getAssignedVehicleID() : "None",
            c.getAssignedShipments().size());
        }

        // Courier selection
        System.out.print("\n  Enter Courier ID -> ");
        String courierID = input.next();

        Courier courier = UserRegistry.getCourierById(courierID);

        if(courier == null){
            System.out.println("\n  [!] Courier \"" + courierID + "\" not found.");
            return;
        }

        if(!courier.isOnDuty()){
            System.out.println("\n  [!] Courier\"" + courierID + "\" is OFF DUTY. Cannot assign shipments");
            System.out.println("  [i] Courier must be ON DUTY first.");
            return;
        }

        if(courier.getAssignedVehicleID() == null){
            System.out.println("\n  [!] Courier \"" + courierID + "\" has no assiged vehicle.");
            System.out.println("  [i] Please assign a vehicle first in Fleet Management (Option 6).");
            return;
        }

        // Retreive shipment so can assign
        System.out.print("  Enter Tracking ID -> ");
        String trackingID = input.next();

        Shipment shipment = shipmentRegistry.findByTrackingID(trackingID);

        if(shipment == null){
            System.out.println("\n  [!] Shipment \"" + trackingID + "\" not found.");
            return;
        }

        if(shipment.getStatus() != Shipment.ShipmentStatus.PAID){
            System.out.println("\n  [!] Can only assign PAID status shipments to couriers.");
            System.out.println("  [i] Current status -> " + shipment.getStatus());
            return;
        }

        if(shipment.getCourierID() != null){
            System.out.println("\n  [!] Shipment already assigned to courier \"" + shipment.getCourierID() + "\".");
            System.out.print("  Reassign to " + courier.getName() + "? (Y/N) -> ");
            String confirm = input.next();
            if(!confirm.equalsIgnoreCase("Y")){
                System.out.println("\n  [i] Assignment cancelled.");
                return;
            }   

            Courier oldCourier = UserRegistry.getCourierById(shipment.getCourierID());
            // getAssignedShipments = ArrayList
            if(oldCourier != null){
                oldCourier.getAssignedShipments().remove(shipment);
            }


            // Remove the shipment from old Courier if REASSIGNED
        }

        courier.receiveShipment(shipment);
        System.out.println("\n  ╔════════════════════════════════════════════════════════╗");
        System.out.println("  ║              DRIVER ASSIGNED SUCCESSFULLY              ║");
        System.out.println("  ╚════════════════════════════════════════════════════════╝");
        System.out.println("  Courier     : " + courier.getName() + " (" + courier.getPersonID() + ")");
        System.out.println("  Tracking    : " + trackingID);
        System.out.println("  Destination : " + shipment.getDeliveryAddress());
    }

    // OPTION 4
    static void handleDeliveryFailure(){
        System.out.println("\n  ╔════════════════════════════════════════════════════════╗");
        System.out.println("  ║               HANDLE DELIVERY FAILURES                 ║");
        System.out.println("  ╚════════════════════════════════════════════════════════╝");

        List<Shipment> failedShipments = shipmentRegistry.findByStatus(Shipment.ShipmentStatus.FAILED_ATTEMPT);

        if(failedShipments.isEmpty()){
            System.out.println("\n  [!] No failed delivery attempts.");
            return;
        }

        System.out.println("\n  FAILED DELIVERY ATTEMPTS:");
        System.out.println("  Total: " + failedShipments.size() + " shipment(s)\n");
        System.out.printf("  %-15s %-15s %-20s%n", "Tracking ID", "Courier", "Last Update");
        System.out.println("  " + "─".repeat(60));

        for(Shipment s : failedShipments){
            String courierID = s.getCourierID() != null ? s.getCourierID() : "Not Assigned";

            System.out.printf("  %-15s %-15s %-20s%n", s.getTrackingID(), courierID, s.getCreatedAt().toString().substring(0, 19).replace('T', ' '));
        }

        // Admin can view details or reassign shipment
        System.out.print("\n  Enter Tracking ID to view details (or 0 to cancel) -> ");
        String trackingID = input.next();

        // use .equals bc string
        if(trackingID.equals("0")){
            return;
        }

        Shipment s = shipmentRegistry.findByTrackingID(trackingID);

        if(s == null){
            System.out.println("\n  [!] Shipment \"" + trackingID + "\" not found.");
            return;
        }
        if(s.getStatus() != Shipment.ShipmentStatus.FAILED_ATTEMPT){
            System.out.println("\n  [!] Shipment \"" + trackingID + "\" status is not \"FAILED_ATTEMPT\"");
            System.out.println("  [i] Shipment Current Status -> " + s.getStatus());
            return;
        }

        // Show full details and history
        s.displayFullDetails();
        s.displayTrackingHistory();

        System.out.println("\n  What would you like to do?");
        System.out.println("   1. Reassign to another courier");
        System.out.println("   2. Mark as OUT_FOR_DELIVERY (retry)");
        System.out.println("   0. Back");
        System.out.print("  Choice -> ");
        
        int choice = input.nextInt();
        input.nextLine();
        switch(choice){
            case 1 -> {
                // Reassign to another courier
                System.out.print("\n  Enter new Courier ID -> ");
                String newCourierID = input.next();

                Courier newCourier = UserRegistry.getCourierById(newCourierID);

                if(newCourier == null){
                    System.out.println("\n  [!]  Courier \"" + newCourierID + "\" not found.");
                    return;
                }

                // Remove from old courier if assigned
                if(s.getCourierID() != null){
                    Courier oldCourier = UserRegistry.getCourierById(s.getCourierID());
                    if(oldCourier != null){
                        oldCourier.getAssignedShipments().remove(s);
                    }
                }

                newCourier.receiveShipment(s);
                // Status back to PAID again to redo everything
                s.updateStatus(Shipment.ShipmentStatus.PAID, "Reassigned to courier " + newCourier.getName());
                System.out.println("  [DONE] Shipment reassigned to " + newCourier.getName() + "(" + newCourier.getPersonID() + ")");
            }
            case 2 -> {
                // Retry delivery
                s.updateStatus(Shipment.ShipmentStatus.OUT_FOR_DELIVERY, "Retry delivery attempt");
                System.out.println("\n  [DONE] Status updated to OUT_FOR_DELIVERY");
            }
            case 3 -> {
                System.out.print("\n  Confirm cancellation? (Y/N) -> ");
                String confirm = input.next();


                if(confirm.equalsIgnoreCase("Y")){
                    shipmentRegistry.cancelShipment(trackingID);
                }
            }
        }
    }

    // OPTION 7
    static void displayDriverWorkload(){
        System.out.println("\n  ╔════════════════════════════════════════════════════════╗");
        System.out.println("  ║                 DRIVER DELIVERY LISTS                  ║");
        System.out.println("  ╚════════════════════════════════════════════════════════╝");

        ArrayList<Courier> allCouriers = UserRegistry.getAllCouriers();

        if(allCouriers.isEmpty()){
            System.out.println("\n  [!] No couriers available in the system.");
        }

        System.out.println("\n  Option:");
        System.out.println("   1. View All Couriers");
        System.out.println("   2. View Specific Courier");
        System.out.print("  Choice -> ");

        int choice = input.nextInt();
        input.nextLine();

        if(choice == 2){
            System.out.print("  Enter Courier ID -> ");
            String courierID = input.next();

            Courier courier = UserRegistry.getCourierById(courierID);

            if(courier == null){
                System.out.println("\n  [!] Courier \"" + courierID + "\" not found.");
                return;
            }

            displayCourierDeliveryList(courier);
        }
        else{
            for(Courier courier : allCouriers){
                displayCourierDeliveryList(courier);
                System.out.println();
            }

            // Summary
            int totalShipments = 0;
            for(Courier c : allCouriers){
                totalShipments += c.getAssignedShipments().size();
            }

            System.out.println("  ╔═════════════════════════════════════════════════════╗");
            System.out.println("  ║                      SUMMARY                        ║ ");
            System.out.println("  ╠═════════════════════════════════════════════════════╣");
            System.out.printf("  ║ %-51s ║\n", "Total Couriers  : " + allCouriers.size());
            System.out.printf("  ║ %-51s ║\n", "Total Shipments : " + totalShipments);
            System.out.println("  ╚═════════════════════════════════════════════════════╝");
        }
    }
    
    public static void viewAdminProfile(){
        System.out.print("\n  Your Admin ID -> ");
        String id = input.next();

        Admin user = UserRegistry.getAdminById(id);
        if(user != null){
            user.displayInfo();
        }
        else{
            System.out.println("\n  >> Error: Admin ID not found.");
        }
    }

    // DISPLAY DESIGN
    // HELPER METHOD FOR COURIER DELIVERY LIST DISPLAYING (OPTION 7)
    static void displayCourierDeliveryList(Courier courier){
        System.out.println("\n  ┌────────────────────────────────────────────────────────┐");
        System.out.printf("  │  COURIER: %-45s│%n", courier.getName() + " (" + courier.getPersonID() + ")");
        System.out.printf("  │  Status : %-45s│%n", courier.isOnDuty() ? "ON DUTY" : "OFF DUTY");
        System.out.printf("  │  Vehicle: %-45s│%n", courier.getAssignedVehicleID() != null ? courier.getAssignedVehicleID() : "None");
        System.out.printf("  │  Assigned: %-44s│%n", courier.getAssignedShipments().size() + " shipment(s)");
        System.out.println("  └────────────────────────────────────────────────────────┘");
        
        if(courier.getAssignedShipments().isEmpty()){
            System.out.println("  No shipments assigned.");
        } 
        else{
            System.out.printf("  %-15s %-34s %-15s%n", "Tracking ID", "Destination", "Status");
            System.out.println("  " + "─".repeat(70));
            
            for(Shipment s : courier.getAssignedShipments()){
                String destination = s.getDeliveryAddress();
                if(destination.length() > 30){
                    destination = destination.substring(0, 27) + "...";
                }
                
                System.out.printf("  %-15s %-34s %-15s%n", s.getTrackingID(), destination, s.getStatus());
            }
        }
    }

    public static void displayMainMenu(){
        System.out.println();
        System.out.println("  ╔═══════════════════════════════════════════╗");
        System.out.println("  ║   COURIER & LOGISTICS MANAGEMENT SYSTEM   ║");
        System.out.println("  ╠═══════════════════════════════════════════╣");
        System.out.println("  ║  1. Sender Portal                         ║");
        System.out.println("  ║  2. Courier (Driver) Portal               ║");
        System.out.println("  ║  3. Admin Portal                          ║");
        System.out.println("  ║  4. Quick Track (Enter ID)                ║");
        System.out.println("  ║  0. Exit                                  ║");
        System.out.println("  ╚═══════════════════════════════════════════╝");
    }

    public static void displayLogo(){
        System.out.println("\n  ____                 _           ____            ");
        System.out.println(" / ___|___  _   _ _ __(_) ___ _ __|  _ \\ _ __ ___  ");
        System.out.println("| |   / _ \\| | | | '__| |/ _ \\ '__| |_) | '__/ _ \\ ");
        System.out.println("| |__| (_) | |_| | |  | |  __/ |  |  __/| | | (_) |");
        System.out.println(" \\____\\___/ \\__,_|_|  |_|\\___|_|  |_|   |_|  \\___/ ");
        System.out.println("====================================================");
    }
}