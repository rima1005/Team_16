package team16.easytracker

import android.app.Activity
import android.view.Gravity
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import androidx.test.runner.lifecycle.ActivityLifecycleMonitorRegistry
import androidx.test.runner.lifecycle.Stage
import com.google.android.material.navigation.NavigationView
import org.junit.*
import org.junit.runner.RunWith
import team16.easytracker.database.DbHelper
import java.time.LocalDate
import java.time.LocalDateTime
import androidx.test.espresso.contrib.DrawerActions
import androidx.test.espresso.contrib.DrawerMatchers
import androidx.test.espresso.contrib.NavigationViewActions
import team16.easytracker.model.Company
import com.google.android.material.internal.NavigationMenuItemView


@RunWith(AndroidJUnit4::class)
class NavigationDrawerTests : TestFramework() {

    private var company : Company? = null

    private fun insertDummyCompany(companyName: String): Int {
        if (company == null)
        {
            val companyId = dbHelper.saveCompany(companyName, 1)
            company = dbHelper.loadCompany(companyId)!!
        }
        return company!!.getId()
    }



    @Before
    override fun setup() {
        super.setup()
        setupLoggedInWorker()
    }

    fun loginwithCompanyWorker()
    {
        val company = insertDummyCompany()
        dbHelper.addWorkerToCompany(MyApplication.loggedInWorker?.getId()!!, company.getId(), "Test")
        dbHelper.setCompanyAdmin(MyApplication.loggedInWorker?.getId()!!, company.getId(), false)
        var activity = getCurrentActivity() as LoginActivity
        onView(withId(R.id.etEmail)).perform(typeText(loggedInWorker.email))
        closeSoftKeyboard()
        onView(withId(R.id.etPassword)).perform(typeText("12345678"))
        closeSoftKeyboard()
        onView(withId(R.id.btnLogin)).perform(click())
    }

    fun loginWithNoCompanyWorker()
    {
        var activity = getCurrentActivity() as LoginActivity
        onView(withId(R.id.etEmail)).perform(typeText(loggedInWorker.email))
        closeSoftKeyboard()
        onView(withId(R.id.etPassword)).perform(typeText("12345678"))
        closeSoftKeyboard()
        onView(withId(R.id.btnLogin)).perform(click())
    }

    fun loginwithCompanyWorkerAdmin()
    {
        val company = insertDummyCompany()
        dbHelper.addWorkerToCompany(MyApplication.loggedInWorker?.getId()!!, company.getId(), "Test")
        dbHelper.setCompanyAdmin(MyApplication.loggedInWorker?.getId()!!, company.getId(), true)
        var activity = getCurrentActivity() as LoginActivity
        onView(withId(R.id.etEmail)).perform(typeText(loggedInWorker.email))
        closeSoftKeyboard()
        onView(withId(R.id.etPassword)).perform(typeText("12345678"))
        closeSoftKeyboard()
        onView(withId(R.id.btnLogin)).perform(click())
    }

    @get:Rule
    val activityRule = ActivityScenarioRule(LoginActivity::class.java)

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
    fun initialFragmentDashboardTest()
    {
        loginwithCompanyWorker()
        var activity = getCurrentActivity() as HomeActivity
        var fragment = activity.supportFragmentManager.findFragmentByTag("TAG_DASHBOARD")
        assert(fragment?.isVisible!!)
    }

    @Test
    fun checkMenuItemVisabilityNoCompany()
    {
        loginWithNoCompanyWorker()
        var activity = getCurrentActivity() as HomeActivity
        var navigationView = activity.findViewById<NavigationView>(R.id.navigationView)
        assert(!navigationView.menu.findItem(R.id.itemAddEmployee).isVisible)
        assert(!navigationView.menu.findItem(R.id.itemOverview).isVisible)
    }

    @Test
    fun checkMenuItemVisabilityCompany()
    {
        loginwithCompanyWorkerAdmin()
        var activity = getCurrentActivity() as HomeActivity
        onView(withId(R.id.drawerLayout))
                .check(matches(DrawerMatchers.isClosed(Gravity.LEFT)))// Left Drawer should be closed.
                .perform(DrawerActions.open()) // Open Drawer

        var navigationView = activity.findViewById<NavigationView>(R.id.navigationView)
        assert(navigationView.menu.findItem(R.id.itemAddEmployee).isVisible)
        assert(navigationView.menu.findItem(R.id.itemOverview).isVisible)
        assert(!navigationView.menu.findItem(R.id.itemCreateCompany).isVisible)
    }

