package org.nield.kotlinstatistics

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue


class DoubleStatisticsTest {

    val groups = sequenceOf("A", "B", "B", "C", "C")
    val doubleVector = sequenceOf(0.0, 1.0, 3.0, 5.0, 11.0)

    @Test
    fun sumBy() {
        val r = mapOf("A" to 0.0, "B" to 4.0, "C" to 16.0)

        assertEquals(groups.zip(doubleVector).sumBy(), r)

        groups.zip(doubleVector).sumBy(
            keySelector = { it.first },
            doubleSelector = { it.second }
        ).let { assertTrue(it["A"] == r["A"] && it["B"] == r["B"]) }
    }

    @Test
    fun averageBy() {
        val r = mapOf("A" to 0.0, "B" to 2.0, "C" to 8.0)

        groups.zip(doubleVector).averageBy(
            keySelector = { it.first },
            doubleSelector = { it.second }
        ).let { assertEquals(it, r) }
    }


    @Test
    fun binTest() {
        val binned = sequenceOf(
            doubleVector,
            doubleVector.map { it + 100.0 },
            doubleVector.map { it + 200.0 }
        ).flatMap { it }
            .zip(groups.repeat())
            .binByDouble(
                binSize = 100.0,
                valueSelector = { it.first },
                rangeStart = 0.0
            )

        assertEquals(binned.bins.size, 3)
        println(binned.bins)
        assertTrue(binned[5.0]!!.range.let { it.lowerBound == 0.0 && it.upperBound == 100.0 })
        assertTrue(binned[105.0]!!.range.let { it.lowerBound == 100.0 && it.upperBound == 200.0 })
        assertTrue(binned[205.0]!!.range.let { it.lowerBound == 200.0 && it.upperBound == 300.0 })
    }

    private fun <T> Sequence<T>.repeat(): Sequence<T> = sequence {
        while (true) yieldAll(this@repeat)
    }
}