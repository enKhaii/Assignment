// AdminPortal - class that consists of all ADMIN PORTAL methods

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class AdminPortal {
    private Scanner input;
    private ShipmentManagement shipmentManagement;
    private ShipmentRegistry shipmentRegistry;
    private FleetManagement fleetManagement;

    // Constructor
    public AdminPortal(Scanner input, ShipmentManagement shipmentManagement, ShipmentRegistry shipmentRegistry, FleetManagement fleetManagement){
        this.input = input;
        this.shipmentManagement = shipmentManagement;
        this.shipmentRegistry = shipmentRegistry;
        this.fleetManagement = fleetManagement;
    }

    public void start(Admin user, Scanner input){
        boolean active = true;
        while(active){
            System.out.println("\n\n  ╔══════════════════════════════════════════╗");
            System.out.println("  ║               ADMIN PORTAL               ║");
            System.out.printf("  ║ %-40s ║\n", "Welcome, " + user.getName());
            System.out.printf("  ║ %-40s ║\n", "Role -> " + user.getAdminRole());
            System.out.println("  ╚══════════════════════════════════════════╝");
            System.out.println("\n  [ USER MANAGEMENT ]");
            // only display if ROLE == SUPER_ADMIN
            if(user.getAdminRole() == Admin.Role.SUPER_ADMIN){
                System.out.println("   1. Manage Users");
            }
            System.out.println("\n  [ SHIPMENT CONTROL ]");
            System.out.println("   2. Assign Driver to Shipment");    
            System.out.println("   3. Update Shipment Status");       
            System.out.println("   4. Calculate/Review Shipping Fees");
            System.out.println("   5. Handle Delivery Failures");     
            System.out.println("\n  [ FLEET & LOGISTICS ]");
            System.out.println("   6. View All Shipments");
            System.out.println("   7. Manage Fleet & Maintenance");   
            System.out.println("   8. View Driver Delivery Lists");    
            System.out.println("\n  [ SYSTEM ]");
            System.out.println("   9. View My Profile");
            System.out.println("   0. Back to Main Menu (Logout)");
            System.out.println("  ──────────────────────────────────────────");

            System.out.print("  Choice -> ");
            int choice = input.nextInt();
            input.nextLine();
            
            switch(choice){
                case 1 -> {
                    if(user.getAdminRole() == Admin.Role.SUPER_ADMIN){
                        manageUsers();
                    }
                    else{
                        System.out.println("\n  [!] ACCESS DENIED. You do not have permission to access this feature.");
                    }
                }
                case 2 -> assignDriverToShipment();
                case 3 -> shipmentManagement.show();
                case 4 -> shipmentManagement.show();
                case 5 -> handleDeliveryFailure();
                case 6 -> shipmentManagement.show();
                case 7 -> fleetManagement.show();
                case 8 -> displayDriverWorkload();
                case 9 -> viewAdminProfile(user);
                case 0 -> {
                    System.out.println("\n  [i] Logging out...");
                    active = false;
                }
                default -> System.out.println("\n  [!] Invalid option. Please enter a number.");
            }
        }
    }

    // OPTION 1
    private void manageUsers(){
        boolean inUserManagementMenu = true;
        while(inUserManagementMenu){
            displayMenu();
            int choice = input.nextInt();
            input.nextLine();   // clear buffer
    
            switch(choice){
                case 1 -> registerNewAdmin();
                case 2 -> registerNewCourier();
                case 3 -> viewAllAdmins();
                case 4 -> viewAllCouriers();
                case 0 -> {
                    System.out.println("\n  [i] Returning to Admin Portal...");
                    inUserManagementMenu = false;
                }
                default -> System.out.println("\n  [!] Invalid option. Please try again.");
            }
        }
    }

    private void registerNewAdmin(){
        System.out.println("\n  ╔════════════════════════════════════════════════════════╗");
        System.out.println("  ║                   REGISTER NEW ADMIN                   ║");
        System.out.println("  ╚════════════════════════════════════════════════════════╝");

        try{
            // Generate Admin ID
            String adminID = "ADM" + String.format("%02d", UserRegistry.getAllAdmins().size() + 1);
            System.out.println("  [i] Admin ID (auto-generated) -> " + adminID);

            // Get loginID
            System.out.print("\n  Login ID (no spaces) -> ");
            String loginID = input.next();
            input.nextLine();

            // Check if loginID already exists
            if(UserRegistry.checkAdminIdExists(loginID)){
                System.out.println("\n  [!] Login ID already exists. Please use a different Login ID.");
                return;
            }

            // Get name
            System.out.print("\n  Full Name -> ");
            String name = input.nextLine();

            // Get password
            System.out.print("\n  Password (no spaces) -> ");
            String password = input.next();
            input.nextLine();

            // Confirm password
            System.out.print("\n  Confirm Password -> ");
            String confirmPassword = input.next();
            input.nextLine();

            // Check if passworrd matches (password & confirmPassword)
            if(!password.equals(confirmPassword)){
                System.out.println("\n  [!] Passwords do not match! Please try again.");
                return;
            }

            // Get email
            System.out.print("\n  Email -> ");
            
            String email = input.next();
            input.nextLine();
            
            // Validate email format
            if(!email.contains("@") || !email.contains(".")){
                System.out.println("\n  [!] Invalid email format! Please try again.");
                System.out.println("  [i] Example format -> abc123@gmail.com");
                return;
            }

            // Get phone
            System.out.print("\n  Phone (e.g. 012-345-6789) -> ");
            String phone = input.next();
            input.nextLine();

            // Select Admin Role
            System.out.println("\n  Select Admin Role:");
            System.out.println("   1. SUPER_ADMIN");
            System.out.println("   2. OPERATIONS_MANAGER");
            System.out.println("   3. DISPATCH_OFFICER");
            System.out.print("  Choice -> ");
            int roleChoice = input.nextInt();
            input.nextLine();

            Admin.Role role = switch(roleChoice){
                case 1 -> Admin.Role.SUPER_ADMIN;
                case 2 -> Admin.Role.OPERATIONS_MANAGER;
                // default role is DISPATCH_OFFICER if no other specify options
                default -> Admin.Role.DISPATCH_OFFICER;
            };
            
            // Show Summary
            Admin newAdmin = new Admin(adminID, loginID, name, password, email, phone, role);

            System.out.println("\n  ──── Registration Summary ────");
            System.out.println("  Admin ID   : " + newAdmin.getPersonID());
            System.out.println("  Login ID   : " + newAdmin.getLoginID());
            System.out.println("  Name       : " + newAdmin.getName());
            System.out.println("  Email      : " + newAdmin.getEmail());
            System.out.println("  Phone      : " + newAdmin.getPhoneNum());
            System.out.println("  Role       : " + newAdmin.getAdminRole());
            System.out.println("  Department : " + newAdmin.getDepartment());
            System.out.print("\n  Confirm registration? (Y/N) -> ");
            String confirm = input.nextLine();

            if(confirm.equalsIgnoreCase("Y")){
                UserRegistry.addAdmin(newAdmin);

                System.out.println("\n  ╔═════════════════════════════════════════════════════╗");
                System.out.println("  ║            ADMIN REGISTERED SUCCESSFULLY            ║");
                System.out.println("  ╚═════════════════════════════════════════════════════╝");
                System.out.println("  Admin ID: " + newAdmin.getPersonID());
                System.out.println("  Login ID: " + newAdmin.getLoginID());
                System.out.println("  Password: " + newAdmin.getPassword());
                System.out.println("\n  [i] Please save these credentials securely.");
            }
            else{
                System.out.println("\n  [!] Registration cancelled. Information is not saved.");
            }
        } catch(InputMismatchException e){
            System.out.println("\n  [!] Invalid input. Please try again.");
            input.nextLine();
        }
    }

    private void registerNewCourier(){
        System.out.println("\n  ╔══════════════════════════════════════════════════════════╗");
        System.out.println("  ║                   REGISTER NEW COURIER                   ║");
        System.out.println("  ╚══════════════════════════════════════════════════════════╝");

        try{
            // Generate Admin ID
            String courierID = "CRR" + String.format("%02d", UserRegistry.getAllCouriers().size() + 1);
            System.out.println("  [i] Courier ID (auto-generated) -> " + courierID);

            // Get loginID
            System.out.print("\n  Login ID (no spaces) -> ");
            String loginID = input.next();
            input.nextLine();

            // Check if loginID already exists
            if(UserRegistry.checkCourierIdExists(loginID)){
                System.out.println("\n  [!] Login ID already exists. Please use a different Login ID.");
                return;
            }

            // Get name
            System.out.print("\n  Full Name -> ");
            String name = input.nextLine();

            // Get password
            System.out.print("\n  Password (no spaces) -> ");
            String password = input.next();
            input.nextLine();

            // Confirm password
            System.out.print("\n  Confirm Password -> ");
            String confirmPassword = input.next();
            input.nextLine();

            // Check if passworrd matches (password & confirmPassword)
            if(!password.equals(confirmPassword)){
                System.out.println("\n  [!] Passwords do not match! Please try again.");
                return;
            }

            // Get email
            System.out.print("\n  Email -> ");
            
            String email = input.next();
            input.nextLine();
            
            // Validate email format
            if(!email.contains("@") || !email.contains(".")){
                System.out.println("\n  [!] Invalid email format! Please try again.");
                System.out.println("  [i] Example format -> abc123@gmail.com");
                return;
            }

            // Get phone
            System.out.print("\n  Phone (e.g. 012-345-6789) -> ");
            String phone = input.next();
            input.nextLine();

            // Get license number
            System.out.print("\n  Driver's License Number (e.g. D???????) -> ");
            String licenseNumber = input.next();
            input.nextLine();

            // Validate license number
            if(licenseNumber.trim().isEmpty() || licenseNumber.length() < 5){
                System.out.println("\n  [!] Invalid license number! Please try again.");
                return;
            }

            // Show Summary
            Courier newCourier = new Courier(courierID, loginID, name, password, email, phone, licenseNumber);

            System.out.println("\n  ──── Registration Summary ────");
            System.out.println("  Courier ID : " + newCourier.getPersonID());
            System.out.println("  Login ID   : " + newCourier.getLoginID());
            System.out.println("  Name       : " + newCourier.getName());
            System.out.println("  Email      : " + newCourier.getEmail());
            System.out.println("  Phone      : " + newCourier.getPhoneNum());
            System.out.println("  License No : " + newCourier.getLicenseNumber());
            System.out.print("\n  Confirm registration? (Y/N) -> ");
            String confirm = input.nextLine();

            if(confirm.equalsIgnoreCase("Y")){
                UserRegistry.addCourier(newCourier);

                System.out.println("\n  ╔═══════════════════════════════════════════════════╗");
                System.out.println("  ║          COURIER REGISTERED SUCCESSFULLY          ║");
                System.out.println("  ╚═══════════════════════════════════════════════════╝");
                System.out.println("  Courier ID: " + newCourier.getPersonID());
                System.out.println("  Login ID  : " + newCourier.getLoginID());
                System.out.println("  Password  : " + newCourier.getPassword());
                System.out.println("\n  [i] Courier is OFF DUTY by default.");
                System.out.println("  [i] Please assign a vehicle before courier can start deliveries.");
            }
            else{
                System.out.println("\n  [!] Registration cancelled. Information is not saved.");
            }
        } catch(InputMismatchException e){
            System.out.println("\n  [!] Invalid input. Please try again.");
            input.nextLine();
        }
    }

    private void viewAllAdmins(){
        System.out.println("\n  ╔════════════════════════════════════════════════════════╗");
        System.out.println("  ║                   ALL ADMINISTRATORS                   ║");
        System.out.println("  ╚════════════════════════════════════════════════════════╝");

        ArrayList<Admin> allAdmins = UserRegistry.getAllAdmins();

        if(allAdmins.isEmpty()){
            System.out.println("\n  [i] No admins registered.");
            return;
        }

        System.out.println("  Total: " + allAdmins.size() + " admin(s)\n");
        System.out.printf("  %-10s %-30s %-25s %-20s%n", "ID", "Name", "Role", "Department");
        System.out.println("  " + "─".repeat(93));
        
        for (Admin a : allAdmins) {
            System.out.printf("  %-10s %-30s %-25s %-20s%n", a.getPersonID(), a.getName(), a.getAdminRole(), a.getDepartment());
        }
    }

    private void viewAllCouriers(){
        System.out.println("\n  ╔════════════════════════════════════════════════════════╗");
        System.out.println("  ║                      ALL COURIERS                      ║");
        System.out.println("  ╚════════════════════════════════════════════════════════╝");

        ArrayList<Courier> allCouriers = UserRegistry.getAllCouriers();

        if(allCouriers.isEmpty()){
            System.out.println("\n  [i] No admins registered.");
            return;
        }

        System.out.println("  Total: " + allCouriers.size() + " courier(s)");
        System.out.println();
        System.out.printf("  %-10s %-30s %-12s %-15s %-10s%n", 
                        "ID", "Name", "Status", "Vehicle", "Delivered");
        System.out.println("  " + "─".repeat(82));
        
        for (Courier c : allCouriers) {
            System.out.printf("  %-10s %-30s %-12s %-15s %-10d%n", c.getPersonID(), c.getName(), c.isOnDuty() ? "ON DUTY" : "OFF DUTY", c.getAssignedVehicleID() != null ? c.getAssignedVehicleID() : "None", c.getDeliveredCount());
        }
    }

    // OPTION 2
    private void assignDriverToShipment(){
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
        input.nextLine();

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
        input.nextLine();

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
            input.nextLine();
            if(!confirm.equalsIgnoreCase("Y")){
                System.out.println("\n  [i] Assignment cancelled.");
                return;
            }   

            // Remove the shipment from old Courier if REASSIGNED
            Courier oldCourier = UserRegistry.getCourierById(shipment.getCourierID());
            // getAssignedShipments = ArrayList
            if(oldCourier != null){
                oldCourier.getAssignedShipments().remove(shipment);
            }
        }

        courier.receiveShipment(shipment);
        System.out.println("\n  ╔════════════════════════════════════════════════════════╗");
        System.out.println("  ║              DRIVER ASSIGNED SUCCESSFULLY              ║");
        System.out.println("  ╚════════════════════════════════════════════════════════╝");
        System.out.println("  Courier     : " + courier.getName() + " (" + courier.getPersonID() + ")");
        System.out.println("  Tracking    : " + trackingID);
        System.out.println("  Destination : " + shipment.getDeliveryAddress());
    }

    // OPTION 5
    private void handleDeliveryFailure(){
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
        input.nextLine();

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
        System.out.println("    1. Reassign to another courier");
        System.out.println("    2. Mark as OUT_FOR_DELIVERY (retry)");
        System.out.println("    0. Back");
        System.out.print("  Choice -> ");
        
        int choice = input.nextInt();
        input.nextLine();
        switch(choice){
            case 1 -> {
                // Reassign to another courier
                System.out.print("\n  Enter new Courier ID -> ");
                String newCourierID = input.next();
                input.nextLine();

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
                input.nextLine();

                if(confirm.equalsIgnoreCase("Y")){
                    shipmentRegistry.cancelShipment(trackingID);
                }
            }
        }
    }

    // OPTION 8
    private void displayDriverWorkload(){
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
            input.nextLine();

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
    
    private void viewAdminProfile(Admin admin){
        System.out.println();
        admin.displayInfo();
    }

       // HELPER METHOD FOR COURIER DELIVERY LIST DISPLAYING (OPTION 7)
    private void displayCourierDeliveryList(Courier courier){
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

    // MENU DISPLAY for Option 1
    private void displayMenu(){
        System.out.println("\n\n  ╔═══════════════════════════════════╗");
        System.out.println("  ║          USER MANAGEMENT          ║");
        System.out.println("  ╠═══════════════════════════════════╣");
        System.out.println("  ║  1.  Register New Admin           ║");
        System.out.println("  ║  2.  Register New Courier         ║");
        System.out.println("  ║  3.  View All Admins              ║");
        System.out.println("  ║  4.  View All Couriers            ║");
        System.out.println("  ║  0.  Return to Admin Portal       ║");
        System.out.println("  ╚═══════════════════════════════════╝");
        System.out.print("  Choice -> ");
    }
}