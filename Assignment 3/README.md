# Programming in Java – Assignment 3 (CO3)

**Student:** D. Sneha  
**Register Number:** 192311114  
**Java version:** Java 17 or later (tested syntax uses records and modern switch-free Java APIs)

## Tasks
1. ATM exception handling – checked/unchecked exceptions, multi-catch, finally.
2. User-defined exceptions – exception hierarchy, propagation and chaining.
3. Try-with-resources – sales file processing and safe cleanup.
4. Threads – Thread subclass, Runnable, start/join/lifecycle.
5. Synchronization – race-condition prevention in seat booking.
6. Deadlock and producer-consumer – lock ordering, wait/notifyAll.
7. Concurrent result processor – ExecutorService, Future, per-file failure handling and pool-size comparison.

## Compile and run (PowerShell)
Open the relevant task folder, then:
```powershell
javac FileName.java
java ClassName
```
For Task 4 Runnable mode: `java ScannerStationsDemo runnable`.
For Task 7: `java ConcurrentResultProcessor 10000`.

## Input notes
- Task 1: create `card.txt` containing a balance such as `5000`.
- Task 3: create `sales-input` with `storeA.txt`, `storeB.txt`, `storeC.txt`; one integer per line.
- Task 7 generates its own `score-input` sample dataset, including missing and corrupt files.

## Evidence and report
Run each program on your own laptop. Save genuine console screenshots in the corresponding task folder and record actual outputs/timings in `REPORT_TEMPLATE.md`. Do not invent benchmark timings. Add partner details and contribution records before submission.

## Commit plan
Keep one or more meaningful commits per task. This repository folder contains the individual task source files; add your own output screenshots after execution.
