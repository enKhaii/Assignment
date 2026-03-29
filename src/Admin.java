/*
    Admin - manages the entire courier system
    enum - defines fixed admin role (only accepts Role.??? rather than any String)
*/

public class Admin extends Person{
    public enum Role{
        SUPER_ADMIN,        // Full system access, can manage staff and override all operations
        OPERATIONS_MANAGER, // Can assign routes, manage couriers, and oversee daily operations
        DISPATCH_OFFICER    // Basic access to view shipments and update statuses only
    }

    private Role adminRole; 
    private String department;


    public Admin(String staffID, String loginID, String name, String password, String email, String phone, Role adminRole) {
        // super = accessing parent class(Person) variable
        super(staffID, loginID, name, password, email, phone);        
        this.department = switch(adminRole){
            case SUPER_ADMIN -> "Executive HQ";
            case OPERATIONS_MANAGER -> "Logistics & Warehousing";
            case DISPATCH_OFFICER -> "Fleet Operations";
            default -> "General Administration";
        };
        this.adminRole = adminRole;
    }

    // Getter and Setter for "adminRole"
    public Role getAdminRole(){
        return adminRole;
    }

    public void setAdminRole(Role adminRole){
        this.adminRole = adminRole;
    }

    // Getter and Setter for "department"
    public String getDepartment(){
        return department;
    }

    public void setDepartment(String department){
        this.department = department;
    }

    // Method for can assign routes or not (return true if role are correct)
    public boolean canAssignRoutes(){
        return adminRole == Role.SUPER_ADMIN || adminRole == Role.OPERATIONS_MANAGER;
    }

    
    // Override ensures we access the ADMIN displayInfo instead of PERSON displayInfo
    // prevent general displayInfo to be displayed
    @Override
    public void displayInfo(){
        // %-?s, the - means left allignment, default will be right allignment
        System.out.println("\n  ╔══════════════════════════════════════╗");
        System.out.println("  ║           ADMIN INFORMATION          ║");
        System.out.println("  ╠══════════════════════════════════════╣");
        System.out.printf ("  ║  ID         : %-23s║%n", getPersonID());
        System.out.printf ("  ║  Name       : %-23s║%n", getName());
        System.out.printf ("  ║  Email      : %-23s║%n", getEmail());
        System.out.printf ("  ║  Role       : %-23s║%n", adminRole);
        System.out.printf ("  ║  Department : %-23s║%n", department);
        System.out.println("  ╚══════════════════════════════════════╝");
        System.out.println();
    }
}