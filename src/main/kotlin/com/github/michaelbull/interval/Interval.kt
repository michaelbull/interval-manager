package com.github.michaelbull.interval

typealias Interval = IntArray

const val INTERVAL_SIZE = 2

const val INTERVAL_START_MIN = 0
const val INTERVAL_END_INCLUSIVE_MIN = 0

const val INTERVAL_START_MAX = Int.MAX_VALUE
const val INTERVAL_END_INCLUSIVE_MAX = Int.MAX_VALUE

fun checkInterval(interval: IntArray) {
    require(interval.size == INTERVAL_SIZE) {
        "interval.size must be exactly $INTERVAL_SIZE, but was ${interval.size}"
    }

    val (start, endInclusive) = interval

    require(start >= INTERVAL_START_MIN) {
        "interval.start must be non-negative, but was $start"
    }

    require(endInclusive >= INTERVAL_END_INCLUSIVE_MIN) {
        "interval.endInclusive must be non-negative, but was $endInclusive"
    }

    require(endInclusive > start) {
        "interval must be non-empty, but was [${start}..${endInclusive}]"
    }
}

/**
 * Returns `true` if two [Intervals][Interval] have a non-zero-length overlap.
 */
infix fun Interval.intersects(other: Interval): Boolean {
    checkInterval(this)
    checkInterval(other)
    return this[0] < other[1] && this[1] > other[0]
}
