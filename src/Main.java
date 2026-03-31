import java.util.Scanner;

public class Main {
    // GLOBAL VARIABLES
    public static Scanner input = new Scanner(System.in);

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
                CourierPortal portal = new CourierPortal(user, input, fleetManager);
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

                AdminPortal portal = new AdminPortal(input, shipmentManagement, shipmentRegistry, fleetManagement);
                portal.start(user, input);
            }
            else{
                System.out.println("\n  [!] Invalid credentials, please try again.");
            }
        }
        else{
            System.out.println("\n  >> Error: Login ID does not exist.");
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