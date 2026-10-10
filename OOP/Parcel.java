// Abstract parent class - blueprint for all parcel types
public abstract class Parcel {

    // Private fields - encapsulation
    private String trackingNumber;
    private String receiverName;
    private String status;

    // Constructor
    public Parcel(String trackingNumber, String receiverName) {
        this.trackingNumber = trackingNumber;
        this.receiverName = receiverName;
        this.status = "WAITING FOR COLLECTION";
    }

    // Getters
    public String getTrackingNumber() { return trackingNumber; }
    public String getReceiverName()   { return receiverName; }
    public String getStatus()         { return status; }

    // Setter - only status can be changed after creation
    public void setStatus(String status) { this.status = status; }

    // Abstract method - every child must implement this their own way
    public abstract String displayStatus();

    // Shared summary line used in view all parcels
    public String getSummaryLine() {
        return String.format("%-15s | %-20s | %s",
            trackingNumber, receiverName, status);
    }
}
