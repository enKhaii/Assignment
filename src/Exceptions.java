/*
    Customs exceptions for the System
*/
public class Exceptions{
    // empty class bc java files require one public class, acts as placeholder, no function
}

class VehicleNotFoundException extends Exception{
    public VehicleNotFoundException(String vehicleID){
        super("  >> Error: Vehicle ID \"" + vehicleID + "\" not found.");
    }
}