import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * FleetManager — manages the delivery vehicle fleet.
 * Demonstrates: Aggregation, CRUD operations, Encapsulation
 */
public class FleetManager {

    private final List<Vehicle> vehicles;

    public FleetManager() {
        this.vehicles = new ArrayList<>();
    }

    // CREATE
    public void addVehicle(Vehicle vehicle) {
        if (findById(vehicle.getVehicleId()) != null)
            throw new IllegalArgumentException(
                    "Vehicle ID " + vehicle.getVehicleId() + " already exists.");
        vehicles.add(vehicle);
        System.out.println("  [+] Vehicle added: " + vehicle.getVehicleId() +
                           " (" + vehicle.getPlateNumber() + ")");
    }

    // READ
    public Vehicle findById(String vehicleId) {
        return vehicles.stream()
                .filter(v -> v.getVehicleId().equalsIgnoreCase(vehicleId))
                .findFirst()
                .orElse(null);
    }

    public List<Vehicle> getAvailableVehicles() {
        return vehicles.stream()
                .filter(Vehicle::isAvailable)
                .collect(Collectors.toList());
    }

    public List<Vehicle> getVehiclesDueForMaintenance() {
        return vehicles.stream()
                .filter(Vehicle::isMaintenanceDue)
                .collect(Collectors.toList());
    }

    // UPDATE — assign vehicle to courier
    public boolean assignToCourier(String vehicleId, String courierId)
            throws VehicleNotFoundException {
        Vehicle v = findById(vehicleId);
        if (v == null) throw new VehicleNotFoundException(vehicleId);
        if (!v.isAvailable())
            throw new IllegalStateException(
                    "Vehicle " + vehicleId + " is not available (status: " + v.getStatus() + ")");
        v.setAssignedCourierId(courierId);
        v.setStatus(Vehicle.VehicleStatus.IN_USE);
        System.out.println("  [✓] Vehicle " + vehicleId + " assigned to courier " + courierId);
        return true;
    }

    // UPDATE — release vehicle from courier
    public boolean releaseVehicle(String vehicleId) throws VehicleNotFoundException {
        Vehicle v = findById(vehicleId);
        if (v == null) throw new VehicleNotFoundException(vehicleId);
        v.setAssignedCourierId(null);
        v.setStatus(Vehicle.VehicleStatus.AVAILABLE);
        System.out.println("  [✓] Vehicle " + vehicleId + " released and now AVAILABLE.");
        return true;
    }

    // DELETE
    public boolean removeVehicle(String vehicleId) throws VehicleNotFoundException {
        Vehicle v = findById(vehicleId);
        if (v == null) throw new VehicleNotFoundException(vehicleId);
        if (v.getStatus() == Vehicle.VehicleStatus.IN_USE)
            throw new IllegalStateException("Cannot remove a vehicle currently in use.");
        vehicles.remove(v);
        System.out.println("  [-] Vehicle " + vehicleId + " removed.");
        return true;
    }

    public void displayAll() {
        if (vehicles.isEmpty()) {
            System.out.println("  No vehicles in the fleet.");
            return;
        }
        System.out.printf("  %-10s %-12s %-12s %-18s %s%n",
                "ID", "Plate", "Type", "Status", "Maintenance");
        System.out.println("  " + "─".repeat(65));
        for (Vehicle v : vehicles) v.displaySummary();
    }

    public int getTotalVehicles() { return vehicles.size(); }
}
