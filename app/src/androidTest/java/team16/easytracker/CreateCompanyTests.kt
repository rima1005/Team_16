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
class CreateCompanyTests {

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

    private fun insertDummyCompany(companyName: String) {
        DbHelper.saveCompany(companyName, 1);
    }

    @Before
    fun init() {
        // TODO: this doesn't work because further reads (e.g.: in fragment) don't work if transaction is open
        // DbHelper.writableDatabase.beginTransaction()
        if(MyApplication.loggedInWorker == null) {
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
        getInstrumentation().runOnMainSync { run { currentActivity = ActivityLifecycleMonitorRegistry.getInstance().getActivitiesInStage(Stage.RESUMED).elementAtOrNull(0) } }
        return currentActivity!!
    }

    fun openFragment() {
        val activity = getCurrentActivity() as HomeActivity
        val transaction = activity.supportFragmentManager.beginTransaction()
        transaction.replace(R.id.flFragment, CreateCompanyFragment())
        transaction.disallowAddToBackStack()
        transaction.commit()
    }

    @Test
    fun testOpenFragment() {
        openFragment()
        onView(withId(R.id.clFragmentCreateCompany)).check(matches((isDisplayed())))
    }

    @Test
    fun errorsHiddenOnStart() {
        openFragment()
        // Error textviews should not be visible before clicking registration button
        onView(withId(R.id.tvErrorCompanyName))
                .check(matches(not(isDisplayed())))

        onView(withId(R.id.tvErrorCompanyPosition))
                .check(matches(not(isDisplayed())))

        onView(withId(R.id.tvErrorPostCode))
                .check(matches(not(isDisplayed())))

        onView(withId(R.id.tvErrorCity))
                .check(matches(not(isDisplayed())))

        onView(withId(R.id.tvErrorStreet))
                .check(matches(not(isDisplayed())))
    }

    @Test
    fun emptyInputData() {

        openFragment()

        // Click registration button without any data entered
        onView(withId(R.id.btnCreateCompany))
                .perform(scrollTo(), click())

        // TODO: replace matcher texts with string resources

        // Most error textviews should now be visible
        onView(withId(R.id.tvErrorCompanyName))
                .check(matches(isDisplayed()))
                .check(matches(withText("The company name is required")))

        onView(withId(R.id.tvErrorCompanyPosition))
                .check(matches(isDisplayed()))
                .check(matches(withText("The position is required")))

        onView(withId(R.id.tvErrorPostCode))
                .check(matches(isDisplayed()))
                .check(matches(withText("The post code is required")))

        onView(withId(R.id.tvErrorCity))
                .check(matches(isDisplayed()))
                .check(matches(withText("The city is required")))

        onView(withId(R.id.tvErrorStreet))
                .check(matches(isDisplayed()))
                .check(matches(withText("The street is required")))
    }

    @Test
    fun invalidInputDataTooShort() {
        openFragment()

        // type too short company name
        onView(withId(R.id.etCompanyName))
                .perform(scrollTo(), typeText("A"))

        // type too short company position
        onView(withId(R.id.etCompanyPosition))
                .perform(scrollTo(), typeText("A"))

        // type too short postal code
        onView(withId(R.id.etPostCode))
                .perform(scrollTo(), typeText("A"))

        // type too short city
        onView(withId(R.id.etCity))
                .perform(scrollTo(), typeText("A"))

        // type Street without nr/street name (only 1 word)
        onView(withId(R.id.etStreet))
                .perform(scrollTo(), typeText("Street"))

        closeSoftKeyboard()

        // Click registration button
        val btn = withId(R.id.btnCreateCompany)
        onView(btn).perform(scrollTo(), click())

        // check if error messages are correct
        // TODO: replace with string resources
        onView(withId(R.id.tvErrorCompanyName))
                .check(matches(isDisplayed()))
                .check(matches(withText("The company name must be between 2 and 255 characters")))

        onView(withId(R.id.tvErrorCompanyPosition))
                .check(matches(isDisplayed()))
                .check(matches(withText("The position must be between 2 and 255 characters")))

        onView(withId(R.id.tvErrorPostCode))
                .check(matches(isDisplayed()))
                .check(matches(withText("The post code must be between 4 and 10 characters")))

        onView(withId(R.id.tvErrorCity))
                .check(matches(isDisplayed()))
                .check(matches(withText("The city must be between 2 and 255 characters")))

        onView(withId(R.id.tvErrorStreet))
                .check(matches(isDisplayed()))
                .check(matches(withText("The street must contain a street name and a street number")))

    }

    @Test
    fun invalidInputDataTooLong() {
        openFragment()

        // TODO: add as variable to Validator?
        val characterLimit = 255
        val characterLimitPostalCode = 10

        // type too long company name
        onView(withId(R.id.etCompanyName))
                .perform(scrollTo(), typeText("a".repeat(characterLimit + 1)))

        // type too long company position
        onView(withId(R.id.etCompanyPosition))
                .perform(scrollTo(), typeText("a".repeat(characterLimit + 1)))

        // type too long postal code
        onView(withId(R.id.etPostCode))
                .perform(scrollTo(), typeText("a".repeat(characterLimitPostalCode + 1)))

        // type too long city
        onView(withId(R.id.etCity))
                .perform(scrollTo(), typeText("a".repeat(characterLimit + 1)))

        closeSoftKeyboard()

        // Click registration button
        val btn = withId(R.id.btnCreateCompany)
        onView(btn).perform(scrollTo(), click())

        // check if error messages are correct
        // TODO: replace with string resources
        onView(withId(R.id.tvErrorCompanyName))
                .check(matches(isDisplayed()))
                .check(matches(withText("The company name must be between 2 and 255 characters")))

        onView(withId(R.id.tvErrorCompanyPosition))
                .check(matches(isDisplayed()))
                .check(matches(withText("The position must be between 2 and 255 characters")))

        onView(withId(R.id.tvErrorPostCode))
                .check(matches(isDisplayed()))
                .check(matches(withText("The post code must be between 4 and 10 characters")))

        onView(withId(R.id.tvErrorCity))
                .check(matches(isDisplayed()))
                .check(matches(withText("The city must be between 2 and 255 characters")))
    }

    @Test
    fun duplicateCompanyFails() {

        openFragment()

        // assume company exists
        val dummyCompanyName = "Google123"
        insertDummyCompany(dummyCompanyName)

        // try to create company with same name:

        // type valid company name
        onView(withId(R.id.etCompanyName))
            .perform(scrollTo(), typeText(dummyCompanyName))

        // type valid company position
        onView(withId(R.id.etCompanyPosition))
            .perform(scrollTo(), typeText("CEO"))

        // type valid postal code
        onView(withId(R.id.etPostCode))
            .perform(scrollTo(), typeText("8010"))

        // type valid city
        onView(withId(R.id.etCity))
            .perform(scrollTo(), typeText("Graz"))

        // type valid Street
        onView(withId(R.id.etStreet))
            .perform(scrollTo(), typeText("Street 1"))

        // Click registration button
        val btn = withId(R.id.btnCreateCompany)
        onView(btn).perform(scrollTo(), click())

        // check if correct error is displayed
        onView(withId(R.id.tvErrorCompanyName))
            .check(matches(isDisplayed()))
            .check(matches(withText(R.string.error_company_exists)))
    }

    @Test
    fun validInputData() {
        openFragment()

        val dummyCompanyName = "Google"

        // type valid company name
        onView(withId(R.id.etCompanyName))
                .perform(scrollTo(), typeText(dummyCompanyName))

        // type valid company position
        onView(withId(R.id.etCompanyPosition))
                .perform(scrollTo(), typeText("CEO"))

        // type valid postal code
        onView(withId(R.id.etPostCode))
                .perform(scrollTo(), typeText("8010"))

        // type valid city
        onView(withId(R.id.etCity))
                .perform(scrollTo(), typeText("Graz"))

        // type valid Street
        onView(withId(R.id.etStreet))
                .perform(scrollTo(), typeText("Street 1"))

        closeSoftKeyboard()

        // Click registration button
        val btn = withId(R.id.btnCreateCompany)
        onView(btn).perform(scrollTo(), click())

        // check if company was correctly created and assigned
        assert(MyApplication.loggedInWorker!!.admin)
        val company = MyApplication.loggedInWorker!!.company
        assert(company != null)
        assert(MyApplication.loggedInWorker!!.company!!.name == dummyCompanyName)

        // check for correct redirect + company create button should now be gone
        onView(withId(R.id.flFragmentCompany)).check(matches(isDisplayed()))
        onView(withId(R.id.btnCreateCompany)).check(matches(not(isDisplayed())))
    }

}