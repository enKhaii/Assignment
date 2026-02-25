/**
 * Parcel — represents the physical package details.
 * Used as Composition inside Shipment.
 * Demonstrates: Encapsulation, Composition
 */
public class Parcel {

    public enum ContentType {
        DOCUMENTS, ELECTRONICS, CLOTHING, FRAGILE, FOOD, GENERAL
    }

    private double      weightKg;
    private double      lengthCm;
    private double      widthCm;
    private double      heightCm;
    private ContentType contentType;
    private boolean     isInsured;
    private double      declaredValue; // for insurance

    public Parcel(double weightKg, double lengthCm, double widthCm,
                  double heightCm, ContentType contentType,
                  boolean isInsured, double declaredValue) {
        if (weightKg <= 0)
            throw new IllegalArgumentException("Weight must be greater than 0.");
        this.weightKg     = weightKg;
        this.lengthCm     = lengthCm;
        this.widthCm      = widthCm;
        this.heightCm     = heightCm;
        this.contentType  = contentType;
        this.isInsured    = isInsured;
        this.declaredValue = declaredValue;
    }

    // Getters & Setters
    public double      getWeightKg()     { return weightKg; }
    public double      getLengthCm()     { return lengthCm; }
    public double      getWidthCm()      { return widthCm; }
    public double      getHeightCm()     { return heightCm; }
    public ContentType getContentType()  { return contentType; }
    public boolean     isInsured()       { return isInsured; }
    public double      getDeclaredValue(){ return declaredValue; }

    /** Volumetric weight = (L x W x H) / 5000  (standard courier formula) */
    public double getVolumetricWeight() {
        return (lengthCm * widthCm * heightCm) / 5000.0;
    }

    /** Chargeable weight = max of actual vs volumetric */
    public double getChargeableWeight() {
        return Math.max(weightKg, getVolumetricWeight());
    }

    public double getInsuranceFee() {
        return isInsured ? declaredValue * 0.01 : 0.0; // 1% of declared value
    }

    public void display() {
        System.out.printf("  Weight       : %.2f kg (Chargeable: %.2f kg)%n",
                          weightKg, getChargeableWeight());
        System.out.printf("  Dimensions   : %.0f x %.0f x %.0f cm%n",
                          lengthCm, widthCm, heightCm);
        System.out.printf("  Content      : %s%n", contentType);
        System.out.printf("  Insured      : %s%s%n",
                isInsured ? "Yes" : "No",
                isInsured ? String.format(" (Value: RM %.2f)", declaredValue) : "");
    }
}
