package team16.easytracker.database

import android.content.ContentValues
import android.content.Context
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import org.mindrot.jbcrypt.BCrypt
import team16.easytracker.MyApplication
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
import java.time.*
import java.time.format.DateTimeFormatter
import kotlin.IllegalArgumentException

/*
    TODO: USAGE:
    In order to update the database schema increment DATABASE_VERSION by 1
    Create a .sql file in assets with naming oldversion-newversion.sql (e.g. 1-2.sql) which
    contains your SQL Statement.
    ATTENTION: Also make sure that you add the SQL-Statement to the onCreate method by either changing
    the existing statements or executing a new one with db.execSQL()
 */

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

// TODO: pending flag until worker accepts?
private const val SQL_CREATE_COMPANY_WORKER =
    "CREATE TABLE IF NOT EXISTS ${CompanyWorker.TABLE_NAME} (" +
            "${CompanyWorker.COL_COMPANY_ID} INTEGER," +
            "${CompanyWorker.COL_WORKER_ID} INTEGER," +
            "${CompanyWorker.COL_POSITION} VARCHAR(128)," +
            "${CompanyWorker.COL_ADMIN} INTEGER DEFAULT 0)"

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


// If you change the database schema, you must increment the database version.
private const val DATABASE_VERSION = 2
private const val DATABASE_NAME_PROD = "EasyTracker.db"
private const val DATABASE_NAME_TEST = "EasyTrackerTest.db"

class DbHelper private constructor(context: Context, databaseName: String = DATABASE_NAME_PROD) : SQLiteOpenHelper(context, databaseName, null, DATABASE_VERSION) {

    val ctx = context
    val tag = DbHelper::class.java.name

    companion object {
        private var _instance: DbHelper? = null

        fun getInstance(context: Context = MyApplication.instance): DbHelper
        {
            if (_instance == null)
            {
                if (context != MyApplication.instance)
                    _instance = DbHelper(context, DATABASE_NAME_TEST)
                else
                    _instance = DbHelper(MyApplication.instance, DATABASE_NAME_PROD)
            }
            return _instance!!
        }
    }

    fun clearDbAndCreate(databaseName: String)
    {
        clearDb(databaseName)
        onCreate(writableDatabase)
    }

