package com.github.michaelbull.interval

import kotlin.test.BeforeTest
import kotlin.test.Ignore
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class IntervalManagerTest {

    private lateinit var manager: IntervalManager

    @BeforeTest
    fun setUp() {
        manager = IntervalManager()
    }

    @Test
    fun `adding an empty interval no entries`() {
        assertFailsWith<IllegalArgumentException> {
            manager.addInterval(intArrayOf())
        }
    }

    @Test
    fun `adding an empty interval one entry`() {
        assertFailsWith<IllegalArgumentException> {
            manager.addInterval(intArrayOf(1))
        }
    }

    @Test
    fun `adding an empty interval more than one entry`() {
        assertFailsWith<IllegalArgumentException> {
            manager.addInterval(intArrayOf(1, 2, 3))
        }
    }

    @Test
    fun `adding an interval with a negative start time`() {
        assertFailsWith<IllegalArgumentException> {
            manager.addInterval(intArrayOf(-5, 8))
        }
    }

    @Test
    fun `adding an interval with a negative end time`() {
        assertFailsWith<IllegalArgumentException> {
            manager.addInterval(intArrayOf(10, -18))
        }
    }

    @Test
    fun `adding an interval with a negative progression`() {
        assertFailsWith<IllegalArgumentException> {
            manager.addInterval(intArrayOf(15, 14))
        }
    }

    @Test
    fun `adding an interval with an empty progression`() {
        assertFailsWith<IllegalArgumentException> {
            manager.addInterval(intArrayOf(20, 20))
        }
    }

    @Test
    @Ignore
    fun `first example`() {
        manager.addInterval(intArrayOf(1, 3))
        manager.addInterval(intArrayOf(5, 7))
        manager.addInterval(intArrayOf(2, 6))

        assertEquals(
            expected = listOf(intArrayOf(1, 7)),
            actual = manager.getIntervals(),
        )
    }
}
