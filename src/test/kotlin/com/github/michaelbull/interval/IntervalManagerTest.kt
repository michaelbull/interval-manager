package com.github.michaelbull.interval

import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class IntervalManagerTest {

    private lateinit var manager: IntervalManager

    @BeforeTest
    fun setUp() {
        manager = IntervalManager()
    }

    @Test
    fun `first example`() {
        manager.addInterval(intArrayOf(1, 3))
        manager.addInterval(intArrayOf(5, 7))
        manager.addInterval(intArrayOf(2, 6))

        val intervals = manager.getIntervals()

        assertEquals(
            expected = 1,
            actual = intervals.size,
        )

        assertContentEquals(
            expected = intArrayOf(1, 7),
            actual = intervals[0],
        )
    }

    @Test
    fun `second example`() {
        manager.addInterval(intArrayOf(1, 10))
        manager.removeInterval(intArrayOf(3, 5))

        val intervals = manager.getIntervals()

        assertEquals(
            expected = 2,
            actual = intervals.size,
        )

        assertContentEquals(
            expected = intArrayOf(1, 3),
            actual = intervals[0],
        )

        assertContentEquals(
            expected = intArrayOf(5, 10),
            actual = intervals[1],
        )
    }

    @Test
    fun `add non-overlapping interval`() {
        manager.addInterval(intArrayOf(1, 3))
        manager.addInterval(intArrayOf(5, 7))

        val intervals = manager.getIntervals()

        assertEquals(
            expected = 2,
            actual = intervals.size,
        )

        assertContentEquals(
            expected = intArrayOf(1, 3),
            actual = intervals[0],
        )

        assertContentEquals(
            expected = intArrayOf(5, 7),
            actual = intervals[1],
        )
    }

    @Test
    fun `add contiguous interval`() {
        manager.addInterval(intArrayOf(1, 10))
        manager.addInterval(intArrayOf(10, 20))

        val intervals = manager.getIntervals()

        assertEquals(
            expected = 1,
            actual = intervals.size,
        )

        assertContentEquals(
            expected = intArrayOf(1, 20),
            actual = intervals[0],
        )
    }

    @Test
    fun `add overlapping interval`() {
        manager.addInterval(intArrayOf(1, 15))
        manager.addInterval(intArrayOf(10, 30))

        val intervals = manager.getIntervals()

        assertEquals(
            expected = 1,
            actual = intervals.size,
        )

        assertContentEquals(
            expected = intArrayOf(1, 30),
            actual = intervals[0],
        )
    }

    @Test
    fun `add contained interval`() {
        manager.addInterval(intArrayOf(1, 15))
        manager.addInterval(intArrayOf(5, 10))

        val intervals = manager.getIntervals()

        assertEquals(
            expected = 1,
            actual = intervals.size,
        )

        assertContentEquals(
            expected = intArrayOf(1, 15),
            actual = intervals[0],
        )
    }

    @Test
    fun `add uncontained interval`() {
        manager.addInterval(intArrayOf(30, 40))
        manager.addInterval(intArrayOf(1, 100))

        val intervals = manager.getIntervals()

        assertEquals(
            expected = 1,
            actual = intervals.size,
        )

        assertContentEquals(
            expected = intArrayOf(1, 100),
            actual = intervals[0],
        )
    }

    @Test
    fun `add multiple intervals`() {
        manager.addInterval(intArrayOf(75, 100))
        manager.addInterval(intArrayOf(10, 50))
        manager.addInterval(intArrayOf(55, 56))

        val intervals = manager.getIntervals()

        assertEquals(
            expected = 3,
            actual = intervals.size,
        )

        assertContentEquals(
            expected = intArrayOf(10, 50),
            actual = intervals[0],
        )

        assertContentEquals(
            expected = intArrayOf(55, 56),
            actual = intervals[1],
        )

        assertContentEquals(
            expected = intArrayOf(75, 100),
            actual = intervals[2],
        )
    }

    @Test
    fun `remove contained interval`() {
        manager.addInterval(intArrayOf(1, 10))
        manager.removeInterval(intArrayOf(3, 5))

        val intervals = manager.getIntervals()

        assertEquals(
            expected = 2,
            actual = intervals.size,
        )

        assertContentEquals(
            expected = intArrayOf(1, 3),
            actual = intervals[0],
        )

        assertContentEquals(
            expected = intArrayOf(5, 10),
            actual = intervals[1],
        )
    }

    @Test
    fun `remove leading interval`() {
        manager.addInterval(intArrayOf(1, 10))
        manager.removeInterval(intArrayOf(0, 3))

        val intervals = manager.getIntervals()

        assertEquals(
            expected = 1,
            actual = intervals.size,
        )

        assertContentEquals(
            expected = intArrayOf(3, 10),
            actual = intervals[0],
        )
    }

    @Test
    fun `remove trailing interval`() {
        manager.addInterval(intArrayOf(1, 10))
        manager.removeInterval(intArrayOf(7, 12))

        val intervals = manager.getIntervals()

        assertEquals(
            expected = 1,
            actual = intervals.size,
        )

        assertContentEquals(
            expected = intArrayOf(1, 7),
            actual = intervals[0],
        )
    }

    @Test
    fun `remove uncontained interval`() {
        manager.addInterval(intArrayOf(3, 7))
        manager.removeInterval(intArrayOf(1, 10))

        val intervals = manager.getIntervals()

        assertTrue(intervals.isEmpty())
    }

    @Test
    fun `remove absent interval`() {
        manager.addInterval(intArrayOf(10, 20))
        manager.removeInterval(intArrayOf(1, 5))

        val intervals = manager.getIntervals()

        assertEquals(
            expected = 1,
            actual = intervals.size,
        )

        assertContentEquals(
            expected = intArrayOf(10, 20),
            actual = intervals[0],
        )
    }

    @Test
    fun `remove exact interval`() {
        manager.addInterval(intArrayOf(10, 15))
        manager.addInterval(intArrayOf(1, 5))
        manager.removeInterval(intArrayOf(1, 5))

        val intervals = manager.getIntervals()

        assertEquals(
            expected = 1,
            actual = intervals.size,
        )

        assertContentEquals(
            expected = intArrayOf(10, 15),
            actual = intervals[0],
        )
    }

    @Test
    fun `remove multiple intervals`() {
        manager.addInterval(intArrayOf(1, 3))
        manager.addInterval(intArrayOf(5, 7))
        manager.addInterval(intArrayOf(9, 11))
        manager.addInterval(intArrayOf(13, 15))
        manager.removeInterval(intArrayOf(2, 10))

        val intervals = manager.getIntervals()

        assertEquals(
            expected = 3,
            actual = intervals.size,
        )

        assertContentEquals(
            expected = intArrayOf(1, 2),
            actual = intervals[0],
        )

        assertContentEquals(
            expected = intArrayOf(10, 11),
            actual = intervals[1],
        )

        assertContentEquals(
            expected = intArrayOf(13, 15),
            actual = intervals[2],
        )
    }
}
