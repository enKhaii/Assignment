/*
    Courier - Represents a delivery driver in the system
    Extends Person (Inheritance)
    OOP Concepts: Inheritance, Encapsulation, Polymorphism, Abstraction
*/

import java.util.ArrayList;

public class Courier extends Person {

    // Courier-specific fields (Encapsulation — all private)
    private String licenseNumber;
    private String assignedVehicleID;   // links to Vehicle.vehicleID from FleetManager
    private boolean isOnDuty;
    private int deliveredCount;         // how many delivered today

    private ArrayList<Shipment> assignedShipments;  // daily delivery list

    // ─── Constructor ──────────────────────────────────────────
    // Matches Person(personID, loginID, name, password, email, phoneNum) exactly
    public Courier(String personID, String loginID, String name,
                   String password, String email, String phoneNum,
                   String licenseNumber) {
        super(personID, loginID, name, password, email, phoneNum);
        this.licenseNumber      = licenseNumber;
        this.assignedVehicleID  = null;
        this.isOnDuty           = false;
        this.deliveredCount     = 0;
        this.assignedShipments  = new ArrayList<>();
    }

    // ─── Polymorphism: Override abstract method from Person ───
    @Override
    public void displayInfo() {
        System.out.println("  ╔══════════════════════════════════════════╗");
        System.out.println("  ║           COURIER INFORMATION            ║");
        System.out.println("  ╠══════════════════════════════════════════╣");
        System.out.printf ("  ║  ID         : %-27s║%n", getPersonID());
        System.out.printf ("  ║  Name       : %-27s║%n", getName());
        System.out.printf ("  ║  Email      : %-27s║%n", getEmail());
        System.out.printf ("  ║  Phone      : %-27s║%n", getPhoneNum());
        System.out.printf ("  ║  License    : %-27s║%n", licenseNumber);
        System.out.printf ("  ║  Vehicle    : %-27s║%n",
                assignedVehicleID != null ? assignedVehicleID : "Not Assigned");
        System.out.printf ("  ║  Status     : %-27s║%n", isOnDuty ? "ON DUTY" : "OFF DUTY");
        System.out.printf ("  ║  Delivered  : %-27s║%n", deliveredCount + " today");
        System.out.println("  ╚══════════════════════════════════════════╝");
    }

    // ─── COURIER METHODS ──────────────────────────────────────

    /**
     * Receive a shipment assigned by Admin.
     * Called by UserRegistry when setting up data, or by Admin portal.
     */
    public void receiveShipment(Shipment shipment) {
        assignedShipments.add(shipment);
        shipment.setCourierID(getPersonID());   // link shipment to this courier
        System.out.println("  [✓] Shipment " + shipment.getTrackingID()
                + " assigned to " + getName());
    }

    /**
     * View all shipments assigned today.
     */
    public void viewDailyDeliveryList() {
        System.out.println("\n  ╔══════════════════════════════════════════╗");
        System.out.println("  ║          DAILY DELIVERY LIST             ║");
        System.out.printf ("  ║  Driver  : %-30s║%n", getName());
        System.out.printf ("  ║  Vehicle : %-30s║%n",
                assignedVehicleID != null ? assignedVehicleID : "Not Assigned");
        System.out.printf ("  ║  Total   : %-30s║%n",
                assignedShipments.size() + " shipment(s)");
        System.out.println("  ╠══════════════════════════════════════════╣");

        if (assignedShipments.isEmpty()) {
            System.out.println("  ║  No shipments assigned today.            ║");
        } else {
            for (int i = 0; i < assignedShipments.size(); i++) {
                Shipment s = assignedShipments.get(i);
                System.out.printf("  ║  %d. %-39s║%n", i + 1, s.getTrackingID());
                System.out.printf("  ║     To : %-32s║%n", s.getDeliveryAddress().length() > 32
                        ? s.getDeliveryAddress().substring(0, 29) + "..."
                        : s.getDeliveryAddress());
                System.out.printf("  ║     Status : %-28s║%n", s.getStatus());
                System.out.println("  ║                                          ║");
            }
        }
        System.out.println("  ╚══════════════════════════════════════════╝");
    }

