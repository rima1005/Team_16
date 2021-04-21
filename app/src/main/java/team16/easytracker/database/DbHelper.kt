package team16.easytracker.database

import android.content.ContentValues
import android.content.Context
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import team16.easytracker.database.Contracts.Company
import team16.easytracker.database.Contracts.Address
import team16.easytracker.database.Contracts.Worker
import team16.easytracker.database.Contracts.CompanyWorker
import team16.easytracker.database.Contracts.Tracking

import team16.easytracker.model.Address as AddressModel
import team16.easytracker.model.Company as CompanyModel
import team16.easytracker.model.Tracking as TrackingModel
import team16.easytracker.model.Worker as WorkerModel
import java.io.*
import java.lang.IllegalArgumentException
import java.time.*
import java.time.format.DateTimeFormatter


private const val SQL_CREATE_COMPANY =
    "CREATE TABLE IF NOT EXISTS ${Company.TABLE_NAME} (" +
            "${Company.COL_ID} INTEGER PRIMARY KEY AUTOINCREMENT," +
            "${Company.COL_NAME} VARCHAR(256)," +
            "${Company.COL_ADDRESS_ID} INTEGER)"

private const val SQL_CREATE_ADDRESS =
    "CREATE TABLE IF NOT EXISTS ${Address.TABLE_NAME} (" +
            "${Address.COL_ID} INTEGER PRIMARY KEY AUTOINCREMENT," +
            "${Address.COL_STREET} VARCHAR(256)," +
            "${Address.COL_ZIP_CODE} VARCHAR(16)," +
            "${Address.COL_CITY} VARCHAR(64))"

private const val SQL_CREATE_COMPANY_WORKER =
    "CREATE TABLE IF NOT EXISTS ${CompanyWorker.TABLE_NAME} (" +
            "${CompanyWorker.COL_COMPANY_ID} INTEGER," +
            "${CompanyWorker.COL_WORKER_ID} INTEGER," +
            "${CompanyWorker.COL_POSITION} VARCHAR(128))"

private const val SQL_CREATE_TRACKING =
    "CREATE TABLE IF NOT EXISTS ${Tracking.TABLE_NAME} (" +
            "${Tracking.COL_ID} INTEGER PRIMARY KEY AUTOINCREMENT," +
            "${Tracking.COL_NAME} VARCHAR(128)," +
            "${Tracking.COL_WORKER_ID} INTEGER," +
            "${Tracking.COL_START_TIME} LONG," +
            "${Tracking.COL_END_TIME} LONG," +
            "${Tracking.COL_DESCRIPTION} TEXT," +
            "${Tracking.COL_BLUETOOTH_DEVICE} VARCHAR(128))"

private const val SQL_CREATE_WORKERS =
    "CREATE TABLE IF NOT EXISTS ${Worker.TABLE_NAME} (" +
            "${Worker.COL_ID} INTEGER PRIMARY KEY AUTOINCREMENT," +
            "${Worker.COL_FIRST_NAME} VARCHAR(128)," +
            "${Worker.COL_LAST_NAME} VARCHAR(128)," +
            "${Worker.COL_DATE_OF_BIRTH} LONG," +
            "${Worker.COL_TITLE} VARCHAR(64)," +
            "${Worker.COL_EMAIL} VARCHAR(128) UNIQUE," +
            "${Worker.COL_PASSWORD} VARCHAR(128)," +
            "${Worker.COL_PHONE_NUMBER} VARCHAR(32)," +
            "${Worker.COL_CREATED_AT} LONG," +
            "${Worker.COL_ADDRESS_ID} INTEGER)"

class DbHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    val ctx = context;
    val tag = DbHelper::class.java.name;

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_COMPANY)
        db.execSQL(SQL_CREATE_ADDRESS)
        db.execSQL(SQL_CREATE_COMPANY_WORKER)
        db.execSQL(SQL_CREATE_TRACKING)
        db.execSQL(SQL_CREATE_WORKERS)
        //db.execSQL(SQL_CREATE_WORKERS)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        Log.d(tag, "Updating DB version from $oldVersion ")
        try {
            for (i in oldVersion until newVersion) {
                val fileName = "${i}-${i + 1}.sql"
                val file = ctx.assets.open(fileName)
                executeSQLFile(db, file)
                file.close()
            }
        } catch (e: IOException) {
            Log.e(tag, "Failed to open DB version file", e)
        } catch (e: SQLException) {
            Log.e(tag, "Failed to update DB version", e)
        }
    }

    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        onUpgrade(db, oldVersion, newVersion)
    }

    companion object {
        // If you change the database schema, you must increment the database version.
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "EasyTracker.db"
    }

    @Throws(SQLException::class)
    fun executeSQLFile(db: SQLiteDatabase, inputStream: InputStream) {
        val reader = inputStream.bufferedReader()
        var line = reader.readLine()
        while (line != null) {
            if (!line.endsWith(";")) {
                throw SQLException("Invalid .sql file!")
            }
            db.execSQL(line)
            line = reader.readLine()
        }
    }

    fun loadAddress(id: Int): AddressModel? {
        val result = readableDatabase.rawQuery(
            "SELECT * FROM ${Address.TABLE_NAME} WHERE ${Address.COL_ID} = ?",
            arrayOf(id.toString())
        )
        if (!result.moveToFirst())
            return null
        val street = result.getString(result.getColumnIndex(Address.COL_STREET))
        val zipCode = result.getString(result.getColumnIndex(Address.COL_ZIP_CODE))
        val city = result.getString(result.getColumnIndex(Address.COL_CITY))
        result.close();
        return AddressModel(id, street, zipCode, city)
    }

    fun saveAddress(street: String, zipCode: String, city: String): Int {
        val values = ContentValues().apply {
            put(Address.COL_STREET, street)
            put(Address.COL_ZIP_CODE, zipCode)
            put(Address.COL_CITY, city)
        }
        return writableDatabase.insert(Address.TABLE_NAME, null, values).toInt()
    }

    fun loadCompany(id: Int): CompanyModel? {
        val result = readableDatabase.rawQuery(
            "SELECT * FROM ${Company.TABLE_NAME} WHERE ${Company.COL_ID} = ?",
            arrayOf(id.toString())
        )
        if (!result.moveToFirst())
            return null

        val name = result.getString(result.getColumnIndex(Company.COL_NAME))
        val addressId = result.getInt(result.getColumnIndex(Company.COL_ADDRESS_ID))
        result.close();
        return CompanyModel(id, name, addressId);
    }

    fun saveCompany(name: String, addressId: Int): Int {
        val values = ContentValues().apply {
            put(Company.COL_NAME, name)
            put(Company.COL_ADDRESS_ID, addressId)
        }
        return writableDatabase.insert(Company.TABLE_NAME, null, values).toInt()
    }

    fun loadTracking(id: Int): TrackingModel? {
        val result = readableDatabase.rawQuery(
            "SELECT * FROM ${Tracking.TABLE_NAME} WHERE ${Tracking.COL_ID} = ?",
            arrayOf(id.toString())
        )
        if (!result.moveToFirst())
            return null
        val name = result.getString(result.getColumnIndex(Tracking.COL_NAME))
        val workerId = result.getInt(result.getColumnIndex(Tracking.COL_NAME))
        val startTime = LocalDateTime.ofEpochSecond(
            result.getLong(result.getColumnIndex(Tracking.COL_START_TIME)),
            0,
            ZoneOffset.UTC
        )
        val endTime = LocalDateTime.ofEpochSecond(
            result.getLong(result.getColumnIndex(Tracking.COL_NAME)),
            0,
            ZoneOffset.UTC
        )
        val description = result.getString(result.getColumnIndex(Tracking.COL_NAME))
        val bluetoothDevice = result.getString(result.getColumnIndex(Tracking.COL_NAME))

        result.close();
        return TrackingModel(id, name, workerId, startTime, endTime, description, bluetoothDevice);
    }

    fun saveTracking(
        name: String,
        workerId: Int,
        startTime: LocalDateTime,
        endTime: LocalDateTime,
        description: String,
        bluetoothDevice: String
    ): Int {
        val values = ContentValues().apply {
            put(Tracking.COL_NAME, name)
            put(Tracking.COL_WORKER_ID, workerId)
            put(Tracking.COL_START_TIME, startTime.toEpochSecond(ZoneOffset.UTC))
            put(Tracking.COL_END_TIME, endTime.toEpochSecond(ZoneOffset.UTC))
            put(Tracking.COL_DESCRIPTION, description)
            put(Tracking.COL_BLUETOOTH_DEVICE, bluetoothDevice)
        }
        return writableDatabase.insert(Tracking.TABLE_NAME, null, values).toInt()
    }

    fun loadWorker(id: Int): WorkerModel? {
        val result = readableDatabase.rawQuery(
            "SELECT * FROM ${Worker.TABLE_NAME} WHERE ${Worker.COL_ID} = ?",
            arrayOf(id.toString())
        )
        if (!result.moveToFirst())
            return null
        val firstName = result.getString(result.getColumnIndex(Worker.COL_FIRST_NAME))
        val lastName = result.getString(result.getColumnIndex(Worker.COL_LAST_NAME))
        val dateOfBirth =
            LocalDate.ofEpochDay(result.getLong(result.getColumnIndex(Worker.COL_DATE_OF_BIRTH)))
        val title = result.getString(result.getColumnIndex(Worker.COL_TITLE))
        val email = result.getString(result.getColumnIndex(Worker.COL_EMAIL))
        val password = result.getString(result.getColumnIndex(Worker.COL_PASSWORD))
        val phoneNumber = result.getString(result.getColumnIndex(Worker.COL_PHONE_NUMBER))
        val createdAt = LocalDateTime.ofEpochSecond(
            result.getLong(result.getColumnIndex(Worker.COL_CREATED_AT)),
            0,
            ZoneOffset.UTC
        )
        val addressId = result.getInt(result.getColumnIndex(Worker.COL_ADDRESS_ID))
        var company: CompanyModel? = null
        var position: String? = null

        result.close();
        val resultCompany = readableDatabase.rawQuery(
            "SELECT * FROM ${CompanyWorker.TABLE_NAME} WHERE ${CompanyWorker.COL_WORKER_ID} = ?",
            arrayOf(id.toString())
        )
        if (result.moveToFirst()) {
            val companyId = result.getInt(result.getColumnIndex(CompanyWorker.COL_COMPANY_ID))
            company = loadCompany(companyId)
            if (company == null)
                throw IllegalArgumentException("CompanyWorker: The Company with ID $companyId does not exist!")
            position = result.getString(result.getColumnIndex(CompanyWorker.COL_POSITION))
        }
        resultCompany.close()
        return WorkerModel(
            id,
            firstName,
            lastName,
            dateOfBirth,
            title,
            email,
            password,
            phoneNumber,
            createdAt,
            addressId,
            position,
            company
        );
    }

    fun saveWorker(
        firstName: String, lastName: String,
        dateOfBirth: LocalDate,
        title: String,
        email: String,
        password: String,
        phoneNumber: String,
        createdAt: LocalDateTime,
        addressId: Int,
    ): Int {
        val values = ContentValues().apply {
            put(Worker.COL_FIRST_NAME, firstName)
            put(Worker.COL_LAST_NAME, lastName)
            put(Worker.COL_DATE_OF_BIRTH, dateOfBirth.toEpochDay())
            put(Worker.COL_TITLE, title)
            put(Worker.COL_EMAIL, email)
            put(Worker.COL_PASSWORD, password)
            put(Worker.COL_PHONE_NUMBER, phoneNumber)
            put(Worker.COL_CREATED_AT, createdAt.toEpochSecond(ZoneOffset.UTC))
            put(Worker.COL_ADDRESS_ID, addressId)
        }
        return writableDatabase.insert(Worker.TABLE_NAME, null, values).toInt()
    }




}
