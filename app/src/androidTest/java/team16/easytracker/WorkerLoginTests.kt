package team16.easytracker

import android.content.ContentValues
import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.hamcrest.CoreMatchers.*
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import team16.easytracker.database.Contracts
import team16.easytracker.database.DbHelper


@RunWith(AndroidJUnit4::class)
class WorkerLoginTests {

    val appContext = InstrumentationRegistry.getInstrumentation().targetContext
    val dbHelper = DbHelper(appContext)

    val writableDb = dbHelper.writableDatabase
    val readableDb = dbHelper.readableDatabase

    @Before
    fun init() {
        writableDb.beginTransaction()
    }

    @After
    fun tearDown() {
        writableDb.endTransaction()
    }

    private fun insertDummyWorker(): Long {

        val values = ContentValues().apply {
            put(Contracts.Worker.COL_FIRST_NAME, "john")
            put(Contracts.Worker.COL_LAST_NAME, "doe")
            //put(Worker.COL_DATE_OF_BIRTH, "1998-05-10")
            put(Contracts.Worker.COL_TITLE, "")
            put(Contracts.Worker.COL_EMAIL, "john.doe@something.com")
            put(Contracts.Worker.COL_PASSWORD, "12345678")
            put(Contracts.Worker.COL_PHONE_NUMBER, "+43 9509579554")
            //put(Worker.COL_CREATED_AT, DateTimeFormatter.ISO_INSTANT.format(Instant.now()))
            put(Contracts.Worker.COL_ADDRESS_ID, 1)
        }

        return writableDb.insert(Contracts.Worker.TABLE_NAME, null, values)
    }

    @get:Rule
    val activityRule = ActivityScenarioRule(LoginActivity::class.java)


    @Test
    fun invalidLoginWithEmptyInputData() {
        // Error textviews should not be visible before clicking login button

        onView(withId(R.id.tvErrorEmail))
                .check(matches(not(isDisplayed())))

        onView(withId(R.id.tvErrorPassword))
                .check(matches(not(isDisplayed())))

        // Click login button
        onView(withId(R.id.btnLogin))
                .perform(click())

       // Most error textviews should now be visible
        onView(withId(R.id.tvErrorEmail))
                .check(matches(isDisplayed()))
                .check(matches(withText("The email is required")))


        onView(withId(R.id.tvErrorPassword))
                .check(matches(isDisplayed()))
                .check(matches(withText("The password is required")))

        Thread.sleep(1000)
    }

    @Test
    fun invalidLogin_emailNotValid() {
        onView(withId(R.id.etEmail))
                .perform(typeText("john.doe.mail"), closeSoftKeyboard())
                .check(matches(withText("john.doe.mail")))

        // Click Login button
        onView(withId(R.id.btnLogin))
                .perform(click())

        onView(withId(R.id.tvErrorEmail))
                .check(matches(isDisplayed()))
                .check(matches(withText("The email must be a valid email address")))
    }

    @Test
    fun validLogin_passwordAndMailValid() {
        val newRowId = insertDummyWorker()
        val query = "SELECT * FROM ${Contracts.Worker.TABLE_NAME} WHERE ${Contracts.Worker.COL_ID} = ?"
        val result = readableDb.rawQuery(query, arrayOf(newRowId.toString()))
        result.moveToFirst()

        val workerEmail = result.getString((result.getColumnIndexOrThrow(Contracts.Worker.COL_EMAIL)))
        val workerPassword = result.getString((result.getColumnIndexOrThrow(Contracts.Worker.COL_PASSWORD)))

        onView(withId(R.id.etEmail))
                .perform(typeText("john.doe@something.com"), closeSoftKeyboard())
                .check(matches(withText(workerEmail)))


        onView(withId(R.id.etPassword))
                .perform(typeText("12345678"), closeSoftKeyboard())
                .check(matches(withText(workerPassword)))

    }

    @Test
    fun invalidLogin_passwordNotValid() {
        val newRowId = insertDummyWorker()
        val query = "SELECT * FROM ${Contracts.Worker.TABLE_NAME} WHERE ${Contracts.Worker.COL_ID} = ?"
        val result = readableDb.rawQuery(query, arrayOf(newRowId.toString()))
        result.moveToFirst()

        val workerEmail = result.getString((result.getColumnIndexOrThrow(Contracts.Worker.COL_EMAIL)))
        val workerPassword = result.getString((result.getColumnIndexOrThrow(Contracts.Worker.COL_PASSWORD)))

        onView(withId(R.id.etEmail))
                .perform(typeText("john.doe@something.com"), closeSoftKeyboard())
                .check(matches(withText(workerEmail)))


        onView(withId(R.id.etPassword))
                .perform(typeText("123"), closeSoftKeyboard())
                .check(matches(not(withText(workerPassword))))

        // Click Login button
        onView(withId(R.id.btnLogin))
                .perform(click())

        onView(withId(R.id.tvErrorPassword))
                .check(matches(isDisplayed()))
                .check(matches(withText("Invalid email or password")))

    }




}
