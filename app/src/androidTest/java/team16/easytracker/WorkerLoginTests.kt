package team16.easytracker

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
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
import org.junit.*
import org.junit.runner.RunWith
import team16.easytracker.database.Contracts
import team16.easytracker.database.DbHelper
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


@RunWith(AndroidJUnit4::class)
class WorkerLoginTests {

    val appContext = InstrumentationRegistry.getInstrumentation().targetContext
    lateinit var writableDb: SQLiteDatabase
    lateinit var readable: SQLiteDatabase

    val firstName = "Max"
    val lastName = "Mustermann"
    val email = "test.test@test.at"
    val password = "securePassword"
    val dateOfBirth = "11.06.1999"
    val street = "straße 1"
    val postCode = "0989"
    val city = "graz"
    val title = ""
    val phoneNumber = "43660151625"
    val phonePrefix = "43"

    @Before
    fun init() {
        writableDb = DbHelper.writableDatabase
        readable = DbHelper.readableDatabase
        writableDb.beginTransaction()
    }

    @After
    fun tearDown() {
        writableDb.endTransaction()
    }

    private fun insertDummyWorker(): Int {

         /*val values = ContentValues().apply {
            put(Contracts.Worker.COL_FIRST_NAME, "john")
            put(Contracts.Worker.COL_LAST_NAME, "doe")
            //put(Worker.COL_DATE_OF_BIRTH, "1998-05-10")
            put(Contracts.Worker.COL_TITLE, "")
            put(Contracts.Worker.COL_EMAIL, "john.doe@something.com")
            put(Contracts.Worker.COL_PASSWORD, "12345678")
            put(Contracts.Worker.COL_PHONE_NUMBER, "+43 9509579554")
            //put(Worker.COL_CREATED_AT, DateTimeFormatter.ISO_INSTANT.format(Instant.now()))
            put(Contracts.Worker.COL_ADDRESS_ID, 1)
        }*/

        val addressId = DbHelper.saveAddress(street, postCode, city)

        val workerId = DbHelper.saveWorker(
                firstName,
                lastName,
                LocalDate.now(),
                title,
                email,
                password,
                phonePrefix + phoneNumber,
                LocalDateTime.now().withNano(0),
                1
        )

        return workerId
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

    @Ignore
    @Test
    fun validLogin_passwordAndMailValid() {
        val newRowId = insertDummyWorker()

        onView(withId(R.id.etEmail))
                .perform(typeText("john.doe@something.com"), closeSoftKeyboard())


        onView(withId(R.id.etPassword))
                .perform(typeText("12345678"), closeSoftKeyboard())

        /*// Click Login button
        onView(withId(R.id.btnLogin))
                .perform(click())*/

    }

    @Ignore
    @Test
    fun invalidLogin_passwordNotValid() {
        val newRowId = insertDummyWorker()

      // val worker = dbHelper.loginWorker("john@so.at", "123")

       //dbHelper.close()


        onView(withId(R.id.etEmail))
                .perform(typeText("john.doe@something.com"), closeSoftKeyboard())


        onView(withId(R.id.etPassword))
                .perform(typeText("123"), closeSoftKeyboard())

        // Click Login button
        onView(withId(R.id.btnLogin))
                .perform(click())

        onView(withId(R.id.tvErrorPassword))
                .check(matches(isDisplayed()))
                .check(matches(withText("Invalid email or password")))

    }

    @Test
    fun setGlobalWorkerAfterLogin()
    {
        val result = insertDummyWorker()
        DbHelper.loginWorker("test.test@test.at", "securePassword")
        assert(MyApplication.loggedInWorker != null)
        assert(MyApplication.loggedInWorker?.firstName == firstName)
    }

//TODO Refactoring dbHelper singleton pattern


}
