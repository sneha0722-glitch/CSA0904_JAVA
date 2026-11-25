import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.sql.*;
import java.util.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

/** Swing UI + SwingWorker; JDBC access is isolated in RegistrationService. */
public class EventRegistrationApp extends JFrame {
    private final JTextField studentId = new JTextField(10), studentName = new JTextField(12);
    private final JComboBox<EventItem> eventBox = new JComboBox<>();
    private final JComboBox<String> filterBox = new JComboBox<>(new String[]{"All","Cultural Fest","Tech Symposium","Sports Day"});
    private final DefaultTableModel model = new DefaultTableModel(
        new String[]{"ID","Student ID","Student Name","Event","Registered At"}, 0) {
        @Override public boolean isCellEditable(int r, int c) { return false; }
    };
    private final JTable table = new JTable(model);
    private final JLabel status = new JLabel("Ready.");
    private final RegistrationService service = new RegistrationService(
        System.getenv().getOrDefault("EVENT_DB_URL","jdbc:mysql://localhost:3306/campus_events"),
        System.getenv().getOrDefault("EVENT_DB_USER","root"),
        System.getenv().getOrDefault("EVENT_DB_PASSWORD",""));

    record EventItem(int id, String name) { public String toString() { return name; } }

    public EventRegistrationApp() {
        super("Campus Event Registration");
        setLayout(new BorderLayout(8,8));
        JMenuBar bar = new JMenuBar();
        JMenu file = new JMenu("File"); JMenuItem refresh = new JMenuItem("Refresh");
        JMenuItem export = new JMenuItem("Export CSV"); JMenuItem exit = new JMenuItem("Exit");
        file.add(refresh); file.add(export); file.addSeparator(); file.add(exit); bar.add(file); setJMenuBar(bar);

        JPanel register = new JPanel(new FlowLayout(FlowLayout.LEFT));
        register.setBorder(BorderFactory.createTitledBorder("Student Registration"));
        register.add(new JLabel("Student ID")); register.add(studentId);
        register.add(new JLabel("Name")); register.add(studentName);
        register.add(new JLabel("Event")); register.add(eventBox);
        JButton submit = new JButton("Register"); register.add(submit);

        JPanel filter = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filter.add(new JLabel("Filter event:")); filter.add(filterBox);
        JButton apply = new JButton("Apply Filter"); filter.add(apply);
        JPanel north = new JPanel(new BorderLayout()); north.add(register,BorderLayout.NORTH); north.add(filter,BorderLayout.SOUTH);
        add(north,BorderLayout.NORTH); add(new JScrollPane(table),BorderLayout.CENTER); add(status,BorderLayout.SOUTH);

        submit.addActionListener(e -> registerStudent());
        apply.addActionListener(e -> loadPage(0));
        refresh.addActionListener(e -> { loadEvents(); loadPage(0); });
        export.addActionListener(e -> exportCsv());
        exit.addActionListener(e -> dispose());
        filterBox.addActionListener(e -> loadPage(0));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(900,520); setLocationRelativeTo(null);
        loadEvents(); loadPage(0);
    }

