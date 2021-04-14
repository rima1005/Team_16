package team16.easytracker.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import team16.easytracker.database.Contracts.Company
import team16.easytracker.database.Contracts.Address
import team16.easytracker.database.Contracts.Worker
import team16.easytracker.database.Contracts.CompanyWorker
import team16.easytracker.database.Contracts.Tracking


private const val SQL_CREATE_ENTRIES =
    "CREATE TABLE IF NOT EXISTS ${Company.TABLE_NAME} (" +
            "${Company.COL_ID} INTEGER PRIMARY KEY AUTOINCREMENT," +
            "${Company.COL_NAME} VARCHAR(256)," +
            "${Company.COL_ADDRESS_ID} INTEGER);" +

    "CREATE TABLE IF NOT EXISTS ${Address.TABLE_NAME} (" +
            "${Address.COL_ID} INTEGER PRIMARY KEY AUTOINCREMENT," +
            "${Address.COL_STREET} VARCHAR(256)," +
            "${Address.COL_ZIP_CODE} VARCHAR(16)," +
            "${Address.COL_CITY} VARCHAR(64));" +

    "CREATE TABLE IF NOT EXISTS ${Worker.TABLE_NAME} (" +
            "${Worker.COL_ID} INTEGER PRIMARY KEY AUTOINCREMENT," +
            "${Worker.COL_FIRST_NAME} VARCHAR(128)," +
            "${Worker.COL_LAST_NAME} VARCHAR(128)," +
            "${Worker.COL_DATE_OF_BIRTH} DATE," +
            "${Worker.COL_TITLE} VARCHAR(64)," +
            "${Worker.COL_EMAIL} VARCHAR(128)," +
            "${Worker.COL_PASSWORD} VARCHAR(128)," +
            "${Worker.COL_PHONE_NUMBER} VARCHAR(32)," +
            "${Worker.COL_CREATED_AT} TIMESTAMP," +
            "${Worker.COL_ADDRESS_ID} INTEGER);" +

    "CREATE TABLE IF NOT EXISTS ${CompanyWorker.TABLE_NAME} (" +
            "${CompanyWorker.COL_COMPANY_ID} INTEGER," +
            "${CompanyWorker.COL_WORKER_ID} INTEGER," +
            "${CompanyWorker.COL_POSITION} VARCHAR(128));" +

    "CREATE TABLE IF NOT EXISTS ${Tracking.TABLE_NAME} (" +
            "${Tracking.COL_ID} INTEGER PRIMARY KEY AUTOINCREMENT," +
            "${Tracking.COL_NAME} VARCHAR(128)," +
            "${Tracking.COL_WORKER_ID} INTEGER," +
            "${Tracking.COL_START_TIME} DATETIME," +
            "${Tracking.COL_END_TIME} DATETIME," +
            "${Tracking.COL_DESCRIPTION} TEXT," +
            "${Tracking.COL_BLUETOOTH_DEVICE} VARCHAR(128));"

private const val SQL_CREATE_WORKERS =
        "CREATE TABLE IF NOT EXISTS ${Worker.TABLE_NAME} (" +
        "${Worker.COL_ID} INTEGER PRIMARY KEY AUTOINCREMENT," +
        "${Worker.COL_FIRST_NAME} VARCHAR(128)," +
        "${Worker.COL_LAST_NAME} VARCHAR(128)," +
        "${Worker.COL_DATE_OF_BIRTH} DATE," +
        "${Worker.COL_TITLE} VARCHAR(64)," +
        "${Worker.COL_EMAIL} VARCHAR(128)," +
        "${Worker.COL_PASSWORD} VARCHAR(128)," +
        "${Worker.COL_PHONE_NUMBER} VARCHAR(32)," +
        "${Worker.COL_CREATED_AT} TIMESTAMP," +
        "${Worker.COL_ADDRESS_ID} INTEGER)"

class DbHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_ENTRIES)
        //db.execSQL(SQL_CREATE_WORKERS)
    }
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // This database is only a cache for online data, so its upgr   ade policy is
        // to simply to discard the data and start over
        //db.execSQL(SQL_DELETE_ENTRIES)
        onCreate(db)
    }
    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        onUpgrade(db, oldVersion, newVersion)
    }
    companion object {
        // If you change the database schema, you must increment the database version.
        const val DATABASE_VERSION = 4
        const val DATABASE_NAME = "EasyTracker.db"
    }
}