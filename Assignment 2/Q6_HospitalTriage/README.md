# Q6 – Hospital triage PriorityQueue

**D. Sneha | 192311114**

Run: `javac TriageQueueDemo.java` then `java TriageQueueDemo`.

Lower severity number means higher urgency (1 is most severe). Comparator first orders by severity ascending and then by arrival counter ascending, so equal-severity patients are treated FIFO. The eight sample patients make the order easy to check manually.

PriorityQueue offer/poll are O(log n), peek is O(1), and storage is O(n). The included 100, 1,000 and 5,000 patient runs measure a complete offer-and-poll cycle. This is a local microbenchmark, not a hospital production load test.

Starvation can occur when urgent patients continually arrive. Aging policy: every 10 minutes waiting, improve effective severity by one level, capped at 1. For a 5,000-patient/day hospital, PriorityQueue is a reasonable baseline when severity-based next-patient selection matters. A sorted LinkedList has O(n) insertion and O(1) removal at head; five FIFO queues (one per severity) have O(1) enqueue/dequeue but need a policy for selecting the highest non-empty severity and aging between levels.
