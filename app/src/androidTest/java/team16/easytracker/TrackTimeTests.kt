package team16.easytracker

import android.app.Activity
import androidx.test.espresso.Espresso.closeSoftKeyboard
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
class TrackTimeTests {

    private fun setupLoggedInWorker() {

        val addressId = DbHelper.saveAddress("street", "1234", "city")

        val email = "email@email.at";
        val pw = "12345678"
        val workerId = DbHelper.saveWorker(
            "firstName",
            "lastName",
            LocalDate.now(),
            "title",
            email,
            pw,
            "12345678",
            LocalDateTime.now().withNano(0),
            addressId
        )

        DbHelper.loginWorker(email, pw)
    }

    @Before
    fun init() {
        if (MyApplication.loggedInWorker == null) {
            setupLoggedInWorker()
        }
    }

    @After
    fun tearDown() {
        //DbHelper.writableDatabase.endTransaction()
    }

    @get:Rule
    val activityRule = ActivityScenarioRule(HomeActivity::class.java)

    fun getCurrentActivity(): Activity {
        var currentActivity: Activity? = null
        getInstrumentation().runOnMainSync { run { currentActivity =
            ActivityLifecycleMonitorRegistry.getInstance().getActivitiesInStage(Stage.RESUMED)
                .elementAtOrNull(0)
        }
        }
        return currentActivity!!
    }

    @Ignore
    @Test
    fun testStartTracking(){
        onView(withId(R.id.btnStartTracking))
            .check(matches(isDisplayed()))
    }

}