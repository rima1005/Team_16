package team16.easytracker

import android.app.Activity
import android.app.NotificationManager
import android.content.Context
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.runner.lifecycle.ActivityLifecycleMonitorRegistry
import androidx.test.runner.lifecycle.Stage
import org.hamcrest.CoreMatchers
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class NotificationTests : TestFramework() {

    @Before
    override fun setup() {
        super.setup()
        setupLoggedInWorker()
    }

    @get:Rule
    val activityRule = ActivityScenarioRule(HomeActivity::class.java)

    fun openFragment() {
        val activity = getCurrentActivity() as HomeActivity
        val transaction = activity.supportFragmentManager.beginTransaction()
        transaction.replace(R.id.flFragment, DashboardFragment())
        transaction.disallowAddToBackStack()
        transaction.commit()
    }

    fun getCurrentActivity(): Activity {
        var currentActivity: Activity? = null
        InstrumentationRegistry.getInstrumentation().runOnMainSync { run { currentActivity =
            ActivityLifecycleMonitorRegistry.getInstance().getActivitiesInStage(Stage.RESUMED)
                .elementAtOrNull(0)
        }
        }
        return currentActivity!!
    }

    @Test
    fun testOpenFragment() {
        openFragment()
        Espresso.onView(ViewMatchers.withId(R.id.flFragmentDashboard))
            .check(ViewAssertions.matches((ViewMatchers.isDisplayed())))
    }

    @Test
    fun validStartTracking(){
        openFragment()

        Espresso.onView(ViewMatchers.withId(R.id.tvLabelActiveTracking))
            .check(ViewAssertions.matches(CoreMatchers.not(ViewMatchers.isDisplayed())))

        Espresso.onView(ViewMatchers.withId(R.id.tvActiveTracking))
            .check(ViewAssertions.matches(CoreMatchers.not(ViewMatchers.isDisplayed())))

        Espresso.onView(ViewMatchers.withId(R.id.btnStopTracking))
            .check(ViewAssertions.matches(CoreMatchers.not(ViewMatchers.isDisplayed())))

        Espresso.onView(ViewMatchers.withId(R.id.btnStartTracking))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
            .perform(ViewActions.click())

        Espresso.onView(ViewMatchers.withText(R.string.tracking_name))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        Espresso.onView(ViewMatchers.withId(R.id.etActiveTrackingName))
            .perform(ViewActions.typeText("Example tracking"), ViewActions.closeSoftKeyboard())

        Espresso.onView(ViewMatchers.withText(android.R.string.ok))
            .perform(ViewActions.click())

        Thread.sleep(1000);

        var exists = false
        val notificationManager : NotificationManager = MyApplication.instance.getSystemService(
            Context.NOTIFICATION_SERVICE) as NotificationManager
        var notifications = notificationManager!!.activeNotifications
        for (notification in notifications) {
            if (notification.id == 1) {
                exists = true
            }
        }

        assert(exists)


        Espresso.onView(ViewMatchers.withId(R.id.btnStopTracking))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
            .perform(ViewActions.click())

        Espresso.onView(ViewMatchers.withText(android.R.string.ok))
            .perform(ViewActions.click())

        Thread.sleep(1000)

        exists = false
        notifications = notificationManager!!.activeNotifications
        for (notification in notifications) {
            if (notification.id == 1) {
                exists = true
            }
        }

        assert(!exists)
    }

    //TODO: Notification Click can not be tested via Espresso tests

}