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


    // CRUD OPERATIONS
    // ---------- CREATE, READ(GET DATA) OPERATIONS METHODS -----------
    public String generateTrackingID(){
        return "TRK" + (++trackingCounter); // pre increment, increment before use, so 10001
    }

    public void addShipment(Shipment shipment){
        shipments.add(shipment);
        System.out.println("\n  [DONE] Shipment created: " + shipment.getTrackingID());
    }

}
