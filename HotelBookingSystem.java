import java.awt.*;
import java.util.*;
import javax.swing.*;

// ================= ROOM =================
class Room {
    int no;
    String type;
    double price;
    boolean available = true;

    Room(int no, String type, double price) {
        this.no = no;
        this.type = type;
        this.price = price;
    }
}

// ================= CUSTOMER =================
class Customer {
    String name, phone;

    Customer(String name, String phone) {
        this.name = name;
        this.phone = phone;
    }
}

// ================= BOOKING =================
class Booking {
    Customer customer;
    int roomNo, days;
    double amount;

    Booking(Customer customer, int roomNo, int days, double amount) {
        this.customer = customer;
        this.roomNo = roomNo;
        this.days = days;
        this.amount = amount;
    }
}

// ================= MAIN CLASS =================
public class HotelBookingSystem extends JFrame {

    // Array - stores room information
    Room[] rooms = {
        new Room(101, "Single", 1500),
        new Room(102, "Single", 1500),
        new Room(201, "Double", 2500),
        new Room(202, "Double", 2500),
        new Room(301, "Deluxe", 4000)
    };

    // LinkedList - stores booking records
    LinkedList<Booking> bookings = new LinkedList<>();

    // HashMap - searches rooms using room number
    HashMap<Integer, Room> roomMap = new HashMap<>();

    // TreeMap - displays rooms in sorted order
    TreeMap<Integer, Room> sortedRooms = new TreeMap<>();

    // GUI fields
    JTextField name = new JTextField();
    JTextField phone = new JTextField();
    JTextField room = new JTextField();
    JTextField days = new JTextField();
    JTextArea output = new JTextArea();

