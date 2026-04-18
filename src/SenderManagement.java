import java.util.ArrayList;
import java.util.Scanner;
import java.util.InputMismatchException;
import java.util.List;

public class SenderManagement {
    
    private ArrayList<Sender> senderList;
    private Scanner input;
    
    // Connection to ShipmentRegistry
    private ShipmentRegistry shipmentRegistry; 
    
    private int nextSenderId = 200; 
    
    public SenderManagement(Scanner input, ShipmentRegistry shipmentRegistry) {
        this.senderList = new ArrayList<Sender>();
        this.input = input;
        this.shipmentRegistry = shipmentRegistry; // Connect to the main database
        
        // Dummy account
        Sender testUser = new Sender("SND200", "Lim Yi Ming", "123456", "yiming@gmail.com", "012-3456789", Sender.MemberTier.STANDARD);
        senderList.add(testUser);
    }
    
    // --------------------------------------------------------
    // MAIN PORTAL
    // --------------------------------------------------------
    public void start() {
        boolean keepRunning = true;
        
        while (keepRunning == true) {
            System.out.println("\n  ╔════════════════════════════════════════════════════╗");
            System.out.println("  ║              WELCOME TO SENDER PORTAL              ║");
            System.out.println("  ╚════════════════════════════════════════════════════╝");
            System.out.println("  Send packages quickly and easily!\n");
            System.out.println("  Option:");
            System.out.println("   1. Register New Sender");
            System.out.println("   2. Enter Sender ID (Login)");
            System.out.println("   0. Back to Main Menu");
            System.out.print("  Enter your choice -> ");
            
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
                    System.out.println("  Returning to main menu...");
                    keepRunning = false; 
                } else {
                    System.out.println("\n  [!] Invalid option! Please try again.");
                }
            } catch (InputMismatchException e) {
                System.out.println("\n  Error >> Please enter a valid number!");
                input.nextLine(); 
            }
        }
    }
    
    private Sender registerNewSender() {
        System.out.println("\n  ───────── SENDER REGISTRATION ─────────");
        nextSenderId++; 
        String newId = "SND" + nextSenderId; 
        
        System.out.print("  Enter Full Name -> ");
        String name = input.nextLine();
        System.out.print("  Enter Email -> ");
        String email = input.nextLine();
        String phone = "";
        while (true) {
            System.out.print("  Enter Phone (Numbers only) -> ");
            phone = input.nextLine();
            if (phone.matches("[0-9]+")) {
                break; 
            } else {
                System.out.println("\n  [!] Invalid phone number. Please enter numbers only.\n");
            }
        }
        
        
        System.out.print("  Set a Password -> "); 
        String password = input.nextLine();
        Sender sender = new Sender(newId, name, password, email, phone, Sender.MemberTier.STANDARD) ;
        senderList.add(sender);
        
        System.out.println("\n  [DONE] Registration complete!\n");
        System.out.println("  Your fixed Sender ID is: " + newId);
        return sender;
    }

    private Sender findExistingSender() {
        System.out.print("\n  Enter your Sender ID (e.g. SND200) -> ");
        String searchId = input.nextLine().trim();
        
        for (int i = 0; i < senderList.size(); i++) {
            Sender currentSender = senderList.get(i);
        
            if (currentSender.getPersonID().equalsIgnoreCase(searchId)) {
                
                System.out.print("  Enter Password -> ");
                String passInput = input.nextLine();
                
                if (currentSender.getPassword().equals(passInput)) {
                    System.out.println("\n  Welcome back, " + currentSender.getName() + "!");
                    return currentSender; 
                } else {
                    System.out.println("\n  [!] Error: Incorrect password. Login failed.");
                    return null;
                }
            }
        }
        System.out.println("\n  [!] Sender ID not found. Please register first.");
        return null;
    }
    
    // --------------------------------------------------------
    // SENDER DASHBOARD & FEATURES
    // --------------------------------------------------------
    private void senderMainMenu(Sender sender) {
        boolean inMenu = true;
        
        while (inMenu == true) {
            System.out.println("\n  ╔═════════════════════════════════════════════════╗");
            System.out.println("  ║             SENDER PORTAL - " + sender.getPersonID() + "              ║");
            System.out.println("  ╠═════════════════════════════════════════════════╣");
            System.out.println("  ║  1. Create New Shipment                         ║");
            System.out.println("  ║  2. View My Shipments                           ║");
            System.out.println("  ║  3. Track a Shipment                            ║");
            System.out.println("  ║  4. Pay for Shipment                            ║");
            System.out.println("  ║  5. Cancel Shipment                             ║");
            System.out.println("  ║  6. View My Profile                             ║");
            System.out.println("  ║  7. Upgrade Membership                          ║");
            System.out.println("  ║  0. Logout                                      ║");
            System.out.println("  ╚═════════════════════════════════════════════════╝");
            System.out.print("  Choice -> ");
            
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
                } else if (choice == 7) {             
                     manageMembership(sender);
                } else if (choice == 0) {
                    System.out.println("  Logging out...");
                    inMenu = false; 
                } else {
                    System.out.println("\n  [!] Invalid option. Please try again.");
                }
            } catch (InputMismatchException e) {
                System.out.println("\n  Error >> Please enter a valid number!");
                input.nextLine(); 
            }
        }
    }
    
    // --- FEATURE 1: Create Shipment ---
    private void createShipment(Sender sender) {
        System.out.println("\n  ╔═══════════════════════════════════════════════════╗");
        System.out.println("  ║                CREATE NEW SHIPMENT                ║");
        System.out.println("  ╚═══════════════════════════════════════════════════╝");
        try {
            System.out.print("  Pickup Address -> ");
            String pickupAddress = input.nextLine();
            System.out.print("  Delivery Address -> ");
            String deliveryAddress = input.nextLine();
            System.out.print("  Distance (km) -> ");
            double distance = input.nextDouble();
            input.nextLine();
            
            System.out.println("\n  Shipping Speed: ");
            System.out.println("   1. STANDARD (RM5.00/kg, 3-5 Days)");
            System.out.println("   2. EXPRESS (RM9.50/kg, 1-2 Days)");
            System.out.print("  Choice -> ");
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
            
            // Get a new Tracking ID 
            String trackingId = shipmentRegistry.generateTrackingID();
            
            // Create the shipment 
            Shipment newShipment = new Shipment(trackingId, sender.getPersonID(), parcel, pickupAddress, deliveryAddress, distance, speed);
            
            // Save it to the system
            shipmentRegistry.addShipment(newShipment);

            System.out.println("\n  ╔═══════════════════════════════════════════════════╗");
            System.out.println("  ║           SHIPMENT CREATED SUCCESSFULLY           ║");
            System.out.println("  ╚═══════════════════════════════════════════════════╝");
            System.out.println("  Tracking ID -> " + trackingId);
            System.out.printf("\n  Total Fee to Pay -> RM%.2f%n", newShipment.getTotalFee());
            System.out.println("  Status -> " + newShipment.getStatus());
            System.out.println("\n  [i] Please proceed to 'Pay for Shipment' to confirm shipment.");
            
        } catch (Exception e) {
            System.out.println("\n  Error >> Invalid reading input. Please try again.");
            input.nextLine();
        }
    }
    
    private Parcel createParcel() {
        System.out.println("\n  ───────── Parcel Details ─────────");
        System.out.println("  Content Type:");
        System.out.println("   1. DOCUMENTS");
        System.out.println("   2. ELECTRONICS");
        System.out.println("   3. CLOTHING");
        System.out.println("   4. FRAGILE");
        System.out.println("   5. FOOD");
        System.out.println("   6. GENERAL");
        System.out.print("  Choice -> ");
        int typeChoice = input.nextInt();
        input.nextLine();
        
        Parcel.ContentType contentType = Parcel.ContentType.GENERAL;
        if (typeChoice == 1) contentType = Parcel.ContentType.DOCUMENTS;
        else if (typeChoice == 2) contentType = Parcel.ContentType.ELECTRONICS;
        else if (typeChoice == 3) contentType = Parcel.ContentType.CLOTHING;
        else if (typeChoice == 4) contentType = Parcel.ContentType.FRAGILE; 
        else if (typeChoice == 5) contentType = Parcel.ContentType.FOOD; 
        
        System.out.print("\n  Description -> ");
        String description = input.nextLine();
        System.out.print("  Weight (kg) -> ");
        double weight = input.nextDouble();
        System.out.print("  Length (cm) -> ");
        double length = input.nextDouble();
        System.out.print("  Width (cm) -> ");
        double width = input.nextDouble();
        System.out.print("  Height (cm) -> ");
        double height = input.nextDouble();
        System.out.print("  Declared Value (RM) -> ");
        double value = input.nextDouble();
        input.nextLine();
        
        return new Parcel(contentType, description, weight, length, width, height, value);
    }
    
    // --- FEATURE 2: View My Shipments ---
    private void viewMyShipments(Sender sender) {
        List<Shipment> myShipments = shipmentRegistry.findBySender(sender.getPersonID());
        
        if (myShipments.isEmpty()) {
            System.out.println("\n  [!] You have no shipments yet.");
        } else {
            System.out.println("\n  ╔══════════════════════════════════════════════════════════╗");
            System.out.println("  ║                       MY SHIPMENTS                       ║");
            System.out.println("  ╚══════════════════════════════════════════════════════════╝");
            System.out.println("  Total: " + myShipments.size() + " shipment(s)\n");

            System.out.printf("  %-15s %-20s %-10s", "Tracking ID", "Status", "Total Fee");
            System.out.println("\n  " + "─".repeat(47));
            for (int i = 0; i < myShipments.size(); i++) {
                Shipment s = myShipments.get(i);
                System.out.printf("  %-15s %-20s RM %-10.2f%n", s.getTrackingID(), s.getStatus(), s.getTotalFee());
            }
        }
        
         // Pause before returning to menu
        System.out.println("\n  Press ENTER to return to dashboard...");
        input.nextLine(); 
    }
    
    // --- FEATURE 3: Track Shipment ---
    private void trackShipment(Sender sender) {
        System.out.print("\n  Enter Tracking ID -> ");
        String trackingId = input.nextLine();
        
        Shipment s = shipmentRegistry.findByTrackingID(trackingId);
        
        // invalid tracking ID and verify shipment ownership
        if (s != null && s.getSenderID().equals(sender.getPersonID())) {
            s.displayFullDetails();
            s.displayTrackingHistory(); 
        } else {
            System.out.println("\n  [!] Shipment not found or belongs to another sender.");
        }
    // Pause before returning to menu
        System.out.println("\n\n  Press ENTER to return to dashboard...");
        input.nextLine();
    }
    
    // --- FEATURE 4: Pay for Shipment ---
    private void payForShipment(Sender sender) {
        System.out.print("\n  Enter Tracking ID to pay -> ");
        String trackingId = input.nextLine();
        
        Shipment s = shipmentRegistry.findByTrackingID(trackingId);
        
        if (s != null && s.getSenderID().equals(sender.getPersonID())) {
            if (s.isPaid()) {
                System.out.println(" \n  [i] This shipment is already paid.");
            } else {
                System.out.println("\n  ╔════════════════════════════════════════════════╗");
                System.out.println("  ║              PAYMENT CONFIRMATION              ║");
                System.out.println("  ╚════════════════════════════════════════════════╝");
                System.out.println("  Tracking ID: " + trackingId);
            
                double originalTotal = s.getTotalFee();
                double discount = 0.0;
                String discountReason = "";

                if (sender.getTier() == Sender.MemberTier.PREMIUM) {
                    discount = originalTotal * 0.10; 
                    discountReason = "PREMIUM Tier, 10% Off";
                } else if (sender.getTier() == Sender.MemberTier.BUSINESS) {
                    discount = s.getInsuranceFee();
                    discountReason = "BUSINESS Tier, Free Insurance";
                }

                double finalAmount = originalTotal - discount;

                System.out.printf("  Amount Due: RM %.2f%n", finalAmount);
                System.out.println();

                System.out.println("  Fee Breakdown:");
                System.out.printf("    Base Fee      : RM %.2f%n", s.getBaseFee());
                System.out.printf("    Distance Fee  : RM %.2f%n", s.getDistanceFee());
                System.out.printf("    Insurance Fee : RM %.2f%n", s.getInsuranceFee());
                
                if (discount > 0) {
                    System.out.printf("    Discount      :-RM %.2f (%s)%n", discount, discountReason);
                }
                
                System.out.println("  " + "─".repeat(60));
                System.out.printf("  FINAL TOTAL     : RM %.2f%n", finalAmount); 
                System.out.println();

                System.out.print("  Confirm Payment? (Y/N) -> ");
                String confirm = input.nextLine();
                if (confirm.equalsIgnoreCase("Y")) {
                    s.updateStatus(Shipment.ShipmentStatus.PAID, "Payment confirmed");

                    System.out.println("\n  ╔════════════════════════════════════════════╗");
                    System.out.println("  ║             PAYMENT SUCCESSFUL             ║");
                    System.out.println("  ╚════════════════════════════════════════════╝");
                    System.out.println("  Your shipment will be processed shortly.");
                    System.out.println("  We will notify you when a courier is assigned.");
                } else {
                    System.out.println("\n  [i] Payment Cancelled.");
                }
            }
        } else {
            System.out.println("\n  [!] Shipment not found. Please try again.");
        }
        System.out.println("\n  Press ENTER to return to dashboard...");
        input.nextLine();
    }
    
    // --- FEATURE 5: Cancel Shipment ---
    private void cancelShipment(Sender sender) {
        System.out.print("\n  Enter Tracking ID to cancel -> ");
        String trackingId = input.nextLine();
        
        Shipment s = shipmentRegistry.findByTrackingID(trackingId);
        if (s == null) {
            System.out.println("\n  [!] Shipment not found.");
            return;
        }    
        if(!s.getSenderID().equals(sender.getPersonID())) {
            System.out.println("\n  [!] This shipment does not belong to you.");
            return;
        }
        if (!s.canBeCancelled()) {
            System.out.println("\n  [!] Cannot cancel shipment in status -> " + s.getStatus());
            System.out.println("  [i] Only PENDING_PAYMENT or PAID shipments can be cancelled.");
            return;
        }

        System.out.println("\n  Shipment to cancel:");
        System.out.println("   Tracking ID -> " + trackingId);
        System.out.println("   Status -> " + s.getStatus());
        System.out.printf("   Total Fee -> RM %.2f%n", s.getTotalFee());
        
        System.out.print("\n  Are you sure you want to cancel? (Y/N) -> ");
        String confirm = input.nextLine();
        if (confirm.equalsIgnoreCase("Y")) {
            try {
                shipmentRegistry.cancelShipment(trackingId); 

                if (s.isPaid()) {
                    System.out.println("\n  [i] Refund will be processed within 3-5 business days.");
                }
            } catch (Exception e) {
                System.out.println("\n  Error >> " + e.getMessage());
            }
        } else {
            System.out.println("\n  [i] Cancellation aborted.");
        }
    
    // Pause before returning to menu
        System.out.println("\n  Press ENTER to return to dashboard...");
        input.nextLine();
    }

    // --- FEATURE 6: View My Profile ---
    private void viewMyProfile(Sender sender) {
        System.out.println();
        
        sender.displayInfo(); 

        // Show shipment statistics
        List<Shipment> myShipments = shipmentRegistry.findBySender(sender.getPersonID());
        
        System.out.println("  SHIPMENT STATISTICS:");
        System.out.println("  ─────────────────────────────────────────────────────────");
        System.out.println("  Total Shipments: " + myShipments.size());
        
        int delivered = 0;
        int inTransit = 0;
        int pending = 0;
        
        for (Shipment s : myShipments) {
            switch (s.getStatus()) {
                case DELIVERED -> delivered++;
                case IN_TRANSIT, OUT_FOR_DELIVERY, PICKED_UP -> inTransit++;
                case PENDING_PAYMENT, PAID -> pending++;
                default -> {} // ignore other status
            }
        }
        
        System.out.println("  Delivered: " + delivered);
        System.out.println("  In Transit: " + inTransit);
        System.out.println("  Pending: " + pending);
        System.out.println();
        
        // Pause before returning to menu
        System.out.println("\n  Press ENTER to return to dashboard...");
        input.nextLine(); 
    }

   // --- FEATURE 7: Manage Membership (Upgrade / Downgrade) ---
    private void manageMembership(Sender sender) {
        System.out.println("\n  ╔═════════════════════════════════════════════════╗");
        System.out.println("  ║                MANAGE MEMBERSHIP                ║");
        System.out.println("  ╚═════════════════════════════════════════════════╝");
        System.out.println("  Current Tier: " + sender.getTier());

        if (sender.getTier() == Sender.MemberTier.STANDARD) {
            System.out.println("\n  Available Actions:");
            System.out.println("   1. Upgrade to PREMIUM  (RM 50.00)  - 10% discount on shipments");
            System.out.println("   2. Upgrade to BUSINESS (RM 200.00) - Corporate billing & Free Insurance");
            System.out.println("   0. Cancel");
            
            System.out.print("\n  Select your choice -> ");
            String choice = input.nextLine().trim();
            
            if (choice.equals("1")) {
                sender.setTier(Sender.MemberTier.PREMIUM);
                System.out.println("\n  [SUCCESS] Paid RM 50.00. You are now a PREMIUM member!");
            } else if (choice.equals("2")) {
                sender.setTier(Sender.MemberTier.BUSINESS);
                System.out.println("\n  [SUCCESS] Paid RM 200.00. You are now a BUSINESS member!");
            } else {
                System.out.println("\n  [i] Action cancelled.");
            }
            
        } else if (sender.getTier() == Sender.MemberTier.PREMIUM) {
            System.out.println("\n  Available Actions:");
            System.out.println("   1. Upgrade to BUSINESS (RM 150.00) - Corporate billing & Free Insurance");
            System.out.println("   2. Downgrade to STANDARD (Free)    - Lose priority support and discounts");
            System.out.println("   0. Cancel");
            
            System.out.print("\n  Select your choice -> ");
            String choice = input.nextLine().trim();
            
            if (choice.equals("1")) {
                sender.setTier(Sender.MemberTier.BUSINESS);
                System.out.println("\n  [SUCCESS] Paid RM 150.00. You are now a BUSINESS member!");
            } else if (choice.equals("2")) {
                System.out.print("  Are you sure you want to lose your Premium benefits? (Y/N) -> ");
                if (input.nextLine().trim().equalsIgnoreCase("Y")) {
                    sender.setTier(Sender.MemberTier.STANDARD);
                    System.out.println("\n  [DONE] You have been downgraded to STANDARD. Monthly billing cancelled.");
                } else {
                    System.out.println("\n  [i] Smart choice! Keeping PREMIUM tier.");
                }
            } else {
                System.out.println("\n  [i] Action cancelled.");
            }
            
        } else if (sender.getTier() == Sender.MemberTier.BUSINESS) {
            System.out.println("\n  Available Actions:");
            System.out.println("   1. Downgrade to PREMIUM  - Keep 10% discount, lose free insurance");
            System.out.println("   2. Downgrade to STANDARD - Cancel all subscriptions and benefits");
            System.out.println("   0. Cancel");
            
            System.out.print("\n  Select your choice -> ");
            String choice = input.nextLine().trim();
            
            if (choice.equals("1")) {
                sender.setTier(Sender.MemberTier.PREMIUM);
                System.out.println("\n  [DONE] Downgraded to PREMIUM. Next billing will be RM 50.00.");
            } else if (choice.equals("2")) {
                System.out.print("  Are you sure you want to cancel your Business subscription? (Y/N) -> ");
                if (input.nextLine().trim().equalsIgnoreCase("Y")) {
                    sender.setTier(Sender.MemberTier.STANDARD);
                    System.out.println("\n  [DONE] You have been downgraded to STANDARD. All subscriptions cancelled.");
                } else {
                    System.out.println("\n  [i] Excellent choice! Keeping BUSINESS tier.");
                }
            } else {
                System.out.println("\n  [i] Action cancelled.");
            }
        }
        
        System.out.println("\n  Press ENTER to return to dashboard...");
        input.nextLine();
    }
}