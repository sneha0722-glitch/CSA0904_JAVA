# Campus Smart-Parking System — Java OOP Assignment 1

**Student:** Dalli Sneha  
**Register Number:** 192311114  
**Course:** Java / Object-Oriented Programming  
**Repository:** sneha0722-glitch/CSA0904_JAVA

> Tasks 1–8 are based on assignment details shared in chat. The original remaining questions were not supplied, so Task 9 is a clearly labelled supplementary integrated implementation, not a claim about the missing assignment wording.

## Project structure
- Task01 — candidate classes, responsibilities and encapsulation
- Task02 — jagged arrays for multi-floor slots
- Task03 — first-fit allocation and release
- Task04 — constructor chaining, this and copy constructor
- Task05 — static ticket counter
- Task06 — inheritance hierarchy
- Task07 — overriding and runtime fee calculation
- Task08 — overloading and runtime polymorphism
- Supplementary/Task09 — integrated parking system with EV charging, tickets, fees and monthly passes

## Requirements
JDK 17 or later; no external libraries.

## Compile and run (Windows PowerShell)
From this folder, for example:
```powershell
javac .\Task01\Task01.java
java -cp .\Task01 Task01
```
Repeat with Task02 through Task08. For the integrated demo:
```powershell
javac .\Supplementary\Task09\CampusParkingSystem.java
java -cp .\Supplementary\Task09 CampusParkingSystem
```

## Compile and run (Linux/macOS)
```bash
javac Task01/Task01.java
java -cp Task01 Task01
javac Supplementary/Task09/CampusParkingSystem.java
java -cp Supplementary/Task09 CampusParkingSystem
```

## Task 3 expected output
```text
[0, 0]
[0, 1]
[1, 0]
null
true false false
[0, 1]
```

## Assumptions
- The integrated example uses 15 rupees/hour for two-wheelers, 40 for cars and 30 for EVs.
- Every fifth slot on each floor is an EV charging slot.
- EVs are assigned charging slots first, then standard slots; non-EVs use standard slots.
- Any started hour is charged as a full hour. Valid monthly-pass holders pay zero.
- The project is an educational in-memory model; it does not persist data to a database.

## Complexity
- First-fit allocation: O(S) worst case for S slots.
- Direct indexed release in Task03: O(1).
- Jagged-array storage: O(S).
- Ticket generation: O(1) per ticket.
