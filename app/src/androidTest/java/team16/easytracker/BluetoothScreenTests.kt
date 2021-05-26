package team16.easytracker

import android.app.Activity
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.scrollTo
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.runner.lifecycle.ActivityLifecycleMonitorRegistry
import androidx.test.runner.lifecycle.Stage
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class BluetoothScreenTests : TestFramework(){
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

    fun openFragment() {
        val activity = getCurrentActivity() as HomeActivity
        val transaction = activity.supportFragmentManager.beginTransaction()
        transaction.replace(R.id.flFragment, BluetoothDevicesFragment())
        transaction.disallowAddToBackStack()
        transaction.commit()
    }

    @Test
    fun testOpenFragment() {
        openFragment()
        onView(withId(R.id.llBluetoothDevices))
            .check(matches(isDisplayed()))
    }

    @Test
    fun checkAddBluetoothDevice(){
        openFragment()
        insertDummyBluetoothDevice()

        onView(withId(R.id.btnAddBluetoothDevice))
            .check(matches(isDisplayed()))
    }

    @Test
    fun checkBluetoothItems(){
        openFragment()
        insertDummyBluetoothDevice()

        onView(withId(R.id.btnAddBluetoothDevice))
            .check(matches(isDisplayed()))

        onView(withText("00:80:41:ae:fd:69"))
            .check(matches(isDisplayed()))

        onView(withText("TestDevice"))
            .check(matches(isDisplayed()))
    }
}