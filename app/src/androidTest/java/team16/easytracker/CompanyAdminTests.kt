package team16.easytracker

import android.app.Activity
import android.content.ContentValues
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.closeSoftKeyboard
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import androidx.test.runner.lifecycle.ActivityLifecycleMonitorRegistry
import androidx.test.runner.lifecycle.Stage
import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.not
import org.junit.*
import org.junit.runner.RunWith
import team16.easytracker.database.Contracts
import team16.easytracker.database.DbHelper
import java.time.LocalDate
import java.time.LocalDateTime

@RunWith(AndroidJUnit4::class)
class CompanyAdminTests : TestFramework() {

    @Before
    override fun setup() {
        super.setup()
        setupLoggedInWorker()
        val company = insertDummyCompany()
        dbHelper.addWorkerToCompany(MyApplication.loggedInWorker?.getId()!!, company.getId(), "")
        dbHelper.setCompanyAdmin(MyApplication.loggedInWorker?.getId()!!, company.getId(), true)

    }

    @After
    override fun teardown() {
        super.teardown()
        MyApplication.loggedInWorker = null
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
        transaction.replace(R.id.flFragment, CompanyAdminFragment())
        transaction.disallowAddToBackStack()
        transaction.commit()
    }

    @Test
    fun testOpenFragment() {
        openFragment()
        onView(withId(R.id.clFragmentAddWorker))
            .check(matches((isDisplayed())))
    }

    @Test
    fun errorsHiddenOnStart() {
        openFragment()
        // Error textviews should not be visible before clicking registration button
        onView(withId(R.id.tvErrorEmail))
            .check(matches(not(isDisplayed())))

        onView(withId(R.id.tvErrorCompanyPosition))
            .check(matches(not(isDisplayed())))
    }

    @Test
    fun emptyInputData() {

        openFragment()

        // Click registration button without any data entered
        onView(withId(R.id.btnAddWorker))
            .perform(click())

        // Most error textviews should now be visible
        onView(withId(R.id.tvErrorEmail))
            .check(matches(isDisplayed()))
            .check(matches(withText(R.string.email_required)))

        onView(withId(R.id.tvErrorCompanyPosition))
            .check(matches(isDisplayed()))
            .check(matches(withText(R.string.position_required)))
    }

    @Test
    fun validInputData() {

        insertDummyWorker()
        addloggedInWorkerToCompany()

        openFragment()

        onView(withId(R.id.etEmail))
            .perform(typeText(dummyWorker.email))

        onView(withId(R.id.etCompanyPosition))
            .perform(typeText("boss"))

        onView(withId(R.id.btnAddWorker))
            .perform(click())

        onView(CoreMatchers.allOf(withId(com.google.android.material.R.id.snackbar_text)))
            .check(matches(withText(R.string.success_adding_employee)))
    }

    @Test
    fun invalidInputData() {
        openFragment()

        onView(withId(R.id.etEmail))
            .perform(typeText("test@test.de"))

        onView(withId(R.id.etCompanyPosition))
            .perform(typeText("boss"))

        onView(withId(R.id.btnAddWorker))
            .perform(click())

        onView(CoreMatchers.allOf(withId(com.google.android.material.R.id.snackbar_text)))
            .check(matches(withText(R.string.error_adding_employee)))
    }
}