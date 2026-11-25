import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/** UI collects input and displays feedback; service owns validation and JDBC. */
public class FeeWindow extends JFrame {
    private final JTextField regNo = new JTextField(14);
    private final JTextField amount = new JTextField(14);
    private final JButton pay = new JButton("Pay");
    private final JButton clear = new JButton("Clear");
    private final JButton receipt = new JButton("Receipt");
    private final JLabel status = new JLabel("Enter payment details.");
    private String lastReceipt = "No payment has been made.";
    private final FeeService service = new FeeService(
        System.getenv().getOrDefault("FEE_DB_URL", "jdbc:mysql://localhost:3306/college"),
        System.getenv().getOrDefault("FEE_DB_USER", "root"),
        System.getenv().getOrDefault("FEE_DB_PASSWORD", "")
    );

    public FeeWindow() {
        super("Student Fee Payment");
        setLayout(new BorderLayout(8, 8));
        JPanel form = new JPanel(new GridLayout(2, 2, 8, 8));
        form.add(new JLabel("Register number:")); form.add(regNo);
        form.add(new JLabel("Amount (INR):")); form.add(amount);
        JPanel actions = new JPanel(new FlowLayout());
        actions.add(pay); actions.add(clear); actions.add(receipt);
        add(form, BorderLayout.CENTER); add(actions, BorderLayout.NORTH); add(status, BorderLayout.SOUTH);

        pay.addActionListener(e -> payFee());
        clear.addActionListener(e -> { regNo.setText(""); amount.setText(""); status.setText("Cleared."); });
        receipt.addActionListener(e -> JOptionPane.showMessageDialog(this, lastReceipt, "Receipt", JOptionPane.INFORMATION_MESSAGE));
        addWindowListener(new WindowAdapter() {
            @Override public void windowClosing(WindowEvent e) { dispose(); }
        });
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        pack(); setLocationRelativeTo(null);
    }

    private void payFee() {
        pay.setEnabled(false);
        status.setText("Processing payment...");
        // JDBC work runs off the Swing event-dispatch thread.
        new SwingWorker<String, Void>() {
            @Override protected String doInBackground() throws Exception {
                return service.pay(regNo.getText(), amount.getText());
            }
            @Override protected void done() {
                pay.setEnabled(true);
                try {
                    lastReceipt = get();
                    status.setText(lastReceipt);
                    JOptionPane.showMessageDialog(FeeWindow.this, lastReceipt, "Payment", JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception ex) {
                    Throwable cause = ex.getCause() == null ? ex : ex.getCause();
                    status.setText("Payment failed: " + cause.getMessage());
                }
            }
        }.execute();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new FeeWindow().setVisible(true));
    }
}
/*
Database setup:
CREATE TABLE fees (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  reg_no VARCHAR(30) NOT NULL,
  amount DECIMAL(10,2) NOT NULL CHECK (amount > 0),
  paid_at TIMESTAMP NOT NULL
);
Add the MySQL Connector/J driver to the classpath and set FEE_DB_URL,
FEE_DB_USER and FEE_DB_PASSWORD environment variables before running.
*/