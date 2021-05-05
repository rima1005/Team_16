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
class EditTrackingTests {
    @get:Rule
    val activityRule = ActivityScenarioRule(HomeActivity::class.java)

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

    val startTime = LocalDateTime.parse("05.05.2021 12:00", DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"))
    val endTime = LocalDateTime.parse("05.05.2021 18:00", DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"))
    val trackingName = "Work ASD"
    val description = ""
    val bluetoothDevice = ""

    private fun insertDummyTracking(): Int {

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
                addressId
        )

        val trackingId = DbHelper.saveTracking(trackingName, workerId, startTime, endTime, description, bluetoothDevice)

        return trackingId
    }

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

    fun getCurrentActivity(): Activity? {
        var currentActivity: Activity? = null
        getInstrumentation().runOnMainSync { run { currentActivity = ActivityLifecycleMonitorRegistry.getInstance().getActivitiesInStage(Stage.RESUMED).elementAtOrNull(0) } }
        return currentActivity
    }

    @Ignore
    @Test
    fun invalidStartDateEditTracking() {
        val currentActivity : HomeActivity = getCurrentActivity() as HomeActivity

        val editTrackingFragment = EditTrackingFragment()
        currentActivity!!.supportFragmentManager.beginTransaction()
                .replace(R.id.flFragment, editTrackingFragment, "EditTrackingFragment")
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

        onView(withId(R.id.btnEditTrackingBack))
                .perform(scrollTo())

        onView(withId(R.id.btnUpdateTracking))
                .perform(click())

        onView(withId(R.id.tvTrackingStartDate))
                .perform(scrollTo())

        onView(withId(R.id.tvErrorTrackingStartDate))
                .check(matches(isDisplayed()))
                .check(matches(withText("The start date must be of format DD.MM.YYYY")))
    }

    @Ignore
    @Test
    fun emptyStartDateEditTracking() {
        val currentActivity : HomeActivity = getCurrentActivity() as HomeActivity

        val editTrackingFragment = EditTrackingFragment()
        currentActivity!!.supportFragmentManager.beginTransaction()
                .replace(R.id.flFragment, editTrackingFragment, "EditTrackingFragment")
                .addToBackStack(null)
                .commit()

        onView(withId(R.id.btnEditTracking))
                .perform(click())

        onView(withId(R.id.etTrackingStartTimeEdit))
                .perform(typeText("10:00"), closeSoftKeyboard())

        onView(withId(R.id.etTrackingEndDateEdit))
                .perform(typeText("28.04.2021"), closeSoftKeyboard())

        onView(withId(R.id.etTrackingEndTimeEdit))
                .perform(typeText("14:30"), closeSoftKeyboard())

        onView(withId(R.id.etTrackingNameEdit))
                .perform(typeText("Some example tracking name"), closeSoftKeyboard())

        onView(withId(R.id.btnEditTrackingBack))
                .perform(scrollTo())

        onView(withId(R.id.btnUpdateTracking))
                .perform(click())

        onView(withId(R.id.tvTrackingStartDateEdit))
                .perform(scrollTo())

        onView(withId(R.id.tvErrorTrackingStartDateEdit))
                .check(matches(isDisplayed()))
                .check(matches(withText("The start date is required")))
    }

    @Ignore
    @Test
    fun invalidEndDateCreateTracking() {
        val currentActivity : HomeActivity = getCurrentActivity() as HomeActivity

        val editTrackingFragment = EditTrackingFragment()
        currentActivity!!.supportFragmentManager.beginTransaction()
                .replace(R.id.flFragment, editTrackingFragment, "EditTrackingFragment")
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

        onView(withId(R.id.btnEditTrackingBack))
                .perform(scrollTo())

        onView(withId(R.id.btnUpdateTracking))
                .perform(click())

        onView(withId(R.id.tvTrackingEndDate))
                .perform(scrollTo())

        onView(withId(R.id.tvErrorTrackingEndDate))
                .check(matches(isDisplayed()))
                .check(matches(withText("The end date must be of format DD.MM.YYYY")))
    }

    @Ignore
    @Test
    fun emptyEndDateEditTracking() {
        val currentActivity : HomeActivity = getCurrentActivity() as HomeActivity

        val editTrackingFragment = EditTrackingFragment()
        currentActivity!!.supportFragmentManager.beginTransaction()
                .replace(R.id.flFragment, editTrackingFragment, "EditTrackingFragment")
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

        onView(withId(R.id.btnEditTrackingBack))
                .perform(scrollTo())

        onView(withId(R.id.btnUpdateTracking))
                .perform(click())

        onView(withId(R.id.tvTrackingEndDate))
                .perform(scrollTo())

        onView(withId(R.id.tvErrorTrackingEndDate))
                .check(matches(isDisplayed()))
                .check(matches(withText("The end date is required")))
    }

    @Ignore
    @Test
    fun invalidStartTimeEditTracking() {
        val currentActivity : HomeActivity = getCurrentActivity() as HomeActivity

        val editTrackingFragment = EditTrackingFragment()
        currentActivity!!.supportFragmentManager.beginTransaction()
                .replace(R.id.flFragment, editTrackingFragment, "EditTrackingFragment")
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

        onView(withId(R.id.btnEditTrackingBack))
                .perform(scrollTo())

        onView(withId(R.id.btnUpdateTracking))
                .perform(click())

        onView(withId(R.id.tvTrackingStartTime))
                .perform(scrollTo())

        onView(withId(R.id.tvErrorTrackingStartTime))
                .check(matches(isDisplayed()))
                .check(matches(withText("The start time must be of format H:mm")))
    }

    @Ignore
    @Test
    fun emptyStartTimeEditTracking() {
        val currentActivity : HomeActivity = getCurrentActivity() as HomeActivity

        val editTrackingFragment = EditTrackingFragment()
        currentActivity!!.supportFragmentManager.beginTransaction()
                .replace(R.id.flFragment, editTrackingFragment, "EditTrackingFragment")
                .addToBackStack(null)
                .commit()

        onView(withId(R.id.btnCreateTracking))
                .perform(click())

        onView(withId(R.id.etTrackingStartDate))
                .perform(typeText("28.04.2021"), closeSoftKeyboard())


        onView(withId(R.id.etTrackingEndDate))
                .perform(typeText("28.04.2021"), closeSoftKeyboard())

        onView(withId(R.id.etTrackingEndTime))
                .perform(typeText("14:00"), closeSoftKeyboard())

        onView(withId(R.id.etTrackingName))
                .perform(typeText("Some example tracking name"), closeSoftKeyboard())

        onView(withId(R.id.btnEditTrackingBack))
                .perform(scrollTo())

        onView(withId(R.id.btnUpdateTracking))
                .perform(click())

        onView(withId(R.id.tvTrackingStartTime))
                .perform(scrollTo())

        onView(withId(R.id.tvErrorTrackingStartTime))
                .check(matches(isDisplayed()))
                .check(matches(withText("The start time is required")))
    }

    @Ignore
    @Test
    fun invalidEndTimeEditTracking() {
        val currentActivity : HomeActivity = getCurrentActivity() as HomeActivity

        val editTrackingFragment = EditTrackingFragment()
        currentActivity!!.supportFragmentManager.beginTransaction()
                .replace(R.id.flFragment, editTrackingFragment, "EditTrackingFragment")
                .addToBackStack(null)
                .commit()

        onView(withId(R.id.btnCreateTracking))
                .perform(click())

        onView(withId(R.id.etTrackingStartDate))
                .perform(typeText("28.04.2021"), closeSoftKeyboard())

        onView(withId(R.id.etTrackingStartTime))
                .perform(typeText("10:00"), closeSoftKeyboard())

        onView(withId(R.id.etTrackingEndDate))
                .perform(typeText("28.04.2021"), closeSoftKeyboard())

        onView(withId(R.id.etTrackingEndTime))
                .perform(typeText("10asdf:asdf:99"), closeSoftKeyboard())

        onView(withId(R.id.etTrackingName))
                .perform(typeText("Some example tracking name"), closeSoftKeyboard())

        onView(withId(R.id.btnEditTrackingBack))
                .perform(scrollTo())

        onView(withId(R.id.btnUpdateTracking))
                .perform(click())

        onView(withId(R.id.tvTrackingEndTime))
                .perform(scrollTo())

        onView(withId(R.id.tvErrorTrackingEndTime))
                .check(matches(isDisplayed()))
                .check(matches(withText("The end time must be of format H:mm")))
    }

    @Ignore
    @Test
    fun emptyEndTimeEditTracking() {
        val currentActivity : HomeActivity = getCurrentActivity() as HomeActivity

        val editTrackingFragment = EditTrackingFragment()
        currentActivity!!.supportFragmentManager.beginTransaction()
                .replace(R.id.flFragment, editTrackingFragment, "EditTrackingFragment")
                .addToBackStack(null)
                .commit()

        onView(withId(R.id.btnCreateTracking))
                .perform(click())

        onView(withId(R.id.etTrackingStartDate))
                .perform(typeText("28.04.2021"), closeSoftKeyboard())

        onView(withId(R.id.etTrackingStartTime))
                .perform(typeText("10:00"), closeSoftKeyboard())

        onView(withId(R.id.etTrackingEndDate))
                .perform(typeText("28.04.2021"), closeSoftKeyboard())

        onView(withId(R.id.etTrackingName))
                .perform(typeText("Some example tracking name"), closeSoftKeyboard())

        onView(withId(R.id.btnEditTrackingBack))
                .perform(scrollTo())

        onView(withId(R.id.btnUpdateTracking))
                .perform(click())

        onView(withId(R.id.tvTrackingEndTime))
                .perform(scrollTo())

        onView(withId(R.id.tvErrorTrackingEndTime))
                .check(matches(isDisplayed()))
                .check(matches(withText("The end time is required")))
    }

    @Ignore
    @Test
    fun emptyTrackingNameEditTracking() {
        val currentActivity : HomeActivity = getCurrentActivity() as HomeActivity

        val editTrackingFragment = EditTrackingFragment()
        currentActivity!!.supportFragmentManager.beginTransaction()
                .replace(R.id.flFragment, editTrackingFragment, "EditTrackingFragment")
                .addToBackStack(null)
                .commit()

        onView(withId(R.id.btnCreateTracking))
                .perform(click())

        onView(withId(R.id.etTrackingStartDate))
                .perform(typeText("28.04.2021"), closeSoftKeyboard())

        onView(withId(R.id.etTrackingStartTime))
                .perform(typeText("10:00"), closeSoftKeyboard())

        onView(withId(R.id.etTrackingEndDate))
                .perform(typeText("28.04.2021"), closeSoftKeyboard())

        onView(withId(R.id.etTrackingEndTime))
                .perform(typeText("14:00"), closeSoftKeyboard())

        onView(withId(R.id.btnEditTrackingBack))
                .perform(scrollTo())

        onView(withId(R.id.btnUpdateTracking))
                .perform(click())

        onView(withId(R.id.tvLabelTrackingName))
                .perform(scrollTo())

        onView(withId(R.id.tvErrorTrackingName))
                .check(matches(isDisplayed()))
                .check(matches(withText("The tracking name is required")))
    }

    @Ignore
    @Test
    fun emptyInputEditTracking() {
        val currentActivity : HomeActivity = getCurrentActivity() as HomeActivity

        val editTrackingFragment = EditTrackingFragment()
        currentActivity!!.supportFragmentManager.beginTransaction()
                .replace(R.id.flFragment, editTrackingFragment, "EditTrackingFragment")
                .addToBackStack(null)
                .commit()

        onView(withId(R.id.btnCreateTracking))
                .perform(click())

        onView(withId(R.id.tvErrorTrackingStartDate))
                .check(matches(not(isDisplayed())))

        onView(withId(R.id.tvErrorTrackingStartTime))
                .check(matches(not(isDisplayed())))

        onView(withId(R.id.tvErrorTrackingEndDate))
                .check(matches(not(isDisplayed())))

        onView(withId(R.id.tvErrorTrackingEndTime))
                .check(matches(not(isDisplayed())))

        onView(withId(R.id.tvErrorTrackingName))
                .check(matches(not(isDisplayed())))

        onView(withId(R.id.btnEditTrackingBack))
                .perform(scrollTo())

        onView(withId(R.id.btnUpdateTracking))
                .perform(click())

        onView(withId(R.id.tvTrackingStartDate))
                .perform(scrollTo())
        onView(withId(R.id.tvErrorTrackingStartDate))
                .check(matches(isDisplayed()))
                .check(matches(withText("The start date is required")))

        onView(withId(R.id.tvTrackingStartTime))
                .perform(scrollTo())
        onView(withId(R.id.tvErrorTrackingStartTime))
                .check(matches(isDisplayed()))
                .check(matches(withText("The start time is required")))

        onView(withId(R.id.tvTrackingEndDate))
                .perform(scrollTo())
        onView(withId(R.id.tvErrorTrackingEndDate))
                .check(matches(isDisplayed()))
                .check(matches(withText("The end date is required")))

        onView(withId(R.id.tvTrackingEndTime))
                .perform(scrollTo())
        onView(withId(R.id.tvErrorTrackingEndTime))
                .check(matches(isDisplayed()))
                .check(matches(withText("The end time is required")))

        onView(withId(R.id.tvLabelTrackingName))
                .perform(scrollTo())
        onView(withId(R.id.tvErrorTrackingName))
                .check(matches(isDisplayed()))
                .check(matches(withText("The tracking name is required")))
    }

    @Ignore
    @Test
    fun validEditTracking() {
        val currentActivity : HomeActivity = getCurrentActivity() as HomeActivity

        val editTrackingFragment = EditTrackingFragment()
        currentActivity!!.supportFragmentManager.beginTransaction()
                .replace(R.id.flFragment, editTrackingFragment, "EditTrackingFragment")
                .addToBackStack(null)
                .commit()

        onView(withId(R.id.btnCreateTracking))
                .perform(click())

        onView(withId(R.id.etTrackingStartDate))
                .perform(typeText("28.04.2021"), closeSoftKeyboard())

        onView(withId(R.id.etTrackingStartTime))
                .perform(typeText("10:00"), closeSoftKeyboard())

        onView(withId(R.id.etTrackingEndDate))
                .perform(typeText("28.04.2021"), closeSoftKeyboard())

        onView(withId(R.id.etTrackingEndTime))
                .perform(typeText("14:00"), closeSoftKeyboard())

        onView(withId(R.id.etTrackingName))
                .perform(typeText("Some example tracking"), closeSoftKeyboard())

        onView(withId(R.id.btnEditTrackingBack))
                .perform(scrollTo())

        onView(withId(R.id.btnUpdateTracking))
                .perform(click())
    }

}