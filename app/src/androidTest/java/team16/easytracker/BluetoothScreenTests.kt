package team16.easytracker

import android.app.Activity
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.runner.lifecycle.ActivityLifecycleMonitorRegistry
import androidx.test.runner.lifecycle.Stage
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class BluetoothScreenTests {
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
        transaction.replace(R.id.flFragment, BluetoothDevicesFragment())
        transaction.disallowAddToBackStack()
        transaction.commit()
    }

    @Test
    fun testOpenFragment() {
        openFragment()
        onView(ViewMatchers.withId(R.id.rvBluetoothDevicesList))
            .check(ViewAssertions.matches((ViewMatchers.isDisplayed())))
    }

    //TODO: add tests after "add bluetooth" functionality is done
}