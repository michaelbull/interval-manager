package com.github.michaelbull.interval

import java.util.NavigableSet
import java.util.TreeSet

private val compareIntervalsByStart = compareBy<Interval> { (start, _) -> start }
private val INTERVAL_MIN = intArrayOf(INTERVAL_START_MIN, INTERVAL_END_INCLUSIVE_MIN)

class IntervalManager {

    private val intervals = sortedSetOf(compareIntervalsByStart)

    /**
     * Adds a new interval, merging with overlaps.
     */
    fun addInterval(interval: Interval) {
        checkInterval(interval)

        var (start, endInclusive) = interval

        if (intervals.isNotEmpty()) {
            val overlapWindow = intervals.subsetUpTo(endInclusive)
            val iterator = overlapWindow.iterator()

            while (iterator.hasNext()) {
                val (candidateStart, candidateEndInclusive) = iterator.next()

                if (candidateStart > endInclusive) {
                    /* the candidate starts after the new interval; no valid merge candidates remain */
                    break
                } else if (candidateEndInclusive >= start) {
                    /* the candidate ends either before or as we start, merge with it */

                    iterator.remove()

                    start = start.coerceAtMost(candidateStart)
                    endInclusive = endInclusive.coerceAtLeast(candidateEndInclusive)
                }
            }
        }

        intervals.add(intArrayOf(start, endInclusive))
    }

    /**
     * Returns the list of merged intervals, sorted by start time.
     */
    fun getIntervals(): List<Interval> {
        return intervals.map(Interval::clone)
    }

    private fun TreeSet<Interval>.subsetUpTo(endInclusive: Int): NavigableSet<Interval> {
        return inclusiveSubset(
            fromInclusive = INTERVAL_MIN,
            toInclusive = intArrayOf(endInclusive, INTERVAL_END_INCLUSIVE_MAX),
        )
    }

    private fun <E> TreeSet<E>.inclusiveSubset(fromInclusive: E, toInclusive: E): NavigableSet<E> {
        return subSet(
            /* fromElement = */ fromInclusive,
            /* fromInclusive = */ true,
            /* toElement = */ toInclusive,
            /* toInclusive = */ true,
        )
    }
}
