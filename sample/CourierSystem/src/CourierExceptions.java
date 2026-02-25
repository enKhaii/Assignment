/**
 * Custom exceptions for the Courier & Logistics Management System.
 * Demonstrates: Exception Handling
 */

// Thrown when a shipment tracking ID is not found
class ShipmentNotFoundException extends Exception {
    public ShipmentNotFoundException(String trackingId) {
        super("Shipment not found with Tracking ID: " + trackingId);
    }
}

// Thrown when a sender is not found
class SenderNotFoundException extends Exception {
    public SenderNotFoundException(String senderId) {
        super("Sender not found with ID: " + senderId);
    }
}

// Thrown when a courier is not found
class CourierNotFoundException extends Exception {
    public CourierNotFoundException(String courierId) {
        super("Courier not found with ID: " + courierId);
    }
}

// Thrown when a vehicle is not found
class VehicleNotFoundException extends Exception {
    public VehicleNotFoundException(String vehicleId) {
        super("Vehicle not found with ID: " + vehicleId);
    }
}

// Thrown when no courier is available for assignment
class NoCourierAvailableException extends Exception {
    public NoCourierAvailableException() {
        super("No available couriers at the moment. Please try again later.");
    }
}

// Thrown when a shipment is in a status that doesn't allow the requested action
class InvalidShipmentStatusException extends Exception {
    public InvalidShipmentStatusException(String trackingId, String action) {
        super("Cannot perform [" + action + "] on shipment " +
              trackingId + " in its current status.");
    }
}

// Thrown when payment has not been made before dispatch
class PaymentNotConfirmedException extends Exception {
    public PaymentNotConfirmedException(String trackingId) {
        super("Shipment " + trackingId + " has not been paid for yet.");
    }
}
