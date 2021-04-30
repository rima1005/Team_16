package team16.easytracker

import android.app.Activity
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
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import androidx.test.runner.lifecycle.ActivityLifecycleMonitorRegistry
import androidx.test.runner.lifecycle.Stage
import com.google.android.material.internal.NavigationMenu
import com.google.android.material.internal.NavigationMenuItemView
import org.hamcrest.CoreMatchers.*
import org.junit.*
import org.junit.runner.RunWith
import team16.easytracker.database.Contracts
import team16.easytracker.database.DbHelper
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


@RunWith(AndroidJUnit4::class)
class CreateTrackingTests {
    @get:Rule
    val activityRule = ActivityScenarioRule(HomeActivity::class.java)

    fun getCurrentActivity(): Activity? {
        var currentActivity: Activity? = null
        getInstrumentation().runOnMainSync { run { currentActivity = ActivityLifecycleMonitorRegistry.getInstance().getActivitiesInStage(Stage.RESUMED).elementAtOrNull(0) } }
        return currentActivity
    }

    @Test
    fun invalidStartDateCreateTracking() {
        val currentActivity : HomeActivity = getCurrentActivity() as HomeActivity

        val createTrackingFragment = Trackings()
        currentActivity!!.supportFragmentManager.beginTransaction()
                .replace(R.id.flFragment, createTrackingFragment, "CreateTrackingFragment")
                .addToBackStack(null)
                .commit()

        onView(withId(R.id.btnCreateTracking))
                .perform(click())

        onView(withId(R.id.etTrackingStartDate))
                .perform(typeText("28.04.asd.2021"), closeSoftKeyboard())

        onView(withId(R.id.etTrackingStartTime))
                .perform(typeText("10:00"), closeSoftKeyboard())

        onView(withId(R.id.etTrackingEndDate))
                .perform(typeText("28.04.2021"), closeSoftKeyboard())

        onView(withId(R.id.etTrackingEndTime))
                .perform(typeText("14:30"), closeSoftKeyboard())

        onView(withId(R.id.etTrackingName))
                .perform(typeText("Some example tracking name"), closeSoftKeyboard())

        onView(withId(R.id.btnCreateTrackingBack))
                .perform(scrollTo())

        onView(withId(R.id.btnCreateTrackingSave))
                .perform(click())

        onView(withId(R.id.tvTrackingStartDate))
                .perform(scrollTo())

        onView(withId(R.id.tvErrorTrackingStartDate))
                .check(matches(isDisplayed()))
                .check(matches(withText("The start date must be of format DD.MM.YYYY")))
    }

    @Test
    fun emptyStartDateCreateTracking() {
        val currentActivity : HomeActivity = getCurrentActivity() as HomeActivity

        val createTrackingFragment = Trackings()
        currentActivity!!.supportFragmentManager.beginTransaction()
                .replace(R.id.flFragment, createTrackingFragment, "CreateTrackingFragment")
                .addToBackStack(null)
                .commit()

        onView(withId(R.id.btnCreateTracking))
                .perform(click())

        onView(withId(R.id.etTrackingStartTime))
                .perform(typeText("10:00"), closeSoftKeyboard())

        onView(withId(R.id.etTrackingEndDate))
                .perform(typeText("28.04.2021"), closeSoftKeyboard())

        onView(withId(R.id.etTrackingEndTime))
                .perform(typeText("14:30"), closeSoftKeyboard())

        onView(withId(R.id.etTrackingName))
                .perform(typeText("Some example tracking name"), closeSoftKeyboard())

        onView(withId(R.id.btnCreateTrackingBack))
                .perform(scrollTo())

        onView(withId(R.id.btnCreateTrackingSave))
                .perform(click())

        onView(withId(R.id.tvTrackingStartDate))
                .perform(scrollTo())

        onView(withId(R.id.tvErrorTrackingStartDate))
                .check(matches(isDisplayed()))
                .check(matches(withText("The start date is required")))
    }

