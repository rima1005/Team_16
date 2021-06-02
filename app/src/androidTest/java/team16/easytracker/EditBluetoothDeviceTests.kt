package team16.easytracker

import android.app.Activity
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import androidx.test.runner.lifecycle.ActivityLifecycleMonitorRegistry
import androidx.test.runner.lifecycle.Stage
import org.hamcrest.CoreMatchers.not
import org.junit.*
import org.junit.runner.RunWith
import team16.easytracker.database.DbHelper
import java.time.LocalDate
import java.time.LocalDateTime

@RunWith(AndroidJUnit4::class)
class EditBluetoothDeviceTests : TestFramework() {

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
        transaction.replace(R.id.flFragment, BluetoothDevicesFragment())
        transaction.disallowAddToBackStack()
        transaction.commit()
    }

    fun getCurrentActivity(): Activity {
        var currentActivity: Activity? = null
        getInstrumentation().runOnMainSync { run { currentActivity =
            ActivityLifecycleMonitorRegistry.getInstance().getActivitiesInStage(Stage.RESUMED)
                .elementAtOrNull(0)
        }
        }
        return currentActivity!!
    }

    @Test
    fun testOpenFragment() {
        openFragment()
        onView(withId(R.id.llBluetoothDevices)).check(matches((isDisplayed())))
    }

    @Test
    fun validEditBluetoothDeviceName() {
        openFragment()

        insertDummyBluetoothDevice()

        onView(withText("TestDevice"))
            .check(matches(isDisplayed()))

        onView(withText(R.string.edit))
            .perform(click())

        onView(withId(R.id.etBluetoothDeviceName))
            .perform(clearText())
            .perform(typeText("Edited BT device name"), closeSoftKeyboard())

        onView(withText(R.string.edit))
            .perform(click())

        onView(withText("Edited BT device name"))
            .check(matches(isDisplayed()))
    }

    @Test
    fun validEditBluetoothDeviceSameName() {
        openFragment()

        insertDummyBluetoothDevice()

        onView(withText("TestDevice"))
            .check(matches(isDisplayed()))

        onView(withText(R.string.edit))
            .perform(click())

        // Do not change the bluetooth device name, just click edit

        onView(withText(R.string.edit))
            .perform(click())

        onView(withText("TestDevice"))
            .check(matches(isDisplayed()))
    }

    @Test
    fun validEditBluetoothDeviceEmptyName() {
        openFragment()

        insertDummyBluetoothDevice()

        onView(withText("TestDevice"))
            .check(matches(isDisplayed()))

        onView(withText(R.string.edit))
            .perform(click())

        onView(withId(R.id.etBluetoothDeviceName))
            .perform(clearText())

        onView(withText(R.string.edit))
            .perform(click())

        onView(withText(R.string.edit))
            .check(matches(isDisplayed()))

        onView(withText(R.string.delete))
            .check(matches(isDisplayed()))
    }

    @Test
    fun validEditBluetoothDeviceNameCancel() {
        openFragment()

        insertDummyBluetoothDevice()

        onView(withText("TestDevice"))
            .check(matches(isDisplayed()))

        onView(withText(R.string.edit))
            .perform(click())

        onView(withId(R.id.etBluetoothDeviceName))
            .perform(clearText())
            .perform(typeText("Edited BT device name"), closeSoftKeyboard())

        onView(withText(R.string.cancel))
            .perform(click())

        onView(withText("TestDevice"))
            .check(matches(isDisplayed()))
    }
}