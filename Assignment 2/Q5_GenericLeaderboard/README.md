# Q5 – Generic leaderboard and Comparator

**D. Sneha | 192311114**

Run: `javac LeaderboardDemo.java` then `java LeaderboardDemo`.

Leaderboard<T> stores any type and receives its ordering as Comparator<? super T>. Team ranking is points descending, goal difference descending, then name ascending. Alpha and Zebra test the final name tie-breaker.

A comparator must be antisymmetric, transitive and consistent in its equality decisions. The faulty comparator shown never returns zero, even for equal point values; it fails to express ties and can violate the comparator contract. Correct comparators should use comparing/thenComparing as above.

Comparable defines a class's natural/default order; Comparator defines external or multiple alternative orders. Sorting n elements is O(n log n); a stable sort preserves the previous relative order of elements that compare equal. Multiple stable sorts from least-significant criterion to most-significant criterion are possible, but comparator chaining is clearer and needs one sort.
