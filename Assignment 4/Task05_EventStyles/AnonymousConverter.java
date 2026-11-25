import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class AnonymousConverter extends JFrame {
    private final JTextField amount = new JTextField(10);
    private final JComboBox<String> target = new JComboBox<>(new String[]{"USD","EUR"});
    private final JLabel result = new JLabel(" ");
    public AnonymousConverter() {
        super("Converter - Anonymous Listener");
        setLayout(new FlowLayout());
        JButton convert = new JButton("Convert");
        add(amount); add(target); add(convert); add(result);
        ActionListener handler = new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                try {
                    double value = Double.parseDouble(amount.getText().trim());
                    if (!Double.isFinite(value) || value < 0) throw new NumberFormatException();
                    String code = (String)target.getSelectedItem();
                    String message = String.format("%.2f %s", value * (code.equals("USD") ? .012 : .011), code);
                    result.setText(message); JOptionPane.showMessageDialog(AnonymousConverter.this, message);
                } catch (NumberFormatException ex) {
                    result.setText("Invalid amount"); JOptionPane.showMessageDialog(AnonymousConverter.this, "Invalid amount");
                }
            }
        };
        convert.addActionListener(handler); amount.addActionListener(handler);
        addWindowListener(new WindowAdapter() {
            @Override public void windowClosing(WindowEvent e) { dispose(); }
        });
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE); pack();
    }
    public static void main(String[] args) { SwingUtilities.invokeLater(() -> new AnonymousConverter().setVisible(true)); }
}