    private void loadEvents() {
        new SwingWorker<java.util.List<EventItem>,Void>() {
            protected java.util.List<EventItem> doInBackground() throws Exception { return service.events(); }
            protected void done() { try { eventBox.removeAllItems(); for (EventItem e:get()) eventBox.addItem(e); }
                catch(Exception ex) { showError(ex); } }
        }.execute();
    }
    private void registerStudent() {
        String id=studentId.getText().trim(), name=studentName.getText().trim();
        EventItem event=(EventItem)eventBox.getSelectedItem();
        if(id.isBlank() || name.isBlank() || event==null) { status.setText("Student ID, name and event are required."); return; }
        setBusy(true,"Registering...");
        new SwingWorker<Void,Void>() {
            protected Void doInBackground() throws Exception { service.register(id,name,event.id()); return null; }
            protected void done() {
                setBusy(false,"");
                try { get(); status.setText("Registration successful."); loadPage(0); }
                catch(Exception ex) { Throwable c=ex.getCause()==null?ex:ex.getCause();
                    status.setText("Registration failed: " + c.getMessage()); }
            }
        }.execute();
    }
    private void loadPage(int offset) {
        String selected=(String)filterBox.getSelectedItem();
        setBusy(true,"Loading registrations...");
        new SwingWorker<java.util.List<Object[]>,Void>() {
            protected java.util.List<Object[]> doInBackground() throws Exception { return service.page(selected,50,offset); }
            protected void done() { setBusy(false,""); try {
                model.setRowCount(0); for(Object[] row:get()) model.addRow(row);
                status.setText("Showing up to 50 registrations.");
            } catch(Exception ex) { showError(ex); } }
        }.execute();
    }
    private void exportCsv() {
        JFileChooser chooser=new JFileChooser();
        if(chooser.showSaveDialog(this)!=JFileChooser.APPROVE_OPTION) return;
        File file=chooser.getSelectedFile();
        try(PrintWriter out=new PrintWriter(new FileWriter(file))) {
            for(int c=0;c<model.getColumnCount();c++){ if(c>0)out.print(","); out.print(model.getColumnName(c)); } out.println();
            for(int r=0;r<model.getRowCount();r++){ for(int c=0;c<model.getColumnCount();c++){
                if(c>0)out.print(","); out.print('"'+String.valueOf(model.getValueAt(r,c)).replace(""","""")+'"');
            } out.println(); }
            status.setText("Exported current page to " + file.getName());
        } catch(IOException ex) { showError(ex); }
    }
    private void setBusy(boolean busy,String message) { status.setText(message); }
    private void showError(Exception ex) { status.setText("Error: " + ex.getMessage()); }
    public static void main(String[] args) { SwingUtilities.invokeLater(() -> new EventRegistrationApp().setVisible(true)); }

    static class RegistrationService {
        private final String url,user,password;
        RegistrationService(String url,String user,String password){this.url=url;this.user=user;this.password=password;}
        private Connection connect() throws SQLException { return DriverManager.getConnection(url,user,password); }
        java.util.List<EventItem> events() throws SQLException {
            java.util.List<EventItem> list=new ArrayList<>();
            try(Connection c=connect(); Statement s=c.createStatement(); ResultSet rs=s.executeQuery("SELECT event_id,event_name FROM events ORDER BY event_name")){
                while(rs.next()) list.add(new EventItem(rs.getInt(1),rs.getString(2)));
            } return list;
        }
        void register(String id,String name,int eventId) throws SQLException {
            String sql="INSERT INTO registrations(student_id,student_name,event_id) VALUES(?,?,?)";
            try(Connection c=connect();PreparedStatement p=c.prepareStatement(sql)){
                p.setString(1,id);p.setString(2,name);p.setInt(3,eventId);p.executeUpdate();
            } // UNIQUE(student_id,event_id) prevents duplicates even under concurrent requests.
        }
        java.util.List<Object[]> page(String event,int limit,int offset) throws SQLException {
            java.util.List<Object[]> rows=new ArrayList<>();
            String sql="SELECT r.registration_id,r.student_id,r.student_name,e.event_name,r.registered_at " +
                "FROM registrations r JOIN events e ON e.event_id=r.event_id " +
                (event==null || event.equals("All") ? "" : "WHERE e.event_name=? ") +
                "ORDER BY r.registration_id DESC LIMIT ? OFFSET ?";
            try(Connection c=connect();PreparedStatement p=c.prepareStatement(sql)){
                int i=1;if(event!=null&&!event.equals("All"))p.setString(i++,event);
                p.setInt(i++,limit);p.setInt(i,offset);
                try(ResultSet rs=p.executeQuery()){while(rs.next())rows.add(new Object[]{
                    rs.getLong(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getTimestamp(5)});}
            } return rows;
        }
    }
}
/*
Setup: execute schema.sql, add MySQL Connector/J JAR to classpath, set
EVENT_DB_URL, EVENT_DB_USER, EVENT_DB_PASSWORD. Run:
javac -cp ".;mysql-connector-j.jar" EventRegistrationApp.java
java -cp ".;mysql-connector-j.jar" EventRegistrationApp
The app uses a unique database constraint to reject duplicate student/event
pairs and SwingWorker to keep JDBC calls off the EDT. The table loads one
page (50 rows) at a time; change offset in the loadPage call to add navigation.
For 100,000 rows, full load has O(N) transfer and memory; page load has O(P)
UI memory per page (P=50) and indexed/ordered SQL query cost depends on indexes.
*/