    @Test
    fun checkMenuItemVisabilityCompanyNoAdmin()
    {
        loginwithCompanyWorker()
        var activity = getCurrentActivity() as HomeActivity
        var navigationView = activity.findViewById<NavigationView>(R.id.navigationView)
        assert(!navigationView.menu.findItem(R.id.itemAddEmployee).isVisible)
        assert(navigationView.menu.findItem(R.id.itemOverview).isVisible)
        assert(!navigationView.menu.findItem(R.id.itemCreateCompany).isVisible)
    }

    @Test
    fun navigateToTrackings()
    {
        loginwithCompanyWorker()
        var activity = getCurrentActivity() as HomeActivity
        onView(withId(R.id.drawerLayout))
                .check(matches(DrawerMatchers.isClosed(Gravity.LEFT)))// Left Drawer should be closed.
                .perform(DrawerActions.open()) // Open Drawer

        // Start the screen of your activity.
        onView(withId(R.id.navigationView))
                .perform(NavigationViewActions.navigateTo(R.id.itemTrackings))

        Thread.sleep(1000)
        var fragment = activity.supportFragmentManager.findFragmentByTag("TAG_TRACKINGS")
        assert(fragment?.isVisible!!)
    }

    @Test //TODO: Finish this test if Bluetooth screen is available
    fun navigateToBluetooth()
    {
        assert(true)
    }

    @Test
    fun navigateToAddEmployee()
    {
        loginwithCompanyWorkerAdmin()
        val companyID = insertDummyCompany("DummyCompany")
        dbHelper.addWorkerToCompany(MyApplication.loggedInWorker?.getId()!!, companyID, "Test")
        dbHelper.setCompanyAdmin(MyApplication.loggedInWorker?.getId()!!, companyID, true)
        var activity = getCurrentActivity() as HomeActivity
        onView(withId(R.id.drawerLayout))
                .check(matches(DrawerMatchers.isClosed(Gravity.LEFT)))// Left Drawer should be closed.
                .perform(DrawerActions.open()) // Open Drawer

        // Start the screen of your activity.
        onView(withId(R.id.navigationView))
                .perform(NavigationViewActions.navigateTo(R.id.itemAddEmployee))

        Thread.sleep(1000)
        var fragment = activity.supportFragmentManager.findFragmentByTag("TAG_ADDEMPLOYEE")
        assert(fragment?.isVisible!!)
    }

    @Test
    fun navigateToCompanyOverview()
    {
        loginwithCompanyWorker()
        val companyID = insertDummyCompany("DummyCompany")
        dbHelper.addWorkerToCompany(MyApplication.loggedInWorker?.getId()!!, companyID, "Test")
        var activity = getCurrentActivity() as HomeActivity
        onView(withId(R.id.drawerLayout))
                .check(matches(DrawerMatchers.isClosed(Gravity.LEFT)))// Left Drawer should be closed.
                .perform(DrawerActions.open()) // Open Drawer

        // Start the screen of your activity.
        onView(withId(R.id.navigationView))
                .perform(NavigationViewActions.navigateTo(R.id.itemOverview))

        Thread.sleep(1000)
        var fragment = activity.supportFragmentManager.findFragmentByTag("TAG_OVERVIEW")
        assert(fragment?.isVisible!!)
    }

    @Test
    fun navigateToCreateCompany()
    {
        loginWithNoCompanyWorker()
        var activity = getCurrentActivity() as HomeActivity
        onView(withId(R.id.drawerLayout))
                .check(matches(DrawerMatchers.isClosed(Gravity.LEFT)))// Left Drawer should be closed.
                .perform(DrawerActions.open()) // Open Drawer

        // Start the screen of your activity.
        onView(withId(R.id.navigationView))
                .perform(NavigationViewActions.navigateTo(R.id.itemCreateCompany))

        Thread.sleep(1000)
        var fragment = activity.supportFragmentManager.findFragmentByTag("TAG_CREATECOMPANY")
        assert(fragment?.isVisible!!)
    }

    @Test
    fun logoutTest()
    {
        loginWithNoCompanyWorker()
        var activity = getCurrentActivity() as HomeActivity
        onView(withId(R.id.drawerLayout))
                .check(matches(DrawerMatchers.isClosed(Gravity.LEFT)))// Left Drawer should be closed.
                .perform(DrawerActions.open()) // Open Drawer

        onView(withId(R.id.navigationView))
                .perform(NavigationViewActions.navigateTo(R.id.itemLogout))

        Thread.sleep(1000)
        assert(MyApplication.loggedInWorker == null)
    }
}