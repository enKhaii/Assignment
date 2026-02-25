/**
 * Abstract base class for all persons in the Courier & Logistics System.
 * Demonstrates: Abstraction, Encapsulation
 */
public abstract class Person {

    private String personId;
    private String name;
    private String email;
    private String phone;

    public Person(String personId, String name, String email, String phone) {
        this.personId = personId;
        this.name     = name;
        this.email    = email;
        this.phone    = phone;
    }

    // Getters & Setters (Encapsulation)
    public String getPersonId() { return personId; }
    public void   setPersonId(String personId) { this.personId = personId; }

    public String getName() { return name; }
    public void   setName(String name) {
        if (name == null || name.trim().isEmpty())
            throw new IllegalArgumentException("Name cannot be empty.");
        this.name = name;
    }

    public String getEmail() { return email; }
    public void   setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void   setPhone(String phone) { this.phone = phone; }

    // Abstract method — every subclass must define its own display (Polymorphism)
    public abstract void displayInfo();

    @Override
    public String toString() {
        return "[" + personId + "] " + name + " | " + email + " | " + phone;
    }
}
