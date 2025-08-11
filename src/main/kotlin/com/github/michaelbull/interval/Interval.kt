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
        "interval.end must be non-negative, but was $start"
    }

    require(endInclusive > start) {
        "interval but be non-empty, but was [${start}..${endInclusive}]"
    }
}
