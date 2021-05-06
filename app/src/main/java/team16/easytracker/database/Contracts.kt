package team16.easytracker.database

import android.provider.BaseColumns

object Contracts {
    object Company : BaseColumns {
        const val TABLE_NAME = "companies"
        const val COL_ID = "id"
        const val COL_NAME = "name"
        const val COL_ADDRESS_ID = "address_id"
    }

    object Address : BaseColumns {
        const val TABLE_NAME = "addresses"
        const val COL_ID = "id"
        const val COL_STREET = "street"
        const val COL_ZIP_CODE = "zip_code"
        const val COL_CITY = "city"
    }

    object Worker : BaseColumns {
        const val TABLE_NAME = "workers"
        const val COL_ID = "id"
        const val COL_FIRST_NAME = "first_name"
        const val COL_LAST_NAME = "last_name"
        const val COL_DATE_OF_BIRTH = "date_of_birth"
        const val COL_TITLE = "title"
        const val COL_EMAIL = "email"
        const val COL_PASSWORD = "password"
        const val COL_PHONE_NUMBER = "phone_number"
        const val COL_CREATED_AT = "created_at"
        const val COL_ADDRESS_ID = "address_id"
    }

    object Tracking : BaseColumns {
        const val TABLE_NAME = "trackings"
        const val COL_ID = "id"
        const val COL_NAME = "name"
        const val COL_WORKER_ID = "worker_id"
        const val COL_START_TIME = "start_time"
        const val COL_END_TIME = "end_time"
        const val COL_DESCRIPTION = "description"
        const val COL_BLUETOOTH_DEVICE = "bluetooth_device"
    }

    object CompanyWorker : BaseColumns {
        const val TABLE_NAME = "company_workers"
        const val COL_COMPANY_ID = "company_id"
        const val COL_WORKER_ID = "worker_id"
        const val COL_POSITION = "position"
        const val COL_ADMIN = "admin"
    }
}