import java.util.Scanner;

public class Main {
    static Scanner input = new Scanner(System.in);

    public static void main(String[] args){
        displayLogo();
        UserRegistry.initializeData(); // Initilaize login credentials data
        
        System.out.println("\n  Welcome to Courier & Logistics Management System!");

        boolean running = true;
        while(running){
            displayMainMenu();
            System.out.print("  Select portal: ");
            int choice = input.nextInt();
            switch(choice){
                case 1 -> senderPortal();
                case 2 -> courierPortal();
                case 3 -> adminLogin();
                case 4 -> trackShipment();
                case 0 -> {
                    System.out.println("\n  Thank you for using CourierPro. Goodbye!\n");
                    running = false;
                }
                default -> System.out.println("  [!] Invalid option.");
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
        System.out.println("Courier");
    }

    // ADMIN PORTAL (3) & LOGIN
    public static void adminLogin(){
        System.out.println("\n  ╔══════════════════════════════════════════╗");
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
                System.out.println("  [!] Invalid credentials, please try again.");
            }
        }
        else{
            System.out.println("  >> Error: Login ID does not exist.");
        }
    }

    public static void adminPortal(Admin user){
        // Admin (data type) parameter to access the "Admin Objects" to display details

        boolean active = true;
        while(active){
            System.out.println("\n  ╔══════════════════════════════════════════╗");
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

            System.out.print("  Choice: ");
            int choice = input.nextInt();
            switch(choice){
                case 1 -> assignCourier();
                case 2 -> updateShipmentStatus();
                case 3 -> calculateReviewShipmentFees();
                case 4 -> handleDeliveryFailure();
                case 5 -> viewAllShipments();
                case 6 -> manageFleetMaintenance();
                case 7 -> displayDriverWorkload();
                case 8 -> viewAdminProfile();
                case 0 -> {
                    System.out.println("  Logging out...");
                    active = false;
                }
                default -> System.out.println("  [!] Invalid option.");
            }
        }
    }

    // QUICK TRACK METHOD (4)
    public static void trackShipment(){
        System.out.println("NULL");
    }

    // ADMIN METHODS
    public static void assignCourier(){
        System.out.println("1");
    }

    public static void updateShipmentStatus(){
        System.out.println("2");
    }

    public static void calculateReviewShipmentFees(){
        System.out.println("3");
    }

    public static void handleDeliveryFailure(){
        System.out.println("4");
    }

    public static void viewAllShipments(){
        System.out.println("5");
    }

    public static void manageFleetMaintenance(){
        System.out.println("6");
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
            System.out.println("  >> Error: Admin ID not found.");
        }
    }

    // DISPLAY DESIGN
    public static void displayMainMenu(){
        System.out.println();
        System.out.println("  ╔══════════════════════════════════════════╗");
        System.out.println("  ║   COURIER & LOGISTICS MANAGEMENT SYSTEM  ║");
        System.out.println("  ╠══════════════════════════════════════════╣");
        System.out.println("  ║  1. Sender Portal                        ║");
        System.out.println("  ║  2. Courier (Driver) Portal              ║");
        System.out.println("  ║  3. Admin Portal                         ║");
        System.out.println("  ║  4. Quick Track (Enter ID)               ║");
        System.out.println("  ║  0. Exit                                 ║");
        System.out.println("  ╚══════════════════════════════════════════╝");
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