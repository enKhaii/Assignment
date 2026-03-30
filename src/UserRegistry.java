/*
    UserRegistry - stores main objects (Sender, Courier, Admin), also consists method for login
    Registry -
*/

import java.util.ArrayList;

public class UserRegistry{
    // ArrayList<x> means this ArrayList can only hold object created in 'x.java'
    private static ArrayList<Admin> admins = new ArrayList<>();
    private static ArrayList<Courier> couriers = new ArrayList<>();

    public static void initializeData(){
        // Admin Objects
        admins.add(new Admin("ADM01", "CHONG01", "Chong Wen Liang", "chongwliang123", "cwenliang22@gmail.com", "017-284-1288", Admin.Role.SUPER_ADMIN));
        admins.add(new Admin("ADM02", "ROHINKUMAR02", "Raj Rohin Kumar", "rohinkumar456", "rohinkumar092@hotmail.com", "011-4824-2995",  Admin.Role.OPERATIONS_MANAGER));
        admins.add(new Admin("ADM03", "KYODUSLIM03", "Kyodus Lim Kai Yuan", "kyodus789", "kyoduslim141@gmail.com", "014-198-6767",  Admin.Role.DISPATCH_OFFICER));        
        
        Courier c1 = new Courier("CRR01", "AHMAD01", "Ahmad Malik",
        "ahmad1234", "ahmad@courierpro.my", "012-345-6789", "D1234567");
        c1.setDutyStatus(true);
        c1.setAssignedVehicleID("VHE501");
        
        Courier c2 = new Courier("CRR02", "RAZIF02", "Mohd Razif bin Johari",
        "razif5678", "razif@courierpro.my", "019-876-5432", "D7654321");
        c2.setDutyStatus(true);
        c2.setAssignedVehicleID("VHE502");
        
        couriers.add(c1);
        couriers.add(c2);
        System.out.println("  [DONE] User credentials loaded.");
    }

    public static void assignSampleShipments(ShipmentRegistry shipmentRegistry){
        if (couriers.isEmpty()) return;

        Courier c1 = couriers.get(0);
        Courier c2 = couriers.get(1);

        // Assign some of the sample shipments to couriers
        Shipment s1 = shipmentRegistry.findByTrackingID("TRK10001");
        Shipment s2 = shipmentRegistry.findByTrackingID("TRK10002");
        Shipment s3 = shipmentRegistry.findByTrackingID("TRK10004");
        Shipment s4 = shipmentRegistry.findByTrackingID("TRK10005");
 
        if (s1 != null) c1.receiveShipment(s1);
        if (s2 != null) c1.receiveShipment(s2);
        if (s3 != null) c2.receiveShipment(s3);
        if (s4 != null) c2.receiveShipment(s4);
    }
    

    // Main.java calls this to check if "LOGIN" ID exists before login
    public static boolean checkAdminIdExists(String loginID){
        for(Admin a : admins){
            if(a.getLoginID().trim().equalsIgnoreCase(loginID.trim())){
                return true;
            }
        }
        return false;
    }

    // Main.java calls this to check if "PERSON" ID exists for displaying info
    public static Admin getAdminById(String loginID){
        for(Admin a : admins){
            if(a.getPersonID().trim().equalsIgnoreCase(loginID.trim())){
                return a;
            }
        }
        return null;
    }

    // Iterates throught stored admin objects
    // return null if no matches, return full admin object if match credentials
    public static Admin checkAdmin(String id, String password){
        // admin data type because "admins" is full of admin object, it can only be treat like admin rather than string
        for(Admin a : admins){
            // check if user input loginID matches ID exists in the list, also make sure looking at CORRECT person password
            if(a.getLoginID().trim().equalsIgnoreCase(id.trim()) && a.getPassword().equals(password)){
                return a; // return full admin object (details)
            }
        }
        return null;
    }


    public static boolean checkCourierIdExists(String loginID) {
        for (Courier c : couriers) {
            if (c.getLoginID().trim().equalsIgnoreCase(loginID.trim())) return true;
        }
        return false;
    }
 
    public static Courier checkCourier(String loginID, String password) {
        for (Courier c : couriers) {
            if (c.getLoginID().trim().equalsIgnoreCase(loginID.trim())
                    && c.getPassword().equals(password)) return c;
        }
        return null;
    }
 
    public static Courier getCourierById(String personID) {
        for (Courier c : couriers) {
            if (c.getPersonID().trim().equalsIgnoreCase(personID.trim())) return c;
        }
        return null;
    }
 
    public static ArrayList<Courier> getAllCouriers() {
        return couriers;
    }
}