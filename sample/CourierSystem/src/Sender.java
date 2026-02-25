import java.util.ArrayList;
import java.util.List;

/**
 * Sender — a customer who creates shipment requests.
 * Demonstrates: Inheritance (extends Person), Encapsulation, Polymorphism
 */
public class Sender extends Person {

    private String address;
    private List<String> shipmentIds; // Aggregation: tracks shipment IDs placed

    public Sender(String senderId, String name, String email,
                  String phone, String address) {
        super(senderId, name, email, phone);
        this.address     = address;
        this.shipmentIds = new ArrayList<>();
    }

    public String getAddress() { return address; }
    public void   setAddress(String address) { this.address = address; }

    public List<String> getShipmentIds() { return new ArrayList<>(shipmentIds); }

    public void addShipmentId(String trackingId) {
        shipmentIds.add(trackingId);
    }

    public int getTotalShipments() { return shipmentIds.size(); }

    // Polymorphism: unique implementation of abstract method
    @Override
    public void displayInfo() {
        System.out.println("  ╔══════════════════════════════════════╗");
        System.out.println("  ║         SENDER INFORMATION           ║");
        System.out.println("  ╠══════════════════════════════════════╣");
        System.out.printf ("  ║  ID       : %-25s║%n", getPersonId());
        System.out.printf ("  ║  Name     : %-25s║%n", getName());
        System.out.printf ("  ║  Email    : %-25s║%n", getEmail());
        System.out.printf ("  ║  Phone    : %-25s║%n", getPhone());
        System.out.printf ("  ║  Address  : %-25s║%n", address);
        System.out.printf ("  ║  Shipments: %-25d║%n", getTotalShipments());
        System.out.println("  ╚══════════════════════════════════════╝");
    }
}
