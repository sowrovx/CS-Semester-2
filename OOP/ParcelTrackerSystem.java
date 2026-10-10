import javax.swing.*;
import java.awt.*;

// Main system - handles all menus, logic, and GUI
public class ParcelTrackerSystem {

    // Array to store all parcels (max 50)
    private static Parcel[] parcels = new Parcel[50];
    private static int parcelCount = 0;

    // Admin credentials - hardcoded for simplicity
    private static final String ADMIN_USER = "admin";
    private static final String ADMIN_PASS = "aiu1234";

    public static void main(String[] args) {
        showMainMenu();
    }

    // ─────────────────────────────────────────
    //  MAIN MENU
    // ─────────────────────────────────────────
    static void showMainMenu() {
        while (true) {
            String[] options = {
                "Admin Login",
                "Track My Parcel",
                "Parcel Room Info & Hours",
                "Exit"
            };

            int choice = JOptionPane.showOptionDialog(
                null,
                "Welcome to AIU Parcel Tracker System\n" +
                "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n" +
                "Please select an option below:",
                "AIU Parcel Tracker",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null, options, options[0]
            );

            // Handle window close
            if (choice == JOptionPane.CLOSED_OPTION) break;

            switch (choice) {
                case 0: adminLogin();       break;
                case 1: trackParcel();      break;
                case 2: showParcelRoomInfo(); break;
                case 3:
                    JOptionPane.showMessageDialog(null,
                        "Thank you for using AIU Parcel Tracker!\n" +
                        "We hope your package arrives safely. 🏠",
                        "Goodbye", JOptionPane.INFORMATION_MESSAGE);
                    System.exit(0);
            }
        }
    }

