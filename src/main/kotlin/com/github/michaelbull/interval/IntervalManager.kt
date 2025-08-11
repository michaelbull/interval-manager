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
     * Removes an interval, splitting or trimming as needed.
     */
    fun removeInterval(interval: IntArray) {
        checkInterval(interval)

        val (start, endInclusive) = interval

        val removals = mutableListOf<Interval>()
        val additions = mutableListOf<Interval>()

        val intersectionWindow = intervals.subsetUpTo(endInclusive)

        for (candidate in intersectionWindow) {
            if (interval intersects candidate) {
                removals += candidate

                val (candidateStart, candidateEndInclusive) = candidate

                /* if the candidate starts before, add back the leading interval [candidateStart,start] */
                if (candidateStart < start) {
                    val head = intArrayOf(candidateStart, start)
                    additions += head
                }

                /* if the candidate ends after, add back the trailing interval [endInclusive,candidateEndInclusive] */
                if (candidateEndInclusive > endInclusive) {
                    val tail = intArrayOf(endInclusive, candidateEndInclusive)
                    additions += tail
                }
            }
        }

        intervals.removeAll(removals)
        intervals.addAll(additions)
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
