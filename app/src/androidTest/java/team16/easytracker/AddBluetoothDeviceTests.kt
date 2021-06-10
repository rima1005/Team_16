package team16.easytracker

import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.database.sqlite.SQLiteDatabase
import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.RootMatchers.isDialog
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.GrantPermissionRule
import androidx.test.runner.lifecycle.ActivityLifecycleMonitorRegistry
import androidx.test.runner.lifecycle.Stage
import org.hamcrest.CoreMatchers.*
import org.hamcrest.collection.IsMapContaining.hasEntry
import org.junit.Before
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class AddBluetoothDeviceTests : TestFramework() {

    private lateinit var writableDb: SQLiteDatabase
    private lateinit var readable: SQLiteDatabase

    @get:Rule
    val activityRule = ActivityScenarioRule(HomeActivity::class.java)
    @get:Rule
    val coarseLocationPermissionRule: GrantPermissionRule = GrantPermissionRule.grant(android.Manifest.permission.ACCESS_COARSE_LOCATION)
    @get:Rule
    val fineLocationPermissionRule: GrantPermissionRule = GrantPermissionRule.grant(android.Manifest.permission.ACCESS_FINE_LOCATION)
    @get:Rule
    val bluetoothPermissionRule: GrantPermissionRule = GrantPermissionRule.grant(android.Manifest.permission.BLUETOOTH)
    @get:Rule
    val bluetoothAdminPermissionRule: GrantPermissionRule = GrantPermissionRule.grant(android.Manifest.permission.BLUETOOTH_ADMIN)

    @Before
    fun init() {
        writableDb = dbHelper.writableDatabase
        readable = dbHelper.readableDatabase

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

    fun openFragment(enableBluetooth: Boolean = true) {
        if (enableBluetooth) {
            val bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
            bluetoothAdapter.enable()
            while (!bluetoothAdapter.isEnabled) {
                // busy wait
            }
        }

        setupLoggedInWorker()
        val activity = getCurrentActivity() as HomeActivity

        activity.supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment, ProfileFragment(), "Profile Fragment")
            addToBackStack(null)
            commit()
        }
        val transaction = activity.supportFragmentManager.beginTransaction()
        transaction.replace(R.id.flFragment, BluetoothFragment())
        transaction.addToBackStack("Profile Fragment")
        transaction.commit()
    }

    @Test
    fun testOpenFragment() {
        openFragment()
        onView(withId(R.id.flBluetooth)).check(matches((isDisplayed())))
    }

    @Test
    fun testOpenFragmentWithDisabledBluetoothAndAcceptEnabling() {
        val bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        bluetoothAdapter.disable()
        while (bluetoothAdapter.isEnabled) {
            // busy wait
        }
        openFragment(false)
        onView(withText(R.string.bluetooth_required_message))
                .inRoot(isDialog())
                .check(matches(isDisplayed()))

        onView(withText(R.string.enable))
                .perform(click())

        Thread.sleep(500)

        assert(bluetoothAdapter.isEnabled)
        onView(withId(R.id.flBluetooth))
                .check(matches((isDisplayed())))
    }

    @Test
    fun testOpenFragmentWithDisabledBluetoothAndKeepOff() {
        val bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        bluetoothAdapter.disable()
        while (bluetoothAdapter.isEnabled) {
            // busy wait
        }
        openFragment(false)
        onView(withText(R.string.bluetooth_required_message))
                .inRoot(isDialog())
                .check(matches(isDisplayed()))

        onView(withText(R.string.keep_off))
                .perform(click())

        assert(!bluetoothAdapter.isEnabled)
        onView(withId(R.id.flBluetooth))
                .check(doesNotExist())
    }

    @Ignore("Pairing is difficult to test")
    @Test
    fun testDiscoverBluetoothDevices() {
        val bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        val deviceName = "Device 1"

        openFragment()

        onView(withId(R.id.btnDiscoverBluetoothDevices))
                .perform(click())

        assert(bluetoothAdapter.isDiscovering)

        Thread.sleep(2000)

        onData(allOf(`is`(instanceOf(String::class.java)),
                hasEntry(equalTo("STR"), `is`("gDevice-beacon - BE:AC:10:00:00:01")),
                hasEntry(equalTo("STR"), `is`("gDevice-beacon - BE:AC:10:00:00:02"))))

        onData(anything()).atPosition(1)
                .perform(click())

        onView(withId(R.id.etBluetoothDeviceName))
                .check(matches(isDisplayed()))
                .check(matches(withText("gDevice-beacon")))
                .perform(clearText())
                .perform(typeText(deviceName))

        onView(withText(R.string.add_device))
                .perform(click())

        // TODO: pair with device
        Thread.sleep(500)

        val devices = dbHelper.loadBluetoothDevicesForWorker(MyApplication.loggedInWorker!!.getId())

        assert(devices.isNotEmpty())

        val deviceNames = devices.map { it.name }

        assert(deviceNames.contains(deviceName))

        Thread.sleep(3000)
    }




}
