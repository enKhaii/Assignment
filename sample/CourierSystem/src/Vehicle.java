import java.time.LocalDate;

/**
 * Vehicle — represents a delivery vehicle in the fleet.
 * Demonstrates: Encapsulation, Association (linked to Courier)
 */
public class Vehicle {

    public enum VehicleType { MOTORCYCLE, VAN, TRUCK }
    public enum VehicleStatus { AVAILABLE, IN_USE, UNDER_MAINTENANCE }

    private final String        vehicleId;
    private String              plateNumber;
    private VehicleType         type;
    private VehicleStatus       status;
    private double              maxLoadKg;
    private LocalDate           lastMaintenanceDate;
    private LocalDate           nextMaintenanceDate;
    private String              assignedCourierId;

    public Vehicle(String vehicleId, String plateNumber, VehicleType type,
                   double maxLoadKg) {
        this.vehicleId            = vehicleId;
        this.plateNumber          = plateNumber;
        this.type                 = type;
        this.maxLoadKg            = maxLoadKg;
        this.status               = VehicleStatus.AVAILABLE;
        this.lastMaintenanceDate  = LocalDate.now().minusMonths(1);
        this.nextMaintenanceDate  = LocalDate.now().plusMonths(2);
        this.assignedCourierId    = null;
    }

    // Getters & Setters
    public String        getVehicleId()          { return vehicleId; }
    public String        getPlateNumber()         { return plateNumber; }
    public void          setPlateNumber(String p) { this.plateNumber = p; }
    public VehicleType   getType()               { return type; }
    public VehicleStatus getStatus()             { return status; }
    public void          setStatus(VehicleStatus s) { this.status = s; }
    public double        getMaxLoadKg()          { return maxLoadKg; }
    public LocalDate     getLastMaintenanceDate() { return lastMaintenanceDate; }
    public LocalDate     getNextMaintenanceDate() { return nextMaintenanceDate; }
    public String        getAssignedCourierId()  { return assignedCourierId; }
    public void          setAssignedCourierId(String id) { this.assignedCourierId = id; }

    public boolean isMaintenanceDue() {
        return LocalDate.now().isAfter(nextMaintenanceDate) ||
               LocalDate.now().isEqual(nextMaintenanceDate);
    }

    public boolean isAvailable() {
        return status == VehicleStatus.AVAILABLE;
    }

    public void scheduleMaintenance() {
        this.status              = VehicleStatus.UNDER_MAINTENANCE;
        this.lastMaintenanceDate = LocalDate.now();
        this.nextMaintenanceDate = LocalDate.now().plusMonths(3);
        this.assignedCourierId   = null;
        System.out.println("  [✓] Vehicle " + vehicleId + " scheduled for maintenance.");
    }

    public void completeMaintenance() {
        this.status = VehicleStatus.AVAILABLE;
        System.out.println("  [✓] Vehicle " + vehicleId + " maintenance complete — now AVAILABLE.");
    }

    public void displayInfo() {
        System.out.println("  ┌──────────────────────────────────────┐");
        System.out.printf ("  │  Vehicle ID  : %-21s│%n", vehicleId);
        System.out.printf ("  │  Plate       : %-21s│%n", plateNumber);
        System.out.printf ("  │  Type        : %-21s│%n", type);
        System.out.printf ("  │  Status      : %-21s│%n", status);
        System.out.printf ("  │  Max Load    : %-18.1f kg│%n", maxLoadKg);
        System.out.printf ("  │  Last Maint  : %-21s│%n", lastMaintenanceDate);
        System.out.printf ("  │  Next Maint  : %-21s│%n", nextMaintenanceDate);
        System.out.printf ("  │  Courier     : %-21s│%n",
                assignedCourierId != null ? assignedCourierId : "Unassigned");
        System.out.printf ("  │  Maint Due?  : %-21s│%n",
                isMaintenanceDue() ? "*** YES ***" : "No");
        System.out.println("  └──────────────────────────────────────┘");
    }

    public void displaySummary() {
        System.out.printf("  %-10s %-12s %-12s %-18s %s%n",
                vehicleId, plateNumber, type, status,
                isMaintenanceDue() ? "⚠ MAINT DUE" : "OK");
    }
}
