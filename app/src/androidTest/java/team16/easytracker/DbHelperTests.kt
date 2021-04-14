package team16.easytracker

import android.content.ContentValues
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import team16.easytracker.database.Contracts.Company
import team16.easytracker.database.DbHelper

@RunWith(AndroidJUnit4::class)
class DbHelperTests {

    val appContext = InstrumentationRegistry.getInstrumentation().targetContext
    val dbHelper = DbHelper(appContext)

    /** NEVER use setTransactionSuccessful() */
    val writableDb = dbHelper.writableDatabase
    val readableDb = dbHelper.readableDatabase

    val COMPANY_DUMMY_NAME = "company 1"

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

    @Test
    fun testInsertCompany() {
        val newRowId = insertDummyCompany()
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
}