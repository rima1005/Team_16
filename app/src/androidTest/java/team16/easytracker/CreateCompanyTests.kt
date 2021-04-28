package team16.easytracker

import android.app.Activity
import androidx.test.espresso.Espresso.closeSoftKeyboard
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import androidx.test.runner.lifecycle.ActivityLifecycleMonitorRegistry
import androidx.test.runner.lifecycle.Stage
import org.hamcrest.CoreMatchers
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CreateCompanyTests {

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
        onView(withId(R.id.etCompanyName)).check(matches((isDisplayed())))
    }

    @Test
    fun errorsHiddenOnStart() {
        openFragment()
        // Error textviews should not be visible before clicking registration button
        onView(withId(R.id.tvErrorCompanyName))
                .check(matches(CoreMatchers.not(isDisplayed())))

        onView(withId(R.id.tvErrorCompanyPosition))
                .check(matches(CoreMatchers.not(isDisplayed())))

        onView(withId(R.id.tvErrorPostCode))
                .check(matches(CoreMatchers.not(isDisplayed())))

        onView(withId(R.id.tvErrorCity))
                .check(matches(CoreMatchers.not(isDisplayed())))

        onView(withId(R.id.tvErrorStreet))
                .check(matches(CoreMatchers.not(isDisplayed())))
    }

    @Test
    fun emptyInputData() {

        openFragment()

        // Click registration button without any data entered
        onView(withId(R.id.btnCreateCompany))
                .perform(ViewActions.scrollTo(), ViewActions.click())

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
                .perform(ViewActions.scrollTo(), ViewActions.typeText("A"))

        // type too short company position
        onView(withId(R.id.etCompanyPosition))
                .perform(ViewActions.scrollTo(), ViewActions.typeText("A"))

        // type too short postal code
        onView(withId(R.id.etPostCode))
                .perform(ViewActions.scrollTo(), ViewActions.typeText("A"))

        // type too short city
        onView(withId(R.id.etCity))
                .perform(ViewActions.scrollTo(), ViewActions.typeText("A"))

        // type Street without nr/street name (only 1 word)
        onView(withId(R.id.etStreet))
                .perform(ViewActions.scrollTo(), ViewActions.typeText("Street"))

        closeSoftKeyboard()

        // Click registration button
        val btn = withId(R.id.btnCreateCompany)
        onView(btn).perform(ViewActions.scrollTo(), ViewActions.click())

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
                .perform(ViewActions.scrollTo(), ViewActions.typeText("A".repeat(characterLimit + 1)))

        // type too long company position
        onView(withId(R.id.etCompanyPosition))
                .perform(ViewActions.scrollTo(), ViewActions.typeText("A".repeat(characterLimit + 1)))

        // type too long postal code
        onView(withId(R.id.etPostCode))
                .perform(ViewActions.scrollTo(), ViewActions.typeText("A".repeat(characterLimitPostalCode + 1)))

        // type too long city
        onView(withId(R.id.etCity))
                .perform(ViewActions.scrollTo(), ViewActions.typeText("A".repeat(characterLimit + 1)))

        closeSoftKeyboard()

        // Click registration button
        val btn = withId(R.id.btnCreateCompany)
        onView(btn).perform(ViewActions.scrollTo(), ViewActions.click())

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
    fun validInputData() {

    }

}