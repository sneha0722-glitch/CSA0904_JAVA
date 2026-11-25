# Q2 – Playlist collections

**D. Sneha | 192311114**

Run: `javac PlaylistBenchmark.java` then `java PlaylistBenchmark`.

The benchmark compares ArrayList and LinkedList at 1,000, 10,000 and 50,000 elements. It records append, 100 front inserts, 100 middle inserts, 10,000 random gets and full iteration. Run it on your own laptop and save the printed CSV rows as your raw results.

| Operation | ArrayList | LinkedList |
|---|---:|---:|
| Append | Amortized O(1) | O(1) at tail |
| Insert/remove at front | O(n) | O(1) |
| Insert/remove middle by index | O(n) | O(n) to reach node |
| Random get | O(1) | O(n) |
| Iterate | O(n) | O(n) |
| Space | O(n), compact array | O(n), node links overhead |

For an Up Next playlist where frequent add/remove at both ends is required, use `ArrayDeque` (addFirst/addLast/removeFirst/removeLast). For indexed browsing, ArrayList is usually better. A plain array needs manual shifting for insert/remove, which is O(n).
