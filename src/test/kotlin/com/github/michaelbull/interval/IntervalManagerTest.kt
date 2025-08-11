package com.github.michaelbull.interval

import kotlin.test.BeforeTest
import kotlin.test.Ignore
import kotlin.test.Test
import kotlin.test.assertEquals

class IntervalManagerTest {

    private lateinit var manager: IntervalManager

    @BeforeTest
    fun setUp() {
        manager = IntervalManager()
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
