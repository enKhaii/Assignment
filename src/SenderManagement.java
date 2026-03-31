import java.util.ArrayList;
import java.util.Scanner;
import java.util.InputMismatchException;
import java.util.List;

public class SenderManagement {
    
    private ArrayList<Sender> senderList;
    private Scanner input;
    
    // IMPORTANT: Add a connection to the team's ShipmentRegistry!
    private ShipmentRegistry shipmentRegistry; 
    
    private int nextSenderId = 200; 
    
    // Constructor updated to receive the team's ShipmentRegistry
    public SenderManagement(Scanner input, ShipmentRegistry shipmentRegistry) {
        this.senderList = new ArrayList<Sender>();
        this.input = input;
        this.shipmentRegistry = shipmentRegistry; // Connect to the main database
        
        // Dummy account for easy testing
        Sender testUser = new Sender("SND200", "SND200", "Lim Yi Ming", "123456", "yiming@email.com", "0123456789", Sender.MemberTier.STANDARD);
        senderList.add(testUser);
    }
    
    // --------------------------------------------------------
    // MAIN PORTAL
    // --------------------------------------------------------
    public void start() {
        boolean keepRunning = true;
        
        while (keepRunning == true) {
            System.out.println("\n================================================");
            System.out.println("             WELCOME TO SENDER PORTAL             ");
            System.out.println("================================================");
            System.out.println("1. Register New Sender");
            System.out.println("2. Enter Sender ID (Login)");
            System.out.println("0. Back to Main System");
            System.out.print("Enter your choice: ");
            
            try {
                int choice = input.nextInt();
                input.nextLine(); 
                
                if (choice == 1) {
                    Sender newSender = registerNewSender();
                    senderMainMenu(newSender); 
                } else if (choice == 2) {
                    Sender existingSender = findExistingSender();
                    if (existingSender != null) {
                        senderMainMenu(existingSender); 
                    }
                } else if (choice == 0) {
                    System.out.println("Returning to main system...");
                    keepRunning = false; 
                } else {
                    System.out.println("Invalid option! Please try again.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Error: Please enter a valid number!");
                input.nextLine(); 
            }
        }
    }
    
    private Sender registerNewSender() {
        System.out.println("\n--- SENDER REGISTRATION ---");
        nextSenderId++; 
        String newId = "SND" + nextSenderId; 
        
        System.out.print("Enter Full Name: ");
        String name = input.nextLine();
        System.out.print("Enter Email: ");
        String email = input.nextLine();
        System.out.print("Enter Phone: ");
        String phone = input.nextLine();
        
        System.out.print("Set a Password: "); 
        String password = input.nextLine();
        
        Sender sender = new Sender(newId, newId, name, password, email, phone, Sender.MemberTier.STANDARD);
        
        System.out.println("\n[SUCCESS] Registration complete!");
        System.out.println("Your fixed Sender ID is: " + newId);
        return sender;
    }
    
    private Sender findExistingSender() {
        System.out.print("\nEnter your Sender ID (e.g., SND200): ");
        String searchId = input.nextLine();
        
        for (int i = 0; i < senderList.size(); i++) {
            Sender currentSender = senderList.get(i);
            if (currentSender.getPersonID().equalsIgnoreCase(searchId)) {
                System.out.println("\nWelcome back, " + currentSender.getName() + "!");
                return currentSender;
            }
        }
        System.out.println("Error: Sender ID not found. Please register first.");
        return null;
    }
    
    // --------------------------------------------------------
    // SENDER DASHBOARD & FEATURES
    // --------------------------------------------------------
    private void senderMainMenu(Sender sender) {
        boolean inMenu = true;
        
        while (inMenu == true) {
            System.out.println("\n------------------------------------------------");
            System.out.println("SENDER DASHBOARD - ID: " + sender.getPersonID());
            System.out.println("------------------------------------------------");
            System.out.println("1. Create New Shipment (寄包裹)");
            System.out.println("2. View My Shipments (查看我的订单)");
            System.out.println("3. Track a Shipment (追踪包裹)");
            System.out.println("4. Pay for Shipment (付款)");
            System.out.println("5. Cancel Shipment (取消包裹)");
            System.out.println("6. View My Profile (查看资料)");
            System.out.println("0. Logout");
            System.out.print("Choice: ");
            
            try {
                int choice = input.nextInt();
                input.nextLine(); 
                
                if (choice == 1) {
                    createShipment(sender);
                } else if (choice == 2) {
                    viewMyShipments(sender);
                } else if (choice == 3) {
                    trackShipment(sender);
                } else if (choice == 4) {
                    payForShipment(sender);
                } else if (choice == 5) {
                    cancelShipment(sender);
                } else if (choice == 6) {
                     viewMyProfile(sender);;
                } else if (choice == 0) {
                    System.out.println("Logging out...");
                    inMenu = false; 
                } else {
                    System.out.println("Invalid option!");
                }
            } catch (InputMismatchException e) {
                System.out.println("Error: Please enter a valid number!");
                input.nextLine(); 
            }
        }
    }
    
    // --- FEATURE 1: Create Shipment ---
    private void createShipment(Sender sender) {
        System.out.println("\n--- CREATE NEW SHIPMENT ---");
        try {
            System.out.print("Pickup Address: ");
            String pickupAddress = input.nextLine();
            System.out.print("Delivery Address: ");
            String deliveryAddress = input.nextLine();
            System.out.print("Distance (km): ");
            double distance = input.nextDouble();
            input.nextLine();
            
            System.out.println("Shipping Speed: 1. STANDARD (3-5 Days) | 2. EXPRESS (1-2 Days)");
            System.out.print("Choice: ");
            int speedChoice = input.nextInt();
            input.nextLine();
            
            Shipment.ShippingSpeed speed;
            if (speedChoice == 2) {
                speed = Shipment.ShippingSpeed.EXPRESS;
            } else {
                speed = Shipment.ShippingSpeed.STANDARD;
            }
            
            // Call the helper method to create a parcel
            Parcel parcel = createParcel();
            
            // Get a new Tracking ID from the team's system
            String trackingId = shipmentRegistry.generateTrackingID();
            
            // Create the shipment using the team's class
            Shipment newShipment = new Shipment(trackingId, sender.getPersonID(), parcel, pickupAddress, deliveryAddress, distance, speed);
            
            // Save it to the team's system
            shipmentRegistry.addShipment(newShipment);
            
            System.out.println("Total Fee to pay: RM " + newShipment.getTotalFee());
            System.out.println("Status: " + newShipment.getStatus());
            
        } catch (Exception e) {
            System.out.println("Error reading input. Please try again.");
            input.nextLine();
        }
    }
    
    private Parcel createParcel() {
        System.out.println("\n-- Parcel Details --");
        System.out.print("Content Type (1.DOCUMENTS, 2.ELECTRONICS, 3.CLOTHING, 4.FRAGILE, 5.FOOD): ");
        int typeChoice = input.nextInt();
        input.nextLine();
        
        // This MUST match the teammate's Parcel class ContentType enum
        // Assuming teammate's Parcel has standard ContentTypes based on the initialized data
        Parcel.ContentType contentType = Parcel.ContentType.DOCUMENTS; // Default
        if (typeChoice == 2) contentType = Parcel.ContentType.ELECTRONICS;
        else if (typeChoice == 3) contentType = Parcel.ContentType.CLOTHING;
        // else if (typeChoice == 4) contentType = Parcel.ContentType.FRAGILE; // Uncomment if team has this
        // else if (typeChoice == 5) contentType = Parcel.ContentType.FOOD; // Uncomment if team has this
        
        System.out.print("Description: ");
        String description = input.nextLine();
        System.out.print("Weight (kg): ");
        double weight = input.nextDouble();
        System.out.print("Length (cm): ");
        double length = input.nextDouble();
        System.out.print("Width (cm): ");
        double width = input.nextDouble();
        System.out.print("Height (cm): ");
        double height = input.nextDouble();
        System.out.print("Declared Value (RM): ");
        double value = input.nextDouble();
        input.nextLine();
        
        return new Parcel(contentType, description, weight, length, width, height, value);
    }
    
    // --- FEATURE 2: View My Shipments ---
    private void viewMyShipments(Sender sender) {
        List<Shipment> myShipments = shipmentRegistry.findBySender(sender.getPersonID());
        
        if (myShipments.isEmpty()) {
            System.out.println("\n[!] You have no shipments yet.");
        } else {
            System.out.println("\n--- MY SHIPMENTS ---");
            for (int i = 0; i < myShipments.size(); i++) {
                Shipment s = myShipments.get(i);
                s.displaySummary(); // Call the display method from team's code
            }
        }
        
         // Pause before returning to menu
        System.out.println("\nPress ENTER to return to dashboard...");
        input.nextLine(); 
    }
    
    // --- FEATURE 3: Track Shipment ---
    private void trackShipment(Sender sender) {
        System.out.print("\nEnter Tracking ID: ");
        String trackingId = input.nextLine();
        
        Shipment s = shipmentRegistry.findByTrackingID(trackingId);
        
        if (s != null && s.getSenderID().equals(sender.getPersonID())) {
            s.displayFullDetails(); // Team's code
            s.displayTrackingHistory(); // Team's code
        } else {
            System.out.println("Error: Shipment not found or belongs to another sender.");
        }
    // Pause before returning to menu
        System.out.println("\nPress ENTER to return to dashboard...");
        input.nextLine();
    }
    
    // --- FEATURE 4: Pay for Shipment ---
    private void payForShipment(Sender sender) {
        System.out.print("\nEnter Tracking ID to pay: ");
        String trackingId = input.nextLine();
        
        Shipment s = shipmentRegistry.findByTrackingID(trackingId);
        
        if (s != null && s.getSenderID().equals(sender.getPersonID())) {
            if (s.isPaid()) {
                System.out.println("This shipment is already paid.");
            } else {
                System.out.println("Total Amount Due: RM " + s.getTotalFee());
                System.out.print("Confirm Payment? (Y/N): ");
                String confirm = input.nextLine();
                if (confirm.equalsIgnoreCase("Y")) {
                    // Update status using team's method
                    s.updateStatus(Shipment.ShipmentStatus.PAID, "Paid by Sender");
                    System.out.println("Payment Successful! Thank you.");
                } else {
                    System.out.println("Payment Cancelled.");
                }
            }
        } else {
            System.out.println("Error: Shipment not found.");
        }
    // Pause before returning to menu
        System.out.println("\nPress ENTER to return to dashboard...");
        input.nextLine();
    }
    
    // --- FEATURE 5: Cancel Shipment ---
    private void cancelShipment(Sender sender) {
        System.out.print("\nEnter Tracking ID to cancel: ");
        String trackingId = input.nextLine();
        
        Shipment s = shipmentRegistry.findByTrackingID(trackingId);
        if (s != null && s.getSenderID().equals(sender.getPersonID())) {
            System.out.print("Are you sure you want to cancel? (Y/N): ");
            String confirm = input.nextLine();
            if (confirm.equalsIgnoreCase("Y")) {
                try {
                    // Uses the team's cancel method which handles exceptions
                    shipmentRegistry.cancelShipment(trackingId); 
                } catch (Exception e) {
                    System.out.println("Error: " + e.getMessage());
                }
            }
        } else {
            System.out.println("Error: Shipment not found.");
        }
    // Pause before returning to menu
        System.out.println("\nPress ENTER to return to dashboard...");
        input.nextLine();
    }

    // --- FEATURE 6: View My Profile ---
    private void viewMyProfile(Sender sender) {
        System.out.println();
        
        sender.displayInfo(); 
        // Pause before returning to menu
        System.out.println("\nPress ENTER to return to dashboard...");
        input.nextLine(); 
    }
}