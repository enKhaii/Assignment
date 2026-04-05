
// Sender - Represents a customer who sends parcels
// Inherits from Person abstract class

public class Sender extends Person {
    
    // Custom enum: Represents the membership tier of the sender
    public enum MemberTier {
        STANDARD,   // Normal User
        PREMIUM,    // Premium member (might have shipping discounts)
        BUSINESS    // Corporate client (monthly billing)
    }

    private MemberTier tier;
    private String password;

    // Parameter Contructor
    public Sender(String personID, String name, String email, String phone, MemberTier tier) {
        super(personID, name, email, phone);
        this.tier = tier;
    }

    // Getter and Setter for "tier"
    public MemberTier getTier() {
        return tier;
    }

    public void setTier(MemberTier tier) {
        this.tier = tier;
    }

    public void setPassword(String password) {
    this.password = password;
}

    public String getPassword() {
        return this.password;
    }

    @Override
    public void displayInfo() {
        System.out.println("  ┌──────────────────────────────────────────┐");
        System.out.println("  │              SENDER PROFILE              │");
        System.out.println("  ├──────────────────────────────────────────┤");
        System.out.printf ("  │  ID       : %-29s│%n", getPersonID());
        System.out.printf ("  │  Name     : %-29s│%n", getName());
        System.out.printf ("  │  Email    : %-29s│%n", getEmail());
        System.out.printf ("  │  Phone    : %-29s│%n", getPhoneNum());
        System.out.printf ("  │  Tier     : %-29s│%n", tier);
        System.out.println("  └──────────────────────────────────────────┘");
        System.out.println();
    }
}