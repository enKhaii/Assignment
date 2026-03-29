import java.util.Scanner;

public class Main {
    // GLOBAL VARIABLES
    static Scanner input = new Scanner(System.in);

    static FleetManager fleetManager = new FleetManager();
    static FleetManagement fleetManagement = new FleetManagement(fleetManager, input);

    static ShipmentRegistry shipmentRegistry = new ShipmentRegistry();
    static ShipmentManagement shipmentManagement = new ShipmentManagement(shipmentRegistry, input);

    static QuickTrack quickTrack = new QuickTrack(shipmentRegistry, input);

    public static void main(String[] args){
        UserRegistry.initializeData();      // Initialize login credentials data
        shipmentRegistry.initializeData();  // Initialize shipments sample data
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
    public static void assignDriverToShipment(){
        System.out.println("1");
    }

    public static void handleDeliveryFailure(){
        System.out.println("4");
    }

    public static void displayDriverWorkload(){
        System.out.println("7");
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

/*
 * ════════════════════════════════════════════════════════════════════════════
 *                   MAIN - ADMIN PORTAL TODO LIST
 *            Courier-Fleet Integration Tasks
 * ════════════════════════════════════════════════════════════════════════════
 * 
 * COORDINATION WITH FLEET MODULE:
 * ────────────────────────────────────────────────────────────────────────────
 * [ ] When removing courier (in manageCouriers):
 *     - Check if courier has assigned vehicle
 *     - Code:
 *       Vehicle v = fleetManagement.fleetManager.findByAssignedCourier(courierId);
 *       if (v != null) {
 *           System.out.println("Cannot remove - courier has assigned vehicle!");
 *           System.out.println("Please release vehicle first.");
 *           return;
 *       }
 * 
 * [ ] Add helper method: findCourierById(String courierId)
 *     - Used by: FleetManagement to validate courier exists
 *     - Returns: Courier object or null
 *     static Courier findCourierById(String courierId) {
 *         for (Courier c : couriers) {
 *             if (c.getCourierId().equalsIgnoreCase(courierId)) {
 *                 return c;
 *             }
 *         }
 *         return null;
 *     }
 * 
 * ────────────────────────────────────────────────────────────────────────────
 * ADMIN REPORTS ENHANCEMENT:
 * ────────────────────────────────────────────────────────────────────────────
 * [ ] Add to systemReports() menu:
 *     - Option: "View Courier-Vehicle Assignments"
 *     - Shows table of which courier has which vehicle
 *     - Can delegate to: fleetManagement.viewCourierVehicleAssignments()
 * 
 * ════════════════════════════════════════════════════════════════════════════
 * SEED DATA ENHANCEMENT:
 * ════════════════════════════════════════════════════════════════════════════
 * [ ] In seedData(), assign some vehicles to couriers for testing:
 *     // After adding vehicles and couriers
 *     fleetManager.assignToCourier("VHL501", "CRR301");
 *     fleetManager.assignToCourier("VHL502", "CRR302");
 * 
 * ════════════════════════════════════════════════════════════════════════════
 */     