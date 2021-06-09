package team16.easytracker

import android.app.Activity
import android.os.Bundle
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import androidx.test.runner.lifecycle.ActivityLifecycleMonitorRegistry
import androidx.test.runner.lifecycle.Stage
import org.hamcrest.CoreMatchers.*
import org.junit.*
import org.junit.runner.RunWith
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


@RunWith(AndroidJUnit4::class)
class EditTrackingTests : TestFramework() {
    @get:Rule
    val activityRule = ActivityScenarioRule(HomeActivity::class.java)

    var trackingId = 0

    private val startTime = LocalDateTime.parse("05.05.2021 12:00", DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"))
    private val endTime = LocalDateTime.parse("05.05.2021 18:00", DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"))
    private val trackingName = "Work ASD"
    private val description = ""
    private val bluetoothDevice = ""

    private fun insertDummyTracking(): Int {
        return dbHelper.saveTracking(
            trackingName, loggedInWorker.getId(),
            startTime,
            endTime,
            description,
            bluetoothDevice
        )
    }

    fun getCurrentActivity(): Activity? {
        var currentActivity: Activity? = null
        getInstrumentation().runOnMainSync { run { currentActivity = ActivityLifecycleMonitorRegistry.getInstance().getActivitiesInStage(Stage.RESUMED).elementAtOrNull(0) } }
        return currentActivity
    }

    @Before
    override fun setup() {
        super.setup()
        setupLoggedInWorker()
    }

    @Test
    fun invalidStartDateEditTracking() {
        val currentActivity : HomeActivity = getCurrentActivity() as HomeActivity
        trackingId = insertDummyTracking()
        val bundle = Bundle()
        bundle.putInt("id", trackingId)
        val editTrackingFragment = EditTrackingFragment()
        editTrackingFragment.arguments = bundle
        currentActivity.supportFragmentManager.beginTransaction()
                .replace(R.id.flFragment, editTrackingFragment, "EditTrackingFragment")
                .addToBackStack(null)
                .commit()

        onView(withId(R.id.etTrackingStartDateEdit))
                .perform(replaceText("28.04.asd.2021"), closeSoftKeyboard())

        onView(withId(R.id.etTrackingStartTimeEdit))
                .perform(replaceText("10:00"), closeSoftKeyboard())

        onView(withId(R.id.etTrackingEndDateEdit))
                .perform(replaceText("28.04.2021"), closeSoftKeyboard())

        onView(withId(R.id.etTrackingEndTimeEdit))
                .perform(replaceText("14:30"), closeSoftKeyboard())

        onView(withId(R.id.etTrackingNameEdit))
                .perform(replaceText("Some example tracking name"), closeSoftKeyboard())

        onView(withId(R.id.btnEditTrackingBack))
                .perform(scrollTo())

        onView(withId(R.id.btnUpdateTracking))
                .perform(click())

        onView(withId(R.id.tvTrackingStartDateEdit))
                .perform(scrollTo())

        onView(withId(R.id.tvErrorTrackingStartDateEdit))
                .check(matches(isDisplayed()))
                .check(matches(withText(R.string.error_invalid_start_date_format)))
    }

    @Test
    fun emptyStartDateEditTracking() {
        val currentActivity : HomeActivity = getCurrentActivity() as HomeActivity

        trackingId = insertDummyTracking()
        val bundle = Bundle()
        bundle.putInt("id", trackingId)
        val editTrackingFragment = EditTrackingFragment()
        editTrackingFragment.arguments = bundle
        currentActivity.supportFragmentManager.beginTransaction()
                .replace(R.id.flFragment, editTrackingFragment, "EditTrackingFragment")
                .addToBackStack(null)
                .commit()

        onView(withId(R.id.etTrackingStartDateEdit))
                .perform(replaceText(""), closeSoftKeyboard())

        onView(withId(R.id.etTrackingStartTimeEdit))
                .perform(replaceText("10:00"), closeSoftKeyboard())

        onView(withId(R.id.etTrackingEndDateEdit))
                .perform(replaceText("28.04.2021"), closeSoftKeyboard())

        onView(withId(R.id.etTrackingEndTimeEdit))
                .perform(replaceText("14:30"), closeSoftKeyboard())

        onView(withId(R.id.etTrackingNameEdit))
                .perform(replaceText("Some example tracking name"), closeSoftKeyboard())

        onView(withId(R.id.btnEditTrackingBack))
                .perform(scrollTo())

        onView(withId(R.id.btnUpdateTracking))
                .perform(click())

        onView(withId(R.id.tvTrackingStartDateEdit))
                .perform(scrollTo())

        onView(withId(R.id.tvErrorTrackingStartDateEdit))
                .check(matches(isDisplayed()))
                .check(matches(withText(R.string.error_start_date_required)))
    }

    @Test
    fun invalidEndDateCreateTracking() {
        val currentActivity : HomeActivity = getCurrentActivity() as HomeActivity

        trackingId = insertDummyTracking()
        val bundle = Bundle()
        bundle.putInt("id", trackingId)
        val editTrackingFragment = EditTrackingFragment()
        editTrackingFragment.arguments = bundle
        currentActivity.supportFragmentManager.beginTransaction()
                .replace(R.id.flFragment, editTrackingFragment, "EditTrackingFragment")
                .addToBackStack(null)
                .commit()

        onView(withId(R.id.etTrackingStartDateEdit))
                .perform(replaceText("28.04.2021"), closeSoftKeyboard())

        onView(withId(R.id.etTrackingStartTimeEdit))
                .perform(replaceText("10:00"), closeSoftKeyboard())

        onView(withId(R.id.etTrackingEndDateEdit))
                .perform(replaceText("28.04.asd.2021"), closeSoftKeyboard())

        onView(withId(R.id.etTrackingEndTimeEdit))
                .perform(replaceText("14:30"), closeSoftKeyboard())

        onView(withId(R.id.etTrackingNameEdit))
                .perform(replaceText("Some example tracking name"), closeSoftKeyboard())

        onView(withId(R.id.btnEditTrackingBack))
                .perform(scrollTo())

        onView(withId(R.id.btnUpdateTracking))
                .perform(click())

        onView(withId(R.id.tvTrackingEndDateEdit))
                .perform(scrollTo())

        onView(withId(R.id.tvErrorTrackingEndDateEdit))
                .check(matches(isDisplayed()))
                .check(matches(withText(R.string.error_invalid_end_date_format)))
    }

    @Test
    fun emptyEndDateEditTracking() {
        val currentActivity : HomeActivity = getCurrentActivity() as HomeActivity

        trackingId = insertDummyTracking()
        val bundle = Bundle()
        bundle.putInt("id", trackingId)
        val editTrackingFragment = EditTrackingFragment()
        editTrackingFragment.arguments = bundle
        currentActivity.supportFragmentManager.beginTransaction()
                .replace(R.id.flFragment, editTrackingFragment, "EditTrackingFragment")
                .addToBackStack(null)
                .commit()

        onView(withId(R.id.etTrackingStartDateEdit))
                .perform(replaceText("28.04.2021"), closeSoftKeyboard())

        onView(withId(R.id.etTrackingStartTimeEdit))
                .perform(replaceText("10:00"), closeSoftKeyboard())

        onView(withId(R.id.etTrackingEndDateEdit))
                .perform(replaceText(""), closeSoftKeyboard())

        onView(withId(R.id.etTrackingEndTimeEdit))
                .perform(replaceText("14:30"), closeSoftKeyboard())

        onView(withId(R.id.etTrackingNameEdit))
                .perform(replaceText("Some example tracking name"), closeSoftKeyboard())

        onView(withId(R.id.btnEditTrackingBack))
                .perform(scrollTo())

        onView(withId(R.id.btnUpdateTracking))
                .perform(click())

        onView(withId(R.id.tvTrackingEndDateEdit))
                .perform(scrollTo())

        onView(withId(R.id.tvErrorTrackingEndDateEdit))
                .check(matches(isDisplayed()))
                .check(matches(withText(R.string.error_end_date_required)))
    }

    @Test
    fun invalidStartTimeEditTracking() {
        val currentActivity : HomeActivity = getCurrentActivity() as HomeActivity

        trackingId = insertDummyTracking()
        val bundle = Bundle()
        bundle.putInt("id", trackingId)
        val editTrackingFragment = EditTrackingFragment()
        editTrackingFragment.arguments = bundle
        currentActivity.supportFragmentManager.beginTransaction()
                .replace(R.id.flFragment, editTrackingFragment, "EditTrackingFragment")
                .addToBackStack(null)
                .commit()

        onView(withId(R.id.etTrackingStartDateEdit))
                .perform(replaceText("28.04.2021"), closeSoftKeyboard())

        onView(withId(R.id.etTrackingStartTimeEdit))
                .perform(replaceText("10asdf:asdf:99"), closeSoftKeyboard())

        onView(withId(R.id.etTrackingEndDateEdit))
                .perform(replaceText("28.04.2021"), closeSoftKeyboard())

        onView(withId(R.id.etTrackingEndTimeEdit))
                .perform(replaceText("14:30"), closeSoftKeyboard())

        onView(withId(R.id.etTrackingNameEdit))
                .perform(replaceText("Some example tracking name"), closeSoftKeyboard())

        onView(withId(R.id.btnEditTrackingBack))
                .perform(scrollTo())

        onView(withId(R.id.btnUpdateTracking))
                .perform(click())

        onView(withId(R.id.tvTrackingStartTimeEdit))
                .perform(scrollTo())

        onView(withId(R.id.tvErrorTrackingStartTimeEdit))
                .check(matches(isDisplayed()))
                .check(matches(withText(R.string.error_invalid_start_time)))
    }

    @Test
    fun emptyStartTimeEditTracking() {
        val currentActivity : HomeActivity = getCurrentActivity() as HomeActivity

        trackingId = insertDummyTracking()
        val bundle = Bundle()
        bundle.putInt("id", trackingId)
        val editTrackingFragment = EditTrackingFragment()
        editTrackingFragment.arguments = bundle
        currentActivity.supportFragmentManager.beginTransaction()
                .replace(R.id.flFragment, editTrackingFragment, "EditTrackingFragment")
                .addToBackStack(null)
                .commit()

        onView(withId(R.id.etTrackingStartDateEdit))
                .perform(replaceText("28.04.2021"), closeSoftKeyboard())

        onView(withId(R.id.etTrackingStartTimeEdit))
                .perform(replaceText(""), closeSoftKeyboard())


        onView(withId(R.id.etTrackingEndDateEdit))
                .perform(replaceText("28.04.2021"), closeSoftKeyboard())

        onView(withId(R.id.etTrackingEndTimeEdit))
                .perform(replaceText("14:00"), closeSoftKeyboard())

        onView(withId(R.id.etTrackingNameEdit))
                .perform(replaceText("Some example tracking name"), closeSoftKeyboard())

        onView(withId(R.id.btnEditTrackingBack))
                .perform(scrollTo())

        onView(withId(R.id.btnUpdateTracking))
                .perform(click())

        onView(withId(R.id.tvTrackingStartTimeEdit))
                .perform(scrollTo())

        onView(withId(R.id.tvErrorTrackingStartTimeEdit))
                .check(matches(isDisplayed()))
                .check(matches(withText(R.string.error_start_time_required)))
    }

    @Test
    fun invalidEndTimeEditTracking() {
        val currentActivity : HomeActivity = getCurrentActivity() as HomeActivity

        trackingId = insertDummyTracking()
        val bundle = Bundle()
        bundle.putInt("id", trackingId)
        val editTrackingFragment = EditTrackingFragment()
        editTrackingFragment.arguments = bundle
        currentActivity.supportFragmentManager.beginTransaction()
                .replace(R.id.flFragment, editTrackingFragment, "EditTrackingFragment")
                .addToBackStack(null)
                .commit()

        onView(withId(R.id.etTrackingStartDateEdit))
                .perform(replaceText("28.04.2021"), closeSoftKeyboard())

        onView(withId(R.id.etTrackingStartTimeEdit))
                .perform(replaceText("10:00"), closeSoftKeyboard())

        onView(withId(R.id.etTrackingEndDateEdit))
                .perform(replaceText("28.04.2021"), closeSoftKeyboard())

        onView(withId(R.id.etTrackingEndTimeEdit))
                .perform(replaceText("10asdf:asdf:99"), closeSoftKeyboard())

        onView(withId(R.id.etTrackingNameEdit))
                .perform(replaceText("Some example tracking name"), closeSoftKeyboard())

        onView(withId(R.id.btnEditTrackingBack))
                .perform(scrollTo())

        onView(withId(R.id.btnUpdateTracking))
                .perform(click())

        onView(withId(R.id.tvTrackingEndTimeEdit))
                .perform(scrollTo())

        onView(withId(R.id.tvErrorTrackingEndTimeEdit))
                .check(matches(isDisplayed()))
                .check(matches(withText(R.string.error_invalid_end_time)))
    }

    @Test
    fun emptyEndTimeEditTracking() {
        val currentActivity : HomeActivity = getCurrentActivity() as HomeActivity

        trackingId = insertDummyTracking()
        val bundle = Bundle()
        bundle.putInt("id", trackingId)
        val editTrackingFragment = EditTrackingFragment()
        editTrackingFragment.arguments = bundle
        currentActivity.supportFragmentManager.beginTransaction()
                .replace(R.id.flFragment, editTrackingFragment, "EditTrackingFragment")
                .addToBackStack(null)
                .commit()

        onView(withId(R.id.etTrackingStartDateEdit))
                .perform(replaceText("28.04.2021"), closeSoftKeyboard())

        onView(withId(R.id.etTrackingStartTimeEdit))
                .perform(replaceText("10:00"), closeSoftKeyboard())

        onView(withId(R.id.etTrackingEndDateEdit))
                .perform(replaceText("28.04.2021"), closeSoftKeyboard())

        onView(withId(R.id.etTrackingEndTimeEdit))
                .perform(replaceText(""), closeSoftKeyboard())

        onView(withId(R.id.etTrackingNameEdit))
                .perform(replaceText("Some example tracking name"), closeSoftKeyboard())

        onView(withId(R.id.btnEditTrackingBack))
                .perform(scrollTo())

        onView(withId(R.id.btnUpdateTracking))
                .perform(click())

        onView(withId(R.id.tvTrackingEndTimeEdit))
                .perform(scrollTo())

        onView(withId(R.id.tvErrorTrackingEndTimeEdit))
                .check(matches(isDisplayed()))
                .check(matches(withText(R.string.error_end_time_required)))
    }

    @Test
    fun emptyTrackingNameEditTracking() {
        val currentActivity : HomeActivity = getCurrentActivity() as HomeActivity

        trackingId = insertDummyTracking()
        val bundle = Bundle()
        bundle.putInt("id", trackingId)
        val editTrackingFragment = EditTrackingFragment()
        editTrackingFragment.arguments = bundle
        currentActivity.supportFragmentManager.beginTransaction()
                .replace(R.id.flFragment, editTrackingFragment, "EditTrackingFragment")
                .addToBackStack(null)
                .commit()

        onView(withId(R.id.etTrackingStartDateEdit))
                .perform(replaceText("28.04.2021"), closeSoftKeyboard())

        onView(withId(R.id.etTrackingStartTimeEdit))
                .perform(replaceText("10:00"), closeSoftKeyboard())

        onView(withId(R.id.etTrackingEndDateEdit))
                .perform(replaceText("28.04.2021"), closeSoftKeyboard())

        onView(withId(R.id.etTrackingEndTimeEdit))
                .perform(replaceText("14:00"), closeSoftKeyboard())

        onView(withId(R.id.etTrackingNameEdit))
                .perform(replaceText(""), closeSoftKeyboard())

        onView(withId(R.id.btnEditTrackingBack))
                .perform(scrollTo())

        onView(withId(R.id.btnUpdateTracking))
                .perform(click())

        onView(withId(R.id.tvLabelTrackingNameEdit))
                .perform(scrollTo())

        onView(withId(R.id.tvErrorTrackingNameEdit))
                .check(matches(isDisplayed()))
                .check(matches(withText(R.string.error_tracking_name_required)))
    }

    @Test
    fun emptyInputEditTracking() {
        val currentActivity : HomeActivity = getCurrentActivity() as HomeActivity

        trackingId = insertDummyTracking()
        val bundle = Bundle()
        bundle.putInt("id", trackingId)
        val editTrackingFragment = EditTrackingFragment()
        editTrackingFragment.arguments = bundle
        currentActivity.supportFragmentManager.beginTransaction()
                .replace(R.id.flFragment, editTrackingFragment, "EditTrackingFragment")
                .addToBackStack(null)
                .commit()


        onView(withId(R.id.tvErrorTrackingStartDateEdit))
                .check(matches(not(isDisplayed())))

        onView(withId(R.id.tvErrorTrackingStartTimeEdit))
                .check(matches(not(isDisplayed())))

        onView(withId(R.id.tvErrorTrackingEndDateEdit))
                .check(matches(not(isDisplayed())))

        onView(withId(R.id.tvErrorTrackingEndTimeEdit))
                .check(matches(not(isDisplayed())))

        onView(withId(R.id.tvErrorTrackingNameEdit))
                .check(matches(not(isDisplayed())))



        onView(withId(R.id.etTrackingStartDateEdit))
                .perform(replaceText(""), closeSoftKeyboard())

        onView(withId(R.id.etTrackingStartTimeEdit))
                .perform(replaceText(""), closeSoftKeyboard())

        onView(withId(R.id.etTrackingEndDateEdit))
                .perform(replaceText(""), closeSoftKeyboard())

        onView(withId(R.id.etTrackingEndTimeEdit))
                .perform(replaceText(""), closeSoftKeyboard())

        onView(withId(R.id.etTrackingNameEdit))
                .perform(replaceText(""), closeSoftKeyboard())

        onView(withId(R.id.btnEditTrackingBack))
                .perform(scrollTo())

        onView(withId(R.id.btnUpdateTracking))
                .perform(click())

        onView(withId(R.id.tvTrackingStartDateEdit))
                .perform(scrollTo())
        onView(withId(R.id.tvErrorTrackingStartDateEdit))
                .check(matches(isDisplayed()))
                .check(matches(withText(R.string.error_start_date_required)))

        onView(withId(R.id.tvTrackingStartTimeEdit))
                .perform(scrollTo())
        onView(withId(R.id.tvErrorTrackingStartTimeEdit))
                .check(matches(isDisplayed()))
                .check(matches(withText(R.string.error_start_time_required)))

        onView(withId(R.id.tvTrackingEndDateEdit))
                .perform(scrollTo())
        onView(withId(R.id.tvErrorTrackingEndDateEdit))
                .check(matches(isDisplayed()))
                .check(matches(withText(R.string.error_end_date_required)))

        onView(withId(R.id.tvTrackingEndTimeEdit))
                .perform(scrollTo())
        onView(withId(R.id.tvErrorTrackingEndTimeEdit))
                .check(matches(isDisplayed()))
                .check(matches(withText(R.string.error_end_time_required)))

        onView(withId(R.id.tvLabelTrackingNameEdit))
                .perform(scrollTo())
        onView(withId(R.id.tvErrorTrackingNameEdit))
                .check(matches(isDisplayed()))
                .check(matches(withText(R.string.error_tracking_name_required)))
    }

    @Test
    fun validEditTracking() {
        val currentActivity : HomeActivity = getCurrentActivity() as HomeActivity

        trackingId = insertDummyTracking()
        val bundle = Bundle()
        bundle.putInt("id", trackingId)
        val editTrackingFragment = EditTrackingFragment()
        editTrackingFragment.arguments = bundle
        currentActivity.supportFragmentManager.beginTransaction()
                .replace(R.id.flFragment, editTrackingFragment, "EditTrackingFragment")
                .addToBackStack(null)
                .commit()

        val newStartDate = "28.04.2021"
        onView(withId(R.id.etTrackingStartDateEdit))
                .perform(replaceText(newStartDate), closeSoftKeyboard())

        val newStartTime = "10:00"
        onView(withId(R.id.etTrackingStartTimeEdit))
                .perform(replaceText(newStartTime), closeSoftKeyboard())

        val newEndDate = "28.04.2021"
        onView(withId(R.id.etTrackingEndDateEdit))
                .perform(replaceText(newEndDate), closeSoftKeyboard())

        val newEndTime = "14:00"
        onView(withId(R.id.etTrackingEndTimeEdit))
                .perform(replaceText(newEndTime), closeSoftKeyboard())

        val newName = "Some example tracking"
        onView(withId(R.id.etTrackingNameEdit))
                .perform(replaceText(newName), closeSoftKeyboard())

        onView(withId(R.id.btnEditTrackingBack))
                .perform(scrollTo())

        onView(withId(R.id.btnUpdateTracking))
                .perform(click())

        val updatedTracking = dbHelper.loadTracking(trackingId)!!
        val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")
        assert(updatedTracking.startTime == LocalDateTime.parse("$newStartDate $newStartTime", formatter))
        assert(updatedTracking.endTime == LocalDateTime.parse("$newEndDate $newEndTime", formatter))
        assert(updatedTracking.name == newName)
    }

    @Test
    fun endDateBeforeStartDate()
    {
        val currentActivity : HomeActivity = getCurrentActivity() as HomeActivity

        trackingId = insertDummyTracking()
        val bundle = Bundle()
        bundle.putInt("id", trackingId)
        val editTrackingFragment = EditTrackingFragment()
        editTrackingFragment.arguments = bundle
        currentActivity.supportFragmentManager.beginTransaction()
            .replace(R.id.flFragment, editTrackingFragment, "EditTrackingFragment")
            .addToBackStack(null)
            .commit()

        onView(withId(R.id.etTrackingStartDateEdit))
            .perform(replaceText("28.04.2021"), closeSoftKeyboard())

        onView(withId(R.id.etTrackingStartTimeEdit))
            .perform(replaceText("10:00"), closeSoftKeyboard())

        onView(withId(R.id.etTrackingEndDateEdit))
            .perform(replaceText("28.04.2021"), closeSoftKeyboard())

        onView(withId(R.id.etTrackingEndTimeEdit))
            .perform(replaceText("09:59"), closeSoftKeyboard())

        onView(withId(R.id.etTrackingNameEdit))
            .perform(replaceText("Some example tracking name"), closeSoftKeyboard())

        onView(withId(R.id.btnEditTrackingBack))
            .perform(scrollTo())

        onView(withId(R.id.btnUpdateTracking))
            .perform(click())

        onView(allOf(withId(com.google.android.material.R.id.snackbar_text)))
            .check(matches(withText(R.string.end_date_before_start_date)))
        Thread.sleep(1000)


    }
}