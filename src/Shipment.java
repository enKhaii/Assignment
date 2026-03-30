/*
    Shipment - Represents a delivery shipment
*/

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Shipment {
    // Shipping speed category
    public enum ShippingSpeed{  // enum can be a class like Vehicle.java
        STANDARD(5.00, "3-5 Days"), // RM5.00 per k g, shipment time 3-5 days
        EXPRESS(9.50, "1-2 Days");  // RM9.50 per kg, shipment time 1-2 days


        // constant(final) as it's fixed rate and time(prevent changing)
        private final double ratePerKg;
        private final String deliveryTime;


        // Constructor (cannot be public bc it's inside ENUM)
        ShippingSpeed(double ratePerKg, String deliveryTime){
            this.ratePerKg = ratePerKg;
            this.deliveryTime = deliveryTime;
        }

        // No SETTERS because it's already set, no need to set the value for it
        // Since data fields is Private, to access the value, we have GETTER METHODS :)
        public double getRatePerKg(){
            return ratePerKg;
        }

        public String getDeliveryTime(){
            return deliveryTime;
        }
    }

    // Shipment Status category
    public enum ShipmentStatus{
        PENDING_PAYMENT,    // Created but not paid
        PAID,               // Payment confirmed
        PICKED_UP,          // Collected from sender
        IN_TRANSIT,         // On the way to destination
        OUT_FOR_DELIVERY,   // Out with courier for final delivery
        DELIVERED,          // Successfully delivered
        FAILED_ATTEMPT,     // Delivery attempt failed
        CANCELLED           // Shipment cancelled
    }

    // Status(Shipment) history
    // Nested Class - It only helps the big class to do stuff, other class CAN'T access this class
    // COMPOSITION - belongs entirely to SHIPMENT only, if SHIPMENT don't exist, this cannot exist
    public static class StatusLog{
        // final because statusLog should not be changeable by anyone once it's created, 
        // - its like a tracking log and no one can change the past(history)
        // private because encapsulation
        private final ShipmentStatus shipmentStatus;
        private final LocalDateTime timeStamp;
        private final String note;
        
        // Constructor
        // 2 parameters only bc we only ask Admin for the Status and Note, date we just get from system
        public StatusLog(ShipmentStatus shipmentStatus, String note){
            this.shipmentStatus = shipmentStatus;
            this.timeStamp = LocalDateTime.now();
            this.note = note;
        }

        // No SETTER bc variables are FINAL, they can't be changed
        // Getter (Since the data fields is private)
        public ShipmentStatus getShipmentStatus(){
            return shipmentStatus;
        }

        public LocalDateTime getTimeStamp(){
            return timeStamp;
        }

        public String getNote(){
            return note;
        }
    }


    // ATTRIBUTES
    // FINAL because if changed midway, system loses package (after assigned, cannot change)
    private final String trackingID;    // Unique ID for tracking (e.g. TRK10001)
    private final String senderID;      // ID that who sent the package (delivery)
    private String courierID;           // Who's delivering this package ID (can be null, no one delivering)
    
    private final Parcel parcel;        // Composition - parcel details

    private final String pickupAddress;
    private final String deliveryAddress;
    private final double distance;      // km

    private final ShippingSpeed speed;
    private ShipmentStatus status;

    // Fees
    private final double baseFee;       // Based on weight x fee
    private final double distanceFee;   // distance x RM0.05/km
    private final double insuranceFee;  // from Parcel.java
    private final double totalFee;

    // Status Tracking (COMPOSITION - owned by Shipment)
    private final List<StatusLog> statusHistory;

    // Timestamps
    private final LocalDateTime createdAt;  // Order created at ???
    private LocalDateTime deliveredAt;      // Order delivered at ???


    // CONSTRUCTOR
    public Shipment(String trackingID, String senderID, Parcel parcel, String pickupAddress, String deliveryAddress, double distance, ShippingSpeed speed){
        this.trackingID = trackingID;
        this.senderID = senderID;
        this.parcel = parcel;
        this.pickupAddress = pickupAddress;
        this.deliveryAddress = deliveryAddress;
        this.distance = distance;
        this.speed = speed;

        // Calculate fees
        this.baseFee = parcel.getChargeableWeight() * speed.getRatePerKg();
        this.distanceFee = distance * 0.05;
        this.insuranceFee = parcel.calculateInsuranceFee();
        this.totalFee = baseFee + distanceFee + insuranceFee;

        // Initialize status
        this.status = ShipmentStatus.PENDING_PAYMENT;
        this.statusHistory = new ArrayList<>();
        this.statusHistory.add(new StatusLog(ShipmentStatus.PENDING_PAYMENT, "Shipment created"));

        // Timestamps
        this.createdAt = LocalDateTime.now();
        this.deliveredAt = null;    // null because not assigned yet
        this.courierID = null;      
    }

    // GETTERS
    public String getTrackingID(){
        return trackingID;
    }

    public String getSenderID(){
        return senderID;
    }

    public String getCourierID(){
        return courierID;
    }

    public Parcel getParcel(){
        return parcel;
    }

    public String getPickupAddress(){
        return pickupAddress;
    }

    public String getDeliveryAddress(){
        return deliveryAddress;
    }

    public double getDistance(){
        return distance;
    }

    public ShippingSpeed getSpeed(){
        return speed;
    }

    public ShipmentStatus getStatus(){
        return status;
    }

    public double getBaseFee(){
        return baseFee;
    }

    public double getDistanceFee(){
        return distanceFee;
    }

    public double getInsuranceFee(){
        return insuranceFee;
    }

    public double getTotalFee(){
        return totalFee;
    }

    public List<StatusLog> getStatusHistory(){
        return new ArrayList<>(statusHistory);
    }

    public LocalDateTime getCreatedAt(){
        return createdAt;
    }

    public LocalDateTime getDeliveredAt(){
        return deliveredAt;
    }


    // SETTERS
    public void setCourierID(String courierID){
        this.courierID = courierID;
    }


    // STATUS MANAGEMENT
    public void updateStatus(ShipmentStatus newStatus, String note){
        this.status = newStatus;
        this.statusHistory.add(new StatusLog(newStatus, note));

        // If status = DELIVERED, record timestamp to assign deliveredAt
        if(newStatus == ShipmentStatus.DELIVERED){
            this.deliveredAt = LocalDateTime.now();
        }
    }

    // Check if shipment can be cancelled (like pending_payment or paid, others status cannot be cancelled e.g. in progress)
    public boolean canBeCancelled(){
        return status == ShipmentStatus.PENDING_PAYMENT || status == ShipmentStatus.PAID;
    }

    // Check if payment is confirmed
    public boolean isPaid(){
        return status != ShipmentStatus.PENDING_PAYMENT;
    }


    // DISPLAY METHODS
    public void displaySummary(){
        System.out.printf("  %-15s %-15s %-20s RM %-10.2f%n", trackingID, senderID, status, totalFee);
    }

    public void displayFullDetails(){
        System.out.println("\n  ╔════════════════════════════════════════════════════════╗");
        System.out.println("  ║                    SHIPMENT DETAILS                    ║");
        System.out.println("  ╚════════════════════════════════════════════════════════╝"); 
        System.out.println("\n  SHIPMENT INFORMATION:");
        System.out.println("  ────────────────────────────────────────────────────────────────────────────────────");
        System.out.println("  Tracking ID       : " + trackingID);
        System.out.println("  Status            : " + status);
        System.out.println("  Sender ID         : " + senderID);
        System.out.println("  Courier ID        : " + (courierID != null ? courierID : "Not assigned"));
        System.out.println("  Shipping Speed    : " + speed + " (" + speed.getDeliveryTime() + ")");
        System.out.println("\n  DELIVERY DETAILS:");
        System.out.println("  ────────────────────────────────────────────────────────────────────────────────────");
        System.out.println("  Pickup Address    : " + pickupAddress);
        System.out.println("  Delivery Address  : " + deliveryAddress);
        System.out.println("  Distance          : " + distance + " km");
        System.out.println("\n  PARCEL INFORMATION:");
        System.out.println("  ────────────────────────────────────────────────────────────────────────────────────");
        System.out.println("  Content Type      : " + parcel.getContentType());
        System.out.println("  Descrption        : " + parcel.getDescription());
        System.out.printf("  Weight            : %.2f kg%n", parcel.getWeight());
        System.out.printf("  Dimensions        : %.1f x %.1f x %.1f cm%n", parcel.getLength(), parcel.getWidth(), parcel.getHeight());
        System.out.printf("  Declared Value    : RM %.2f%n", parcel.getDeclaredValue());        
        System.out.println("\n  FEE BREAKDOWN:");
        System.out.println("  ────────────────────────────────────────────────────────────────────────────────────");
        System.out.printf("  Base Fee          : RM %.2f (%.2f kg x RM %.2f/kg)%n", baseFee, parcel.getChargeableWeight(), speed.getRatePerKg());
        System.out.printf("  Distance Fee      : RM %.2f (%.1f km x RM 0.05/km)%n", distanceFee, distance);
        System.out.printf("  Insurance Fee     : RM %.2f%n", insuranceFee);
        System.out.printf("  TOTAL FEE         : RM %.2f%n", totalFee);        
        System.out.println("\n  TIMESTAMPS:");
        System.out.println("  ────────────────────────────────────────────────────────────────────────────────────");
        System.out.println("  Created           : " + createdAt.toString().substring(0, 19).replace('T', ' '));

        if (deliveredAt != null) {
            System.out.println("  Delivered         : " + deliveredAt);
        }

        System.out.println();
    }

    public void displayTrackingHistory(){
        System.out.println("\n  TRACKING HISTORY:");
        System.out.println("  ────────────────────────────────────────────────────────────────────────────────────");
        
        for (StatusLog log : statusHistory) {
            System.out.printf("  [%s] %s - %s%n",
                log.getTimeStamp().toString().substring(0, 19).replace('T', ' '),
                log.getShipmentStatus(), log.getNote());
        }
    }
}