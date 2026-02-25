import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

/**
 * ============================================================
 *   COURIER & LOGISTICS MANAGEMENT SYSTEM
 *   AMCS2204 Object-Oriented Programming Techniques
 * ============================================================
 * OOP Concepts Demonstrated:
 *   Abstraction    → Abstract class Person
 *   Encapsulation  → Private fields + getters/setters in all classes
 *   Inheritance    → Sender, Courier, Admin all extend Person
 *   Polymorphism   → displayInfo() overridden in each Person subclass
 *   Composition    → Shipment owns Parcel + StatusLog list
 *   Aggregation    → ShipmentRegistry holds Shipment refs
 *   Association    → CourierService uses all registries
 *   Exception Handling → 7 custom exceptions used throughout
 * ============================================================
 */
public class Main {

    static Scanner         sc               = new Scanner(System.in);
    static List<Sender>    senders          = new ArrayList<>();
    static List<Courier>   couriers         = new ArrayList<>();
    static List<Admin>     admins           = new ArrayList<>();
    static ShipmentRegistry shipmentRegistry = new ShipmentRegistry();
    static FleetManager    fleetManager     = new FleetManager();
    static CourierService  service          = new CourierService(
            shipmentRegistry, fleetManager, couriers, senders);

    private static int senderCounter  = 200;
    private static int courierCounter = 300;
    private static int adminCounter   = 400;
    private static int vehicleCounter = 500;

    public static void main(String[] args) {
        seedData();
        System.out.println("\n  Welcome to the Courier & Logistics Management System!");

        boolean running = true;
        while (running) {
            printMainMenu();
            int choice = readInt("  Select portal: ");
            switch (choice) {
                case 1: senderPortal();  break;
                case 2: courierPortal(); break;
                case 3: adminPortal();   break;
                case 0:
                    System.out.println("\n  Thank you for using CourierPro. Goodbye!\n");
                    running = false;
                    break;
                default:
                    System.out.println("  [!] Invalid option.");
            }
        }
        sc.close();
    }

    // ═══════════════════════════════════════════════════════
    //   MAIN MENU
    // ═══════════════════════════════════════════════════════
    static void printMainMenu() {
        System.out.println();
        System.out.println("  ╔══════════════════════════════════════════╗");
        System.out.println("  ║   COURIER & LOGISTICS MANAGEMENT SYSTEM  ║");
        System.out.println("  ╠══════════════════════════════════════════╣");
        System.out.println("  ║  1. Sender Portal                        ║");
        System.out.println("  ║  2. Courier (Driver) Portal              ║");
        System.out.println("  ║  3. Admin Portal                         ║");
        System.out.println("  ║  0. Exit                                 ║");
        System.out.println("  ╚══════════════════════════════════════════╝");
    }

    // ═══════════════════════════════════════════════════════
    //   SENDER PORTAL
    // ═══════════════════════════════════════════════════════
    static void senderPortal() {
        System.out.println("\n  ── SENDER PORTAL ──");
        System.out.println("  1. Register as New Sender");
        System.out.println("  2. Create Shipment Request");
        System.out.println("  3. Pay for Shipment");
        System.out.println("  4. Track My Parcel");
        System.out.println("  5. View My Shipments");
        System.out.println("  6. View My Profile");
        System.out.println("  0. Back");
        int choice = readInt("  Choice: ");
        switch (choice) {
            case 1: registerSender();     break;
            case 2: createShipment();     break;
            case 3: payShipment();        break;
            case 4: trackParcel();        break;
            case 5: viewMyShipments();    break;
            case 6: viewSenderProfile();  break;
            case 0: break;
            default: System.out.println("  [!] Invalid option.");
        }
    }

    static void registerSender() {
        System.out.println("\n  -- Register New Sender --");
        String id      = "SND" + (++senderCounter);
        String name    = readString("  Full Name    : ");
        String email   = readString("  Email        : ");
        String phone   = readString("  Phone        : ");
        String address = readString("  Address      : ");
        Sender sender  = new Sender(id, name, email, phone, address);
        senders.add(sender);
        System.out.println("  [✓] Registered! Your Sender ID: " + id);
    }