    // ─────────────────────────────────────────
    //  ADMIN LOGIN
    // ─────────────────────────────────────────
    static void adminLogin() {
        JTextField userField = new JTextField();
        JPasswordField passField = new JPasswordField();

        Object[] fields = {
            "Username:", userField,
            "Password:", passField
        };

        int result = JOptionPane.showConfirmDialog(
            null, fields, "Admin Login", JOptionPane.OK_CANCEL_OPTION
        );

        if (result != JOptionPane.OK_OPTION) return;

        String username = userField.getText().trim();
        String password = new String(passField.getPassword()).trim();

        if (username.equals(ADMIN_USER) && password.equals(ADMIN_PASS)) {
            showAdminMenu();
        } else {
            JOptionPane.showMessageDialog(null,
                "❌ Wrong username or password. Please try again.",
                "Login Failed", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ─────────────────────────────────────────
    //  ADMIN MENU
    // ─────────────────────────────────────────
    static void showAdminMenu() {
        while (true) {
            String[] options = {
                "Add New Parcel",
                "Mark Parcel as Collected",
                "View All Parcels",
                "Logout"
            };

            int choice = JOptionPane.showOptionDialog(
                null,
                "Welcome, Admin!\n" +
                "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n" +
                "What would you like to do?",
                "Admin Panel",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null, options, options[0]
            );

            if (choice == JOptionPane.CLOSED_OPTION) break;

            switch (choice) {
                case 0: addParcel();           break;
                case 1: markAsCollected();     break;
                case 2: viewAllParcels();      break;
                case 3: return; // back to main menu
            }
        }
    }

    // ─────────────────────────────────────────
    //  FEATURE 1 - ADD NEW PARCEL
    // ─────────────────────────────────────────
    static void addParcel() {
        // Check if array is full
        if (parcelCount >= parcels.length) {
            JOptionPane.showMessageDialog(null,
                "System is full. Cannot add more parcels.",
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JTextField trackingField = new JTextField();
        JTextField receiverField = new JTextField();
        JTextField dateField     = new JTextField();

        Object[] fields = {
            "Tracking Number (e.g. AIU-2025-001):", trackingField,
            "Receiver Name:",                        receiverField,
            "Date Arrived (e.g. 25 May 2025):",     dateField
        };

        int result = JOptionPane.showConfirmDialog(
            null, fields, "Add New Parcel", JOptionPane.OK_CANCEL_OPTION
        );

        if (result != JOptionPane.OK_OPTION) return;

        String tracking = trackingField.getText().trim();
        String receiver = receiverField.getText().trim();
        String date     = dateField.getText().trim();

        // Basic input validation
        if (tracking.isEmpty() || receiver.isEmpty() || date.isEmpty()) {
            JOptionPane.showMessageDialog(null,
                "❌ All fields are required. Please fill in everything.",
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Check if tracking number already exists
        if (findParcel(tracking) != -1) {
            JOptionPane.showMessageDialog(null,
                "❌ Tracking number already exists in the system.",
                "Duplicate Entry", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Create new IncomingParcel and add to array
        parcels[parcelCount] = new IncomingParcel(tracking, receiver, date);
        parcelCount++;

        JOptionPane.showMessageDialog(null,
            "✅ Parcel added successfully!\n\n" +
            "Tracking No : " + tracking + "\n" +
            "Receiver    : " + receiver + "\n" +
            "Date Arrived: " + date     + "\n" +
            "Status      : WAITING FOR COLLECTION",
            "Parcel Added", JOptionPane.INFORMATION_MESSAGE);
    }

    // ─────────────────────────────────────────
    //  FEATURE 2 - TRACK PARCEL (student side)
    // ─────────────────────────────────────────
    static void trackParcel() {
        JTextField trackingField = new JTextField();
        JTextField nameField     = new JTextField();

        Object[] fields = {
            "Tracking Number:", trackingField,
            "Your Full Name:",  nameField
        };

        int result = JOptionPane.showConfirmDialog(
            null, fields, "Track My Parcel", JOptionPane.OK_CANCEL_OPTION
        );

        if (result != JOptionPane.OK_OPTION) return;

        String tracking = trackingField.getText().trim();
        String name     = nameField.getText().trim();

        if (tracking.isEmpty() || name.isEmpty()) {
            JOptionPane.showMessageDialog(null,
                "❌ Please enter both tracking number and your name.",
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int index = findParcel(tracking);

        // Parcel not found
        if (index == -1) {
            JOptionPane.showMessageDialog(null,
                "❌ No parcel found with tracking number: " + tracking + "\n" +
                "Please check the number and try again.",
                "Not Found", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Name doesn't match - security check
        if (!parcels[index].getReceiverName().equalsIgnoreCase(name)) {
            JOptionPane.showMessageDialog(null,
                "❌ Name does not match our records.\n" +
                "Please check your name and try again.",
                "Mismatch", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Show parcel details using polymorphism
        // displayStatus() behaves differently based on parcel type
        String details = parcels[index].displayStatus();

        // If parcel is still waiting, check if fine applies and warn student
        if (parcels[index] instanceof IncomingParcel) {
            IncomingParcel ip = (IncomingParcel) parcels[index];
            String fineInfo = ip.getFineInfo();
            if (fineInfo.contains("Fine:")) {
                // Overdue - show as warning so student notices
                JOptionPane.showMessageDialog(null,
                    details + "\n\n⚠️  Please collect your parcel as soon as possible!",
                    "Parcel Status - OVERDUE", JOptionPane.WARNING_MESSAGE);
                return;
            }
        }

        JOptionPane.showMessageDialog(null,
            details, "Parcel Status", JOptionPane.INFORMATION_MESSAGE);
    }

    // ─────────────────────────────────────────
    //  FEATURE 3 - MARK AS COLLECTED
    // ─────────────────────────────────────────
    static void markAsCollected() {
        JTextField trackingField  = new JTextField();
        JTextField nameField      = new JTextField();
        JTextField studentIDField = new JTextField();
        JTextField dateField      = new JTextField();

        Object[] fields = {
            "Tracking Number:",                      trackingField,
            "Receiver Name:",                        nameField,
            "Student ID:",                           studentIDField,
            "Date Collected (e.g. 27 May 2025):",   dateField
        };

        int result = JOptionPane.showConfirmDialog(
            null, fields, "Mark Parcel as Collected", JOptionPane.OK_CANCEL_OPTION
        );

        if (result != JOptionPane.OK_OPTION) return;

        String tracking  = trackingField.getText().trim();
        String name      = nameField.getText().trim();
        String studentID = studentIDField.getText().trim();
        String date      = dateField.getText().trim();

        if (tracking.isEmpty() || name.isEmpty() || studentID.isEmpty() || date.isEmpty()) {
            JOptionPane.showMessageDialog(null,
                "❌ All fields are required.",
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int index = findParcel(tracking);

        if (index == -1) {
            JOptionPane.showMessageDialog(null,
                "❌ Tracking number not found in system.",
                "Not Found", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Check if already collected
        if (parcels[index].getStatus().equals("COLLECTED")) {
            JOptionPane.showMessageDialog(null,
                "⚠️ This parcel has already been collected.",
                "Already Collected", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Replace IncomingParcel with CollectedParcel in same array slot
        // This is polymorphism - same array holds different object types
        parcels[index] = new CollectedParcel(tracking, name, studentID, date);

        JOptionPane.showMessageDialog(null,
            parcels[index].displayStatus(),
            "Collected Successfully", JOptionPane.INFORMATION_MESSAGE);
    }

    // ─────────────────────────────────────────
    //  FEATURE 4 - VIEW ALL PARCELS
    // ─────────────────────────────────────────
    static void viewAllParcels() {
        if (parcelCount == 0) {
            JOptionPane.showMessageDialog(null,
                "No parcels in the system yet.",
                "Empty", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // Count waiting and collected using loop
        int waiting   = 0;
        int collected = 0;

        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%-15s | %-20s | %s\n",
            "TRACKING NO", "RECEIVER NAME", "STATUS"));
        sb.append("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n");

        // Loop through all parcels and build the list
        for (int i = 0; i < parcelCount; i++) {
            sb.append(parcels[i].getSummaryLine()).append("\n");

            if (parcels[i].getStatus().equals("COLLECTED")) {
                collected++;
            } else {
                waiting++;
            }
        }

        sb.append("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n");
        sb.append("Total Parcels : " + parcelCount  + "\n");
        sb.append("Waiting       : " + waiting      + "\n");
        sb.append("Collected     : " + collected    + "\n");

        // Use monospaced font so table aligns properly
        JTextArea textArea = new JTextArea(sb.toString());
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 13));
        textArea.setEditable(false);
        textArea.setBackground(new Color(245, 245, 245));

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(550, 300));

        JOptionPane.showMessageDialog(null,
            scrollPane, "All Parcels (" + parcelCount + " records)",
            JOptionPane.PLAIN_MESSAGE);
    }

    // ─────────────────────────────────────────
    //  FEATURE 5 - PARCEL ROOM INFO (real AIU data)
    // ─────────────────────────────────────────
    static void showParcelRoomInfo() {
        String info =
            "╔════════════════════════════════════════════════════╗\n" +
            "║           AIU PARCEL ROOM INFORMATION              ║\n" +
            "║           Student Centre, Level 1                  ║\n" +
            "╠════════════════════════════════════════════════════╣\n" +
            "║  COLLECTION HOURS                                  ║\n" +
            "║  ────────────────────────────────────────────────  ║\n" +
            "║  DAY          MORNING         AFTERNOON   NIGHT    ║\n" +
            "║  Mon - Thu    12:00-2:00PM    6:00-8:00PM 8-10PM   ║\n" +
            "║  Friday       10:30-12:30PM   4:00-7:30PM CLOSED   ║\n" +
            "║  Saturday     11:30-2:00PM    4:00-7:30PM CLOSED   ║\n" +
            "║  Sun & Public Holiday         CLOSED               ║\n" +
            "╠════════════════════════════════════════════════════╣\n" +
            "║  GENERAL TERMS                                     ║\n" +
            "║  ────────────────────────────────────────────────  ║\n" +
            "║  • Bring Student ID + parcel purchase proof.       ║\n" +
            "║  • Parcel must be labeled with your real name      ║\n" +
            "║    as stated on your Student ID.                   ║\n" +
            "║  • Large parcels must be opened in front of        ║\n" +
            "║    parcel room assistant for verification.         ║\n" +
            "║  • Cooking equipment parcels sent to SA and        ║\n" +
            "║    can only be collected after verification.       ║\n" +
            "║  • Collection fee: RM 0.50 per parcel.             ║\n" +
            "╠════════════════════════════════════════════════════╣\n" +
            "║  FINE POLICY                                       ║\n" +
            "║  ────────────────────────────────────────────────  ║\n" +
            "║  • First 3 days after arrival: FREE                ║\n" +
            "║  • After 3 days: RM 0.50 fine per day              ║\n" +
            "║  • Example: collected on day 7 = RM 2.00 fine      ║\n" +
            "║    (4 overdue days x RM 0.50)                      ║\n" +
            "╚════════════════════════════════════════════════════╝";

        JTextArea textArea = new JTextArea(info);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 13));
        textArea.setEditable(false);

        JOptionPane.showMessageDialog(null,
            textArea, "AIU Parcel Room Info",
            JOptionPane.INFORMATION_MESSAGE);
    }

    // ─────────────────────────────────────────
    //  HELPER - Search parcel by tracking number
    // ─────────────────────────────────────────
    static int findParcel(String trackingNumber) {
        for (int i = 0; i < parcelCount; i++) {
            if (parcels[i].getTrackingNumber().equalsIgnoreCase(trackingNumber)) {
                return i; // return index if found
            }
        }
        return -1; // not found
    }
}
