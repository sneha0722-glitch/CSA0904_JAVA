# Assignment 5 CO5 Report
Name: D. Sneha | Register: 192311114
GitHub: https://github.com/sneha0722-glitch/CSA0904_JAVA/tree/main/Assignment%205
Java version: ______ MySQL/JDBC version: ______

## Part A (25)
A1: text and binary record layouts; SIMATS pseudocode for add/find/list; damaged/missing/partial-write handling; RandomAccessFile comparison.
A2: 50 MiB read-call prediction; buffered redesign; when buffering does not help; readAllLines time/space.
A3: common DAO pattern; generic Dao<T,K>; StudentDao; field-to-column mapping; schema-change comparison.
A4: transient fields; object graph; serialVersionUID and phone field; CSV/JSON comparison.
A5: transfer transaction pseudocode; injection payload explanation; PreparedStatement; auto-commit failure analysis.

## Part B (25)
### B1 test table
| Test | Input | Expected | Actual |
|---|---|---|---|
| Normal | P1003 | record returned | |
| Missing | P9999 | null | |
| Damaged | wrong field count | skipped | |
| Bad amount | text amount | skipped | |
### B1 measurements
| Size | Unbuffered ms | Buffered ms |
|---|---:|---:|
| 1 MiB | | |
| 10 MiB | | |
| 50 MiB | | |
| Records | Find time ms |
| 1,000 | |
| 10,000 | |
| 100,000 | |
### B2 adversarial
Record unsafe/safe login results; wrong field count, nonnumeric amount and empty-line handling; explain each result.
### B3 JDBC
| Rows | Statement ms | Prepared ms | Batch ms |
|---|---:|---:|---:|
| 1,000 | | | |
| 10,000 | | | |
| 100,000 | | | |
Include 10-row SELECT and ResultSetMetaData; auto-commit vs transaction; forced failure and remaining row count.
### B4
Include migration output, rejected records, database product/driver/table metadata. Decide between files/serialization/database for 12 campuses and concurrent counters.

## Conclusion
Interpret actual performance, correctness, durability, security, concurrency and scalability. Attach genuine screenshots with a sentence stating the requirement each proves.