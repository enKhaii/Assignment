import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * ShipmentRegistry — manages all shipments in the system.
 * Demonstrates: Aggregation (holds Shipment references), CRUD operations
 */
public class ShipmentRegistry {

    private final List<Shipment> shipments;

    public ShipmentRegistry() {
        this.shipments = new ArrayList<>();
    }

    // CREATE
    public void addShipment(Shipment shipment) {
        shipments.add(shipment);
        System.out.println("  [+] Shipment created — Tracking ID: " +
                           shipment.getTrackingId());
        System.out.printf ("      Shipping fee: RM %.2f%n", shipment.getShippingFee());
    }

    // READ — find by tracking ID
    public Shipment findByTrackingId(String trackingId) {
        return shipments.stream()
                .filter(s -> s.getTrackingId().equalsIgnoreCase(trackingId))
                .findFirst()
                .orElse(null);
    }

    // READ — find all shipments by sender
    public List<Shipment> findBySender(String senderId) {
        return shipments.stream()
                .filter(s -> s.getSenderId().equalsIgnoreCase(senderId))
                .collect(Collectors.toList());
    }

    // READ — find all shipments by current status
    public List<Shipment> findByStatus(Shipment.ShipmentStatus status) {
        return shipments.stream()
                .filter(s -> s.getCurrentStatus() == status)
                .collect(Collectors.toList());
    }

    // READ — find all shipments assigned to a courier
    public List<Shipment> findByCourier(String courierId) {
        return shipments.stream()
                .filter(s -> courierId.equals(s.getAssignedCourierId()))
                .filter(s -> s.getCurrentStatus() != Shipment.ShipmentStatus.DELIVERED
                          && s.getCurrentStatus() != Shipment.ShipmentStatus.CANCELLED)
                .collect(Collectors.toList());
    }

    // UPDATE — cancel a shipment
    public boolean cancelShipment(String trackingId)
            throws ShipmentNotFoundException, InvalidShipmentStatusException {
        Shipment s = findByTrackingId(trackingId);
        if (s == null) throw new ShipmentNotFoundException(trackingId);
        if (s.getCurrentStatus() == Shipment.ShipmentStatus.DELIVERED ||
            s.getCurrentStatus() == Shipment.ShipmentStatus.IN_TRANSIT ||
            s.getCurrentStatus() == Shipment.ShipmentStatus.OUT_FOR_DELIVERY) {
            throw new InvalidShipmentStatusException(trackingId, "CANCEL");
        }
        s.updateStatus(Shipment.ShipmentStatus.CANCELLED, "Cancelled by admin.");
        return true;
    }

    // Display all shipments (summary table)
    public void displayAll() {
        if (shipments.isEmpty()) {
            System.out.println("  No shipments in the system.");
            return;
        }
        System.out.printf("  %-12s %-20s %-18s %-22s %s%n",
                "Tracking ID", "Receiver", "Destination", "Status", "Fee");
        System.out.println("  " + "─".repeat(85));
        for (Shipment s : shipments) s.displaySummary();
    }

    public int getTotalShipments()    { return shipments.size(); }
    public List<Shipment> getAll()    { return new ArrayList<>(shipments); }
}
