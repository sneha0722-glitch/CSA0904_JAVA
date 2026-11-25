# Programming in Java – Assignment 4 (CO4): Event-Driven GUI Design

**Student:** D. Sneha  
**Register Number:** 192311114  
**Java:** JDK 17+ (JDK 22 also suitable)

## Task folders
- Task01_FeePayment – diagnose poor event design; FeeWindow + FeeService.
- Task02_AppointmentDesk – event-driven outpatient appointment GUI and design notes.
- Task03_LibraryLayout – nested BorderLayout/FlowLayout search screen.
- Task04_DrawPad – completed mouseClicked TODO and persistent repaint.
- Task05_EventStyles – frame listener, named inner class, anonymous class and lambda converter.
- Task06_EventRegistration – Swing/JDBC event registration, SQL schema and paged loading.

## Compile/run
Open PowerShell in the required task folder. For a standalone Java file:
```powershell
javac FileName.java
java ClassName
```
Task 1: compile FeeService.java and FeeWindow.java together. Requires MySQL Connector/J and a fees table; configure FEE_DB_URL, FEE_DB_USER, FEE_DB_PASSWORD.
Task 2: `javac AppointmentDesk.java; java AppointmentDesk`
Task 3: `javac LibrarySearchScreen.java; java LibrarySearchScreen`
Task 4: `javac DrawPad.java; java DrawPad`
Task 5: compile and run Converter, FrameListenerConverter, InnerClassConverter or AnonymousConverter.
Task 6: execute Task06_EventRegistration/schema.sql, add MySQL Connector/J JAR, configure EVENT_DB_URL, EVENT_DB_USER and EVENT_DB_PASSWORD, then compile/run with the JAR on the classpath.

## Evidence checklist
Run every task on your laptop and add genuine screenshots:
- Task 3: three window sizes (small, medium, large).
- Task 4: after clicks and after minimise/restore.
- Task 5: valid conversion and invalid amount; repeat Enter-key test.
- Task 6: successful, invalid and duplicate registration; filter and CSV export.
Record actual Java version and test results. Do not invent screenshots or database timings.

## Important notes
Currency rates in Task 5 are illustrative. Task 2 uses a local simulated booking timer, not a hospital database. Task 3 uses sample search results. Task 6 is the JDBC/MySQL implementation. Configure database credentials using environment variables; do not commit passwords.