    /**
     * Courier confirms physical pickup of a package.
     */
    public void pickUpShipment(String trackingID) {
        Shipment s = findShipment(trackingID);
        if (s == null) {
            System.out.println("  [!] Shipment not found in your list: " + trackingID);
            return;
        }
        if (s.getStatus() != Shipment.ShipmentStatus.PAID) {
            System.out.println("  [!] Cannot pick up — current status: " + s.getStatus());
            return;
        }
        s.updateStatus(Shipment.ShipmentStatus.PICKED_UP, "Picked up by courier " + getName());
        System.out.println("  [✓] Picked up: " + trackingID);
    }

    /**
     * Update shipment to IN_TRANSIT or OUT_FOR_DELIVERY.
     */
    public void updateShipmentStatus(String trackingID, Shipment.ShipmentStatus newStatus, String note) {
        Shipment s = findShipment(trackingID);
        if (s == null) {
            System.out.println("  [!] Shipment not found in your list: " + trackingID);
            return;
        }
        s.updateStatus(newStatus, note);
        System.out.println("  [✓] Status updated to " + newStatus + " for " + trackingID);
    }

    /**
     * Mark a shipment as delivered.
     * @param receivedBy  name of person who received/signed
     */
    public void markDelivered(String trackingID, String receivedBy) {
        Shipment s = findShipment(trackingID);
        if (s == null) {
            System.out.println("  [!] Shipment not found in your list: " + trackingID);
            return;
        }
        if (s.getStatus() == Shipment.ShipmentStatus.DELIVERED) {
            System.out.println("  [!] Already delivered: " + trackingID);
            return;
        }
        s.updateStatus(Shipment.ShipmentStatus.DELIVERED,
                "Delivered. Received by: " + receivedBy);
        deliveredCount++;
        System.out.println("  [✓] Delivered: " + trackingID
                + " | Signed by: " + receivedBy);
    }

    /**
     * Report a failed delivery — logs reason and reschedules.
     */
    public void reportFailedDelivery(String trackingID, String reason) {
        Shipment s = findShipment(trackingID);
        if (s == null) {
            System.out.println("  [!] Shipment not found in your list: " + trackingID);
            return;
        }
        s.updateStatus(Shipment.ShipmentStatus.FAILED_ATTEMPT,
                "Failed delivery attempt. Reason: " + reason);
        System.out.println("  [✓] Failure logged for " + trackingID);
        System.out.println("  Reason: " + reason);
        System.out.println("  [i] Shipment marked FAILED_ATTEMPT — Admin will reschedule.");
    }

    /**
     * Toggle on/off duty status.
     */
    public void setDutyStatus(boolean onDuty) {
        this.isOnDuty = onDuty;
        System.out.println("  [✓] " + getName() + " is now "
                + (onDuty ? "ON DUTY" : "OFF DUTY"));
    }

    // ─── Private helper ───────────────────────────────────────
    private Shipment findShipment(String trackingID) {
        for (Shipment s : assignedShipments) {
            if (s.getTrackingID().equalsIgnoreCase(trackingID)) return s;
        }
        return null;
    }

    // ─── Getters & Setters ────────────────────────────────────
    public String  getLicenseNumber()                    { return licenseNumber; }
    public String  getAssignedVehicleID()                { return assignedVehicleID; }
    public boolean isOnDuty()                            { return isOnDuty; }
    public int     getDeliveredCount()                   { return deliveredCount; }
    public ArrayList<Shipment> getAssignedShipments()   { return assignedShipments; }

    public void setAssignedVehicleID(String id)          { this.assignedVehicleID = id; }

    @Override
    public String toString() {
        return String.format("  %-8s %-20s %-15s %-10s %d delivered%n",
                getPersonID(), getName(), licenseNumber,
                isOnDuty ? "ON DUTY" : "OFF DUTY", deliveredCount);
    }
}