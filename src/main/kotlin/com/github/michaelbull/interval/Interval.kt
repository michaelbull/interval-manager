package com.github.michaelbull.interval

typealias Interval = IntArray

const val INTERVAL_SIZE = 2

const val INTERVAL_START_MIN = 0
const val INTERVAL_END_MIN = 0

const val INTERVAL_START_MAX = Int.MAX_VALUE
const val INTERVAL_END_MAX = Int.MAX_VALUE

fun checkInterval(interval: IntArray) {
    require(interval.size == INTERVAL_SIZE) {
        "interval.size must be exactly $INTERVAL_SIZE, but was ${interval.size}"
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
