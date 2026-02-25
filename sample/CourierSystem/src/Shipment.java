import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Shipment — the core entity of the logistics system.
 * Demonstrates: Composition (owns Parcel + StatusLog),
 *               Encapsulation, Business Logic (fee calculation)
 */
public class Shipment {

    public enum ShippingSpeed { STANDARD, EXPRESS }

    public enum ShipmentStatus {
        PENDING_PAYMENT, PAID, PICKED_UP, IN_TRANSIT,
        OUT_FOR_DELIVERY, DELIVERED, FAILED_ATTEMPT, CANCELLED
    }

    // ─── Inner class for status history (Composition) ───
    public static class StatusLog {
        private final ShipmentStatus status;
        private final String         note;
        private final String         timestamp;

        public StatusLog(ShipmentStatus status, String note) {
            this.status    = status;
            this.note      = note;
            this.timestamp = LocalDateTime.now()
                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        }

        public ShipmentStatus getStatus()    { return status; }
        public String         getNote()      { return note; }
        public String         getTimestamp() { return timestamp; }

        @Override
        public String toString() {
            return String.format("  [%s] %-20s — %s", timestamp, status, note);
        }
    }

    // ─── Pricing constants ───────────────────────────────
    private static final double BASE_RATE_STANDARD = 5.00;  // RM per kg
    private static final double BASE_RATE_EXPRESS   = 9.50;
    private static final double RATE_PER_KM         = 0.05;
    private static int          trackingCounter      = 10000;

    // ─── Fields ──────────────────────────────────────────
    private final String         trackingId;
    private final String         senderId;
    private final String         receiverName;
    private final String         receiverPhone;
    private final String         receiverAddress;
    private final String         originRegion;
    private final String         destinationRegion;
    private final double         distanceKm;
    private final ShippingSpeed  shippingSpeed;
    private final Parcel         parcel;           // Composition
    private final List<StatusLog> statusHistory;   // Composition

    private ShipmentStatus currentStatus;
    private String         assignedCourierId;
    private int            failedAttempts;
    private double         shippingFee;

    public Shipment(String senderId, String receiverName, String receiverPhone,
                    String receiverAddress, String originRegion,
                    String destinationRegion, double distanceKm,
                    ShippingSpeed speed, Parcel parcel) {
        this.trackingId         = "TRK" + (++trackingCounter);
        this.senderId           = senderId;
        this.receiverName       = receiverName;
        this.receiverPhone      = receiverPhone;
        this.receiverAddress    = receiverAddress;
        this.originRegion       = originRegion;
        this.destinationRegion  = destinationRegion;
        this.distanceKm         = distanceKm;
        this.shippingSpeed      = speed;
        this.parcel             = parcel;
        this.statusHistory      = new ArrayList<>();
        this.currentStatus      = ShipmentStatus.PENDING_PAYMENT;
        this.failedAttempts     = 0;
        this.assignedCourierId  = null;
        this.shippingFee        = calculateFee();

        logStatus(ShipmentStatus.PENDING_PAYMENT, "Shipment created, awaiting payment.");
    }

    // ─── Fee Calculation ─────────────────────────────────
    private double calculateFee() {
        double baseRate   = (shippingSpeed == ShippingSpeed.EXPRESS)
                            ? BASE_RATE_EXPRESS : BASE_RATE_STANDARD;
        double weightFee  = parcel.getChargeableWeight() * baseRate;
        double distanceFee = distanceKm * RATE_PER_KM;
        double insurance  = parcel.getInsuranceFee();
        return weightFee + distanceFee + insurance;
    }

    // ─── Status Management ───────────────────────────────
    public void updateStatus(ShipmentStatus newStatus, String note) {
        this.currentStatus = newStatus;
        logStatus(newStatus, note);
    }

    private void logStatus(ShipmentStatus status, String note) {
        statusHistory.add(new StatusLog(status, note));
    }

    public void confirmPayment() {
        if (currentStatus != ShipmentStatus.PENDING_PAYMENT)
            throw new IllegalStateException("Shipment is not awaiting payment.");
        updateStatus(ShipmentStatus.PAID, "Payment confirmed. RM " +
                String.format("%.2f", shippingFee) + " received.");
    }

    public void recordFailedAttempt(String reason) {
        failedAttempts++;
        updateStatus(ShipmentStatus.FAILED_ATTEMPT,
                "Attempt #" + failedAttempts + " failed: " + reason);
    }

    // ─── Getters ─────────────────────────────────────────
    public String          getTrackingId()          { return trackingId; }
    public String          getSenderId()            { return senderId; }
    public String          getReceiverName()        { return receiverName; }
    public String          getReceiverPhone()       { return receiverPhone; }
    public String          getReceiverAddress()     { return receiverAddress; }
    public String          getOriginRegion()        { return originRegion; }
    public String          getDestinationRegion()   { return destinationRegion; }
    public double          getDistanceKm()          { return distanceKm; }
    public ShippingSpeed   getShippingSpeed()       { return shippingSpeed; }
    public Parcel          getParcel()              { return parcel; }
    public ShipmentStatus  getCurrentStatus()       { return currentStatus; }
    public String          getAssignedCourierId()   { return assignedCourierId; }
    public int             getFailedAttempts()      { return failedAttempts; }
    public double          getShippingFee()         { return shippingFee; }
    public List<StatusLog> getStatusHistory()       { return new ArrayList<>(statusHistory); }

    public void setAssignedCourierId(String courierId) {
        this.assignedCourierId = courierId;
    }

    // ─── Display ─────────────────────────────────────────
    public void displayFull() {
        System.out.println("  ┌─────────────────────────────────────────┐");
        System.out.printf ("  │  Tracking ID  : %-24s│%n", trackingId);
        System.out.printf ("  │  Status       : %-24s│%n", currentStatus);
        System.out.printf ("  │  Speed        : %-24s│%n", shippingSpeed);
        System.out.printf ("  │  From         : %-24s│%n", originRegion);
        System.out.printf ("  │  To           : %-24s│%n", destinationRegion);
        System.out.printf ("  │  Distance     : %-21.1f km│%n", distanceKm);
        System.out.printf ("  │  Receiver     : %-24s│%n", receiverName);
        System.out.printf ("  │  Address      : %-24s│%n", receiverAddress);
        System.out.printf ("  │  Courier      : %-24s│%n",
                assignedCourierId != null ? assignedCourierId : "Not assigned");
        System.out.printf ("  │  Shipping Fee : RM %-21.2f│%n", shippingFee);
        System.out.printf ("  │  Failed Att.  : %-24d│%n", failedAttempts);
        System.out.println("  ├─────────────────────────────────────────┤");
        System.out.println("  │  PARCEL DETAILS                         │");
        System.out.println("  ├─────────────────────────────────────────┤");
        parcel.display();
        System.out.println("  └─────────────────────────────────────────┘");
    }

    public void displaySummary() {
        System.out.printf("  %-12s %-20s %-18s %-12s RM %.2f%n",
                trackingId, receiverName, destinationRegion,
                currentStatus, shippingFee);
    }

    public void displayTrackingHistory() {
        System.out.println("  Tracking history for: " + trackingId);
        System.out.println("  " + "─".repeat(60));
        for (StatusLog log : statusHistory) {
            System.out.println(log);
        }
        System.out.println("  " + "─".repeat(60));
    }
}
