package com.github.michaelbull.interval

typealias Interval = IntArray

private const val INTERVAL_LENGTH = 2

private const val INTERVAL_START_MIN = 0
private const val INTERVAL_END_MIN = 0

private const val INTERVAL_START_MAX = Int.MAX_VALUE
private const val INTERVAL_END_MAX = Int.MAX_VALUE

class IntervalManager {

    /**
     * Adds a new interval. Automatically merge overlapping or adjacent intervals.
     */
    fun addInterval(interval: Interval) {
        checkInterval(interval)

        return TODO()
    }

    /**
     * Returns the list of merged intervals, sorted by start time.
     */
    fun getIntervals(): List<Interval> {
        return TODO()
    }

    private fun checkInterval(interval: IntArray) {
        require(interval.size == INTERVAL_LENGTH) {
            "interval.size must be exactly $INTERVAL_LENGTH, but was ${interval.size}"
        }

        val (start, end) = interval

        require(start >= INTERVAL_START_MIN) {
            "interval.start must be non-negative, but was $start"
        }

        require(end >= INTERVAL_END_MIN) {
            "interval.end must be non-negative, but was $start"
        }

        require(end > start) {
            "interval.end must be greater-than interval.start, but was start=${start} end=${end}"
        }
    }
}