    // ================= CONSTRUCTOR =================
    public HotelBookingSystem() {

        // Store rooms in HashMap and TreeMap
        for (Room r : rooms) {
            roomMap.put(r.no, r);
            sortedRooms.put(r.no, r);
        }

        // ---------- Window ----------
        setTitle("Hotel Booking System");
        setSize(700, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // ---------- Header ----------
        JLabel title = new JLabel(
            "HOTEL BOOKING SYSTEM",
            JLabel.CENTER
        );

        title.setFont(new Font("Arial", Font.BOLD, 26));

        JLabel subtitle = new JLabel(
            "Room Booking & Management",
            JLabel.CENTER
        );

        subtitle.setFont(new Font("Arial", Font.PLAIN, 14));

        JPanel header = new JPanel(new BorderLayout());
        header.add(title, BorderLayout.NORTH);
        header.add(subtitle, BorderLayout.SOUTH);

        // ---------- Form ----------
        JPanel form = new JPanel(new GridLayout(4, 2, 10, 10));
        form.setBorder(
            BorderFactory.createTitledBorder("Booking Details")
        );

        form.add(new JLabel("Customer Name:"));
        form.add(name);

        form.add(new JLabel("Phone Number:"));
        form.add(phone);

        form.add(new JLabel("Room Number:"));
        form.add(room);

        form.add(new JLabel("Number of Days:"));
        form.add(days);

        // ---------- Buttons ----------
        JButton bookButton = new JButton("Book Room");
        JButton cancelButton = new JButton("Cancel Booking");
        JButton availableButton = new JButton("Available Rooms");
        JButton allButton = new JButton("All Rooms");

        JPanel buttons = new JPanel(
            new GridLayout(2, 2, 10, 10)
        );

        buttons.add(bookButton);
        buttons.add(cancelButton);
        buttons.add(availableButton);
        buttons.add(allButton);

        // ---------- Top Section ----------
        JPanel top = new JPanel(new BorderLayout(10, 10));
        top.add(form, BorderLayout.CENTER);
        top.add(buttons, BorderLayout.SOUTH);

        // ---------- Output ----------
        output.setEditable(false);
        output.setFont(new Font("Monospaced", Font.PLAIN, 14));
        output.setMargin(new Insets(10, 10, 10, 10));

        JPanel outputPanel = new JPanel(new BorderLayout());
        outputPanel.setBorder(
            BorderFactory.createTitledBorder("System Output")
        );

        outputPanel.add(
            new JScrollPane(output),
            BorderLayout.CENTER
        );

        // ---------- Main Layout ----------
        JPanel main = new JPanel(new BorderLayout(10, 10));

        main.setBorder(
            BorderFactory.createEmptyBorder(15, 20, 20, 20)
        );

        main.add(header, BorderLayout.NORTH);
        main.add(top, BorderLayout.CENTER);
        main.add(outputPanel, BorderLayout.SOUTH);

        outputPanel.setPreferredSize(
            new Dimension(600, 230)
        );

        setContentPane(main);

        // ---------- Button Actions ----------
        bookButton.addActionListener(
            e -> bookRoom()
        );

        cancelButton.addActionListener(
            e -> cancelRoom()
        );

        availableButton.addActionListener(
            e -> showAvailable()
        );

        allButton.addActionListener(
            e -> showAll()
        );

        setVisible(true);
    }

    // ================= BOOK ROOM =================
    void bookRoom() {

        // Check empty fields
        if (name.getText().trim().isEmpty()
                || phone.getText().trim().isEmpty()
                || room.getText().trim().isEmpty()
                || days.getText().trim().isEmpty()) {

            message("Please fill all fields.");
            return;
        }

        try {

            int roomNo = Integer.parseInt(
                room.getText().trim()
            );

            int d = Integer.parseInt(
                days.getText().trim()
            );

            // Check valid days
            if (d <= 0) {
                message(
                    "Number of days must be greater than 0."
                );
                return;
            }

            // Search room using HashMap
            Room r = roomMap.get(roomNo);

            if (r == null) {
                message("Room not found.");
                return;
            }

            // Check room availability
            if (!r.available) {
                message("Room is already booked.");
                return;
            }

            // Create customer
            Customer c = new Customer(
                name.getText().trim(),
                phone.getText().trim()
            );

            // Calculate bill
            double total = r.price * d;

            // Create booking
            Booking b = new Booking(
                c,
                roomNo,
                d,
                total
            );

            // Add booking to LinkedList
            bookings.add(b);

            // Mark room as booked
            r.available = false;

            // Display result
            output.setText(
                "========== BOOKING SUCCESSFUL ==========\n\n" +
                "Customer : " + c.name + "\n" +
                "Phone    : " + c.phone + "\n" +
                "Room     : " + roomNo + "\n" +
                "Type     : " + r.type + "\n" +
                "Days     : " + d + "\n" +
                "Price    : ₹" + r.price + " / day\n" +
                "----------------------------------------\n" +
                "TOTAL BILL: ₹" + total + "\n" +
                "========================================"
            );

            clear();

        } catch (NumberFormatException e) {

            message(
                "Room number and days must be numbers."
            );
        }
    }

    // ================= CANCEL BOOKING =================
    void cancelRoom() {

        if (room.getText().trim().isEmpty()) {
            message("Enter a room number.");
            return;
        }

        try {

            int roomNo = Integer.parseInt(
                room.getText().trim()
            );

            // Iterator safely removes booking
            Iterator<Booking> it =
                bookings.iterator();

            while (it.hasNext()) {

                Booking b = it.next();

                if (b.roomNo == roomNo) {

                    // Remove booking
                    it.remove();

                    // Make room available
                    Room r = roomMap.get(roomNo);

                    if (r != null) {
                        r.available = true;
                    }

                    output.setText(
                        "========== BOOKING CANCELLED ==========\n\n" +
                        "Room     : " + roomNo + "\n" +
                        "Customer : " + b.customer.name + "\n" +
                        "========================================"
                    );

                    clear();
                    return;
                }
            }

            message(
                "No booking found for this room."
            );

        } catch (NumberFormatException e) {

            message("Enter a valid room number.");
        }
    }

    // ================= AVAILABLE ROOMS =================
    void showAvailable() {

        output.setText(
            "========== AVAILABLE ROOMS ==========\n\n"
        );

        boolean found = false;

        for (Room r : rooms) {

            if (r.available) {

                found = true;

                output.append(
                    "Room: " + r.no +
                    " | " + r.type +
                    " | ₹" + r.price + "/day\n"
                );
            }
        }

        if (!found) {
            output.append(
                "No rooms are currently available.\n"
            );
        }

        output.append(
            "\n====================================="
        );
    }

    // ================= ALL ROOMS =================
    void showAll() {

        output.setText(
            "========== ALL ROOMS ==========\n\n"
        );

        // TreeMap keeps rooms sorted by room number
        for (Room r : sortedRooms.values()) {

            String status =
                r.available ? "Available" : "Booked";

            output.append(
                "Room: " + r.no +
                " | " + r.type +
                " | ₹" + r.price +
                " | " + status + "\n"
            );
        }

        output.append(
            "\n================================"
        );
    }

    // ================= CLEAR FIELDS =================
    void clear() {

        name.setText("");
        phone.setText("");
        room.setText("");
        days.setText("");
    }

    // ================= MESSAGE =================
    void message(String text) {

        JOptionPane.showMessageDialog(
            this,
            text
        );
    }

    // ================= MAIN =================
    public static void main(String[] args) {

        SwingUtilities.invokeLater(
            HotelBookingSystem::new
        );
    }
}