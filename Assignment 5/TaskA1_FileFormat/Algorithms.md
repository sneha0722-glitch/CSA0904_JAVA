# A1.2 – Algorithms (SIMATS-style pseudocode)

**Algorithm: ADD_PASS**
Input: pass fields, file path
Output: success or validation error
1. START
2. VALIDATE pass ID, register number, name, route, stop, validity date and amount.
3. IF any field is invalid, DISPLAY validation error and RETURN.
4. OPEN file using BufferedWriter in CREATE and APPEND mode.
5. WRITE the encoded pass record and a line separator.
6. CLOSE the writer using structured resource handling.
7. DISPLAY "Pass added successfully".
8. STOP

**Algorithm: FIND_PASS**
Input: pass ID, file path
Output: matching pass or NOT_FOUND
1. START
2. IF file does not exist, RETURN NOT_FOUND.
3. OPEN BufferedReader for the file.
4. WHILE a line can be read:
   4.1 PARSE the line into fields.
   4.2 IF field count or field validation is invalid, RECORD damaged line and CONTINUE.
   4.3 IF record pass ID equals requested ID, CLOSE reader and RETURN record.
5. CLOSE reader.
6. RETURN NOT_FOUND.
7. STOP

**Algorithm: LIST_BY_ROUTE**
Input: route number, file path
Output: all valid pass records for that route
1. START
2. CREATE an empty result collection.
3. IF file does not exist, RETURN the empty collection.
4. OPEN BufferedReader.
5. WHILE a line can be read:
   5.1 PARSE and VALIDATE the line.
   5.2 IF damaged, RECORD line number and CONTINUE.
   5.3 IF record route equals requested route, ADD record to result.
6. CLOSE reader.
7. DISPLAY/RETURN result.
8. STOP

The BufferedReader/Writer operations are selected for efficient character-oriented
text processing. Binary DataInputStream/DataOutputStream would be selected for
the binary layout instead.