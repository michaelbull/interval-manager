package com.github.michaelbull.interval

import kotlin.test.Test
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class IntervalTest {

    @Test
    fun `check an empty interval no entries`() {
        assertFailsWith<IllegalArgumentException> {
            checkInterval(intArrayOf())
        }
    }

    @Test
    fun `check an empty interval one entry`() {
        assertFailsWith<IllegalArgumentException> {
            checkInterval(intArrayOf(1))
        }
    }

    @Test
    fun `check an empty interval more than one entry`() {
        assertFailsWith<IllegalArgumentException> {
            checkInterval(intArrayOf(1, 2, 3))
        }
    }

    @Test
    fun `check an interval with a negative start time`() {
        assertFailsWith<IllegalArgumentException> {
            checkInterval(intArrayOf(-5, 8))
        }
    }

    @Test
    fun `check an interval with a negative end time`() {
        assertFailsWith<IllegalArgumentException> {
            checkInterval(intArrayOf(10, -18))
        }
    }

    @Test
    fun `check an interval with a negative progression`() {
        assertFailsWith<IllegalArgumentException> {
            checkInterval(intArrayOf(15, 14))
        }
    }

    @Test
    fun `check an interval with an empty progression`() {
        assertFailsWith<IllegalArgumentException> {
            checkInterval(intArrayOf(20, 20))
        }
    }

    @Test
    fun `intersects overlapping interval`() {
        assertTrue(intArrayOf(1, 5) intersects intArrayOf(3, 7))
    }

    @Test
    fun `intersects contained interval`() {
        assertTrue(intArrayOf(1, 10) intersects intArrayOf(3, 7))
    }

    @Test
    fun `intersects contiguous interval`() {
        assertFalse(intArrayOf(1, 5) intersects intArrayOf(5, 8))
    }

    @Test
    fun `intersects disjoint interval`() {
        assertFalse(intArrayOf(1, 4) intersects intArrayOf(5, 8))
    }
}
