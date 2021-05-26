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
class WorkerLoginTests : TestFramework() {

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
                .check(matches(withText(R.string.email_required)))


        onView(withId(R.id.tvErrorPassword))
                .check(matches(isDisplayed()))
                .check(matches(withText(R.string.pw_required)))

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
                .check(matches(withText(R.string.email_valid_address)))
    }

    @Test
    fun validLogin_passwordAndMailValid() {
        val dummyWorker = insertDummyWorker()

        onView(withId(R.id.etEmail))
                .perform(typeText(dummyWorker.email), closeSoftKeyboard())


        onView(withId(R.id.etPassword))
                .perform(typeText("12345678"), closeSoftKeyboard())

        /*// Click Login button
        onView(withId(R.id.btnLogin))
                .perform(click())*/

    }

    @Test
    fun invalidLogin_passwordNotValid() {
        val dummyWorker = insertDummyWorker()

      // val worker = dbHelper.loginWorker("john@so.at", "123")

       //dbHelper.close()


        onView(withId(R.id.etEmail))
                .perform(typeText(dummyWorker.email), closeSoftKeyboard())


        onView(withId(R.id.etPassword))
                .perform(typeText("1231564684asdf"), closeSoftKeyboard())

        // Click Login button
        onView(withId(R.id.btnLogin))
                .perform(click())

        onView(withId(R.id.tvErrorPassword))
                .check(matches(isDisplayed()))
                .check(matches(withText(R.string.invalid_password_mail)))

    }

    @Test
    fun setGlobalWorkerAfterLogin()
    {
        insertDummyWorker()
        dbHelper.loginWorker(dummyWorker.email, "12345678")
        assert(MyApplication.loggedInWorker != null)
        assert(MyApplication.loggedInWorker?.firstName == dummyWorker.firstName)
    }
}
