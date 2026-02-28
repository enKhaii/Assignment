/*
    Customs exceptions for the System
*/

class VehicleNotFoundException extends Exception{
    public VehicleNotFoundException(String vehicleID){
        super("  >> Error: Vehicle ID \"" + vehicleID + "\" not found.");
    }
}