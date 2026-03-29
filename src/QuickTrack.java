import java.util.Scanner;

public class QuickTrack{
    private ShipmentRegistry shipmentRegistry;
    private Scanner input;

    // Constructor
    public QuickTrack(ShipmentRegistry shipmentRegistry, Scanner input){
        this.shipmentRegistry = shipmentRegistry;
        this.input = input;
    }

    // MAIN METHOD
    public void track(){
        System.out.println("\n  ╔════════════════════════════════════════════════════════╗");
        System.out.println("  ║                      QUICK TRACK                       ║");
        System.out.println("  ╚════════════════════════════════════════════════════════╝");
        System.out.println("  Track your shipment by entering the Tracking ID.");
        System.out.println("  No login required!\n");

        System.out.print("  Enter Tracking ID -> ");
        String trackingID  = input.next();;

        Shipment s = shipmentRegistry.findByTrackingID(trackingID);

        if(s == null)
            displayNotFound(trackingID);
        else{
            displayTrackingInfo(s);
        }
    }

    // DISPLAY METHODS
    private void displayNotFound(String trackingID){
        System.out.println("\n  ╔════════════════════════════════════════════════════════╗");
        System.out.println("  ║                   SHIPMENT NOT FOUND                   ║");
        System.out.println("  ╚════════════════════════════════════════════════════════╝");
        System.out.println("\n  >> The tracking ID \"" + trackingID + "\" was not found.\n");
        System.out.println("  Please check:");
        System.out.println("  - Tracking number is correct");
        System.out.println("  - No extra spaces or characters");
        System.out.println("  - Tracking ID format: TRK10001, TRK10002, etc.");
        System.out.println();
    }

    private void displayTrackingInfo(Shipment s){
        System.out.println("\n  ╔════════════════════════════════════════════════════════╗");
        System.out.println("  ║                SHIPMENT TRACKING DETAILS               ║");
        System.out.println("  ╚════════════════════════════════════════════════════════╝\n");

        System.out.println("  SHIPMENT INFORMATION:");
        System.out.println("  ────────────────────────────────────────────────────────────────────────────────────");
        System.out.println("  Tracking ID       : " + s.getTrackingID());
        System.out.println("  Current Status    : " + s.getStatus());
        displayStatusDetails(s.getStatus());
        System.out.println("  Shipping Speed    : " + s.getSpeed() + " (" + s.getSpeed().getDeliveryTime() + ")");

        if(s.getStatus() == Shipment.ShipmentStatus.PENDING_PAYMENT){
            System.out.printf("  Total Fee         : RM %.2f\n", s.getTotalFee());
        }
        
        // Delivery details
        System.out.println("\n  DELIVERY DETAILS:");
        System.out.println("  ────────────────────────────────────────────────────────────────────────────────────");
        System.out.println("  Pickup Address    : " + s.getPickupAddress());
        System.out.println("  Delivery Address  : " + s.getDeliveryAddress());
        System.out.println("  Distance          : " + s.getDistance() + " km");
        
        // Parcel info
        Parcel p = s.getParcel();   // Retrieve Parcel Object
        System.out.println("\n  PARCEL INFORMATION:");
        System.out.println("  ────────────────────────────────────────────────────────────────────────────────────");
        System.out.println("  Content Type      : " + p.getContentType());
        System.out.println("  Descrption        : " + p.getDescription());
        System.out.printf("  Weight            : %.2f kg%n", p.getWeight());
        System.out.printf("  Dimensions        : %.1f x %.1f x %.1f cm%n", p.getLength(), p.getWidth(), p.getHeight());
        
        // Tracking history
        s.displayTrackingHistory();
        
        // Additional info based on status
        displayStatusMessage(s.getStatus());
        
        System.out.println();
    }

    // Display Status Method
    private void displayStatusDetails(Shipment.ShipmentStatus status){
        String statusMsg = switch(status){
            case PENDING_PAYMENT -> "Awaiting payment confirmation";
            case PAID -> "Payment received - Preparing for pickup";
            case PICKED_UP -> "Package collected from sender";
            case IN_TRANSIT -> "Package is on the way";
            case OUT_FOR_DELIVERY -> "Out for delivery - Arriving soon";
            case DELIVERED -> "Package delivered successfully";
            case FAILED_ATTEMPT -> "Delivery attempt failed";
            case CANCELLED -> "Shipment has been cancelled";
        };

        System.out.println("  Details           : " + statusMsg);
    }

    // Status Message Method
    private void displayStatusMessage(Shipment.ShipmentStatus status) {
        System.out.println("\n  WHAT HAPPENS NEXT?");
        System.out.println("  ────────────────────────────────────────────────────────────────────────────────────");
        
        String message = switch (status){
            case PENDING_PAYMENT -> 
                "  Please complete your payment to proceed with shipment.\n" +
                "  Visit the Sender Portal to pay.";
            case PAID -> 
                "  Your payment has been received!\n" +
                "  A courier will pick up your package within 24 hours.";
            case PICKED_UP -> 
                "  Your package has been collected and is being sorted.\n" +
                "  It will be in transit soon.";
            case IN_TRANSIT -> 
                "  Your package is currently on the way to the delivery address.\n" +
                "  Check back for updates.";
            case OUT_FOR_DELIVERY -> 
                "  Your package is with the courier for final delivery.\n" +
                "  Expected delivery: Today";
            case DELIVERED -> 
                "  Your package has been delivered successfully!\n" +
                "  Thank you for using our service.";
            case FAILED_ATTEMPT -> 
                "  Delivery attempt failed. The courier will try again.\n" +
                "  Please ensure someone is available to receive the package.";
            case CANCELLED -> 
                "  This shipment has been cancelled.\n" +
                "  If this was a mistake, please contact support.";
        };
        
        System.out.println(message);
    }
}