import java.util.InputMismatchException;
import java.util.Scanner;
 
public class CourierPortal {
 
    private Courier courier;    // the logged-in courier
    private Scanner input;      // shared Scanner from Main.java
 
    // Constructor
    public CourierPortal(Courier courier, Scanner input) {
        this.courier = courier;
        this.input = input;
    }
 
    // ─── MAIN DISPLAY METHOD ──────────────────────────────────
    public void show() {
        boolean inCourierMenu = true;
 
        while (inCourierMenu) {
            try {
                displayMenu();
                int choice = input.nextInt();
                input.nextLine(); // clear buffer
 
                switch (choice) {
                    case 1 -> courier.viewDailyDeliveryList();
                    case 2 -> pickUpShipment();
                    case 3 -> updateStatus();
                    case 4 -> markDelivered();
                    case 5 -> reportFailed();
                    case 6 -> courier.displayInfo();
                    case 7 -> toggleDuty();
                    case 0 -> {
                        System.out.println("  [i] Logging out...");
                        inCourierMenu = false;
                    }
                    default -> System.out.println("  [!] Invalid option. Please try again.");
                }
            } catch (InputMismatchException e) {
                System.out.println("\n  [!] Invalid input! Please enter a number.");
                input.nextLine(); // clear wrong input
            }
        }
    }
 
    // ─── OPERATIONS ───────────────────────────────────────────
 
    private void pickUpShipment() {
        System.out.println("\n  ╔══════════════════════════════════════════╗");
        System.out.println("  ║             PICK UP SHIPMENT             ║");
        System.out.println("  ╚══════════════════════════════════════════╝");
        System.out.print("  Enter Tracking ID -> ");
        String id = input.next();
        input.nextLine();
        courier.pickUpShipment(id);
    }
 
    private void updateStatus() {
        System.out.println("\n  ╔══════════════════════════════════════════╗");
        System.out.println("  ║           UPDATE SHIPMENT STATUS         ║");
        System.out.println("  ╚══════════════════════════════════════════╝");
        System.out.print("  Enter Tracking ID -> ");
        String id = input.next();
        input.nextLine();
 
        System.out.println("\n  Update to:");
        System.out.println("   1. IN_TRANSIT");
        System.out.println("   2. OUT_FOR_DELIVERY");
        System.out.print("  Choice -> ");
 
        int choice = input.nextInt();
        input.nextLine();
 
        Shipment.ShipmentStatus newStatus = switch (choice) {
            case 1 -> Shipment.ShipmentStatus.IN_TRANSIT;
            case 2 -> Shipment.ShipmentStatus.OUT_FOR_DELIVERY;
            default -> null;
        };
 
        if (newStatus != null) {
            System.out.print("  Add note (optional, press Enter to skip) -> ");
            String note = input.nextLine();
            if (note.trim().isEmpty()) note = "Status updated by courier " + courier.getName();
            courier.updateShipmentStatus(id, newStatus, note);
        } else {
            System.out.println("  [!] Invalid choice.");
        }
    }
 
    private void markDelivered() {
        System.out.println("\n  ╔══════════════════════════════════════════╗");
        System.out.println("  ║           MARK AS DELIVERED              ║");
        System.out.println("  ╚══════════════════════════════════════════╝");
        System.out.print("  Enter Tracking ID -> ");
        String id = input.next();
        input.nextLine();
 
        System.out.print("  Received by (name of person who signed) -> ");
        String receivedBy = input.nextLine();
 
        courier.markDelivered(id, receivedBy);
    }
 
    private void reportFailed() {
        System.out.println("\n  ╔══════════════════════════════════════════╗");
        System.out.println("  ║          REPORT FAILED DELIVERY          ║");
        System.out.println("  ╚══════════════════════════════════════════╝");
        System.out.print("  Enter Tracking ID -> ");
        String id = input.next();
        input.nextLine();
 
        System.out.println("\n  Reason for failure:");
        System.out.println("   1. Receiver not home");
        System.out.println("   2. Wrong address");
        System.out.println("   3. Receiver refused delivery");
        System.out.println("   4. No access to building");
        System.out.println("   5. Other");
        System.out.print("  Choice -> ");
 
        int choice = input.nextInt();
        input.nextLine();
 
        String reason = switch (choice) {
            case 1 -> "Receiver not home";
            case 2 -> "Wrong address";
            case 3 -> "Receiver refused delivery";
            case 4 -> "No access to building";
            default -> "Other";
        };
 
        courier.reportFailedDelivery(id, reason);
    }
 
    private void toggleDuty() {
        courier.setDutyStatus(!courier.isOnDuty());
    }
 
    // ─── MENU DESIGN (same style as FleetManagement) ─────────
    private void displayMenu() {
        System.out.println("\n  ╔══════════════════════════════════════════╗");
        System.out.println("  ║         COURIER (DRIVER) PORTAL          ║");
        System.out.println("  ╠══════════════════════════════════════════╣");
        System.out.printf ("  ║  %-40s║%n", "Driver : " + courier.getName());
        System.out.printf ("  ║  %-40s║%n", "Status : " + (courier.isOnDuty() ? "ON DUTY" : "OFF DUTY"));
        System.out.printf ("  ║  %-40s║%n", "Delivered Today : " + courier.getDeliveredCount());
        System.out.println("  ╠══════════════════════════════════════════╣");
        System.out.println("  ║  [ DELIVERIES ]                          ║");
        System.out.println("  ║   1. View Daily Delivery List            ║");
        System.out.println("  ║   2. Pick Up a Shipment                  ║");
        System.out.println("  ║   3. Update Shipment Status              ║");
        System.out.println("  ║   4. Mark as Delivered                   ║");
        System.out.println("  ║   5. Report Failed Delivery              ║");
        System.out.println("  ║                                          ║");
        System.out.println("  ║  [ MY INFO ]                             ║");
        System.out.println("  ║   6. View My Profile                     ║");
        System.out.println("  ║   7. Toggle Duty Status                  ║");
        System.out.println("  ║                                          ║");
        System.out.println("  ║   0. Logout                              ║");
        System.out.println("  ╚══════════════════════════════════════════╝");
        System.out.print("  Choice -> ");
    }
}