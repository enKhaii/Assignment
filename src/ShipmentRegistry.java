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
    private int trackingCounter = 10000;    // e.g. TRK10001, starts from 10001(pre-increment)

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
        System.out.println("  ║                      ALL SHIPMENTS                     ║");
        System.out.println("  ╚════════════════════════════════════════════════════════╝");
        System.out.println("  Total: " + shipments.size() + " shipment(s)");
        System.out.println();
        System.out.printf("  %-12s %-15s %-20s %-12s%n","Tracking ID", "Sender", "Status", "Total Fee");
        System.out.println("  " + "─".repeat(70));
        
        for (Shipment s : shipments) {
            s.displaySummary();
        }
        System.out.println();
    }
}
