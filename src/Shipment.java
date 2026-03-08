/*
    Shipment - Represents a delivery shipment
*/

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Shipment {
    // Shipping speed category
    public enum ShippingSpeed{  // enum can be a class like Vehicle.java
        STANDARD(5.00, "3-5 Days"), // RM5.00 per kg, shipment time 3-5 days
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
    private final String trackingID;    // Unique ID for tracking (e.g. TRK10001)
    private final String senderID;      // ID that who sent the package (delivery)
    // FINAL because if changed midway, system loses package
    private String courierID;           // Who's delivering this package ID (can be null)
}
