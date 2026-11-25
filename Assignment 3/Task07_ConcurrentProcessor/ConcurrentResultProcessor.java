import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.*;

public class ConcurrentResultProcessor {
    record Result(String file, boolean success, int score, String message) {}

    static Result process(Path file) {
        try {
            String raw = Files.readString(file).trim();
            int score = Integer.parseInt(raw);
            if (score < 0 || score > 100) throw new IllegalArgumentException("Score outside 0..100");
            return new Result(file.getFileName().toString(), true, score, "OK");
        } catch (IOException | IllegalArgumentException e) {
            return new Result(file.getFileName().toString(), false, 0, e.getMessage());
        }
    }

    static void createSampleFiles(Path dir, int total) throws IOException {
        Files.createDirectories(dir);
        for (int i = 1; i <= total; i++) {
            Path file = dir.resolve(String.format("score-%05d.txt", i));
            if (i % 1000 == 0) { Files.deleteIfExists(file); continue; } // missing-file test
            if (i % 997 == 0) Files.writeString(file, "not-a-number"); // corrupt-file test
            else Files.writeString(file, Integer.toString(i % 101));
        }
    }

    static void run(Path dir, int total, int poolSize) throws Exception {
        ExecutorService pool = Executors.newFixedThreadPool(poolSize);
        List<Future<Result>> futures = new ArrayList<>();
        long start = System.nanoTime();
        try {
            for (int i = 1; i <= total; i++) {
                Path file = dir.resolve(String.format("score-%05d.txt", i));
                futures.add(pool.submit(() -> process(file)));
            }
            int success = 0, failure = 0, sum = 0;
            List<String> errors = new ArrayList<>();
            for (Future<Result> future : futures) {
                try {
                    Result r = future.get();
                    if (r.success()) { success++; sum += r.score(); }
                    else { failure++; if (errors.size() < 5) errors.add(r.file() + ": " + r.message()); }
                } catch (ExecutionException e) {
                    failure++;
                    if (errors.size() < 5) errors.add("Task failure: " + e.getCause());
                }
            }
            long elapsedMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start);
            System.out.printf("Pool=%d Total=%d Success=%d Failure=%d ScoreSum=%d TimeMs=%d%n",
                    poolSize, total, success, failure, sum, elapsedMs);
            for (String error : errors) System.out.println("Sample error: " + error);
            if (success + failure != total) throw new IllegalStateException("Count mismatch");
        } finally {
            pool.shutdown();
            if (!pool.awaitTermination(1, TimeUnit.MINUTES)) pool.shutdownNow();
        }
    }

    public static void main(String[] args) throws Exception {
        int total = args.length > 0 ? Integer.parseInt(args[0]) : 10000;
        Path dir = Path.of("score-input");
        createSampleFiles(dir, total);
        for (int pool : new int[]{1, 2, 4, 8}) run(dir, total, pool);
        System.out.println("Small hand-check: scores 10, 20, 30 should sum to 60.");
    }
}
/*
Run: java ConcurrentResultProcessor [numberOfFiles]
The demo creates input files, intentionally omitting every 1000th file and
corrupting every 997th file. Each Callable task returns a Result; failures
are recorded per file, so one bad file does not stop the batch. Future.get()
collects results. Compare pool sizes 1, 2, 4, 8 using the printed elapsed
times on your own computer; do not copy timings from another machine.
Completeness check: success + failure must equal total. More threads may not
help when disk I/O is the bottleneck; scheduling and thread stacks add cost.
Memory is O(total tasks/results) in this simple demonstration.
*/