package team16.easytracker.utils

import java.lang.StringBuilder

object CSVConverter {

    fun <T> serialize(data: Array<Array<T>>, delimiter: String = ","): String {
        return data.joinToString("\n") { row ->
            row.joinToString(delimiter) { "\"${it.toString()}\"" }
        }
    }

    inline fun <reified T> deserialize(csv: String, converter: (String) -> T): Array<Array<T>> {
        return arrayOf(arrayOf(converter(""))) // TODO: implementation
    }
}