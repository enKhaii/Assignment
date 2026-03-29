/*
    ShipmentRegistry - Backend (Central Database Class) data for shipment management
    == Data Storage and CRUD operations
    don't handle User Inputs or Menus (ShipmentManagement job)
*/

import java.util.ArrayList;
import java.util.List;

public class ShipmentRegistry {
    // List<Shipment> means this list can only hold object created from 'Shipment.java'
    private final List<Shipment> shipments;
    private int trackingCounter = 20000;    // e.g. TRK10001, starts from 10001(pre-increment)

    // Constructor
    public ShipmentRegistry(){
        this.shipments = new ArrayList<>();
    }


    // CRUD OPERATIONS (no delete cause cannot delete shipments, only cancel which is update)
    // ---------- Create ----------
    public String generateTrackingID(){
        return "TRK" + (++trackingCounter); // pre increment, increment before use, so 10001
    }

    public void addShipment(Shipment shipment){
        shipments.add(shipment);
        System.out.println("\n  [DONE] Shipment created: " + shipment.getTrackingID());
    }


    // ---------- Read ----------
    public Shipment findByTrackingID(String trackingID){
        for(Shipment s : shipments){
            if(s.getTrackingID().equalsIgnoreCase(trackingID)){
                return s;  
            }
        }
        return null;
    }

    public List<Shipment> findBySender(String senderID){
        List<Shipment> results = new ArrayList<>(); // make it list bc one person can create multiple shipment (objects)
        
        for(Shipment s: shipments){
            if(s.getSenderID().equalsIgnoreCase(senderID)){
                results.add(s);
            }
        }
        return results; // return the entire shipment (object)
    }

    public List<Shipment> findByStatus(Shipment.ShipmentStatus status){
        List<Shipment> results = new ArrayList<>();

        for(Shipment s : shipments){
            if(s.getStatus() == status){
                results.add(s);
            }
        }
        return results;
    }

    public int getTotalShipments(){
        return shipments.size();    // .size() returns number of collection in list (number)
    }

    public List<Shipment> getAllShipments(){
        return new ArrayList<>(shipments);      // new object to protect original data (prevent modifying)
        // return all shipments in the collection in Shipment List
    }


    // ---------- Update ----------
    public boolean cancelShipment(String trackingID){
        Shipment s = findByTrackingID(trackingID);

        if(s == null){
            throw new IllegalArgumentException("\n  [!] Shipment " + trackingID + " not found.");
        }

        if(!s.canBeCancelled()){
            throw new IllegalStateException("\n  [!] Cannot cancel shipment in status: " + s.getStatus());
        }

        s.updateStatus(Shipment.ShipmentStatus.CANCELLED, "Cancelled By User");
        System.out.println("  [DONE] Shipment " + trackingID + " cancelled.");
        return true;
    } 

    
    // Display
    public void displayAll(){
        if(shipments.isEmpty()){
            System.out.println("\n  [i] No shipments in the system.");
            return;
        }

        System.out.println("\n  ╔════════════════════════════════════════════════════════╗");
        System.out.println("  ║                     ALL SHIPMENTS                      ║");
        System.out.println("  ╚════════════════════════════════════════════════════════╝");
        System.out.println("  Total: " + shipments.size() + " shipment(s)");
        System.out.println();
        System.out.printf("  %-15s %-15s %-20s %-12s%n","Tracking ID", "Sender", "Status", "Total Fee");
        System.out.println("  " + "─".repeat(70));
        
        for (Shipment s : shipments) {
            s.displaySummary();
        }
        System.out.println();
    }

