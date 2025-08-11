# Interval Manager Tech Test

## Setup

1. Clone the repository

    ```bash
    git clone git@github.com:michaelbull/interval-manager.git
    ```

2. Assemble the library and run the tests using Gradle:

    Windows:
    ```bash
    gradlew.bat build
    ```

    Unix:
    ```bash
    ./gradlew build
    ```

To run the tests without reassembling the code, run `check` instead of `build`.

## Example

Below is a worked example, demonstrating the addition and removal of intervals.

Intervals are a tuple `IntArray`, with the elements being `start` and `endInclusive` respectively.

```kt
import com.github.michaelbull.interval.IntervalManager

fun main() {
    val manager = IntervalManager()
    manager.addInterval(intArrayOf(1, 3))
    manager.addInterval(intArrayOf(5, 7))
    manager.addInterval(intArrayOf(9, 11))
    manager.addInterval(intArrayOf(13, 15))
    manager.removeInterval(intArrayOf(2, 10))

    val intervals = manager.getIntervals()
    println("size: ${intervals.size}") // "size: 3"
    println("first: ${intervals[0].contentToString()}") // "first: [1, 2]"
    println("second: ${intervals[1].contentToString()}") // "second: [10, 11]"
    println("third: ${intervals[2].contentToString()}") // "third: [13, 15]"
}
```

## Implementation

The `IntervalManager` is backed by a `TreeSet<IntArray>`, sorted by each interval's start time.

This provides two key benefits:
1. The collection is always sorted.
2. Finding elements or ranges is extremely fast: `O(log N)`.

### Adding an interval

The `addInterval` method is designed to be highly efficient by minimizing the number of intervals it needs to inspect.

#### Strategy

1. **Search**
   - Instead of scanning all `N` intervals, we use the `TreeSet.subSet` method to create a candidate window.
   - This window contains only the intervals that could possibly overlap or be adjacent to the new interval.
   - This is an `O(log N)` operation.

2. **Iterate and Merge**
   - We loop through the candidate window.
   - For each candidate, we check for both overlaps and adjacency (i.e. intervals that touch at a single point).

3. **Combine**
   - All matching candidates are removed from the set and combined into a single new interval, which is then added back.

#### Performance

The resulting time complexity is `O(k * log N)`, where:
- `N` is the total number of intervals in the `TreeSet`.
- `k` is the number of intervals in the `TreeSet` that can be merged with the interval to add.

This is significantly faster than a simple list-based approach, which would be `O(N)`.

### Removing an interval

Removing an interval is a more complex operation that can result in intervals being trimmed, split in two, or deleted entirely.

#### Strategy

1. **Search**
   - Similar to adding, we use `TreeSet.subSet` to efficiently find a candidate window of all intervals that could possibly be affected by the removal.

2. **Iterate and Split**
   - We iterate through this candidate window.
   - For each existing interval that intersects (using a strict check that ignores mere adjacency), we calculate which intervals would remain after the subtraction.
   - An interval can result in zero, one, or two new intervals.

3. **Combine**
   - To avoid errors from modifying a collection while iterating over it (`ConcurrentModificationException`), we first collect all the original intervals that need to be removed and all the new intervals that need to be added.
   - After the loop finishes, we perform a single `removeAll` and a single `addAll` operation to update the set.

#### Performance

The resulting time complexity is `O(k * log N)`, where:
- `N` is the total number of intervals in the `TreeSet`.
- `k` is the number of intervals in the `TreeSet` that overlap with the interval to remove.

## Assumptions

- Intervals are presumed to be `0 <= start < end`, preventing the modelling of an empty interval which would noop in most operations
- Intervals are modelled as closed ranges, such that their start and end points are inclusive

## Edge Case Considerations

The design explicitly handles numerous edge cases:

1. **Overlapping Intervals**
   - The core logic for both adding and removing is built around these.

2. **Adjacent Intervals**
   - The distinction is handled by our two helper methods.
   - `addInterval` merges adjacent intervals (e.g., `[1,5]` and `[5,8]`), while `removeInterval` ignores them.

3. **Nested Intervals**
   - The logic correctly handles cases where one interval fully contains another.
   - During addition, there is no change.
   - During removal, the interval is split.

4. **Validation**
   - The `checkInterval` top-level function throws an `IllegalArgumentException` for invalid intervals, such as `[10, 1]` or `[-50,5]`.

## Next Steps

Various API improvements could occur, but were avoided to ensure that the provided specification was fulfilled:

- A data class should be used to represent the `Interval` instead of an `IntArray`.
  - This would ensure immutability and avoid the need to clone the list in `IntervalManager.getIntervals`, as users would not be able to arbitrarily change the values within the interval.
  - Similarly, it would remove the need to check if the `IntArray` is an exact tuple.
- A factory function for `Interval`s should perform the `checkInterval` internally.
  - This would remove the need for the `IntervalManager` to `checkInterval` in each function as it would already be validated.
  - Given it can throw an exception, this should occur in a factory function and not the constructor - the constructor could instead be made private.
- The constructor for `IntervalManager` could support seeding the initial `intervals`.
  - This would enable a unit test to provide an initial data set instead of having to manually seed it via the `addInterval` function.
