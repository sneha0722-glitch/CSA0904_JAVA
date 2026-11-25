import java.util.Locale;

public class StringComparison {
    private static volatile long blackHole;

    private static String buildWithString(int tickets, int fields) {
        String result = "";
        for (int t = 1; t <= tickets; t++) {
            result += "TICKET-" + t;
            for (int f = 1; f <= fields; f++) result += "|FIELD-" + f + "=" + (t + f);
            result += "\\n";
        }
        return result;
    }

    private static String buildWithStringBuilder(int tickets, int fields) {
        StringBuilder result = new StringBuilder(tickets * fields * 16);
        for (int t = 1; t <= tickets; t++) {
            result.append("TICKET-").append(t);
            for (int f = 1; f <= fields; f++)
                result.append("|FIELD-").append(f).append('=').append(t + f);
            result.append('\\n');
        }
        return result.toString();
    }

    private static String buildWithStringBuffer(int tickets, int fields) {
        StringBuffer result = new StringBuffer(tickets * fields * 16);
        for (int t = 1; t <= tickets; t++) {
            result.append("TICKET-").append(t);
            for (int f = 1; f <= fields; f++)
                result.append("|FIELD-").append(f).append('=').append(t + f);
            result.append('\\n');
        }
        return result.toString();
    }

    private static long measure(String method, int tickets, int fields, int repeats) {
        long[] times = new long[repeats];
        for (int i = 0; i < 2; i++) blackHole += run(method, tickets, fields).length();
        for (int i = 0; i < repeats; i++) {
            long start = System.nanoTime();
            String output = run(method, tickets, fields);
            times[i] = System.nanoTime() - start;
            blackHole += output.length();
        }
        java.util.Arrays.sort(times);
        return times[repeats / 2];
    }

    private static String run(String method, int tickets, int fields) {
        switch (method) {
            case "String": return buildWithString(tickets, fields);
            case "StringBuilder": return buildWithStringBuilder(tickets, fields);
            default: return buildWithStringBuffer(tickets, fields);
        }
    }

    public static void main(String[] args) {
        int[][] workloads = {{10, 5}, {10000, 50}, {1000, 20}};
        String[] methods = {"String", "StringBuilder", "StringBuffer"};
        System.out.println("Workload,Method,MedianTimeNs,OutputChars");
        for (int[] w : workloads) {
            for (String method : methods) {
                long time = measure(method, w[0], w[1], 5);
                int chars = run(method, w[0], w[1]).length();
                System.out.printf(Locale.US, "%d tickets x %d fields,%s,%d,%d%n",
                        w[0], w[1], method, time, chars);
            }
        }
        System.out.println("Checksum=" + blackHole);
    }
}
