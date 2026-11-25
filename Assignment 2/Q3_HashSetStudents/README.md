# Q3 – HashSet and Student equality

**D. Sneha | 192311114**

Run: `javac StudentSetDemo.java` then `java StudentSetDemo`.

The Student identity is the student ID. Therefore equals compares IDs and hashCode returns the same ID hash. The contract is: equal objects must have equal hash codes. Unequal objects may share a hash code; HashSet handles collisions using equals.

If equals is overridden but hashCode is not, two equal students may enter different buckets and appear as duplicates. A small test can accidentally pass when the default identity hash happens to land in a compatible bucket, so test multiple IDs and repeated inserts. Never mutate fields used by equals/hashCode while an object is a HashSet/HashMap key: the object may become unreachable in its old bucket.

TreeSet uses its comparator (or Comparable) to decide whether values are duplicates: compare(a,b)==0 means same set element. Use a consistent comparator. HashMap keyed by immutable student ID is a straightforward alternative.

Expected average HashSet add/search/remove O(1), worst case O(n); TreeSet operations O(log n).
