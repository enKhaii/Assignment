/**
 * Admin — manages the entire courier system.
 * Demonstrates: Inheritance (extends Person), Encapsulation, Polymorphism
 */
public class Admin extends Person {

    public enum Role { SUPER_ADMIN, OPERATIONS_MANAGER, DISPATCH_OFFICER }

    private Role   role;
    private String department;

    public Admin(String adminId, String name, String email,
                 String phone, Role role, String department) {
        super(adminId, name, email, phone);
        this.role       = role;
        this.department = department;
    }

    public Role   getRole()       { return role; }
    public void   setRole(Role role) { this.role = role; }

    public String getDepartment()  { return department; }
    public void   setDepartment(String department) { this.department = department; }

    public boolean canAssignRoutes() {
        return role == Role.SUPER_ADMIN || role == Role.OPERATIONS_MANAGER;
    }

    // Polymorphism: unique implementation
    @Override
    public void displayInfo() {
        System.out.println("  ╔══════════════════════════════════════╗");
        System.out.println("  ║         ADMIN INFORMATION            ║");
        System.out.println("  ╠══════════════════════════════════════╣");
        System.out.printf ("  ║  ID         : %-23s║%n", getPersonId());
        System.out.printf ("  ║  Name       : %-23s║%n", getName());
        System.out.printf ("  ║  Email      : %-23s║%n", getEmail());
        System.out.printf ("  ║  Role       : %-23s║%n", role);
        System.out.printf ("  ║  Department : %-23s║%n", department);
        System.out.println("  ╚══════════════════════════════════════╝");
    }
}
