# Assignment 4 – CO4 Report

**Name:** D. Sneha  
**Register Number:** 192311114  
**GitHub:** https://github.com/sneha0722-glitch/CSA0904_JAVA/tree/main/Assignment%204  
**Java version:** __________________  
**Submission date:** __________________

## Task 1 – GUI design review (6)
### 1.1 Deficiencies
1. UI, validation, JDBC and business logic are mixed in one listener.
2. String action command uses == instead of equals; identity comparison is not content comparison.
3. Invalid/empty/non-numeric amounts are not validated.
4. JDBC and Thread.sleep run on the event-dispatch thread; the window freezes.
5. No useful success/failure feedback and no receipt implementation.
6. SQL concatenation permits SQL injection and malformed values.
7. Connection/statement are not closed; checked exceptions are not handled.
8. No reliable window-close handling.
### 1.2 Repair
FeeWindow collects fields and displays feedback; FeeService validates and executes a parameterized PreparedStatement. SwingWorker performs database work off the EDT. Resources use try-with-resources.
### 1.3 Defence (under 100 words)
Separating the window from FeeService makes each class have one responsibility. The service can be tested without opening a GUI, while the window can be tested with a fake service. Parameterized SQL, validation and try-with-resources reduce security and resource errors. SwingWorker keeps the interface responsive during database work. Smaller classes are easier to modify and debug.

Alternative: one listener per button or Swing Action avoids a chain of up to k comparisons; dispatch is O(1) after source registration, with O(k) listener/action storage. A command chain is O(k) worst case and O(1) extra handler storage.

## Task 2 – Appointment desk (8)
### Event table
| Source | Event | Listener | Action |
|---|---|---|---|
| Department JComboBox | ActionEvent | ActionListener | Refresh doctor model |
| Book JButton | ActionEvent | ActionListener | Validate and book |
| Window close | WindowEvent | WindowAdapter | Confirm if booking in progress |
| Slot JComboBox | ActionEvent | ActionListener | Update selected slot / dependent availability |
### Component hierarchy
JFrame(BorderLayout) -> form JPanel(GridLayout 4x2) -> patient JTextField, department/doctor/slot JComboBox; Book JButton SOUTH; status JLabel NORTH.
### Pseudocode
RefreshDoctors: clear doctor list; get selected department; retrieve matching doctor collection from map; add each doctor; select first if present.
Book: validate patient ID and selections; if invalid display error and return; set bookingInProgress; invoke booking service; on completion show confirmation or error and clear flag.
### Event trace
Construct frame -> initialise components -> register listeners -> load doctor model -> setVisible. Department selection -> ActionEvent -> refreshDoctors. Book -> ActionEvent -> validate -> booking -> feedback. Close -> WINDOW_CLOSING -> windowClosing -> confirm if busy -> dispose -> WINDOW_CLOSED.
Applet comparison: init/start/paint/stop/destroy are container-controlled lifecycle callbacks; JFrame is constructed and reacts to events, then disposed.
Cached Map: average O(1) department lookup plus O(k) list population; O(d*m) storage. Database query: each change adds network/database latency; less cached memory but repeated I/O.

## Task 3 – Library layout (9)
Use JFrame BorderLayout; title NORTH; center panel BorderLayout with search FlowLayout NORTH and JScrollPane CENTER; SOUTH panel with button FlowLayout CENTER and status SOUTH. CENTER receives remaining size; controls retain preferred sizes. Test sizes 520x350, 720x480, 1000x700 and attach screenshots. GridBagLayout is flexible but constraint-heavy; absolute positioning fails to adapt. Layout work approximately O(c) for c components; nested design uses a few extra panel objects.

## Task 4 – DrawPad forensics (7)
Original defect: mouseclicked has incorrect capitalization; it does not override mouseClicked, so callback is never invoked. The label is never changed. Direct getGraphics drawing is temporary; minimising/restoring triggers repaint and clears unretained pixels. Store Points and redraw them in paint. @Override makes a misspelled method a compile-time error. Point list: O(n) repaint work and O(n) memory. BufferedImage: image copy per repaint and O(width*height) pixel memory.

## Task 5 – Event styles (7)
Run four converter implementations: frame implements ActionListener, named inner class, anonymous class and lambda. Add Enter-key listener and dialog feedback in each. Record code changes and compare:
- Frame listener: simple for tiny window, but couples frame to handler and is harder to reuse.
- Named inner class: readable and reusable within outer class; accesses outer fields.
- Anonymous class: concise for one-off handlers, but verbose.
- Lambda: shortest for a single-method interface; best for simple actions.
For large applications prefer named actions/handlers or shared AbstractAction for reuse, menu items and keyboard shortcuts. Method reference requires a compatible method signature, e.g. action event parameter. A shared Action object can be attached to multiple triggers.

## Task 6 – Campus event registration (13)
### Requirements and testable rules
Student can enter ID/name and select event; committee can view/filter/export; invalid fields are rejected; same student cannot register twice for one event; GUI remains responsive during JDBC work.
### Screens and flow
Registration form + registrations JTable + event filter + File menu (Refresh, Export CSV, Exit). Register -> validate -> SwingWorker -> service -> DAO/JDBC -> DB unique constraint -> success/error dialog/status. Filter -> SwingWorker page query -> table model. Export -> file chooser -> CSV.
### Layers
EventRegistrationApp (view/controller) -> RegistrationService (business rules) -> JDBC repository methods -> MySQL events/registrations tables. Database unique key on (student_id,event_id) is the final duplicate guard.
### Pseudocode
Register: validate ID/name/event; disable button; background service inserts with PreparedStatement; handle duplicate SQL exception; re-enable controls; refresh table.
Filter: read event filter; execute parameterized SELECT with LIMIT/OFFSET in SwingWorker; on EDT replace table rows.
### Complexity and comparison
Loading all N=100,000 rows transfers and stores O(N) rows and can block/freeze the EDT if done synchronously. Paging P rows transfers/stores O(P) UI rows per page and keeps JDBC off EDT using SwingWorker; database index/query plan affects query cost. Attach actual timings from your laptop.
### Test evidence
| Test | Input | Expected | Actual |
|---|---|---|---|
| Normal | valid student and event | one row inserted | |
| Invalid | blank ID or name | validation message | |
| Duplicate | same student/event twice | second rejected by unique key | |
| Filter | select one event | only matching rows | |
| Export | choose CSV path | current page exported | |

## Screenshots
Paste genuine screenshots and explain which requirement each one proves.

## Conclusion
Summarise event delegation, layouts, repainting, listener styles, SwingWorker, JDBC parameterization and duplicate prevention.