    static void createShipment() {
        System.out.println("\n  -- Create Shipment Request --");
        String senderId = readString("  Your Sender ID   : ");
        try {
            if (service.findSenderById(senderId) == null) {
                System.out.println("  [!] Sender ID not found."); return;
            }
            String recvName    = readString("  Receiver Name    : ");
            String recvPhone   = readString("  Receiver Phone   : ");
            String recvAddress = readString("  Receiver Address : ");
            String origin      = readString("  Origin Region    : ");
            String dest        = readString("  Destination      : ");
            double distance    = readDouble("  Distance (km)    : ");

            System.out.println("  Speed: 1=STANDARD  2=EXPRESS");
            int speedChoice = readInt("  Choice           : ");
            Shipment.ShippingSpeed speed = (speedChoice == 2)
                    ? Shipment.ShippingSpeed.EXPRESS
                    : Shipment.ShippingSpeed.STANDARD;

            double weight = readDouble("  Parcel Weight (kg): ");
            double length = readDouble("  Length (cm)       : ");
            double width  = readDouble("  Width  (cm)       : ");
            double height = readDouble("  Height (cm)       : ");

            System.out.println("  Content: 1=DOCUMENTS 2=ELECTRONICS 3=CLOTHING " +
                               "4=FRAGILE 5=FOOD 6=GENERAL");
            int contentChoice = readInt("  Choice            : ");
            Parcel.ContentType contentType = Parcel.ContentType.values()[
                    Math.min(Math.max(contentChoice - 1, 0), 5)];

            System.out.println("  Insurance: 1=Yes  2=No");
            boolean insured = (readInt("  Choice            : ") == 1);
            double declaredValue = 0;
            if (insured) declaredValue = readDouble("  Declared Value (RM): ");

            Parcel parcel = new Parcel(weight, length, width, height,
                                       contentType, insured, declaredValue);
            Shipment shipment = service.createShipment(
                    senderId, recvName, recvPhone, recvAddress,
                    origin, dest, distance, speed, parcel);

            System.out.println("  [✓] Shipment created! Tracking ID: " +
                               shipment.getTrackingId());
            System.out.printf ("  [i] Please pay RM %.2f to proceed.%n",
                               shipment.getShippingFee());

        } catch (SenderNotFoundException e) {
            System.out.println("  [!] " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("  [!] Input error: " + e.getMessage());
        }
    }

    static void payShipment() {
        String trackingId = readString("\n  Tracking ID: ");
        try {
            service.payForShipment(trackingId);
        } catch (ShipmentNotFoundException e) {
            System.out.println("  [!] " + e.getMessage());
        } catch (IllegalStateException e) {
            System.out.println("  [!] " + e.getMessage());
        }
    }

    static void trackParcel() {
        String trackingId = readString("\n  Enter Tracking ID: ");
        try {
            service.trackParcel(trackingId);
        } catch (ShipmentNotFoundException e) {
            System.out.println("  [!] " + e.getMessage());
        }
    }

    static void viewMyShipments() {
        String senderId = readString("\n  Your Sender ID: ");
        List<Shipment> list = shipmentRegistry.findBySender(senderId);
        if (list.isEmpty()) {
            System.out.println("  No shipments found for sender: " + senderId);
        } else {
            System.out.println("  Your shipments (" + list.size() + "):");
            System.out.printf("  %-12s %-20s %-18s %s%n",
                    "Tracking ID", "Receiver", "Status", "Fee");
            System.out.println("  " + "─".repeat(65));
            for (Shipment s : list) s.displaySummary();
        }
    }

    static void viewSenderProfile() {
        String senderId = readString("\n  Your Sender ID: ");
        Sender sender = service.findSenderById(senderId);
        if (sender == null) System.out.println("  [!] Sender not found.");
        else sender.displayInfo();
    }

    // ═══════════════════════════════════════════════════════
    //   COURIER PORTAL
    // ═══════════════════════════════════════════════════════
    static void courierPortal() {
        System.out.println("\n  ── COURIER PORTAL ──");
        System.out.println("  1. View My Delivery List");
        System.out.println("  2. Mark Shipment as Delivered");
        System.out.println("  3. Record Failed Delivery Attempt");
        System.out.println("  4. Update Shipment Status");
        System.out.println("  5. View My Profile");
        System.out.println("  0. Back");
        int choice = readInt("  Choice: ");
        switch (choice) {
            case 1: courierDeliveryList();  break;
            case 2: markDelivered();        break;
            case 3: recordFailed();         break;
            case 4: courierUpdateStatus();  break;
            case 5: viewCourierProfile();   break;
            case 0: break;
            default: System.out.println("  [!] Invalid option.");
        }
    }

    static void courierDeliveryList() {
        String courierId = readString("\n  Your Courier ID: ");
        try {
            service.viewCourierDeliveryList(courierId);
        } catch (CourierNotFoundException e) {
            System.out.println("  [!] " + e.getMessage());
        }
    }

    static void markDelivered() {
        String trackingId = readString("\n  Tracking ID  : ");
        String courierId  = readString("  Your Courier ID: ");
        try {
            service.markDelivered(trackingId, courierId);
        } catch (Exception e) {
            System.out.println("  [!] " + e.getMessage());
        }
    }

    static void recordFailed() {
        String trackingId = readString("\n  Tracking ID: ");
        String reason     = readString("  Reason (e.g. Receiver not home): ");
        try {
            service.recordFailedAttempt(trackingId, reason);
        } catch (ShipmentNotFoundException e) {
            System.out.println("  [!] " + e.getMessage());
        }
    }

    static void courierUpdateStatus() {
        String trackingId = readString("\n  Tracking ID: ");
        System.out.println("  Statuses: 1=PICKED_UP  2=IN_TRANSIT  " +
                           "3=OUT_FOR_DELIVERY  4=DELIVERED");
        int choice = readInt("  New status: ");
        Shipment.ShipmentStatus[] options = {
            Shipment.ShipmentStatus.PICKED_UP,
            Shipment.ShipmentStatus.IN_TRANSIT,
            Shipment.ShipmentStatus.OUT_FOR_DELIVERY,
            Shipment.ShipmentStatus.DELIVERED
        };
        if (choice < 1 || choice > 4) {
            System.out.println("  [!] Invalid status."); return;
        }
        String note = readString("  Note: ");
        try {
            service.updateShipmentStatus(trackingId, options[choice - 1], note);
        } catch (ShipmentNotFoundException e) {
            System.out.println("  [!] " + e.getMessage());
        }
    }

    static void viewCourierProfile() {
        String courierId = readString("\n  Your Courier ID: ");
        Courier courier  = service.findCourierById(courierId);
        if (courier == null) System.out.println("  [!] Courier not found.");
        else courier.displayInfo();
    }

    // ═══════════════════════════════════════════════════════
    //   ADMIN PORTAL
    // ═══════════════════════════════════════════════════════
    static void adminPortal() {
        System.out.println("\n  ── ADMIN PORTAL ──");
        System.out.println("  1.  Assign Courier to Shipment");
        System.out.println("  2.  Update Shipment Status");
        System.out.println("  3.  Cancel Shipment");
        System.out.println("  4.  View All Shipments");
        System.out.println("  5.  View Shipments by Status");
        System.out.println("  6.  View Full Shipment Details");
        System.out.println("  7.  Manage Couriers");
        System.out.println("  8.  Manage Fleet (Vehicles)");
        System.out.println("  9.  System Summary Report");
        System.out.println("  0.  Back");
        int choice = readInt("  Choice: ");
        switch (choice) {
            case 1: adminAssignCourier();    break;
            case 2: adminUpdateStatus();     break;
            case 3: adminCancelShipment();   break;
            case 4: shipmentRegistry.displayAll(); break;
            case 5: adminFilterByStatus();   break;
            case 6: adminViewShipmentDetail(); break;
            case 7: manageCouriers();        break;
            case 8: manageFleet();           break;
            case 9: printSystemSummary();    break;
            case 0: break;
            default: System.out.println("  [!] Invalid option.");
        }
    }

    static void adminAssignCourier() {
        String trackingId = readString("\n  Tracking ID  : ");
        System.out.println("  Available couriers:");
        couriers.stream()
                .filter(Courier::isAvailable)
                .forEach(c -> System.out.println("    " + c.getPersonId() +
                              " — " + c.getName()));
        String courierId = readString("  Courier ID   : ");
        try {
            service.assignCourierToShipment(trackingId, courierId);
        } catch (Exception e) {
            System.out.println("  [!] " + e.getMessage());
        }
    }

    static void adminUpdateStatus() {
        String trackingId = readString("\n  Tracking ID: ");
        System.out.println("  1=PICKED_UP  2=IN_TRANSIT  3=OUT_FOR_DELIVERY  4=DELIVERED");
        int choice = readInt("  New status: ");
        Shipment.ShipmentStatus[] options = {
            Shipment.ShipmentStatus.PICKED_UP,
            Shipment.ShipmentStatus.IN_TRANSIT,
            Shipment.ShipmentStatus.OUT_FOR_DELIVERY,
            Shipment.ShipmentStatus.DELIVERED
        };
        if (choice < 1 || choice > 4) { System.out.println("  [!] Invalid."); return; }
        String note = readString("  Note: ");
        try {
            service.updateShipmentStatus(trackingId, options[choice - 1], note);
        } catch (ShipmentNotFoundException e) {
            System.out.println("  [!] " + e.getMessage());
        }
    }

    static void adminCancelShipment() {
        String trackingId = readString("\n  Tracking ID to cancel: ");
        try {
            shipmentRegistry.cancelShipment(trackingId);
            System.out.println("  [✓] Shipment " + trackingId + " cancelled.");
        } catch (Exception e) {
            System.out.println("  [!] " + e.getMessage());
        }
    }

    static void adminFilterByStatus() {
        System.out.println("  1=PENDING_PAYMENT  2=PAID  3=PICKED_UP  4=IN_TRANSIT");
        System.out.println("  5=OUT_FOR_DELIVERY  6=DELIVERED  7=FAILED_ATTEMPT  8=CANCELLED");
        int choice = readInt("  Status: ");
        Shipment.ShipmentStatus[] all = Shipment.ShipmentStatus.values();
        if (choice < 1 || choice > all.length) { System.out.println("  [!] Invalid."); return; }
        List<Shipment> filtered = shipmentRegistry.findByStatus(all[choice - 1]);
        System.out.println("  Results: " + filtered.size() + " shipment(s)");
        filtered.forEach(Shipment::displaySummary);
    }

    static void adminViewShipmentDetail() {
        String trackingId = readString("\n  Tracking ID: ");
        Shipment s = shipmentRegistry.findByTrackingId(trackingId);
        if (s == null) System.out.println("  [!] Shipment not found.");
        else s.displayFull();
    }

    // ─── Courier Management ───────────────────────────────
    static void manageCouriers() {
        System.out.println("\n  -- Courier Management --");
        System.out.println("  1. Add New Courier");
        System.out.println("  2. List All Couriers");
        System.out.println("  3. View Courier Details");
        System.out.println("  0. Back");
        int choice = readInt("  Choice: ");
        switch (choice) {
            case 1: addCourier();   break;
            case 2: listCouriers(); break;
            case 3:
                String id = readString("  Courier ID: ");
                Courier c = service.findCourierById(id);
                if (c == null) System.out.println("  [!] Not found.");
                else c.displayInfo();
                break;
            case 0: break;
        }
    }

    static void addCourier() {
        System.out.println("\n  -- Add New Courier --");
        String id      = "CRR" + (++courierCounter);
        String name    = readString("  Name          : ");
        String email   = readString("  Email         : ");
        String phone   = readString("  Phone         : ");
        String license = readString("  License No.   : ");
        Courier courier = new Courier(id, name, email, phone, license);
        couriers.add(courier);
        System.out.println("  [✓] Courier added! ID: " + id);
    }

    static void listCouriers() {
        if (couriers.isEmpty()) { System.out.println("  No couriers."); return; }
        System.out.printf("  %-8s %-20s %-12s %-10s%n",
                          "ID", "Name", "Status", "Deliveries");
        System.out.println("  " + "─".repeat(55));
        for (Courier c : couriers) {
            System.out.printf("  %-8s %-20s %-12s %d%n",
                c.getPersonId(), c.getName(),
                c.getStatus(), c.getTotalDeliveries());
        }
    }

    // ─── Fleet Management ─────────────────────────────────
    static void manageFleet() {
        System.out.println("\n  -- Fleet Management --");
        System.out.println("  1. Add Vehicle");
        System.out.println("  2. View All Vehicles");
        System.out.println("  3. Assign Vehicle to Courier");
        System.out.println("  4. Release Vehicle");
        System.out.println("  5. Schedule Maintenance");
        System.out.println("  6. Complete Maintenance");
        System.out.println("  7. Remove Vehicle");
        System.out.println("  8. View Maintenance Due");
        System.out.println("  0. Back");
        int choice = readInt("  Choice: ");
        switch (choice) {
            case 1: addVehicle();          break;
            case 2: fleetManager.displayAll(); break;
            case 3: assignVehicle();       break;
            case 4: releaseVehicle();      break;
            case 5: scheduleVehicleMaint(); break;
            case 6: completeVehicleMaint(); break;
            case 7: removeVehicle();       break;
            case 8:
                List<Vehicle> due = fleetManager.getVehiclesDueForMaintenance();
                System.out.println("  Vehicles due for maintenance: " + due.size());
                due.forEach(v -> System.out.println("    " + v.getVehicleId() +
                            " — " + v.getPlateNumber()));
                break;
            case 0: break;
        }
    }

    static void addVehicle() {
        String id    = "VHL" + (++vehicleCounter);
        String plate = readString("  Plate Number : ");
        System.out.println("  Type: 1=MOTORCYCLE  2=VAN  3=TRUCK");
        int t        = readInt("  Choice       : ");
        Vehicle.VehicleType[] types = Vehicle.VehicleType.values();
        Vehicle.VehicleType type = types[Math.min(Math.max(t - 1, 0), 2)];
        double maxLoad = readDouble("  Max Load (kg): ");
        Vehicle vehicle = new Vehicle(id, plate, type, maxLoad);
        fleetManager.addVehicle(vehicle);
    }

    static void assignVehicle() {
        String vehicleId = readString("\n  Vehicle ID : ");
        String courierId = readString("  Courier ID : ");
        try {
            fleetManager.assignToCourier(vehicleId, courierId);
            Courier courier = service.findCourierById(courierId);
            if (courier != null) courier.setAssignedVehicleId(vehicleId);
        } catch (Exception e) {
            System.out.println("  [!] " + e.getMessage());
        }
    }

    static void releaseVehicle() {
        String vehicleId = readString("\n  Vehicle ID: ");
        try { fleetManager.releaseVehicle(vehicleId); }
        catch (Exception e) { System.out.println("  [!] " + e.getMessage()); }
    }

    static void scheduleVehicleMaint() {
        String vehicleId = readString("\n  Vehicle ID: ");
        Vehicle v = fleetManager.findById(vehicleId);
        if (v == null) System.out.println("  [!] Vehicle not found.");
        else v.scheduleMaintenance();
    }

    static void completeVehicleMaint() {
        String vehicleId = readString("\n  Vehicle ID: ");
        Vehicle v = fleetManager.findById(vehicleId);
        if (v == null) System.out.println("  [!] Vehicle not found.");
        else v.completeMaintenance();
    }

    static void removeVehicle() {
        String vehicleId = readString("\n  Vehicle ID: ");
        try { fleetManager.removeVehicle(vehicleId); }
        catch (Exception e) { System.out.println("  [!] " + e.getMessage()); }
    }

    // ─── System Summary ───────────────────────────────────
    static void printSystemSummary() {
        System.out.println();
        System.out.println("  ═══════════════════════════════════════════");
        System.out.println("        SYSTEM SUMMARY REPORT");
        System.out.println("  ═══════════════════════════════════════════");
        System.out.println("  Total Shipments    : " + shipmentRegistry.getTotalShipments());
        System.out.println("  Pending Payment    : " + shipmentRegistry
                .findByStatus(Shipment.ShipmentStatus.PENDING_PAYMENT).size());
        System.out.println("  In Transit         : " + shipmentRegistry
                .findByStatus(Shipment.ShipmentStatus.IN_TRANSIT).size());
        System.out.println("  Delivered          : " + shipmentRegistry
                .findByStatus(Shipment.ShipmentStatus.DELIVERED).size());
        System.out.println("  Failed Attempts    : " + shipmentRegistry
                .findByStatus(Shipment.ShipmentStatus.FAILED_ATTEMPT).size());
        System.out.println("  Registered Senders : " + senders.size());
        System.out.println("  Active Couriers    : " + couriers.size());
        System.out.println("  Fleet Size         : " + fleetManager.getTotalVehicles());
        System.out.println("  Available Vehicles : " +
                           fleetManager.getAvailableVehicles().size());
        System.out.println("  ═══════════════════════════════════════════");
    }

    // ═══════════════════════════════════════════════════════
    //   HELPER METHODS
    // ═══════════════════════════════════════════════════════
    static String readString(String prompt) {
        System.out.print(prompt);
        return sc.nextLine().trim();
    }

    static int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                int val = Integer.parseInt(sc.nextLine().trim());
                return val;
            } catch (NumberFormatException e) {
                System.out.println("  [!] Please enter a valid number.");
            }
        }
    }

    static double readDouble(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Double.parseDouble(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("  [!] Please enter a valid number.");
            }
        }
    }

    // ═══════════════════════════════════════════════════════
    //   SEED DATA — pre-loaded for testing
    // ═══════════════════════════════════════════════════════
    static void seedData() {
        System.out.println("  [*] Loading sample data...");

        // Senders
        Sender s1 = new Sender("SND201", "Alice Tan",  "alice@email.com",  "012-3456789", "12 Jalan Ampang, KL");
        Sender s2 = new Sender("SND202", "Bob Lim",    "bob@email.com",    "016-9876543", "45 Jalan Ipoh, KL");
        senders.add(s1); senders.add(s2);

        // Couriers
        Courier c1 = new Courier("CRR301", "David Wong", "david@courier.com", "011-1234567", "D-1234-KL");
        Courier c2 = new Courier("CRR302", "Farah Ali",  "farah@courier.com", "014-7654321", "F-5678-KL");
        couriers.add(c1); couriers.add(c2);

        // Admins
        Admin a1 = new Admin("ADM401", "Manager Raj", "raj@logistics.com",
                "017-0001111", Admin.Role.OPERATIONS_MANAGER, "Operations");
        admins.add(a1);

        // Vehicles
        Vehicle v1 = new Vehicle("VHL501", "WKL 1234", Vehicle.VehicleType.VAN, 500.0);
        Vehicle v2 = new Vehicle("VHL502", "WKL 5678", Vehicle.VehicleType.MOTORCYCLE, 50.0);
        fleetManager.addVehicle(v1);
        fleetManager.addVehicle(v2);

        // Sample shipment — fully through the workflow
        try {
            Parcel parcel1 = new Parcel(2.5, 30, 20, 15,
                    Parcel.ContentType.ELECTRONICS, true, 500.0);
            Shipment ship1 = service.createShipment(
                    "SND201", "Chong Wei", "013-9999888",
                    "88 Jalan Bukit Bintang, KL", "Petaling Jaya", "Kuala Lumpur",
                    25.0, Shipment.ShippingSpeed.EXPRESS, parcel1);
            ship1.confirmPayment();
            ship1.setAssignedCourierId("CRR301");
            ship1.updateStatus(Shipment.ShipmentStatus.PICKED_UP, "Package picked up from sender.");
            ship1.updateStatus(Shipment.ShipmentStatus.IN_TRANSIT, "En route to destination.");
            s1.addShipmentId(ship1.getTrackingId());
            c1.assignShipment(ship1.getTrackingId());

            Parcel parcel2 = new Parcel(0.8, 20, 15, 10,
                    Parcel.ContentType.DOCUMENTS, false, 0);
            Shipment ship2 = service.createShipment(
                    "SND202", "Nurul Huda", "012-5556666",
                    "22 Jalan Duta, KL", "Subang Jaya", "Kuala Lumpur",
                    18.0, Shipment.ShippingSpeed.STANDARD, parcel2);
            s2.addShipmentId(ship2.getTrackingId());

        } catch (SenderNotFoundException e) {
            System.out.println("  Seed error: " + e.getMessage());
        }

        System.out.println("  [*] Sample data loaded!\n");
    }
}
