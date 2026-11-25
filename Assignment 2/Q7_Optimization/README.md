# Q7 – Optimization case study

**D. Sneha | 192311114**

## Selected bottleneck
Use Q1's repeated immutable String concatenation as the candidate bottleneck. It repeatedly copies the accumulated output, creating intermediate strings. Confirm the bottleneck using the three Q1 methods on the same JDK, machine and workload; attach the measured Q1 output before claiming a speedup.

## Objective
For the same ticket count and field values, reduce median ticket-generation time versus repeated String concatenation, while producing exactly the same output characters.

## Redesign
Use a pre-sized local StringBuilder per ticket. Estimate capacity from the fixed prefix and field lengths, append each field and delimiter, then convert once to String. This avoids sharing mutable state between booking threads.

Pseudocode:
1. Calculate capacity from prefix and all field lengths.
2. Create StringBuilder(capacity).
3. Append ticket ID.
4. For each field, append delimiter, field number and value.
5. Return builder.toString().

## Validation
Compile and run Q1 and this program using the same JDK and computer. Compare exact generated output and median timings over repeated runs. Record environment, warm-up, workload and raw results. Do not state a measured improvement until the local test confirms it.

## Trade-offs
The builder uses a mutable temporary buffer and capacity estimate; an inaccurate estimate may cause resizing. It is not thread-safe, so create one builder per ticket/thread rather than sharing it. The final String still occupies memory. The redesign improves allocation/copying behavior for incremental construction but should be retained only if measurements show a meaningful improvement.
