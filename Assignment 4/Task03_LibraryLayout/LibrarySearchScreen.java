import java.awt.*;
import javax.swing.*;

public class LibrarySearchScreen extends JFrame {
    private final JTextField search = new JTextField(22);
    private final JComboBox<String> category = new JComboBox<>(new String[]{"All","Book","Journal","Thesis"});
    private final DefaultListModel<String> resultsModel = new DefaultListModel<>();
    private final JList<String> results = new JList<>(resultsModel);
    private final JLabel status = new JLabel("Ready to search.");

    public LibrarySearchScreen() {
        super("Campus Library Search");
        setLayout(new BorderLayout(8,8));
        JLabel title = new JLabel("Campus Library", SwingConstants.CENTER);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 20f));
        add(title, BorderLayout.NORTH);

        JPanel searchRow = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchRow.add(new JLabel("Search:")); searchRow.add(search);
        searchRow.add(category); JButton find = new JButton("Search"); searchRow.add(find);

        JScrollPane resultScroll = new JScrollPane(results);
        JPanel center = new JPanel(new BorderLayout(5,5));
        center.add(searchRow, BorderLayout.NORTH);
        center.add(resultScroll, BorderLayout.CENTER);
        add(center, BorderLayout.CENTER);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton prev = new JButton("Prev"), next = new JButton("Next"), reserve = new JButton("Reserve");
        bottom.add(prev); bottom.add(next); bottom.add(reserve);
        JPanel south = new JPanel(new BorderLayout());
        south.add(bottom, BorderLayout.CENTER); south.add(status, BorderLayout.SOUTH);
        add(south, BorderLayout.SOUTH);

        find.addActionListener(e -> {
            resultsModel.clear();
            if (search.getText().isBlank()) { status.setText("Enter a search term."); return; }
            resultsModel.addElement(search.getText().trim() + " — sample result 1");
            resultsModel.addElement(search.getText().trim() + " — sample result 2");
            status.setText("2 sample results. Category: " + category.getSelectedItem());
        });
        prev.addActionListener(e -> status.setText("Previous page requested."));
        next.addActionListener(e -> status.setText("Next page requested."));
        reserve.addActionListener(e -> status.setText(results.getSelectedValue() == null
            ? "Select a result first." : "Reservation requested: " + results.getSelectedValue()));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(520,350)); setSize(720,480); setLocationRelativeTo(null);
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LibrarySearchScreen().setVisible(true));
    }
}
/*
Nested layout:
JFrame BorderLayout
  NORTH: title JLabel
  CENTER: JPanel BorderLayout
    NORTH: search JPanel FlowLayout (fixed preferred control sizes)
    CENTER: JScrollPane/JList (receives extra space)
  SOUTH: JPanel BorderLayout
    CENTER: button JPanel FlowLayout
    SOUTH: status JLabel
BorderLayout gives remaining space to CENTER; FlowLayout preserves natural
control sizes. Resize test: run at 520x350, 720x480, 1000x700 and capture
screenshots. The results scroll area should grow/shrink. GridBagLayout can
achieve this with weightx/weighty but requires more constraints; absolute
positioning does not adapt. Nested panels add O(1) containers; layout work is
approximately O(c) in the number of components for these simple managers.
This is a demonstration UI with sample results, not a library database client.
*/