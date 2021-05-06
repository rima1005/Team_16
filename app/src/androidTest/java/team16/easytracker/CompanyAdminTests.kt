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
class CompanyAdminTests {

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

    private fun insertDummyWorker(): Int {

        val addressId = DbHelper.saveAddress(street, postCode, city)

        val workerId = DbHelper.saveWorker(
            firstName,
            lastName,
            LocalDate.now(),
            title,
            email,
            password,
            phonePrefix + phoneNumber,
            LocalDateTime.now().withNano(0),
            1
        )

        return workerId
    }

    private fun insertDummyCompany(companyName: String): Int {
        return DbHelper.saveCompany(companyName, 1);
    }

    @Before
    fun init() {
        // TODO: this doesn't work because further reads (e.g.: in fragment) don't work if transaction is open
        // DbHelper.writableDatabase.beginTransaction()
        if (MyApplication.loggedInWorker == null) {
            setupLoggedInWorker()
            val companyId = insertDummyCompany("TestCompany")
            DbHelper.addWorkerToCompany(MyApplication.loggedInWorker?.getId()!!, companyId, "")
            DbHelper.setCompanyAdmin(MyApplication.loggedInWorker?.getId()!!, companyId, true)
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
        onView(withId(R.id.tvErrorAddWorker))
            .check(matches(not(isDisplayed())))

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

        openFragment()

        onView(withId(R.id.etEmail))
            .perform(typeText(email))

        onView(withId(R.id.etCompanyPosition))
            .perform(typeText("boss"))

        onView(withId(R.id.btnAddWorker))
            .perform(click())

        onView(withId(R.id.tvErrorAddWorker))
            .check(matches(isDisplayed()))
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

        onView(withId(R.id.tvErrorAddWorker))
            .check(matches(isDisplayed()))
            .check(matches(withText(R.string.error_adding_employee)))
    }
}