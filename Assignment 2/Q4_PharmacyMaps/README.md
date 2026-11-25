# Q4 – Pharmacy inventory maps

**D. Sneha | 192311114**

Run: `javac PharmacyMapBenchmark.java` then `java PharmacyMapBenchmark`.

The benchmark builds 1k, 10k and 50k inventory records, performs 100,000 lookups and measures ordered report iteration. Record your own output in the report.

- HashMap: average O(1) put/get/remove; no iteration-order guarantee.
- TreeMap: O(log n) operations; keys remain sorted.
- LinkedHashMap: average O(1) operations; preserves insertion order.
- Hashtable is a legacy synchronized Map; it disallows null keys/values. Prefer modern alternatives such as ConcurrentHashMap when concurrent access is required.

Choose based on a stated requirement: for example, require 100k lookups to complete within the application's measured response-time budget and require sorted reports. If sorted reports are essential, TreeMap is simplest; otherwise HashMap plus sorting only for report generation may be faster. A sorted array with Arrays.binarySearch gives O(log n) search but O(n) insertion/removal. Memory depends on JVM and map implementation.
