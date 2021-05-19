package team16.easytracker

import android.app.Activity
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
class DeleteTrackingTests : TestFramework() {
    @get:Rule
    val activityRule = ActivityScenarioRule(HomeActivity::class.java)

     fun getCurrentActivity(): Activity? {
        var currentActivity: Activity? = null
        InstrumentationRegistry.getInstrumentation().runOnMainSync { run { currentActivity = ActivityLifecycleMonitorRegistry.getInstance().getActivitiesInStage(Stage.RESUMED).elementAtOrNull(0) } }
        return currentActivity

    }

    @Before
    override fun setup() {
        super.setup()
        setupLoggedInWorker()
    }

    @Test
    fun testDeleteTracking()
    {
        var trackingId = dbHelper.saveTracking("TestTracking", MyApplication.loggedInWorker?.getId()!!, LocalDateTime.now(), LocalDateTime.now().plusHours(1), "description", "device")
        val currentActivity : HomeActivity = getCurrentActivity() as HomeActivity

        val createTrackingFragment = TrackingsFragment()
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
        var trackingId = dbHelper.saveTracking("TESTTRACKING", MyApplication.loggedInWorker?.getId()!!, LocalDateTime.now(), LocalDateTime.now().plusHours(1), "description", "device")
        var tracking = dbHelper.loadTracking(trackingId)
        val currentActivity : HomeActivity = getCurrentActivity() as HomeActivity

        val createTrackingFragment = TrackingsFragment()
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