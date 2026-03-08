/*
    Parcel - Represents the physical package being shipped
    Used by shipment, COMPOSITION >:)
*/

public class Parcel {
    // package content category
    public enum ContentType{
        DOCUMENTS,      // Papers, Letters
        ELECTRONICS,    // Phones, Laptops (fragile, expensive)
        CLOTHING,       // Clothes
        FRAGILE,        // Glass, ceramics
        FOOD,           // Easy to get rotten
        GENERAL         // Other items
    }

    // Data Fields (IMMUTABLE OBJECT, cant be change once it's created)
    private final ContentType contentType;
    private final String description;

    // Physical Properties
    private final double weight;    // kg
    private final double length;    // cm
    private final double width;     // cm
    private final double height;    // cm

    // Insurance for package
    private final double declaredValue;    // RM (for insurance)


    // Constructor
    public Parcel(ContentType contentType, String description, double weight, double length, double width, double height, double declaredValue){
        this.contentType = contentType;
        this.description = description;
        this.weight = weight;
        this.length = length;
        this.width = width;
        this.height = height;
        this.declaredValue = declaredValue;
    }

    // No SETTER, so the parcel data(e.g. size, weight) is read-only, to prevent human errors
    // GETTER
    public ContentType getContentType(){
        return contentType;
    }

    public String getDescription(){
        return description;
    }

    public double getWeight(){
        return weight;
    }

    public double getLength(){
        return length;
    }

    public double getWidth(){
        return width;
    }

    public double getHeight(){
        return height;
    }

    public double getDeclaredValue(){
        return declaredValue;
    }


    // CALCULATE OPERATIONS (METHODS)
    /* Volumetric Weight = Calculate the space that package occupies,
                       determine shipping cost when it exceeds actual weight
                       Formula = (Length x Width x Height) / Dimensional Factor(common is 5000)
    */
    public double getVolumetricWeight(){
        return (length * width * height) / 5000.0;
    }

    /* Chargeable Weight = Determine whether to use the original weight or Volumetric weight
                        Chargeable Weight will be used to determine the final price (e.g. delivery fee)
    */
    public double getChargeableWeight(){
        return Math.max(weight, getVolumetricWeight());
        // Math.max(x, y) | if x = 20, y = 10, 'x' will be return because it's the biggest
    }

    // Need Insurance cause shipping value items
    public double getInsuranceFee(){
        if(declaredValue <= 0){
            return 0;
        }

        // if fee < 2.00, finalFee = 2.00
        // if fee > 2.00, finalFee = fee
        double fee = declaredValue * 0.01;  // Insurance Fee = 1% of the declared value
        return Math.max(fee, 2.00);         // Minimum 2.00 for insurance fee
    }
}