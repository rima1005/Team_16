package team16.easytracker

import org.junit.Test

import org.junit.Assert.*
import team16.easytracker.utils.CSVConverter

class CSVTests {

    @Test
    fun serializeInts() {
        val sampleInts = arrayOf(
            arrayOf(1, 2, 3),
            arrayOf(4, 5, 6),
            arrayOf(7, 8, 9),
        )

        val sampleIntsCsv = "" +
                "\"1\",\"2\",\"3\"\n" +
                "\"4\",\"5\",\"6\"\n" +
                "\"7\",\"8\",\"9\""

        val resultCsv = CSVConverter.serialize(sampleInts)
        assert(resultCsv == sampleIntsCsv)
    }
}