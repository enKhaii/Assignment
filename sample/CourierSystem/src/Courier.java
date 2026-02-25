import java.util.ArrayList;
import java.util.List;

/**
 * Courier — a driver who handles deliveries.
 * Demonstrates: Inheritance (extends Person), Encapsulation, Polymorphism
 */
public class Courier extends Person {

    public enum Status { AVAILABLE, ON_DELIVERY, OFF_DUTY }

    private String       licenseNumber;
    private String       assignedVehicleId; // Association with Vehicle
    private Status       status;
    private List<String> assignedShipmentIds; // today's delivery list
    private int          totalDeliveries;

    public Courier(String courierId, String name, String email,
                   String phone, String licenseNumber) {
        super(courierId, name, email, phone);
        this.licenseNumber       = licenseNumber;
        this.status              = Status.AVAILABLE;
        this.assignedShipmentIds = new ArrayList<>();
        this.totalDeliveries     = 0;
        this.assignedVehicleId   = null;
    }

    // Getters & Setters
    public String getLicenseNumber()  { return licenseNumber; }
    public void   setLicenseNumber(String licenseNumber) { this.licenseNumber = licenseNumber; }

    public String getAssignedVehicleId()  { return assignedVehicleId; }
    public void   setAssignedVehicleId(String vehicleId) { this.assignedVehicleId = vehicleId; }

    public Status getStatus()  { return status; }
    public void   setStatus(Status status) { this.status = status; }

    public int getTotalDeliveries() { return totalDeliveries; }

    public List<String> getAssignedShipmentIds() {
        return new ArrayList<>(assignedShipmentIds);
    }

    public void assignShipment(String trackingId) {
        assignedShipmentIds.add(trackingId);
        status = Status.ON_DELIVERY;
    }

    public void completeDelivery(String trackingId) {
        assignedShipmentIds.remove(trackingId);
        totalDeliveries++;
        if (assignedShipmentIds.isEmpty()) status = Status.AVAILABLE;
    }

    public boolean isAvailable() { return status == Status.AVAILABLE; }

    // Polymorphism: unique implementation
    @Override
    public void displayInfo() {
        System.out.println("  ╔══════════════════════════════════════╗");
        System.out.println("  ║        COURIER INFORMATION           ║");
        System.out.println("  ╠══════════════════════════════════════╣");
        System.out.printf ("  ║  ID       : %-25s║%n", getPersonId());
        System.out.printf ("  ║  Name     : %-25s║%n", getName());
        System.out.printf ("  ║  Phone    : %-25s║%n", getPhone());
        System.out.printf ("  ║  License  : %-25s║%n", licenseNumber);
        System.out.printf ("  ║  Vehicle  : %-25s║%n",
                assignedVehicleId != null ? assignedVehicleId : "None");
        System.out.printf ("  ║  Status   : %-25s║%n", status);
        System.out.printf ("  ║  Deliveries Done: %-20d║%n", totalDeliveries);
        System.out.printf ("  ║  Active Jobs: %-24d║%n", assignedShipmentIds.size());
        System.out.println("  ╚══════════════════════════════════════╝");
    }
}
