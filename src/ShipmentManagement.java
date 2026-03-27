/*
    ShipmentManagement - Admin UI for shipment operations
    Handles view shipments, update status, review/calculate fees, search/filter shipments
*/

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class ShipmentManagement {
    private ShipmentRegistry shipmentRegistry;
    private Scanner input;

    // Constructor
    public ShipmentMangement(ShipmentRegistry shipmentRegistry, Scanner input){
        this.shipmentRegistry = shipmentRegistry;
        this.input = input;
    }

    // Display Main Menu
    public void show(){
        boolean inShipmentMenu = true;
        while(inShipmentMenu){
            try{
                displayMenu();
                int choice = input.nextInt();
                input.nextLine();   // clear buffer

                switch(choice){
                    case 1 -> displayAllShipments();
                    case 2 -> filterByStatus();
                    case 3 -> searchByTrackingID();
                    case 4 -> searchBySenderID();
                    case 5 -> viewShipmentDetails();
                    case 6 -> reviewShippingFees();
                    case 7 -> updateShipmentStatus();
                    case 0 -> {
                        System.out.println("  [i] Returning to Admin Portal...");
                        inShipmentMenu = false;
                    }
                    default -> System.out.println("  [!] Invalid option. Please try again.");
                }
            } catch(InputMismatchException e){
                System.out.println("\n  [!] Invalid input! Please enter a number.");
                input.nextLine(); // clear the wrong input
            }
        }
    }


    // Operations
    private void displayAllShipments(){
        shipmentRegistry.displayAll();
    }

    private void filterByStatus(){
        System.out.println("\n  ╔══════════════════════════════════════╗");
        System.out.println("  ║            FILTER BY STATUS          ║");
        System.out.println("  ╚══════════════════════════════════════╝");
        System.out.println("  1. PENDING_PAYMENT");
        System.out.println("  2. PAID");
        System.out.println("  3. PICKED_UP");
        System.out.println("  4. IN_TRANSIT");
        System.out.println("  5. OUT_FOR_DELIVERY");
        System.out.println("  6. DELIVERED");
        System.out.println("  7. FAILED_ATTEMPT");
        System.out.println("  8. CANCELLED");
        System.out.print("  Choice -> ");
        int choice = input.nextInt();
        input.nextLine();   // clear buffer

        Shipment.ShipmentStatus status = switch(choice){
            case 1 -> Shipment.ShipmentStatus.PENDING_PAYMENT;
            case 2 -> Shipment.ShipmentStatus.PAID;
            case 3 -> Shipment.ShipmentStatus.PICKED_UP;
            case 4 -> Shipment.ShipmentStatus.IN_TRANSIT;
            case 5 -> Shipment.ShipmentStatus.OUT_FOR_DELIVERY;
            case 6 -> Shipment.ShipmentStatus.DELIVERED;
            case 7 -> Shipment.ShipmentStatus.FAILED_ATTEMPT;
            case 8 -> Shipment.ShipmentStatus.CANCELLED;
            default -> null;
        };

        if(status != null){
            List<Shipment> filtered = shipmentRegistry.findByStatus(status);

            if(filtered.isEmpty()){
                System.out.println("\n  [i] No shipment with status \"" + status + "\"");
            }
            else{
                System.out.println("\n  ╔════════════════════════════════════════════════════════╗");
                System.out.println("  ║                   FILTERED SHIPMENTS                   ║");
                System.out.println("  ╚════════════════════════════════════════════════════════╝");
                System.out.println("  Status: " + status);
                System.out.println("  Found: " + filtered.size() + " shipment(s)");
                System.out.println();
                System.out.printf("  %-12s %-15s %-20s %-12s%n", "Tracking ID", "Sender", "Status", "Total Fee");
                System.out.println("  " + "─".repeat(70));
                
                for (Shipment s : filtered) {
                    System.out.printf("  %-12s %-15s %-20s RM %-10.2f%n", s.getTrackingID(), s.getSenderID(), s.getStatus(), s.getTotalFee());
                }
                System.out.println();
            }
        }
        else{
            System.out.println("\n  [!] Invalid status choice.");
        }
    }

    private void searchByTrackingID(){
        System.out.println("  Enter Tracking ID -> ");
        String trackingID = input.next();

        Shipment s = shipmentRegistry.findByTrackingID(trackingID);

        if(s == null){
            System.out.println("  [!] Shipment \" + trackingID + \" not found.");
        }
        else{
            System.out.println("  \n [DONE] Shipment Found.");
            s.displayFullDetails();
        }
    }

    private void searchBySenderID(){
        System.out.println("  Enter Sender ID -> ");
        String senderID = input.next();

        List<Shipment> results = shipmentRegistry.findBySender(senderID);

        if(results.isEmpty()){
            System.out.println("\n  [!] No shipments found for Sender \"" + senderID + "\"");
        }
        else{
            System.out.println("\n  ╔════════════════════════════════════════════════════════╗");
            System.out.println("  ║                   SHIPMENTS BY SENDER                  ║");
            System.out.println("  ╚════════════════════════════════════════════════════════╝");
            System.out.println("  Sender ID: " + senderID);
            System.out.println("  Found: " + results.size() + " shipment(s)");
            System.out.println();
            System.out.printf("  %-12s %-20s %-12s%n", "Tracking ID", "Status", "Total Fee");
            System.out.println("  " + "─".repeat(60));
            
            for (Shipment s : results) {
                System.out.printf("  %-12s %-20s RM %-10.2f%n", s.getTrackingID(), s.getStatus(), s.getTotalFee());
            }
            System.out.println();
        }
    }

    // Menu Design
    private void displayMenu(){
        System.out.println("\n  ╔══════════════════════════════════════╗");
        System.out.println("  ║          SHIPMENT MANAGEMENT         ║");
        System.out.println("  ╠══════════════════════════════════════╣");
        System.out.println("  ║  1.  Display All Shipments           ║");
        System.out.println("  ║  2.  Filter By Status                ║");
        System.out.println("  ║  3.  Search By Tracking ID           ║");
        System.out.println("  ║  4.  Search By Sender ID             ║");
        System.out.println("  ║  5.  View Shipment Details           ║");
        System.out.println("  ║  6.  Review Shipping Fees            ║");
        System.out.println("  ║  7.  Update Shipment Status           ║");
        System.out.println("  ║  0.  Return to Admin Portal          ║");
        System.out.println("  ╚══════════════════════════════════════╝");
        System.out.print("  Choice -> ");
    }
}
