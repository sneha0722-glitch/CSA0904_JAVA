import java.io.*;
import java.nio.file.*;
import java.util.*;

public class SalesReportDemo {
    static int readTotal(Path file) throws IOException {
        int total = 0;
        try (BufferedReader reader = Files.newBufferedReader(file)) {
            String line;
            while ((line = reader.readLine()) != null) {
                try { total += Integer.parseInt(line.trim()); }
                catch (NumberFormatException e) {
                    throw new IOException("Invalid sales value in " + file + ": " + line, e);
                }
            }
        }
        return total;
    }

    static void writeSummary(Path output, Map<String,Integer> totals) throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(output)) {
            for (var entry : totals.entrySet()) {
                writer.write(entry.getKey() + "," + entry.getValue());
                writer.newLine();
            }
        }
    }

    // Manual resource management for comparison: close is attempted in finally.
    static int readTotalManual(Path file) throws IOException {
        BufferedReader reader = null;
        try {
            reader = Files.newBufferedReader(file);
            int total = 0;
            String line;
            while ((line = reader.readLine()) != null) total += Integer.parseInt(line.trim());
            return total;
        } catch (NumberFormatException e) {
            throw new IOException("Invalid number in " + file, e);
        } finally {
            if (reader != null) {
                try { reader.close(); }
                catch (IOException closeError) { System.err.println("Close failed: " + closeError); }
            }
        }
    }

    public static void main(String[] args) {
        Path folder = Path.of(args.length > 0 ? args[0] : "sales-input");
        Path output = Path.of("sales-summary.csv");
        Map<String,Integer> totals = new LinkedHashMap<>();
        try {
            for (String store : List.of("storeA.txt", "storeB.txt", "storeC.txt")) {
                Path file = folder.resolve(store);
                totals.put(store, readTotal(file));
            }
            writeSummary(output, totals);
            System.out.println("Report written to " + output.toAbsolutePath());
            totals.forEach((k,v) -> System.out.println(k + " total = " + v));
        } catch (IOException e) {
            System.out.println("Report stopped safely: " + e.getMessage());
        }
    }
}
/*
Create sales-input/storeA.txt etc.; put one integer per line.
Try a missing file and a malformed line to test failure handling.
Try-with-resources closes resources even when an exception occurs;
multiple resources close in reverse declaration order. If close also fails,
the later close failure is recorded as a suppressed exception.
Processing time O(N) for N input lines; open file handles are bounded
by the resources opened together. TWR does not make output atomic: write
to a temporary file and move it into place for all-or-nothing reports.
*/