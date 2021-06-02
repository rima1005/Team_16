package team16.easytracker

import android.app.Activity
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.*
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.RootMatchers.withDecorView
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.runner.lifecycle.ActivityLifecycleMonitorRegistry
import androidx.test.runner.lifecycle.Stage
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.not
import org.junit.Before
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.time.LocalDateTime


@RunWith(AndroidJUnit4::class)
class ExportTimesheetTests: TestFramework() {
    @get:Rule
    val activityRule = ActivityScenarioRule(HomeActivity::class.java)
    lateinit var activity: HomeActivity

    fun getCurrentActivity(): Activity? {
        var currentActivity: Activity? = null
        InstrumentationRegistry.getInstrumentation().runOnMainSync { run { currentActivity =
            ActivityLifecycleMonitorRegistry.getInstance().getActivitiesInStage(Stage.RESUMED)
                .elementAtOrNull(0)
        }
        }
        return currentActivity
    }

    @Before
    override fun setup() {
        super.setup()
        setupLoggedInWorker()
        activity = getCurrentActivity() as HomeActivity
    }

    private fun insertDummyTrackings(count: Int) {
        var start = LocalDateTime.now()
        for( i in 1..count) {
            val end = LocalDateTime.now()
            dbHelper.saveTracking(
                "DummyTracking$i",
                loggedInWorker.getId(),
                start,
                end,
                "",
                ""
            )
            start = end
        }
    }

    fun openFragment(enableBluetooth: Boolean = true) {
        activity.supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment, TrackingsFragment(), "Trackings Fragment")
            addToBackStack(null)
            commit()
        }
    }

    @Ignore("doesn't work yet")
    @Test
    fun exportTimesheetNotVisibleIfListEmpty() {
        openFragment()

        openActionBarOverflowOrOptionsMenu(ApplicationProvider.getApplicationContext())

        assert(MyApplication.menu!!.findItem(R.id.itemExportTimesheet).isVisible)

        onView(withText(R.string.export_timesheet))
            .perform(click())

        onView(withText(R.string.error_export))
            .inRoot(withDecorView(not(`is`(activity.window.decorView))))
            .check(matches(isDisplayed()))
    }

    @Ignore("testing external activities is hard")
    @Test
    fun exportTimesheet() {
        insertDummyTrackings(5)
        openFragment()

        openActionBarOverflowOrOptionsMenu(ApplicationProvider.getApplicationContext())
        assert(MyApplication.menu!!.findItem(R.id.itemExportTimesheet).isVisible)

        onView(withText(R.string.export_timesheet))
            .perform(click())

        //Sorry guys and gals
        //this one's not possible
        onView(withText("SAVE"))
            .perform(click())
    }
}