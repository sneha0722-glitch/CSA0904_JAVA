import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import javax.swing.*;

public class Converter extends JFrame {
    private final JTextField amount = new JTextField(10);
    private final JComboBox<String> target = new JComboBox<>(new String[]{"USD", "EUR"});
    private final JLabel result = new JLabel(" ");
    private static double rate(String currency) {
        return currency.equals("USD") ? 0.012 : 0.011;
    }
    public Converter() {
        super("Currency Converter");
        setLayout(new FlowLayout());
        JButton convertButton = new JButton("Convert");
        add(new JLabel("INR")); add(amount); add(target); add(convertButton); add(result);
        // TODO 1 completed: one shared lambda listener for button and Enter key.
        ActionListener convertAction = e -> convert();
        convertButton.addActionListener(convertAction);
        amount.addActionListener(convertAction);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        pack(); setLocationRelativeTo(null);
    }
    private void convert() {
        // TODO 2 completed: parse, format to two decimals, or report invalid input.
        try {
            double inr = Double.parseDouble(amount.getText().trim());
            if (!Double.isFinite(inr) || inr < 0) throw new NumberFormatException();
            String currency = (String) target.getSelectedItem();
            String message = String.format("%.2f %s", inr * rate(currency), currency);
            result.setText(message);
            JOptionPane.showMessageDialog(this, message, "Conversion", JOptionPane.INFORMATION_MESSAGE);
        } catch (NumberFormatException ex) {
            result.setText("Invalid amount");
            JOptionPane.showMessageDialog(this, "Invalid amount", "Input error", JOptionPane.ERROR_MESSAGE);
        }
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Converter().setVisible(true));
    }
}
/*
For method reference: ActionListener action = e -> convert(); can be replaced
by e -> convert(), but this::convert needs convert(ActionEvent) signature.
A shared Action object (AbstractAction) can be installed on button/menu item and
bound to a keyboard shortcut. One shared handler avoids duplicated logic.
Rates are illustrative only, not live FX rates.
*/