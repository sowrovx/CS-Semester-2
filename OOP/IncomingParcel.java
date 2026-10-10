import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

// Child class 1 - parcel that just arrived in the parcel room
public class IncomingParcel extends Parcel {

    private String dateArrived;
    private LocalDate parsedDate; // used for fine calculation

    private static final DateTimeFormatter FORMAT = DateTimeFormatter.ofPattern("dd MMM yyyy");

    public IncomingParcel(String trackingNumber, String receiverName, String dateArrived) {
        super(trackingNumber, receiverName);
        this.dateArrived = dateArrived;

        // Try to parse date for fine calc - if format wrong just skip
        try {
            this.parsedDate = LocalDate.parse(dateArrived, FORMAT);
        } catch (Exception e) {
            this.parsedDate = null;
        }
    }

    public String getDateArrived() { return dateArrived; }

    // Calculate fine - free for first 3 days, then RM0.50 per day after
    public String getFineInfo() {
        if (parsedDate == null) return "Fine info unavailable";

        long daysSinceArrival = ChronoUnit.DAYS.between(parsedDate, LocalDate.now());

        if (daysSinceArrival <= 3) {
            long daysLeft = 3 - daysSinceArrival;
            return "No fine yet  |  " + daysLeft + " free day(s) remaining";
        } else {
            long fineDays = daysSinceArrival - 3;
            double fine   = fineDays * 0.50;
            return String.format("⚠ Fine: RM %.2f  (%d days overdue)", fine, fineDays);
        }
    }

    // Polymorphism - overrides parent displayStatus() for incoming parcels
    @Override
    public String displayStatus() {
        return "╔══════════════════════════════════════════╗\n" +
               "║           YOUR PARCEL STATUS             ║\n" +
               "╠══════════════════════════════════════════╣\n" +
               "║ Tracking No  : " + padRight(getTrackingNumber(), 26) + "║\n" +
               "║ Receiver     : " + padRight(getReceiverName(), 26)   + "║\n" +
               "║ Date Arrived : " + padRight(dateArrived, 26)         + "║\n" +
               "║ Fine Status  : " + padRight(getFineInfo(), 26)       + "║\n" +
               "║                                          ║\n" +
               "║ STATUS : 📦 WAITING FOR COLLECTION       ║\n" +
               "╚══════════════════════════════════════════╝";
    }

    private String padRight(String text, int length) {
        if (text.length() > length) text = text.substring(0, length);
        return String.format("%-" + length + "s", text);
    }
}
