package team16.easytracker

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.hamcrest.CoreMatchers.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class WorkerLoginTests {

    @get:Rule
    val activityRule = ActivityScenarioRule(LoginActivity::class.java)


    @Test
    fun invalidLoginWithEmptyInputData() {
        // Error textviews should not be visible before clicking login button

        onView(withId(R.id.tvErrorEmail))
                .check(matches(not(isDisplayed())))

        onView(withId(R.id.tvErrorPassword))
                .check(matches(not(isDisplayed())))

        // Click login button
        onView(withId(R.id.btnLogin))
                .perform(click())

       // Most error textviews should now be visible
        onView(withId(R.id.tvErrorEmail))
                .check(matches(isDisplayed()))
                .check(matches(withText("The email is required")))


        onView(withId(R.id.tvErrorPassword))
                .check(matches(isDisplayed()))
                .check(matches(withText("The password is required")))

        Thread.sleep(1000)
    }

    @Test
    fun invalidLogin_emailNotValid() {
        onView(withId(R.id.etEmail))
                .perform(typeText("john.doe.mail"), closeSoftKeyboard())
                .check(matches(withText("john.doe.mail")))

        // Click Login button
        onView(withId(R.id.btnLogin))
                .perform(click())

        onView(withId(R.id.tvErrorEmail))
                .check(matches(isDisplayed()))
                .check(matches(withText("The email must be a valid email address")))
    }


}
