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
import com.levibostian.recyclerviewmatcher.RecyclerViewMatcher
import org.hamcrest.CoreMatchers.*
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import team16.easytracker.database.DbHelper
import java.time.LocalDate
import java.time.LocalDateTime

@RunWith(AndroidJUnit4::class)
class DeleteBluetoothDeviceTests : TestFramework() {
    @Before
    override fun setup() {
        super.setup()
        setupLoggedInWorker()
    }

    @get:Rule
    val activityRule = ActivityScenarioRule(HomeActivity::class.java)

    fun getCurrentActivity(): Activity {
        var currentActivity: Activity? = null
        InstrumentationRegistry.getInstrumentation().runOnMainSync { run { currentActivity =
                ActivityLifecycleMonitorRegistry.getInstance().getActivitiesInStage(Stage.RESUMED)
                        .elementAtOrNull(0)
        }
        }
        return currentActivity!!
    }

    fun openFragment() {
        val activity = getCurrentActivity() as HomeActivity
        val transaction = activity.supportFragmentManager.beginTransaction()
        transaction.replace(R.id.flFragment, BluetoothFragment())
        transaction.disallowAddToBackStack()
        transaction.commit()
    }

    @Test
    fun testOpenFragment() {
        openFragment()
        onView(withId(R.id.flBluetooth)).check(matches((isDisplayed())))
    }

    @Test
    fun testDeleteBluetoothDevice() {
        insertDummyBluetoothDevice()

        val activity = getCurrentActivity() as HomeActivity
        val transaction = activity.supportFragmentManager. beginTransaction()
        transaction.replace(R.id.flFragment, BluetoothDevicesFragment())
        transaction.disallowAddToBackStack()
        transaction.commit()

        onView(RecyclerViewMatcher.recyclerViewWithId(R.id.rvBluetoothDevicesList).viewHolderViewAtPosition(0, R.id.btnDeleteBluetoothDevice))
          .check(matches(isDisplayed()))

        onView(RecyclerViewMatcher.recyclerViewWithId(R.id.rvBluetoothDevicesList).viewHolderViewAtPosition(0, R.id.btnDeleteBluetoothDevice))
                .perform(click())
    }

}