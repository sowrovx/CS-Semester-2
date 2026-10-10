// Child class 2 - parcel that has been collected by the student
public class CollectedParcel extends Parcel {

    private String studentID;
    private String dateCollected;

    public CollectedParcel(String trackingNumber, String receiverName,
                           String studentID, String dateCollected) {
        super(trackingNumber, receiverName);
        this.studentID     = studentID;
        this.dateCollected = dateCollected;
        setStatus("COLLECTED"); // update status on creation
    }

    public String getStudentID()      { return studentID; }
    public String getDateCollected()  { return dateCollected; }

    // Polymorphism - overrides parent displayStatus() for collected parcels
    @Override
    public String displayStatus() {
        return "╔══════════════════════════════════════════╗\n" +
               "║           DELIVERY CONFIRMED             ║\n" +
               "╠══════════════════════════════════════════╣\n" +
               "║ Tracking No    : " + padRight(getTrackingNumber(), 24) + "║\n" +
               "║ Collected By   : " + padRight(getReceiverName(), 24)   + "║\n" +
               "║ Student ID     : " + padRight(studentID, 24)           + "║\n" +
               "║ Date Collected : " + padRight(dateCollected, 24)       + "║\n" +
               "║                                          ║\n" +
               "║ STATUS : ✅ COLLECTED                    ║\n" +
               "╚══════════════════════════════════════════╝";
    }

    private String padRight(String text, int length) {
        if (text.length() > length) text = text.substring(0, length);
        return String.format("%-" + length + "s", text);
    }
}
