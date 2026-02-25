import java.util.ArrayList;

public class UserRegistry{
    private static ArrayList<Admin> admins = new ArrayList<>();
    //private ArrayList<Courier> couriers = new ArrayList<>();

    public static void initializeData(){
        // Admin Objects
        admins.add(new Admin("A01", "CHONG123", "Chong Wen Liang", "chongwliang99", "cwenliang22@gmail.com", "017-284-1288", Admin.Role.SUPER_ADMIN));
        admins.add(new Admin("A02", "ROHINKUMAR00", "Raj Rohin Kumar", "rohinkumar5421@", "rohinkumar092@hotmail.com", "011-4824-2995",  Admin.Role.OPERATIONS_MANAGER));
        admins.add(new Admin("A03", "KYODUSLIM67", "Kyodus Lim Kai Yuan", "kyodus0123@$", "cwenliang22@gmail.com", "017-284-1288",  Admin.Role.DISPATCH_OFFICER));        
    }

    public static Admin checkAdmin(String id, String password){
        for(Admin a : admins){
            if(a.getPersonID().equals(id) && a.getName().equals(password)){
                return a;
            }
        }
        return null;
    }
}

