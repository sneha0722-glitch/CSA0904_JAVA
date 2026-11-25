import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

public class AppointmentDesk extends JFrame {
    private final JTextField patientId = new JTextField(12);
    private final JComboBox<String> department = new JComboBox<>(new String[]{"Cardiology","Neurology","Orthopedics"});
    private final JComboBox<String> doctor = new JComboBox<>();
    private final JComboBox<String> slot = new JComboBox<>(new String[]{"09:00","10:00","11:00","14:00","15:00"});
    private final JLabel status = new JLabel("Ready.");
    private boolean bookingInProgress = false;
    private final Map<String, java.util.List<String>> doctors = Map.of(
        "Cardiology", java.util.List.of("Dr Rao","Dr Kumar"),
        "Neurology", java.util.List.of("Dr Meena","Dr Joseph"),
        "Orthopedics", java.util.List.of("Dr Anand","Dr Priya")
    );

    public AppointmentDesk() {
        super("Hospital Appointment Desk");
        setLayout(new BorderLayout(10,10));
        JPanel form = new JPanel(new GridLayout(4,2,8,8));
        form.add(new JLabel("Patient ID")); form.add(patientId);
        form.add(new JLabel("Department")); form.add(department);
        form.add(new JLabel("Doctor")); form.add(doctor);
        form.add(new JLabel("Time slot")); form.add(slot);
        JButton book = new JButton("Book Appointment");
        add(form, BorderLayout.CENTER); add(book, BorderLayout.SOUTH); add(status, BorderLayout.NORTH);
        department.addActionListener(e -> refreshDoctors());
        book.addActionListener(e -> book());
        addWindowListener(new WindowAdapter() {
            @Override public void windowClosing(WindowEvent e) {
                if (bookingInProgress) {
                    int answer = JOptionPane.showConfirmDialog(AppointmentDesk.this,
                        "A booking is in progress. Close anyway?", "Confirm close", JOptionPane.YES_NO_OPTION);
                    if (answer == JOptionPane.YES_OPTION) dispose();
                } else dispose();
            }
        });
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        refreshDoctors(); pack(); setLocationRelativeTo(null);
    }
    private void refreshDoctors() {
        doctor.removeAllItems();
        for (String name : doctors.getOrDefault((String)department.getSelectedItem(), java.util.List.of()))
            doctor.addItem(name);
    }
    private void book() {
        if (patientId.getText().isBlank() || doctor.getSelectedItem() == null || slot.getSelectedItem() == null) {
            status.setText("Enter patient ID and select all appointment details."); return;
        }
        bookingInProgress = true;
        status.setText("Booking...");
        Timer timer = new Timer(900, e -> {
            bookingInProgress = false;
            status.setText("Appointment confirmed for " + patientId.getText().trim()
                + " with " + doctor.getSelectedItem() + " at " + slot.getSelectedItem());
        });
        timer.setRepeats(false); timer.start();
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AppointmentDesk().setVisible(true));
    }
}
/*
Event model:
- department JComboBox -> ActionEvent -> ActionListener -> refreshDoctors()
- Book JButton -> ActionEvent -> ActionListener -> validate and book
- window close -> WindowEvent -> WindowAdapter.windowClosing -> confirm/dispose
- doctor and slot JComboBoxes -> ActionEvent -> ActionListener if dependent logic is added

Containment: JFrame(BorderLayout) -> form JPanel(GridLayout 4x2) -> JLabel,
JTextField and JComboobox controls; Book JButton in SOUTH; status JLabel in NORTH.
Pseudocode refresh: clear doctor model; read selected department; for each doctor
in map[department], add doctor to combo model.
Pseudocode booking: validate patient ID, department, doctor, slot; show error if
invalid; set booking flag; call service; show confirmation/error; clear booking flag.
Event trace: construct frame -> register listeners -> populate doctors -> set visible;
selection emits ActionEvent -> listener updates model; book emits ActionEvent ->
validate/start booking -> completion feedback; close emits WINDOW_CLOSING ->
windowClosing -> confirm if busy -> dispose -> WINDOW_CLOSED.
Applet lifecycle differs: init once, start when active, paint for rendering, stop when
inactive, destroy before removal; JFrame uses constructor/visibility/events/dispose.
Map lookup is O(1) average plus O(k) to repopulate k doctors; cached data uses
O(d*m) space for d departments and m doctors each. DB fetch uses network/SQL
latency per change and can use less persistent memory but adds repeated I/O.
This UI uses a short Swing Timer as a local booking simulation, not real hospital DB.