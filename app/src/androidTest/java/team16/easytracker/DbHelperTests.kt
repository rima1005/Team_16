package team16.easytracker

import android.content.ContentValues
import android.content.res.AssetManager
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import team16.easytracker.database.Contracts.Company
import team16.easytracker.database.Contracts.Worker
import team16.easytracker.database.DbHelper
import java.time.Instant
import java.time.format.DateTimeFormatter

@RunWith(AndroidJUnit4::class)
class DbHelperTests {

    val appContext = InstrumentationRegistry.getInstrumentation().targetContext
    val dbHelper = DbHelper(appContext)

    /** NEVER use setTransactionSuccessful() */
    val writableDb = dbHelper.writableDatabase
    val readableDb = dbHelper.readableDatabase

    val COMPANY_DUMMY_NAME = "company 1"
    val WORKER_DUMMY_FIRST_NAME = "Max"
    val WORKER_DUMMY_LAST_NAME = "Mustermann"

    @Before
    fun init() {
        writableDb.beginTransaction()
    }

    @After
    fun tearDown() {
        writableDb.endTransaction()
    }

    private fun insertDummyCompany() : Long {
        val values = ContentValues().apply {
            put(Company.COL_NAME, COMPANY_DUMMY_NAME)
            put(Company.COL_ADDRESS_ID, 1)
        }


        return writableDb.insert(Company.TABLE_NAME, null, values)
    }

    private fun insertDummyWorker() : Long {

        val values = ContentValues().apply {
            put(Worker.COL_FIRST_NAME, WORKER_DUMMY_FIRST_NAME)
            put(Worker.COL_LAST_NAME, WORKER_DUMMY_LAST_NAME)
            //put(Worker.COL_DATE_OF_BIRTH, "1998-05-10")
            put(Worker.COL_TITLE, "")
            put(Worker.COL_EMAIL, "max.mustermann@gmail.com")
            put(Worker.COL_PASSWORD, "1235165465")
            put(Worker.COL_PHONE_NUMBER, "+43 9509579554")
            //put(Worker.COL_CREATED_AT, DateTimeFormatter.ISO_INSTANT.format(Instant.now()))
            put(Worker.COL_ADDRESS_ID, 1)
        }

        return writableDb.insert(Worker.TABLE_NAME, null, values)
    }

    @Test
    fun testInsertCompany() {
        val newRowId = insertDummyCompany()
        assert(newRowId > 0)
    }

    @Test
    fun testInsertWorker() {
        val newRowId = insertDummyWorker()
        assert(newRowId > 0)
    }


    @Test
    fun testReadCompany() {
        val newRowId = insertDummyCompany()
        val query = "SELECT * FROM ${Company.TABLE_NAME} WHERE ${Company.COL_ID} = ?"
        val result = readableDb.rawQuery(query, arrayOf(newRowId.toString()))
        result.moveToFirst()
        val companyName = result.getString(result.getColumnIndexOrThrow(Company.COL_NAME))
        assert(companyName == COMPANY_DUMMY_NAME)
    }

    @Test
    fun testReadWorker() {
        val newRowId = insertDummyWorker()
        val query = "SELECT * FROM ${Worker.TABLE_NAME} WHERE ${Worker.COL_ID} = ?"
        val result = readableDb.rawQuery(query, arrayOf(newRowId.toString()))
        result.moveToFirst()
        val workerFirstName = result.getString(result.getColumnIndexOrThrow(Worker.COL_FIRST_NAME))
        assert(workerFirstName == WORKER_DUMMY_FIRST_NAME)
        val workerLastName = result.getString(result.getColumnIndexOrThrow(Worker.COL_LAST_NAME))
        assert(workerLastName == WORKER_DUMMY_LAST_NAME)
    }

    @Test
    fun testExecuteSQLScript() {
        val assetManager = appContext.assets
        val inputStream = assetManager.open("dbtest1.sql")
        dbHelper.executeSQLFile(writableDb, inputStream)
        val newRowId = insertDummyCompany()
        val query = "SELECT * FROM ${Company.TABLE_NAME} WHERE ${Company.COL_ID} = ?"
        val result = readableDb.rawQuery(query, arrayOf(newRowId.toString()))
        result.moveToFirst()
        val columnIndex = result.getColumnIndex("test1")
        assert(columnIndex != -1)
    }
}