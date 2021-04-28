package team16.easytracker.model

import android.content.Context
import team16.easytracker.database.DbHelper

class Address(
    private val id: Int,
    val street: String,
    val zipCode: String,
    val city: String
) {
    companion object {
        fun load(id: Int, dbHelper: DbHelper): Address? {
            return DbHelper.loadAddress(id)
        }

        fun save(street: String, zipCode: String, city: String, dbHelper: DbHelper): Int {
            return DbHelper.saveAddress(street, zipCode, city)
        }
    }
}