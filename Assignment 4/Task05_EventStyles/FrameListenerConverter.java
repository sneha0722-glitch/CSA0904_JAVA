import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class FrameListenerConverter extends JFrame implements ActionListener {
    private final JTextField amount = new JTextField(10);
    private final JComboBox<String> target = new JComboBox<>(new String[]{"USD","EUR"});
    private final JLabel result = new JLabel(" ");
    private final JButton convert = new JButton("Convert");
    public FrameListenerConverter() {
        super("Converter - Frame Listener");
        setLayout(new FlowLayout());
        add(amount); add(target); add(convert); add(result);
        convert.addActionListener(this); amount.addActionListener(this);
        addWindowListener(new WindowAdapter() {
            @Override public void windowClosing(WindowEvent e) { dispose(); }
        });
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE); pack();
    }
    @Override public void actionPerformed(ActionEvent e) {
        try {
            double value = Double.parseDouble(amount.getText().trim());
            if (!Double.isFinite(value) || value < 0) throw new NumberFormatException();
            String code = (String)target.getSelectedItem();
            String message = String.format("%.2f %s", value * (code.equals("USD") ? .012 : .011), code);
            result.setText(message); JOptionPane.showMessageDialog(this, message);
        } catch (NumberFormatException ex) {
            result.setText("Invalid amount"); JOptionPane.showMessageDialog(this, "Invalid amount");
        }
    }
    public static void main(String[] args) { SwingUtilities.invokeLater(() -> new FrameListenerConverter().setVisible(true)); }
}