/*
    Customs exceptions for the System
*/

class VehicleNotFoundException extends Exception{
    public VehicleNotFoundException(String vehicleID){
        super("Vehicle not found with ID: " + vehicleID);
    }
}