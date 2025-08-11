package com.github.michaelbull.interval

typealias Interval = IntArray

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
        require(interval.size == 2) {
            "interval.size must be exactly 2, but was ${interval.size}"
        }

        val (start, end) = interval

        require(start >= 0) {
            "interval.start must be non-negative, but was $start"
        }

        require(end >= 0) {
            "interval.end must be non-negative, but was $start"
        }

        require(end > start) {
            "interval.end must be greater-than interval.start, but was start=${start} end=${end}"
        }
    }
}
