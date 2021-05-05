package team16.easytracker

import android.app.Activity
import androidx.fragment.app.Fragment
import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.runner.lifecycle.ActivityLifecycleMonitorRegistry
import androidx.test.runner.lifecycle.Stage
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.CoreMatchers.anything
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import team16.easytracker.database.DbHelper
import java.time.LocalDate
import java.time.LocalDateTime

@RunWith(AndroidJUnit4::class)
class DeleteTrackingTests {
    @get:Rule
    val activityRule = ActivityScenarioRule(HomeActivity::class.java)

    private fun setupLoggedInWorker() {

        val addressId = DbHelper.saveAddress("street", "1234", "city")

        val email = "emailtester1@email.at";
        val pw = "12345678"
        val workerId = DbHelper.saveWorker(
                "firstName",
                "lastName",
                LocalDate.now(),
                "title",
                email,
                pw,
                "12345678",
                LocalDateTime.now().withNano(0),
                addressId
        )

        DbHelper.loginWorker(email, pw)
    }

    fun getCurrentActivity(): Activity? {
        var currentActivity: Activity? = null
        InstrumentationRegistry.getInstrumentation().runOnMainSync { run { currentActivity = ActivityLifecycleMonitorRegistry.getInstance().getActivitiesInStage(Stage.RESUMED).elementAtOrNull(0) } }
        return currentActivity

    }


    @Before
    fun init()
    {
        //DbHelper.writableDatabase.beginTransaction()
        setupLoggedInWorker()
    }

    @After
    fun teardown()
    {
        //DbHelper.writableDatabase.endTransaction()
    }

    @Test
    fun testDeleteTracking()
    {
        var trackingId = DbHelper.saveTracking("TestTracking", MyApplication.loggedInWorker?.getId()!!, LocalDateTime.now(), LocalDateTime.now().plusHours(1), "description", "device")
        val currentActivity : HomeActivity = getCurrentActivity() as HomeActivity

        val createTrackingFragment = Trackings()
        currentActivity!!.supportFragmentManager.beginTransaction()
                .replace(R.id.flFragment, createTrackingFragment, "CreateTrackingFragment")
                .addToBackStack(null)
                .commit()

        onData(anything()).inAdapterView(withId(R.id.lvTrackings)).atPosition(0).onChildView(withId(R.id.btnDeleteTracking))
                .check(matches(isDisplayed())).perform(click())

        onView(allOf(withId(com.google.android.material.R.id.snackbar_text)))
                .check(matches(withText(R.string.success_delete_tracking)))

    }

    @Test
    fun testUndoDeleteTracking()
    {
        var trackingId = DbHelper.saveTracking("TESTTRACKING", MyApplication.loggedInWorker?.getId()!!, LocalDateTime.now(), LocalDateTime.now().plusHours(1), "description", "device")
        var tracking = DbHelper.loadTracking(trackingId)
        val currentActivity : HomeActivity = getCurrentActivity() as HomeActivity

        val createTrackingFragment = Trackings()
        currentActivity!!.supportFragmentManager.beginTransaction()
                .replace(R.id.flFragment, createTrackingFragment, "CreateTrackingFragment")
                .addToBackStack(null)
                .commit()

        onData(anything()).inAdapterView(withId(R.id.lvTrackings)).atPosition(0).onChildView(withId(R.id.btnDeleteTracking))
                .check(matches(isDisplayed())).perform(click())

        onView(allOf(withId(com.google.android.material.R.id.snackbar_text)))
                .check(matches(withText(R.string.success_delete_tracking)))


        onView(allOf(withId(com.google.android.material.R.id.snackbar_action)))
                .check(matches(withText(R.string.undo_delete_tracking))).perform(click())

        onData(anything()).inAdapterView(withId(R.id.lvTrackings)).atPosition(0).onChildView(withId(R.id.tvName))
                .check(matches(withText(tracking?.name)))

    }
}