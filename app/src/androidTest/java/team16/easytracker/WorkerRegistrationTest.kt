package team16.easytracker

import android.widget.EditText
import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.hamcrest.CoreMatchers.*
import android.view.View
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class WorkerRegistrationTest : TestFramework() {

    @get:Rule
    val activityRule = ActivityScenarioRule(RegisterActivity::class.java)

    @Test
    fun invalidRegisterWithEmptyInputData() {
        // Error textviews should not be visible before clicking registration button
        onView(withId(R.id.tvErrorGender))
                .check(matches(not(isDisplayed())))

        onView(withId(R.id.tvErrorTitle))
                .check(matches(not(isDisplayed())))

        onView(withId(R.id.tvErrorFirstName))
                .check(matches(not(isDisplayed())))

        onView(withId(R.id.tvErrorLastName))
                .check(matches(not(isDisplayed())))

        onView(withId(R.id.tvErrorEmail))
                .check(matches(not(isDisplayed())))

        onView(withId(R.id.tvErrorDateOfBirth))
                .check(matches(not(isDisplayed())))

        onView(withId(R.id.tvErrorPhonePrefix))
                .check(matches(not(isDisplayed())))

        onView(withId(R.id.tvErrorPhoneNumber))
                .check(matches(not(isDisplayed())))

        onView(withId(R.id.tvErrorPostCode))
                .check(matches(not(isDisplayed())))

        onView(withId(R.id.tvErrorCity))
                .check(matches(not(isDisplayed())))

        onView(withId(R.id.tvErrorStreet))
                .check(matches(not(isDisplayed())))

        onView(withId(R.id.tvErrorUsername))
                .check(matches(not(isDisplayed())))

        onView(withId(R.id.tvErrorPassword))
                .check(matches(not(isDisplayed())))

        // Click registration button
        onView(withId(R.id.btnRegistration))
                .perform(scrollTo(), click())

        // Most error textviews should now be visible
        onView(withId(R.id.etTitle))
                .perform(scrollTo())
        onView(withId(R.id.tvErrorGender))
                .check(matches(not(isDisplayed())))

        onView(withId(R.id.etFirstName))
                .perform(scrollTo())
        onView(withId(R.id.tvErrorTitle))
                .check(matches(not(isDisplayed())))

        onView(withId(R.id.etLastName))
                .perform(scrollTo())
        onView(withId(R.id.tvErrorFirstName))
                .check(matches(isDisplayed()))
                .check(matches(withText(R.string.first_name_required)))

        onView(withId(R.id.etEmail))
                .perform(scrollTo())
        onView(withId(R.id.tvErrorLastName))
                .check(matches(isDisplayed()))
                .check(matches(withText(R.string.last_name_required)))

        onView(withId(R.id.etDateOfBirth))
                .perform(scrollTo())
        onView(withId(R.id.tvErrorEmail))
                .check(matches(isDisplayed()))
                .check(matches(withText(R.string.email_required)))

        onView(withId(R.id.etPhonePrefix))
                .perform(scrollTo())
        onView(withId(R.id.tvErrorDateOfBirth))
                .check(matches(isDisplayed()))
                .check(matches(withText(R.string.dob_required)))

        onView(withId(R.id.etPhoneNumber))
                .perform(scrollTo())
        onView(withId(R.id.tvErrorPhonePrefix))
                .check(matches(isDisplayed()))
                .check(matches(withText(R.string.phone_prefix_required)))

        onView(withId(R.id.etPostCode))
                .perform(scrollTo())
        onView(withId(R.id.tvErrorPhoneNumber))
                .check(matches(isDisplayed()))
                .check(matches(withText(R.string.phone_nr_required)))

        onView(withId(R.id.etStreet))
                .perform(scrollTo())
        onView(withId(R.id.tvErrorPostCode))
                .check(matches(isDisplayed()))
                .check(matches(withText(R.string.post_code_required)))

        onView(withId(R.id.etStreet))
                .perform(scrollTo())
        onView(withId(R.id.tvErrorCity))
                .check(matches(isDisplayed()))
                .check(matches(withText(R.string.city_required)))

        onView(withId(R.id.etUsername))
                .perform(scrollTo())
        onView(withId(R.id.tvErrorStreet))
                .check(matches(isDisplayed()))
                .check(matches(withText(R.string.street_required)))

        onView(withId(R.id.etPassword))
                .perform(scrollTo())
        onView(withId(R.id.tvErrorUsername))
                .check(matches(isDisplayed()))
                .check(matches(withText(R.string.username_required)))

        onView(withId(R.id.btnRegistration))
                .perform(scrollTo())
        onView(withId(R.id.tvErrorPassword))
                .check(matches(isDisplayed()))
                .check(matches(withText(R.string.pw_required)))

        Thread.sleep(1000)
    }

    
    @Test
    fun invalidRegister_titleContainsNumber() {
        onView(withId(R.id.tvErrorTitle))
                .check(matches(not(isDisplayed())))

        onView(withId(R.id.etTitle))
                .perform(typeText("Invalid1"), closeSoftKeyboard())
                .check(matches(withText("Invalid1")))

        // Click registration button
        onView(withId(R.id.btnRegistration))
                .perform(scrollTo(), click())

        onView(withId(R.id.etTitle))
                .perform(scrollTo())
        onView(withId(R.id.tvErrorTitle))
                .check(matches(isDisplayed()))
                .check(matches(withText(R.string.no_number_title)))
    }

    
    @Test
    fun invalidRegister_firstNameContainsNumber() {
        onView(withId(R.id.etLastName))
                .perform(scrollTo())
        onView(withId(R.id.tvErrorFirstName))
                .check(matches(not(isDisplayed())))

        onView(withId(R.id.etFirstName))
                .perform(typeText("John1"), closeSoftKeyboard())
                .check(matches(withText("John1")))

        // Click registration button
        onView(withId(R.id.btnRegistration))
                .perform(scrollTo(), click())

        onView(withId(R.id.etFirstName))
                .perform(scrollTo())
        onView(withId(R.id.tvErrorFirstName))
                .check(matches(isDisplayed()))
                .check(matches(withText(R.string.first_name_no_numbers)))
    }

    
    @Test
    fun invalidRegister_firstNameTooShort() {
        onView(withId(R.id.etLastName))
                .perform(scrollTo())
        onView(withId(R.id.tvErrorFirstName))
                .check(matches(not(isDisplayed())))

        onView(withId(R.id.etFirstName))
                .perform(typeText("J"), closeSoftKeyboard())
                .check(matches(withText("J")))

        // Click registration button
        onView(withId(R.id.btnRegistration))
                .perform(scrollTo(), click())

        onView(withId(R.id.etFirstName))
                .perform(scrollTo())
        onView(withId(R.id.tvErrorFirstName))
                .check(matches(isDisplayed()))
                .check(matches(withText(R.string.first_name_length)))
    }

    
    @Test
    fun invalidRegister_firstNameTooLong() {
        onView(withId(R.id.etLastName))
                .perform(scrollTo())
        onView(withId(R.id.tvErrorFirstName))
                .check(matches(not(isDisplayed())))

        onView(withId(R.id.etFirstName))
                .perform(typeText("John".repeat(100)), closeSoftKeyboard())
                .check(matches(withText("John".repeat(100))))

        // Click registration button
        onView(withId(R.id.btnRegistration))
                .perform(scrollTo(), click())

        onView(withId(R.id.etFirstName))
                .perform(scrollTo())
        onView(withId(R.id.tvErrorFirstName))
                .check(matches(isDisplayed()))
                .check(matches(withText(R.string.first_name_length)))
    }

    
    @Test
    fun invalidRegister_lastNameContainsNumber() {
        onView(withId(R.id.etEmail))
                .perform(scrollTo())
        onView(withId(R.id.tvErrorLastName))
                .check(matches(not(isDisplayed())))

        onView(withId(R.id.etLastName))
                .perform(typeText("Doe1"), closeSoftKeyboard())
                .check(matches(withText("Doe1")))

        // Click registration button
        onView(withId(R.id.btnRegistration))
                .perform(scrollTo(), click())

        onView(withId(R.id.etLastName))
                .perform(scrollTo())
        onView(withId(R.id.tvErrorLastName))
                .check(matches(isDisplayed()))
                .check(matches(withText(R.string.last_name_no_numbers)))
    }

    
    @Test
    fun invalidRegister_lastNameTooShort() {
        onView(withId(R.id.etEmail))
                .perform(scrollTo())
        onView(withId(R.id.tvErrorLastName))
                .check(matches(not(isDisplayed())))

        onView(withId(R.id.etLastName))
                .perform(typeText("D"), closeSoftKeyboard())
                .check(matches(withText("D")))

        // Click registration button
        onView(withId(R.id.btnRegistration))
                .perform(scrollTo(), click())

        onView(withId(R.id.etLastName))
                .perform(scrollTo())
        onView(withId(R.id.tvErrorLastName))
                .check(matches(isDisplayed()))
                .check(matches(withText(R.string.last_name_length)))
    }

    
    @Test
    fun invalidRegister_lastNameTooLong() {
        onView(withId(R.id.etEmail))
                .perform(scrollTo())
        onView(withId(R.id.tvErrorLastName))
                .check(matches(not(isDisplayed())))

        onView(withId(R.id.etLastName))
                .perform(typeText("Doe".repeat(100)), closeSoftKeyboard())
                .check(matches(withText("Doe".repeat(100))))

        // Click registration button
        onView(withId(R.id.btnRegistration))
                .perform(scrollTo(), click())

        onView(withId(R.id.etLastName))
                .perform(scrollTo())
        onView(withId(R.id.tvErrorLastName))
                .check(matches(isDisplayed()))
                .check(matches(withText(R.string.last_name_length)))
    }

    
    @Test
    fun invalidRegister_emailTooShort() {
        onView(withId(R.id.etDateOfBirth))
                .perform(scrollTo())
        onView(withId(R.id.tvErrorEmail))
                .check(matches(not(isDisplayed())))

        onView(withId(R.id.etEmail))
                .perform(typeText("j@.d"), closeSoftKeyboard())
                .check(matches(withText("j@.d")))

        // Click registration button
        onView(withId(R.id.btnRegistration))
                .perform(scrollTo(), click())

        onView(withId(R.id.etEmail))
                .perform(scrollTo())
        onView(withId(R.id.tvErrorEmail))
                .check(matches(isDisplayed()))
                .check(matches(withText(R.string.email_length)))
    }

    
    @Test
    fun invalidRegister_emailTooLong() {
        onView(withId(R.id.etDateOfBirth))
                .perform(scrollTo())
        onView(withId(R.id.tvErrorEmail))
                .check(matches(not(isDisplayed())))

        onView(withId(R.id.etEmail))
                .perform(typeText("john".repeat(100) + "@email.com"), closeSoftKeyboard())
                .check(matches(withText("john".repeat(100) + "@email.com")))

        // Click registration button
        onView(withId(R.id.btnRegistration))
                .perform(scrollTo(), click())

        onView(withId(R.id.etEmail))
                .perform(scrollTo())
        onView(withId(R.id.tvErrorEmail))
                .check(matches(isDisplayed()))
                .check(matches(withText(R.string.email_length)))
    }

    
    @Test
    fun invalidRegister_emailNotValid() {
        onView(withId(R.id.etDateOfBirth))
                .perform(scrollTo())
        onView(withId(R.id.tvErrorEmail))
                .check(matches(not(isDisplayed())))

        onView(withId(R.id.etEmail))
                .perform(typeText("john.doe.mail"), closeSoftKeyboard())
                .check(matches(withText("john.doe.mail")))

        // Click registration button
        onView(withId(R.id.btnRegistration))
                .perform(scrollTo(), click())

        onView(withId(R.id.etEmail))
                .perform(scrollTo())
        onView(withId(R.id.tvErrorEmail))
                .check(matches(isDisplayed()))
                .check(matches(withText(R.string.email_valid_address)))
    }

    
    @Test
    fun invalidRegister_dateOfBirthNotValid() {

        onView(withId(R.id.etPhonePrefix))
                .perform(scrollTo())
        onView(withId(R.id.tvErrorDateOfBirth))
                .check(matches(not(isDisplayed())))

        onView(withId(R.id.etDateOfBirth))
                .perform(typeText("1-12-1990"), closeSoftKeyboard())
                .check(matches(withText("1-12-1990")))

        // Click registration button
        onView(withId(R.id.btnRegistration))
                .perform(scrollTo(), click())

        onView(withId(R.id.etDateOfBirth))
                .perform(scrollTo())
        onView(withId(R.id.tvErrorDateOfBirth))
                .check(matches(isDisplayed()))
                .check(matches(withText(R.string.dob_format)))
    }


    @Test
    fun invalidRegister_phonePrefixContainsLetter() {
        onView(withId(R.id.etPostCode))
                .perform(scrollTo())
        onView(withId(R.id.tvErrorPhonePrefix))
                .check(matches(not(isDisplayed())))

        onView(withId(R.id.etPhonePrefix))
                .perform(typeText("43a"), closeSoftKeyboard())
                .check(matches(withText("43a")))

        // Click registration button
        onView(withId(R.id.btnRegistration))
                .perform(scrollTo(), click())

        onView(withId(R.id.etPhonePrefix))
                .perform(scrollTo())
        onView(withId(R.id.tvErrorPhonePrefix))
                .check(matches(isDisplayed()))
                .check(matches(withText(R.string.phone_prefix_digits)))
    }

    
    @Test
    fun invalidRegister_phonePrefixTooShort() {
        onView(withId(R.id.etPostCode))
                .perform(scrollTo())
        onView(withId(R.id.tvErrorPhonePrefix))
                .check(matches(not(isDisplayed())))

        onView(withId(R.id.etPhonePrefix))
                .perform(typeText("4"), closeSoftKeyboard())
                .check(matches(withText("4")))

        // Click registration button
        onView(withId(R.id.btnRegistration))
                .perform(scrollTo(), click())

        onView(withId(R.id.etPhonePrefix))
                .perform(scrollTo())
        onView(withId(R.id.tvErrorPhonePrefix))
                .check(matches(isDisplayed()))
                .check(matches(withText(R.string.phone_prefix_length)))
    }

    
    @Test
    fun invalidRegister_phonePrefixTooLong() {
        onView(withId(R.id.etPostCode))
                .perform(scrollTo())
        onView(withId(R.id.tvErrorPhonePrefix))
                .check(matches(not(isDisplayed())))

        onView(withId(R.id.etPhonePrefix))
                .perform(typeText("43421"), closeSoftKeyboard())
                .check(matches(withText("43421")))

        // Click registration button
        onView(withId(R.id.btnRegistration))
                .perform(scrollTo(), click())

        onView(withId(R.id.etPhonePrefix))
                .perform(scrollTo())
        onView(withId(R.id.tvErrorPhonePrefix))
                .check(matches(isDisplayed()))
                .check(matches(withText(R.string.phone_prefix_length)))
    }

    
    @Test
    fun invalidRegister_phoneNumberContainsLetter() {
        onView(withId(R.id.etPostCode))
                .perform(scrollTo())
        onView(withId(R.id.tvErrorPhoneNumber))
                .check(matches(not(isDisplayed())))

        onView(withId(R.id.etPhoneNumber))
                .perform(typeText("67812345a"), closeSoftKeyboard())
                .check(matches(withText("67812345a")))

        // Click registration button
        onView(withId(R.id.btnRegistration))
                .perform(scrollTo(), click())

        onView(withId(R.id.etPhoneNumber))
                .perform(scrollTo())
        onView(withId(R.id.tvErrorPhoneNumber))
                .check(matches(isDisplayed()))
                .check(matches(withText(R.string.phone_number_digits)))
    }

    
    @Test
    fun invalidRegister_phoneNumberTooShort() {
        onView(withId(R.id.etPostCode))
                .perform(scrollTo())
        onView(withId(R.id.tvErrorPhoneNumber))
                .check(matches(not(isDisplayed())))

        onView(withId(R.id.etPhoneNumber))
                .perform(typeText("6"), closeSoftKeyboard())
                .check(matches(withText("6")))

        // Click registration button
        onView(withId(R.id.btnRegistration))
                .perform(scrollTo(), click())

        onView(withId(R.id.etPhoneNumber))
                .perform(scrollTo())
        onView(withId(R.id.tvErrorPhoneNumber))
                .check(matches(isDisplayed()))
                .check(matches(withText(R.string.phone_number_length)))
    }

    
    @Test
    fun invalidRegister_phoneNumberTooLong() {
        onView(withId(R.id.etPostCode))
                .perform(scrollTo())
        onView(withId(R.id.tvErrorPhoneNumber))
                .check(matches(not(isDisplayed())))

        onView(withId(R.id.etPhoneNumber))
                .perform(typeText("67812345678987"), closeSoftKeyboard())
                .check(matches(withText("67812345678987")))

        // Click registration button
        onView(withId(R.id.btnRegistration))
                .perform(scrollTo(), click())

        onView(withId(R.id.etPhoneNumber))
                .perform(scrollTo())
        onView(withId(R.id.tvErrorPhoneNumber))
                .check(matches(isDisplayed()))
                .check(matches(withText(R.string.phone_number_length)))
    }

    
    @Test
    fun invalidRegister_postCodeTooShort() {
        onView(withId(R.id.etStreet))
                .perform(scrollTo())
        onView(withId(R.id.tvErrorPostCode))
                .check(matches(not(isDisplayed())))

        onView(withId(R.id.etPostCode))
                .perform(typeText("80"), closeSoftKeyboard())
                .check(matches(withText("80")))

        // Click registration button
        onView(withId(R.id.btnRegistration))
                .perform(scrollTo(), click())

        onView(withId(R.id.etPostCode))
                .perform(scrollTo())
        onView(withId(R.id.tvErrorPostCode))
                .check(matches(isDisplayed()))
                .check(matches(withText(R.string.post_code_length)))
    }

    
    @Test
    fun invalidRegister_postCodeTooLong() {
        onView(withId(R.id.etStreet))
                .perform(scrollTo())
        onView(withId(R.id.tvErrorPostCode))
                .check(matches(not(isDisplayed())))

        onView(withId(R.id.etPostCode))
                .perform(typeText("801045702095"), closeSoftKeyboard())
                .check(matches(withText("801045702095")))

        // Click registration button
        onView(withId(R.id.btnRegistration))
                .perform(scrollTo(), click())

        onView(withId(R.id.etPostCode))
                .perform(scrollTo())
        onView(withId(R.id.tvErrorPostCode))
                .check(matches(isDisplayed()))
                .check(matches(withText(R.string.post_code_length)))
    }

    
    @Test
    fun invalidRegister_cityContainsNumber() {
        onView(withId(R.id.etStreet))
                .perform(scrollTo())
        onView(withId(R.id.tvErrorCity))
                .check(matches(not(isDisplayed())))

        onView(withId(R.id.etCity))
                .perform(typeText("Graz 5"), closeSoftKeyboard())
                .check(matches(withText("Graz 5")))

        // Click registration button
        onView(withId(R.id.btnRegistration))
                .perform(scrollTo(), click())

        onView(withId(R.id.etCity))
                .perform(scrollTo())
        onView(withId(R.id.tvErrorCity))
                .check(matches(isDisplayed()))
                .check(matches(withText(R.string.city_name_format)))
    }

    
    @Test
    fun invalidRegister_cityTooShort() {
        onView(withId(R.id.etStreet))
                .perform(scrollTo())
        onView(withId(R.id.tvErrorCity))
                .check(matches(not(isDisplayed())))

        onView(withId(R.id.etCity))
                .perform(typeText("G"), closeSoftKeyboard())
                .check(matches(withText("G")))

        // Click registration button
        onView(withId(R.id.btnRegistration))
                .perform(scrollTo(), click())

        onView(withId(R.id.etCity))
                .perform(scrollTo())
        onView(withId(R.id.tvErrorCity))
                .check(matches(isDisplayed()))
                .check(matches(withText(R.string.city_name_length)))
    }

    
    @Test
    fun invalidRegister_cityTooLong() {
        onView(withId(R.id.etStreet))
                .perform(scrollTo())
        onView(withId(R.id.tvErrorCity))
                .check(matches(not(isDisplayed())))

        onView(withId(R.id.etCity))
                .perform(typeText("Graz".repeat(100)), closeSoftKeyboard())
                .check(matches(withText("Graz".repeat(100))))

        // Click registration button
        onView(withId(R.id.btnRegistration))
                .perform(scrollTo(), click())

        onView(withId(R.id.etCity))
                .perform(scrollTo())
        onView(withId(R.id.tvErrorCity))
                .check(matches(isDisplayed()))
                .check(matches(withText(R.string.city_name_length)))
    }

    
    @Test
    fun invalidRegister_streetDoesNotContainNumber() {
        onView(withId(R.id.etUsername))
                .perform(scrollTo())
        onView(withId(R.id.tvErrorStreet))
                .check(matches(not(isDisplayed())))

        onView(withId(R.id.etStreet))
                .perform(typeText("Hauptstraße"), closeSoftKeyboard())
                .check(matches(withText("Hauptstraße")))

        // Click registration button
        onView(withId(R.id.btnRegistration))
                .perform(scrollTo(), click())

        onView(withId(R.id.etStreet))
                .perform(scrollTo())
        onView(withId(R.id.tvErrorStreet))
                .check(matches(isDisplayed()))
                .check(matches(withText(R.string.street_name_format)))
    }

    
    @Test
    fun invalidRegister_passwordTooShort() {
        onView(withId(R.id.btnRegistration))
                .perform(scrollTo())
        onView(withId(R.id.tvErrorPassword))
                .check(matches(not(isDisplayed())))

        onView(withId(R.id.etPassword))
                .perform(typeText("1234567"), closeSoftKeyboard())
                .check(matches(withText("1234567")))

        // Click registration button
        onView(withId(R.id.btnRegistration))
                .perform(scrollTo(), click())

        onView(withId(R.id.btnRegistration))
                .perform(scrollTo())
        onView(withId(R.id.tvErrorPassword))
                .check(matches(isDisplayed()))
                .check(matches(withText(R.string.pw_length)))
    }

    
    @Test
    fun registerWithValidInputData() {
        // Error textviews should not be visible before clicking registration button
        onView(withId(R.id.tvErrorGender))
                .check(matches(not(isDisplayed())))

        onView(withId(R.id.tvErrorTitle))
                .check(matches(not(isDisplayed())))

        onView(withId(R.id.tvErrorFirstName))
                .check(matches(not(isDisplayed())))

        onView(withId(R.id.tvErrorLastName))
                .check(matches(not(isDisplayed())))

        onView(withId(R.id.tvErrorEmail))
                .check(matches(not(isDisplayed())))

        onView(withId(R.id.tvErrorDateOfBirth))
                .check(matches(not(isDisplayed())))

        onView(withId(R.id.tvErrorPhonePrefix))
                .check(matches(not(isDisplayed())))

        onView(withId(R.id.tvErrorPhoneNumber))
                .check(matches(not(isDisplayed())))

        onView(withId(R.id.tvErrorPostCode))
                .check(matches(not(isDisplayed())))

        onView(withId(R.id.tvErrorCity))
                .check(matches(not(isDisplayed())))

        onView(withId(R.id.tvErrorStreet))
                .check(matches(not(isDisplayed())))

        onView(withId(R.id.tvErrorUsername))
                .check(matches(not(isDisplayed())))

        onView(withId(R.id.tvErrorPassword))
                .check(matches(not(isDisplayed())))

        // Input valid data
        onView(withId(R.id.spGender))
                .perform(click())
        onData(allOf(`is`(instanceOf(String::class.java)), `is`("Male"))).perform(click())
        onView(withId(R.id.spGender)).check(matches(withSpinnerText(containsString("Male"))))

        onView(withId(R.id.etTitle))
                .perform(typeText("Dr."), closeSoftKeyboard())
                .check(matches(withText("Dr.")))

        onView(withId(R.id.etFirstName))
                .perform(typeText("Wolfgang"), closeSoftKeyboard())
                .check(matches(withText("Wolfgang")))

        onView(withId(R.id.etLastName))
                .perform(typeText("Slany"), closeSoftKeyboard())
                .check(matches(withText("Slany")))

        onView(withId(R.id.etEmail))
                .perform(typeText("w.s@catrobat.com"), closeSoftKeyboard())
                .check(matches(withText("w.s@catrobat.com")))

        onView(withId(R.id.etDateOfBirth))
                .perform(typeText("24.12.2000"), closeSoftKeyboard())
                .check(matches(withText("24.12.2000")))

        onView(withId(R.id.etPhonePrefix))
                .perform(typeText("43"), closeSoftKeyboard())
                .check(matches(withText("43")))

        onView(withId(R.id.etPhoneNumber))
                .perform(typeText("6641234567"), closeSoftKeyboard())
                .check(matches(withText("6641234567")))

        onView(withId(R.id.etPostCode))
                .perform(typeText("8010"), closeSoftKeyboard())
                .check(matches(withText("8010")))

        onView(withId(R.id.etCity))
                .perform(typeText("Graz"), closeSoftKeyboard())
                .check(matches(withText("Graz")))

        onView(withId(R.id.etStreet))
                .perform(typeText("Hauptplatz 1"), closeSoftKeyboard())
                .check(matches(withText("Hauptplatz 1")))

        onView(withId(R.id.etUsername))
                .perform(typeText("cat_robat"), closeSoftKeyboard())
                .check(matches(withText("cat_robat")))

        onView(withId(R.id.etPassword))
                .perform(typeText("CaTrObAt"), closeSoftKeyboard())
                .check(matches(withText("CaTrObAt")))

        onView(withId(R.id.btnRegistration))
                .perform(scrollTo(), click())
    }

}