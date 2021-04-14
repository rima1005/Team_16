package team16.easytracker.database

import android.provider.BaseColumns

object Contracts {
    object Company : BaseColumns {
        const val TABLE_NAME = "companies"
        const val COL_ID = "id"
        const val COL_NAME = "name"
        const val COL_ADDRESS_ID = "address_id"
    }
}