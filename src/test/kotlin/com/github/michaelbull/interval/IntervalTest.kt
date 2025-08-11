package com.github.michaelbull.interval

import kotlin.test.Test
import kotlin.test.assertFailsWith

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
}