    @Test
    fun invalidEndDateCreateTracking() {
        val currentActivity : HomeActivity = getCurrentActivity() as HomeActivity

        val createTrackingFragment = Trackings()
        currentActivity!!.supportFragmentManager.beginTransaction()
                .replace(R.id.flFragment, createTrackingFragment, "CreateTrackingFragment")
                .addToBackStack(null)
                .commit()

        onView(withId(R.id.btnCreateTracking))
                .perform(click())

        onView(withId(R.id.etTrackingStartDate))
                .perform(typeText("28.04.2021"), closeSoftKeyboard())

        onView(withId(R.id.etTrackingStartTime))
                .perform(typeText("10:00"), closeSoftKeyboard())

        onView(withId(R.id.etTrackingEndDate))
                .perform(typeText("28.04.asd.2021"), closeSoftKeyboard())

        onView(withId(R.id.etTrackingEndTime))
                .perform(typeText("14:30"), closeSoftKeyboard())

        onView(withId(R.id.etTrackingName))
                .perform(typeText("Some example tracking name"), closeSoftKeyboard())

        onView(withId(R.id.btnCreateTrackingBack))
                .perform(scrollTo())

        onView(withId(R.id.btnCreateTrackingSave))
                .perform(click())

        onView(withId(R.id.tvTrackingEndDate))
                .perform(scrollTo())

        onView(withId(R.id.tvErrorTrackingEndDate))
                .check(matches(isDisplayed()))
                .check(matches(withText("The end date must be of format DD.MM.YYYY")))
    }

    @Test
    fun emptyEndDateCreateTracking() {
        val currentActivity : HomeActivity = getCurrentActivity() as HomeActivity

        val createTrackingFragment = Trackings()
        currentActivity!!.supportFragmentManager.beginTransaction()
                .replace(R.id.flFragment, createTrackingFragment, "CreateTrackingFragment")
                .addToBackStack(null)
                .commit()

        onView(withId(R.id.btnCreateTracking))
                .perform(click())

        onView(withId(R.id.etTrackingStartDate))
                .perform(typeText("28.04.2021"), closeSoftKeyboard())

        onView(withId(R.id.etTrackingStartTime))
                .perform(typeText("10:00"), closeSoftKeyboard())

        onView(withId(R.id.etTrackingEndTime))
                .perform(typeText("14:30"), closeSoftKeyboard())

        onView(withId(R.id.etTrackingName))
                .perform(typeText("Some example tracking name"), closeSoftKeyboard())

        onView(withId(R.id.btnCreateTrackingBack))
                .perform(scrollTo())

        onView(withId(R.id.btnCreateTrackingSave))
                .perform(click())

        onView(withId(R.id.tvTrackingEndDate))
                .perform(scrollTo())

        onView(withId(R.id.tvErrorTrackingEndDate))
                .check(matches(isDisplayed()))
                .check(matches(withText("The end date is required")))
    }

    @Test
    fun invalidStartTimeCreateTracking() {
        val currentActivity : HomeActivity = getCurrentActivity() as HomeActivity

        val createTrackingFragment = Trackings()
        currentActivity!!.supportFragmentManager.beginTransaction()
                .replace(R.id.flFragment, createTrackingFragment, "CreateTrackingFragment")
                .addToBackStack(null)
                .commit()

        onView(withId(R.id.btnCreateTracking))
                .perform(click())

        onView(withId(R.id.etTrackingStartDate))
                .perform(typeText("28.04.2021"), closeSoftKeyboard())

        onView(withId(R.id.etTrackingStartTime))
                .perform(typeText("10asdf:asdf:99"), closeSoftKeyboard())

        onView(withId(R.id.etTrackingEndDate))
                .perform(typeText("28.04.2021"), closeSoftKeyboard())

        onView(withId(R.id.etTrackingEndTime))
                .perform(typeText("14:30"), closeSoftKeyboard())

        onView(withId(R.id.etTrackingName))
                .perform(typeText("Some example tracking name"), closeSoftKeyboard())

        onView(withId(R.id.btnCreateTrackingBack))
                .perform(scrollTo())

        onView(withId(R.id.btnCreateTrackingSave))
                .perform(click())

        onView(withId(R.id.tvTrackingStartTime))
                .perform(scrollTo())

        onView(withId(R.id.tvErrorTrackingStartTime))
                .check(matches(isDisplayed()))
                .check(matches(withText("The start time must be of format mm:hh")))
    }

}