    // LOAD OBJECTS OF SHIPMENT AND PARCEL (SAMPLE DATA)
    // UPDATE STATUS STEP BY STEP BECAUSE OF STATUS LOG
    public void initializeData(){
        // Sample Shipment 1 - Electronic (Express)
        Parcel parcel1 = new Parcel(Parcel.ContentType.ELECTRONICS,"Laptop - Lenovo Legion 5 Gen 10", 3.5, 40, 40, 10, 6000.00);
        Shipment shipment1 = new Shipment("TRK10001", "SDN101", parcel1, "24 Jalan TS 6 Taman Indsutri, 47510 Subang Jaya, Selangor", "14th Floor, Blok B Megan Avenue Ii, 50540 Kuala Lumpur", 30.0, Shipment.ShippingSpeed.EXPRESS);
        shipments.add(shipment1);
        shipment1.updateStatus(Shipment.ShipmentStatus.PAID, "Payment confirmed");

        // Sample Shipment 2 - Documents (Standard)
        Parcel parcel2 = new Parcel(Parcel.ContentType.DOCUMENTS, "Legal documents - Contract paper", 0.5, 30, 20, 2, 100.00);
        Shipment shipment2 = new Shipment("TRK10002", "SDN102", parcel2, "12 Lorong Keramat 21 Kampung Datok Keramat, 54000 Kuala Lumpur", "9 Jalan Ambong Kanan 1 Kepong Baru, 52100 Kuala Lumpur", 10.0, Shipment.ShippingSpeed.STANDARD);
        shipments.add(shipment2);
        shipment2.updateStatus(Shipment.ShipmentStatus.PAID, "Payment confirmed");
        shipment2.updateStatus(Shipment.ShipmentStatus.PICKED_UP, "Picked up from Sender");

        // Sample Shipment 3 - Clothing (Standard - PENDING PAYMENT)
        Parcel parcel3 = new Parcel(Parcel.ContentType.CLOTHING, "Winter Jacket and Accessories", 1.2, 50, 40, 15, 250.00);
        Shipment shipment3 = new Shipment("TRK10003", "SDN101", parcel3, "18, Jalan Todak 2, 13700 Perai, Pulau Pinang", "No 88, Jalan Sultan Ismail, 20200 Kuala Terengganu, Terengganu", 400.0, Shipment.ShippingSpeed.STANDARD);
        shipments.add(shipment3);
        // no status updated cause it stays PENDING_PAYMENT

        // Sample Shipment 4 - Fragile (Express)
        Parcel parcel4 = new Parcel(Parcel.ContentType.FRAGILE, "Glass Vase - Antique", 2.0, 35, 35, 40, 800.00);
        Shipment shipment4 = new Shipment("TRK10004", "SDN102", parcel4, "15, Jalan Merdeka, Taman Melaka Raya, 75000 Melaka, Melaka", "26, Jalan BU 4A, Taman Bachang Utama, 75300 Bachang, Melaka", 15.0, Shipment.ShippingSpeed.EXPRESS);
        shipments.add(shipment4);
        shipment4.updateStatus(Shipment.ShipmentStatus.PAID, "Payment confirmed");
        shipment4.updateStatus(Shipment.ShipmentStatus.PICKED_UP, "Picked up from Sender");
        shipment4.updateStatus(Shipment.ShipmentStatus.IN_TRANSIT, "Package in transit");

        // Sample Shipment 5 - Food (Express)
        Parcel parcel5 = new Parcel(Parcel.ContentType.FOOD, "Fresh Seafood - Premium Lobster", 5.0, 60, 40, 20, 500.00);
        Shipment shipment5 = new Shipment("TRK10005", "SDN101", parcel5, "167, Jalan Sungai Keladi 2, 42000 Port Klang, Selangor", "6226, Jalan Kota Raja, Kawasan 1, 41000 Klang, Selangor", 20.0, Shipment.ShippingSpeed.EXPRESS);
        shipments.add(shipment5);
        shipment5.updateStatus(Shipment.ShipmentStatus.PAID, "Payment confirmed");
        shipment5.updateStatus(Shipment.ShipmentStatus.PICKED_UP, "Picked up from Sender");
        shipment5.updateStatus(Shipment.ShipmentStatus.IN_TRANSIT, "Package in transit");
        shipment5.updateStatus(Shipment.ShipmentStatus.OUT_FOR_DELIVERY, "Out for delivery");
        shipment5.updateStatus(Shipment.ShipmentStatus.DELIVERED, "Delivered successfully");

        System.out.println("  [DONE] Shipment sample loaded.");
    }
}
