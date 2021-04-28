package team16.easytracker

import android.app.Activity
import android.database.sqlite.SQLiteDatabase
import androidx.fragment.app.Fragment
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import androidx.test.runner.lifecycle.ActivityLifecycleMonitorRegistry
import androidx.test.runner.lifecycle.Stage
import org.hamcrest.CoreMatchers
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import team16.easytracker.database.DbHelper

@RunWith(AndroidJUnit4::class)
class CreateCompanyTests {
    val appContext = getInstrumentation().targetContext
    lateinit var dbHelper: DbHelper
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

    @get:Rule
    val activityRule = ActivityScenarioRule(HomeActivity::class.java)

    fun getCurrentActivity(): Activity {
        var currentActivity: Activity? = null
        getInstrumentation().runOnMainSync { run { currentActivity = ActivityLifecycleMonitorRegistry.getInstance().getActivitiesInStage(Stage.RESUMED).elementAtOrNull(0) } }
        return currentActivity!!
    }

    fun openFragment() {
        val activity = getCurrentActivity() as HomeActivity
        val transaction = activity.supportFragmentManager.beginTransaction()
        transaction.replace(R.id.flFragment, CreateCompanyFragment())
        transaction.disallowAddToBackStack()
        transaction.commit()
    }

    @Test
    fun testOpenFragment() {
        openFragment()
        onView(withId(R.id.etCompanyName)).check(matches((isDisplayed())))
    }

    @Test
    fun invalidRegisterWithEmptyInputData() {
        openFragment()
        // Error textviews should not be visible before clicking registration button
        onView(withId(R.id.tvErrorCompanyName))
                .check(matches(CoreMatchers.not(isDisplayed())))

        onView(withId(R.id.tvErrorCompanyPosition))
                .check(matches(CoreMatchers.not(isDisplayed())))

        onView(withId(R.id.tvErrorPostCode))
                .check(matches(CoreMatchers.not(isDisplayed())))

        onView(withId(R.id.tvErrorCity))
                .check(matches(CoreMatchers.not(isDisplayed())))

        onView(withId(R.id.tvErrorStreet))
                .check(matches(CoreMatchers.not(isDisplayed())))


        // Click registration button
        onView(withId(R.id.btnCreateCompany))
                .perform(ViewActions.scrollTo(), ViewActions.click())

        // Most error textviews should now be visible
        onView(withId(R.id.etTitle))
                .perform(ViewActions.scrollTo())
        onView(withId(R.id.tvErrorGender))
                .check(matches(CoreMatchers.not(isDisplayed())))

        onView(withId(R.id.etFirstName))
                .perform(ViewActions.scrollTo())
        onView(withId(R.id.tvErrorTitle))
                .check(matches(CoreMatchers.not(isDisplayed())))

        onView(withId(R.id.etLastName))
                .perform(ViewActions.scrollTo())
        onView(withId(R.id.tvErrorFirstName))
                .check(matches(isDisplayed()))
                .check(matches(ViewMatchers.withText("The first name is required")))

        onView(withId(R.id.etEmail))
                .perform(ViewActions.scrollTo())
        onView(withId(R.id.tvErrorLastName))
                .check(matches(isDisplayed()))
                .check(matches(ViewMatchers.withText("The last name is required")))

        Thread.sleep(1000)
    }

    @Test
    fun invalidInputData() {

    }

    @Test
    fun validInputData() {

    }

}