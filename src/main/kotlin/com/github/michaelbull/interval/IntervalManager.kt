package com.github.michaelbull.interval

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
}
