package team16.easytracker

import android.app.Activity
import androidx.test.espresso.Espresso.*
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import androidx.test.runner.lifecycle.ActivityLifecycleMonitorRegistry
import androidx.test.runner.lifecycle.Stage
import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.anything
import org.hamcrest.CoreMatchers.not
import org.hamcrest.collection.IsMapContaining
import org.junit.*
import org.junit.runner.RunWith
import team16.easytracker.database.DbHelper
import java.time.LocalDate
import java.time.LocalDateTime

@RunWith(AndroidJUnit4::class)
class CompanyOverviewTests : TestFramework() {


    @Before
    override fun setup() {
        super.setup()
        setupLoggedInWorker()
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

    fun openFragment() {
        val activity = getCurrentActivity() as HomeActivity
        val transaction = activity.supportFragmentManager.beginTransaction()
        transaction.replace(R.id.flFragment, CompanyFragment())
        transaction.disallowAddToBackStack()
        transaction.commit()
    }

    @Test
    fun testOpenFragment() {
        openFragment()
        onView(withId(R.id.flFragmentCompany)).check(matches((isDisplayed())))
    }

    @Test
    fun testCorrectWorkersShown() {
        addloggedInWorkerToCompany()
        insertDummyWorker("Zohan", "Slany")
        dbHelper.addWorkerToCompany(dummyWorker.getId(), dummyCompany.getId(), "Chef")
        dummyWorker = dbHelper.loadWorker(dummyWorker.getId())!!

        val currentActivity: HomeActivity = getCurrentActivity() as HomeActivity
        val companyOverviewFragment = CompanyFragment()
        currentActivity.supportFragmentManager.beginTransaction()
                .replace(R.id.flFragment, companyOverviewFragment, "CompanyFragment")
                .addToBackStack(null)
                .commit()

        onView(withId(R.id.tvCompanyName))
                .check(matches(withText(dummyCompany.name)))
        onView(withId(R.id.tvCompanyAddress))
                .check(matches(withText(dummyAddress.toString())))

        onData(anything())
                .inAdapterView(withId(R.id.lvCompanyWorkers))
                .atPosition(1)
                .check(matches(withText(dummyWorker.toString())))
    }
}