
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

    // Parameter Contructor
    public Sender(String personID, String loginID, String name, String password, String email, String phone, MemberTier tier) {
        super(personID, loginID, name, password, email, phone);
        this.tier = tier;
    }

    // Getter and Setter for "tier"
    public MemberTier getTier() {
        return tier;
    }

    public void setTier(MemberTier tier) {
        this.tier = tier;
    }

    @Override
    public void displayInfo() {
        System.out.println("  ┌──────────────────────────────────────────┐");
        System.out.println("  │              SENDER PROFILE              │");
        System.out.println("  ├──────────────────────────────────────────┤");
        System.out.printf ("  │  ID       : %-27s│%n", getPersonID());
        System.out.printf ("  │  Name     : %-27s│%n", getName());
        System.out.printf ("  │  Email    : %-27s│%n", getEmail());
        System.out.printf ("  │  Phone    : %-27s│%n", getPhoneNum());
        System.out.printf ("  │  Tier     : %-27s│%n", tier);
        System.out.println("  └──────────────────────────────────────────┘");
        System.out.println();
    }
}