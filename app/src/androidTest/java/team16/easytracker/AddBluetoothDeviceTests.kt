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
import androidx.test.runner.lifecycle.ActivityLifecycleMonitorRegistry
import androidx.test.runner.lifecycle.Stage
import org.hamcrest.CoreMatchers.*
import org.junit.*
import org.junit.runner.RunWith
import team16.easytracker.database.Contracts
import team16.easytracker.database.DbHelper
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


@RunWith(AndroidJUnit4::class)
class AddBluetoothDeviceTests {

    val appContext = InstrumentationRegistry.getInstrumentation().targetContext
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

    @get:Rule
    val activityRule = ActivityScenarioRule(HomeActivity::class.java)

    @Before
    fun init() {
        if (MyApplication.loggedInWorker == null) {
            setupLoggedInWorker()
        }
        writableDb = DbHelper.writableDatabase
        readable = DbHelper.readableDatabase
        writableDb.beginTransaction()
    }

    @After
    fun tearDown() {
        writableDb.endTransaction()
    }

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
        setupLoggedInWorker()
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
}