    fun clearDb(databaseName: String)
    {
        if(databaseName != DATABASE_NAME_TEST)
            throw UnsupportedOperationException("You are about to delete a productive or non-existing Database! This should never happen!!!")
        ctx.deleteDatabase(databaseName)
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_COMPANY)
        db.execSQL(SQL_CREATE_ADDRESS)
        db.execSQL(SQL_CREATE_COMPANY_WORKER)
        db.execSQL(SQL_CREATE_TRACKING)
        db.execSQL(SQL_CREATE_WORKERS)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        Log.d(tag, "Updating DB version from $oldVersion ")
        try {
            for (i in oldVersion until newVersion) {
                val fileName = "${i}-${i + 1}.sql"
                val file = ctx.assets.open(fileName)
                executeSQLFile(file, db)
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

    @Throws(SQLException::class)
    fun executeSQLFile(inputStream: InputStream, db: SQLiteDatabase) {
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

        // TODO: set address object of company

        result.close()
        return CompanyModel(id, name, addressId)
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
        val workerId = result.getInt(result.getColumnIndex(Tracking.COL_WORKER_ID))
        val startTime = LocalDateTime.ofEpochSecond(
            result.getLong(result.getColumnIndex(Tracking.COL_START_TIME)),
            0,
            ZoneOffset.UTC
        )
        val endTime = LocalDateTime.ofEpochSecond(
            result.getLong(result.getColumnIndex(Tracking.COL_END_TIME)),
            0,
            ZoneOffset.UTC
        )
        val description = result.getString(result.getColumnIndex(Tracking.COL_DESCRIPTION))
        val bluetoothDevice = result.getString(result.getColumnIndex(Tracking.COL_BLUETOOTH_DEVICE))

        result.close()
        return TrackingModel(id, name, workerId, startTime, endTime, description, bluetoothDevice)
    }

    fun loadWorkerTrackings(workerId: Int): List<TrackingModel>? {
        val result = readableDatabase.rawQuery(
                "SELECT * FROM ${Tracking.TABLE_NAME} WHERE ${Tracking.COL_WORKER_ID} = ?",
                arrayOf(workerId.toString())
        )
        var trackings: List<TrackingModel> = ArrayList<TrackingModel>()
        if (result == null || !result.moveToFirst())
            return trackings
        do {
            trackings = trackings.plus(TrackingModel(
                    result.getInt(result.getColumnIndex(Tracking.COL_ID)),
                    result.getString(result.getColumnIndex(Tracking.COL_NAME)),
                    result.getInt(result.getColumnIndex(Tracking.COL_WORKER_ID)),
                    LocalDateTime.ofEpochSecond(
                            result.getLong(result.getColumnIndex(Tracking.COL_START_TIME)),
                            0,
                            ZoneOffset.UTC
                    ),
                    LocalDateTime.ofEpochSecond(
                            result.getLong(result.getColumnIndex(Tracking.COL_END_TIME)),
                            0,
                            ZoneOffset.UTC
                    ),
                    result.getString(result.getColumnIndex(Tracking.COL_DESCRIPTION)),
                    result.getString(result.getColumnIndex(Tracking.COL_BLUETOOTH_DEVICE))
            ))
        } while (result.moveToNext())

        result.close()
        return trackings
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
        val phoneNumber = result.getString(result.getColumnIndex(Worker.COL_PHONE_NUMBER))
        val createdAt = LocalDateTime.ofEpochSecond(
            result.getLong(result.getColumnIndex(Worker.COL_CREATED_AT)),
            0,
            ZoneOffset.UTC
        )
        val addressId = result.getInt(result.getColumnIndex(Worker.COL_ADDRESS_ID))
        var company: CompanyModel? = null
        var position: String? = null
        var admin = false
        result.close();
        val resultCompanyWorker = readableDatabase.rawQuery(
            "SELECT * FROM ${CompanyWorker.TABLE_NAME} WHERE ${CompanyWorker.COL_WORKER_ID} = ?",
            arrayOf(id.toString())
        )
        if (resultCompanyWorker.moveToFirst()) {
            val companyId =
                resultCompanyWorker.getInt(resultCompanyWorker.getColumnIndex(CompanyWorker.COL_COMPANY_ID))
            company = loadCompany(companyId)
            if (company == null)
                throw IllegalArgumentException("CompanyWorker: The Company with ID $companyId does not exist!")
            position =
                resultCompanyWorker.getString(resultCompanyWorker.getColumnIndex(CompanyWorker.COL_POSITION))
            admin =
                resultCompanyWorker.getInt(resultCompanyWorker.getColumnIndex(CompanyWorker.COL_ADMIN)) != 0
        }
        resultCompanyWorker.close()
        return WorkerModel(
            id,
            firstName,
            lastName,
            dateOfBirth,
            title,
            email,
            phoneNumber,
            createdAt,
            addressId,
            admin,
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
        val passwordHash = BCrypt.hashpw(password, BCrypt.gensalt());
        val values = ContentValues().apply {
            put(Worker.COL_FIRST_NAME, firstName)
            put(Worker.COL_LAST_NAME, lastName)
            put(Worker.COL_DATE_OF_BIRTH, dateOfBirth.toEpochDay())
            put(Worker.COL_TITLE, title)
            put(Worker.COL_EMAIL, email)
            put(Worker.COL_PASSWORD, passwordHash)
            put(Worker.COL_PHONE_NUMBER, phoneNumber)
            put(Worker.COL_CREATED_AT, createdAt.toEpochSecond(ZoneOffset.UTC))
            put(Worker.COL_ADDRESS_ID, addressId)
        }
        return writableDatabase.insert(Worker.TABLE_NAME, null, values).toInt()
    }

    fun loginWorker(email: String, password: String): WorkerModel? {
        val result = readableDatabase.rawQuery(
            "SELECT ${Worker.COL_ID}, ${Worker.COL_PASSWORD} FROM ${Worker.TABLE_NAME} WHERE ${Worker.COL_EMAIL} = ?",
            arrayOf(email)
        )

        if (!result.moveToNext()) {
            result.close()
            return null;
        }

        val passwordHash = result.getString(result.getColumnIndex(Worker.COL_PASSWORD))

        if (!BCrypt.checkpw(password, passwordHash)) {
            result.close()
            return null;
        }

        val workerId = result.getInt(result.getColumnIndex(Worker.COL_ID))

        result.close()
        MyApplication.loggedInWorker = loadWorker(workerId)
        return MyApplication.loggedInWorker
    }

    fun addWorkerToCompany(workerId: Int, companyId: Int, position: String): Boolean {
        val company = loadCompany(companyId)
            ?: throw IllegalArgumentException("Company with id $companyId does not exist!")
        val worker = loadWorker(workerId)
            ?: throw IllegalArgumentException("Worker with id $workerId does not exist!")

        val resultCompanyWorker = readableDatabase.rawQuery(
            "SELECT * FROM ${CompanyWorker.TABLE_NAME} WHERE ${CompanyWorker.COL_WORKER_ID} = ? AND ${CompanyWorker.COL_COMPANY_ID} = ?",
            arrayOf(workerId.toString(), companyId.toString())
        )

        val alreadyExists = resultCompanyWorker.moveToFirst()
        resultCompanyWorker.close()

        if (alreadyExists)
            return true // TODO: should this fail?

        val values = ContentValues().apply {
            put(CompanyWorker.COL_WORKER_ID, workerId)
            put(CompanyWorker.COL_COMPANY_ID, companyId)
            put(CompanyWorker.COL_POSITION, position)
        }
        val rowId = writableDatabase.insert(CompanyWorker.TABLE_NAME, null, values).toInt()

        if (rowId < 0) {
            throw SQLException("Failed to assign worker $workerId to company $companyId!")
        }

        return true
    }

    fun companyExists(companyName: String): Boolean {
        // TODO: case sensitive?
        val result = readableDatabase.rawQuery(
            "SELECT * FROM ${Company.TABLE_NAME} WHERE ${Company.COL_NAME} = ?",
            arrayOf(companyName)
        )
        val found = result.moveToFirst()
        result.close()
        return found
    }

    fun setCompanyAdmin(workerId: Int, companyId: Int, admin: Boolean) {
        val company = loadCompany(companyId)
            ?: throw IllegalArgumentException("Company with id $companyId does not exist!")
        val worker = loadWorker(workerId)
            ?: throw IllegalArgumentException("Worker with id $workerId does not exist!")

        val resultCompanyWorker = readableDatabase.rawQuery(
            "SELECT * FROM ${CompanyWorker.TABLE_NAME} WHERE ${CompanyWorker.COL_WORKER_ID} = ? AND ${CompanyWorker.COL_COMPANY_ID} = ?",
            arrayOf(workerId.toString(), companyId.toString())
        )

        val alreadyExists = resultCompanyWorker.moveToFirst()
        resultCompanyWorker.close()

        if (!alreadyExists)
            throw IllegalArgumentException("Worker $workerId does not work at company $companyId!")

        val adminInt = if (admin) 1 else 0

        val values = ContentValues().apply {
            put(CompanyWorker.COL_ADMIN, adminInt)
        }

        writableDatabase.beginTransaction()
        val rowsAffected = writableDatabase.update(
            CompanyWorker.TABLE_NAME, values,
            "${CompanyWorker.COL_WORKER_ID} = ? AND ${CompanyWorker.COL_COMPANY_ID} = ?",
            arrayOf(workerId.toString(), companyId.toString())
        )

        if (rowsAffected == 1) {
            writableDatabase.setTransactionSuccessful()
            writableDatabase.endTransaction()
            return
        }

        writableDatabase.endTransaction()
        throw SQLException("Failed to set worker $workerId to admin $admin for company $companyId")
    }

    fun loadWorker(workerEmail: String) : WorkerModel? {
        val result = readableDatabase.rawQuery(
            "SELECT ${Worker.COL_ID} FROM ${Worker.TABLE_NAME} WHERE ${Worker.COL_EMAIL} = ?",
            arrayOf(workerEmail)
        )
        if (!result.moveToFirst())
            return null
        val workerId = result.getInt(result.getColumnIndex(Worker.COL_ID))
        return loadWorker(workerId)
    }

    fun updateTracking(
        trackingId: Int?,
        trackingName: String,
        workerId: Int,
        startDateTime: LocalDateTime,
        endDateTime: LocalDateTime,
        trackingNotes: String,
        bluetoothDevice: String) {
        val values = ContentValues().apply {
            put(Tracking.COL_NAME, trackingName)
            put(Tracking.COL_WORKER_ID, workerId)
            put(Tracking.COL_START_TIME, startDateTime.toEpochSecond(ZoneOffset.UTC))
            put(Tracking.COL_END_TIME, endDateTime.toEpochSecond(ZoneOffset.UTC))
            put(Tracking.COL_DESCRIPTION, trackingNotes)
            put(Tracking.COL_BLUETOOTH_DEVICE, bluetoothDevice)
        }
        writableDatabase.update(Tracking.TABLE_NAME, values,
                     "${Tracking.COL_ID} = ?",
                                 arrayOf(trackingId.toString()))
        return
    }

    fun deleteTracking(trackingId: Int): Int
    {
        return writableDatabase.delete(Tracking.TABLE_NAME, "${Tracking.COL_ID} = ?", arrayOf(trackingId.toString()))

    }

    fun deleteCompanyWorker(workerId: Int, companyId: Int): Int
    {
        return writableDatabase.delete(CompanyWorker.TABLE_NAME, "${CompanyWorker.COL_WORKER_ID} = ? AND ${CompanyWorker.COL_COMPANY_ID} = ?", arrayOf(workerId.toString(), companyId.toString()))
    }
}
