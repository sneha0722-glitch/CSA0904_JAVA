# Q1 – String, StringBuilder and StringBuffer

**Student:** D. Sneha  
**Register number:** 192311114

## Run (Windows PowerShell)
```powershell
javac StringComparison.java
java StringComparison
```

The program tests a short workload, the required 10,000-ticket × 50-field workload, and an additional 1,000-ticket × 20-field workload. It performs two warm-up runs and reports the median of five measured runs using `System.nanoTime()`.

Copy the CSV-like output into your report. Timings depend on CPU, JDK, background load and garbage collection; do not copy another computer's timings as your own.

## Analysis
- Repeated immutable `String` concatenation creates intermediate strings and can approach quadratic copying as the result grows.
- `StringBuilder` is mutable and unsynchronized; it is the normal choice for a local ticket being assembled by one thread.
- `StringBuffer` synchronizes its methods. It can be useful when the same mutable buffer is shared, but synchronizing individual calls does not make a multi-step ticket transaction atomic.
- For a shared booking log, prefer a thread-safe logging/queue design or synchronize the complete logical operation rather than sharing one builder.

Alternatives: `String.join`, `StringJoiner` for delimiter-separated fields, and `String.format` for formatted output (formatting is usually more expensive in tight loops).

Expected complexity: builder/buffer appends are amortized O(total output characters); repeated immutable concatenation can require repeated copying and approach O(n²) for many incremental appends. Memory use is workload- and JVM-dependent; measure with a profiler for reliable allocation data. A simple heap before/after reading is only an approximation.
