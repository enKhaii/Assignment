/*
    UserRegistry - stores main objects (Sender, Courier, Admin), also consists method for login
    Registry -
*/

import java.util.ArrayList;

public class UserRegistry{
    // ArrayList<x> means this ArrayList can only hold object created in 'x.java'
    private static ArrayList<Admin> admins = new ArrayList<>();
    // private ArrayList<Courier> couriers = new ArrayList<>();

    public static void initializeData(){
        // Admin Objects
        admins.add(new Admin("ADM01", "CHONG123", "Chong Wen Liang", "chongwliang99", "cwenliang22@gmail.com", "017-284-1288", Admin.Role.SUPER_ADMIN));
        admins.add(new Admin("ADM02", "ROHINKUMAR00", "Raj Rohin Kumar", "rohinkumar5421@", "rohinkumar092@hotmail.com", "011-4824-2995",  Admin.Role.OPERATIONS_MANAGER));
        admins.add(new Admin("ADM03", "KYODUSLIM67", "Kyodus Lim Kai Yuan", "kyodus0123@$", "cwenliang22@gmail.com", "017-284-1288",  Admin.Role.DISPATCH_OFFICER));        
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
}

