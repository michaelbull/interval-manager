package com.github.michaelbull.interval

import java.util.NavigableSet
import java.util.TreeSet

private val compareIntervalsByStart = compareBy(Interval::first)
private val INTERVAL_MIN = intArrayOf(INTERVAL_START_MIN, INTERVAL_END_INCLUSIVE_MIN)

class IntervalManager {

    private val intervals = sortedSetOf(compareIntervalsByStart)

    /**
     * Adds a new interval, merging with overlaps.
     *
     * This function operates at a time complexity of **O(k * log N)**, where:
     * - `N` is the size of the [intervals] set.
     * - `k` is the number of [intervals][Interval] in the set that can be merged with the [interval to add][interval].
     *
     * This efficiency is achieved by using the backing [TreeSet]s fast lookups (log N) to only operate on the `k`
     * intervals that are actually affected.
     *
     * @param interval The [Interval] to add.
     */
    fun addInterval(interval: Interval) {
        checkInterval(interval)

        var (start, endInclusive) = interval

        if (intervals.isNotEmpty()) {
            val overlapWindow = intervals.subSetUpTo(endInclusive)
            val iterator = overlapWindow.iterator()

            while (iterator.hasNext()) {
                val (candidateStart, candidateEndInclusive) = iterator.next()

                if (candidateStart > endInclusive) {
                    /* the candidate starts after the new interval; no valid merge candidates remain */
                    break
                } else if (candidateEndInclusive >= start) {
                    /* the candidate ends either before or as we start, merge with it */

                    iterator.remove() // O(log N)

                    start = start.coerceAtMost(candidateStart)
                    endInclusive = endInclusive.coerceAtLeast(candidateEndInclusive)
                }
            }
        }

        intervals.add(intArrayOf(start, endInclusive)) // O(1)
    }

    /**
     * Removes a range from existing intervals, splitting or trimming as needed.
     *
     * This function operates at a time complexity of **O(k * log N)**, where:
     * - `N` is the size of the [intervals] set.
     * - `k` is the number of [intervals][Interval] in the set that overlap with [interval to remove][interval].
     *
     * It efficiently finds and processes only the `k` affected intervals using the [TreeSet]'s logarithmic time
     * complexity for searches, additions, and removals.
     *
     * @param interval The [Interval] to remove.
     */
    fun removeInterval(interval: IntArray) {
        checkInterval(interval)

        val (start, endInclusive) = interval

        val removals = mutableListOf<Interval>()
        val additions = mutableListOf<Interval>()

        val intersectionWindow = intervals.subSetUpTo(endInclusive)

        for (candidate in intersectionWindow) {
            if (interval intersects candidate) {
                removals += candidate // O(1)

                val (candidateStart, candidateEndInclusive) = candidate

                /* if the candidate starts before, add back the leading interval [candidateStart,start] */
                if (candidateStart < start) {
                    val head = intArrayOf(candidateStart, start)
                    additions += head // O(1)
                }

                /* if the candidate ends after, add back the trailing interval [endInclusive,candidateEndInclusive] */
                if (candidateEndInclusive > endInclusive) {
                    val tail = intArrayOf(endInclusive, candidateEndInclusive)
                    additions += tail // O(1)
                }
            }
        }

        /*
         * removing a Set ensures that the sets hash-based structure
         * is used to check for presence in the intervals TreeSet,
         * as opposed to removing a List which will scan and compare
         * each element in the list one-by-one
         *
         * the creation of the set is O(k) where k is the size of the removals,
         * meaning that the removeAll beneath becomes O(k * log N), result in a
         * totalling complexity of O(k) + O(k * log N) for removing
         */
        val removalsSet = removals.toSet() // O(k)
        intervals.removeAll(removalsSet) // O(k * log N)

        intervals.addAll(additions) // 2k * O(log N) worst case, depending on whether each interval is split into two
    }

    /**
     * Returns a list of merged intervals, sorted by start time.
     */
    fun getIntervals(): List<Interval> {
        return intervals.map(Interval::clone)
    }

    /**
     * Returns a [subSet][TreeSet.subSet] of [Intervals][Interval] in [this] [TreeSet] up to a starting time of
     * [endInclusive].
     *
     * This function operates at a time complexity of **O(log N)**, where `N` is the [size][TreeSet.size] of [this].
     */
    private fun TreeSet<Interval>.subSetUpTo(endInclusive: Int): NavigableSet<Interval> {
        return inclusiveSubSet(
            fromInclusive = INTERVAL_MIN,
            toInclusive = intArrayOf(endInclusive, INTERVAL_END_INCLUSIVE_MAX),
        )
    }

    private fun <E> TreeSet<E>.inclusiveSubSet(fromInclusive: E, toInclusive: E): NavigableSet<E> {
        return subSet(
            /* fromElement = */ fromInclusive,
            /* fromInclusive = */ true,
            /* toElement = */ toInclusive,
            /* toInclusive = */ true,
        )
    }
}
