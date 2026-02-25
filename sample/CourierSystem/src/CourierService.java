import java.util.List;

/**
 * CourierService — orchestrates the core logistics operations.
 * Demonstrates: Association (uses all registries),
 *               Exception Handling, Business Logic
 */
public class CourierService {

    private final ShipmentRegistry shipmentRegistry; // Association
    private final FleetManager     fleetManager;     // Association

    // In-memory courier and sender lists (Association)
    private final java.util.List<Courier> couriers;
    private final java.util.List<Sender>  senders;

    public CourierService(ShipmentRegistry shipmentRegistry,
                          FleetManager fleetManager,
                          java.util.List<Courier> couriers,
                          java.util.List<Sender> senders) {
        this.shipmentRegistry = shipmentRegistry;
        this.fleetManager     = fleetManager;
        this.couriers         = couriers;
        this.senders          = senders;
    }

    // ─── SENDER: Create Shipment ──────────────────────────
    public Shipment createShipment(String senderId, String receiverName,
                                   String receiverPhone, String receiverAddress,
                                   String originRegion, String destinationRegion,
                                   double distanceKm, Shipment.ShippingSpeed speed,
                                   Parcel parcel)
            throws SenderNotFoundException {

        Sender sender = findSenderById(senderId);
        if (sender == null) throw new SenderNotFoundException(senderId);

        Shipment shipment = new Shipment(senderId, receiverName, receiverPhone,
                receiverAddress, originRegion, destinationRegion,
                distanceKm, speed, parcel);

        shipmentRegistry.addShipment(shipment);
        sender.addShipmentId(shipment.getTrackingId());
        return shipment;
    }

    // ─── SENDER: Pay for Shipment ─────────────────────────
    public void payForShipment(String trackingId)
            throws ShipmentNotFoundException {
        Shipment s = getShipmentOrThrow(trackingId);
        s.confirmPayment();
        System.out.println("  [✓] Payment confirmed for: " + trackingId);
    }

    // ─── ADMIN: Assign Courier to Shipment ────────────────
    public void assignCourierToShipment(String trackingId, String courierId)
            throws ShipmentNotFoundException, CourierNotFoundException,
                   PaymentNotConfirmedException, InvalidShipmentStatusException {

        Shipment shipment = getShipmentOrThrow(trackingId);

        if (shipment.getCurrentStatus() == Shipment.ShipmentStatus.PENDING_PAYMENT)
            throw new PaymentNotConfirmedException(trackingId);

        if (shipment.getCurrentStatus() != Shipment.ShipmentStatus.PAID)
            throw new InvalidShipmentStatusException(trackingId, "ASSIGN COURIER");

        Courier courier = findCourierById(courierId);
        if (courier == null) throw new CourierNotFoundException(courierId);

        shipment.setAssignedCourierId(courierId);
        shipment.updateStatus(Shipment.ShipmentStatus.PICKED_UP,
                "Assigned to courier: " + courier.getName());
        courier.assignShipment(trackingId);

        System.out.println("  [✓] Shipment " + trackingId +
                           " assigned to " + courier.getName());
    }

    // ─── ADMIN: Update Shipment Status ───────────────────
    public void updateShipmentStatus(String trackingId,
                                     Shipment.ShipmentStatus newStatus,
                                     String note)
            throws ShipmentNotFoundException {
        Shipment s = getShipmentOrThrow(trackingId);
        s.updateStatus(newStatus, note);
        System.out.println("  [✓] " + trackingId + " → " + newStatus);
    }

    // ─── COURIER: Mark as Delivered ──────────────────────
    public void markDelivered(String trackingId, String courierId)
            throws ShipmentNotFoundException, CourierNotFoundException,
                   InvalidShipmentStatusException {

        Shipment s = getShipmentOrThrow(trackingId);

        if (s.getCurrentStatus() != Shipment.ShipmentStatus.OUT_FOR_DELIVERY
         && s.getCurrentStatus() != Shipment.ShipmentStatus.IN_TRANSIT
         && s.getCurrentStatus() != Shipment.ShipmentStatus.PICKED_UP) {
            throw new InvalidShipmentStatusException(trackingId, "MARK DELIVERED");
        }

        Courier courier = findCourierById(courierId);
        if (courier == null) throw new CourierNotFoundException(courierId);

        s.updateStatus(Shipment.ShipmentStatus.DELIVERED,
                "Delivered by courier: " + courier.getName());
        courier.completeDelivery(trackingId);

        System.out.println("  [✓] Shipment " + trackingId +
                           " marked DELIVERED by " + courier.getName());
    }

    // ─── COURIER: Record Failed Delivery Attempt ─────────
    public void recordFailedAttempt(String trackingId, String reason)
            throws ShipmentNotFoundException {
        Shipment s = getShipmentOrThrow(trackingId);
        s.recordFailedAttempt(reason);
        System.out.println("  [!] Failed attempt logged for " + trackingId +
                           ". Reason: " + reason);
        System.out.println("      Total failed attempts: " + s.getFailedAttempts());
    }

    // ─── PUBLIC: Track Parcel ────────────────────────────
    public void trackParcel(String trackingId)
            throws ShipmentNotFoundException {
        Shipment s = getShipmentOrThrow(trackingId);
        s.displayTrackingHistory();
        System.out.println("  Current Status: " + s.getCurrentStatus());
        if (s.getAssignedCourierId() != null) {
            Courier c = findCourierById(s.getAssignedCourierId());
            if (c != null)
                System.out.println("  Handled by    : " + c.getName() +
                                   " (" + c.getPhone() + ")");
        }
    }

    // ─── COURIER: View Daily Delivery List ───────────────
    public void viewCourierDeliveryList(String courierId)
            throws CourierNotFoundException {
        Courier courier = findCourierById(courierId);
        if (courier == null) throw new CourierNotFoundException(courierId);

        List<Shipment> jobs = shipmentRegistry.findByCourier(courierId);
        System.out.println("  Delivery list for: " + courier.getName());
        System.out.println("  " + "─".repeat(50));
        if (jobs.isEmpty()) {
            System.out.println("  No active deliveries.");
        } else {
            for (Shipment s : jobs) {
                System.out.printf("  %-12s | %-20s | %s%n",
                        s.getTrackingId(), s.getReceiverName(),
                        s.getReceiverAddress());
                System.out.printf("               Status: %s%n", s.getCurrentStatus());
            }
        }
        System.out.println("  " + "─".repeat(50));
    }

    // ─── Helpers ─────────────────────────────────────────
    private Shipment getShipmentOrThrow(String trackingId)
            throws ShipmentNotFoundException {
        Shipment s = shipmentRegistry.findByTrackingId(trackingId);
        if (s == null) throw new ShipmentNotFoundException(trackingId);
        return s;
    }

    public Sender findSenderById(String id) {
        return senders.stream()
                .filter(s -> s.getPersonId().equalsIgnoreCase(id))
                .findFirst().orElse(null);
    }

    public Courier findCourierById(String id) {
        return couriers.stream()
                .filter(c -> c.getPersonId().equalsIgnoreCase(id))
                .findFirst().orElse(null);
    }

    public java.util.List<Courier> getAllCouriers()  { return couriers; }
    public java.util.List<Sender>  getAllSenders()   { return senders; }
    public ShipmentRegistry        getRegistry()     { return shipmentRegistry; }
    public FleetManager            getFleetManager() { return fleetManager; }
}
