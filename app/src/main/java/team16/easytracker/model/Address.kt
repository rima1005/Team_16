package team16.easytracker.model

import android.content.Context
import team16.easytracker.database.DbHelper

class Address(
    private val id: Int,
    val street: String,
    val zipCode: String,
    val city: String
) {

    fun getId() : Int
    {
        return id;
    }
}