# Java Assignment 5 – CO5
**Name:** D. Sneha  
**Register:** 192311114  
**Java:** JDK 17+ (installed JDK 22.0.2 is suitable)

## Folders
TaskA1_FileFormat – text/binary record layouts and damaged-data strategy.
TaskA2_BufferedIO – read-call analysis and buffering.
TaskA3_DAO – generic DAO and StudentDao.
TaskA4_Serialization – transient fields and versioning.
TaskA5_Transactions – JDBC transaction and injection analysis.
TaskB1_FileStore – completed PassStore TODO and buffered line counter.
TaskB2_Adversarial – unsafe vs PreparedStatement login demo.
TaskB3_JdbcBatch – schema and completed PassLoader TODOs.
TaskB4_Migration – migration validation and DatabaseMetaData.

## Run
In PowerShell, open the task folder. For B1:
javac PassStore.java
java PassStore
For line counter:
javac BufferedLineCounter.java
java BufferedLineCounter path-to-log.txt
B2/B3/B4 require MySQL Connector/J. Execute TaskB3_JdbcBatch/schema.sql first. Set TEST_DB_URL, TEST_DB_USER and TEST_DB_PASSWORD. Compile with:
javac -cp ".;mysql-connector-j.jar" *.java
Run LoginSecurityDemo, BatchBenchmark or PassMigration passes.csv with the same classpath.

## Evidence to collect locally
B1: normal/missing/damaged tests; timings at 1, 10 and 50 MiB; lookup at 1k–100k rows.
B2: unsafe and safe login outputs; wrong field count, nonnumeric amount and empty line.
B3: 10/1k/10k/100k correctness, column metadata, timings, auto-commit vs transaction and forced rollback.
B4: migration output, rejected-line report, DB product/driver/tables metadata.
Capture genuine screenshots and actual timings from your laptop; do not invent results. Do not commit DB passwords. The sample PassStore CSV is simplified and assumes no comma in names; production CSV needs quoting/parser support.