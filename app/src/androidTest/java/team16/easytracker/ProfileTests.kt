package team16.easytracker

import android.app.Activity
import android.os.Bundle
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
import androidx.test.platform.TestFrameworkException
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.runner.lifecycle.ActivityLifecycleMonitorRegistry
import androidx.test.runner.lifecycle.Stage
import org.junit.Before
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
class ProfileTests : TestFramework() {

    fun getCurrentActivity(): Activity? {
        var currentActivity: Activity? = null
        InstrumentationRegistry.getInstrumentation().runOnMainSync { run { currentActivity = ActivityLifecycleMonitorRegistry.getInstance().getActivitiesInStage(Stage.RESUMED).elementAtOrNull(0) } }
        return currentActivity
    }

    @Before
    override fun setup() {
        super.setup()
        //insertDummyAddress()
        setupLoggedInWorker()
    }

    @get:Rule
    val activityRule = ActivityScenarioRule(HomeActivity::class.java)

    @Test
    fun invalidSettingsWithEmptyInputData() {
        val currentActivity: HomeActivity = getCurrentActivity() as HomeActivity
        val profileFragment = ProfileFragment()
        currentActivity!!.supportFragmentManager.beginTransaction()
                .replace(R.id.flFragment, profileFragment, "ProfileFragment")
                .addToBackStack(null)
                .commit()
        // Error textviews should not be visible before clicking registration button
        onView(withId(R.id.tvErrorGenderSetting))
                .check(matches(not(isDisplayed())))

        onView(withId(R.id.tvErrorTitleSetting))
                .check(matches(not(isDisplayed())))

        onView(withId(R.id.tvErrorFirstNameSetting))
                .check(matches(not(isDisplayed())))

        onView(withId(R.id.tvErrorLastNameSetting))
                .check(matches(not(isDisplayed())))

        onView(withId(R.id.tvErrorPhonePrefixSetting))
                .check(matches(not(isDisplayed())))

        onView(withId(R.id.tvErrorPhoneNumberSetting))
                .check(matches(not(isDisplayed())))

        onView(withId(R.id.tvErrorPostCodeSetting))
                .check(matches(not(isDisplayed())))

        onView(withId(R.id.tvErrorCitySetting))
                .check(matches(not(isDisplayed())))

        onView(withId(R.id.tvErrorStreetSetting))
                .check(matches(not(isDisplayed())))

        onView(withId(R.id.tvErrorUsernameSetting))
                .check(matches(not(isDisplayed())))

        // Click SaveChanges button
        onView(withId(R.id.btnSaveChangesSetting))
                .perform(scrollTo(), click())

        // Most error textviews should now be visible
        onView(withId(R.id.etTitleSetting))
                .perform(scrollTo())

        onView(withId(R.id.etFirstNameSetting))
                .perform(scrollTo())
        onView(withId(R.id.tvErrorTitleSetting))
                .check(matches(not(isDisplayed())))

        onView(withId(R.id.etLastNameSetting))
                .perform(scrollTo())
        onView(withId(R.id.tvErrorFirstNameSetting))
                .check(matches(not(isDisplayed())))

        onView(withId(R.id.etPhoneNumberSetting))
                .perform(scrollTo())
        onView(withId(R.id.tvErrorLastNameSetting))
                .check(matches(not(isDisplayed())))

        onView(withId(R.id.etPostCodeSetting))
                .perform(scrollTo())
        onView(withId(R.id.tvErrorPhoneNumberSetting))
                .check(matches(not(isDisplayed())))

        onView(withId(R.id.etStreetSetting))
                .perform(scrollTo())
        onView(withId(R.id.tvErrorPostCodeSetting))
                .check(matches(not(isDisplayed())))

        onView(withId(R.id.etStreetSetting))
                .perform(scrollTo())
        onView(withId(R.id.tvErrorCitySetting))
                .check(matches(not(isDisplayed())))

        onView(withId(R.id.btnSaveChangesSetting))
                .perform(scrollTo())
        onView(withId(R.id.tvErrorStreetSetting))
                .check(matches(not(isDisplayed())))

        onView(allOf(withId(com.google.android.material.R.id.snackbar_text)))
                .check(matches(withText(R.string.update_settings_succeeded)))
        Thread.sleep(1000)
    }


    @Test
    fun invalidRegister_titleContainsNumber() {
        val currentActivity: HomeActivity = getCurrentActivity() as HomeActivity
        val profileFragment = ProfileFragment()
        currentActivity!!.supportFragmentManager.beginTransaction()
                .replace(R.id.flFragment, profileFragment, "ProfileFragment")
                .addToBackStack(null)
                .commit()

        onView(withId(R.id.tvErrorTitleSetting))
                .check(matches(not(isDisplayed())))

        onView(withId(R.id.etTitleSetting))
                .perform(typeText("Invalid1"), closeSoftKeyboard())

        // Click registration button
        onView(withId(R.id.btnSaveChangesSetting))
                .perform(scrollTo(), click())

        onView(withId(R.id.etTitleSetting))
                .perform(scrollTo())
        onView(withId(R.id.tvErrorTitleSetting))
                .check(matches(isDisplayed()))
                .check(matches(withText(R.string.no_number_title)))
    }


    @Test
    fun invalidRegister_firstNameContainsNumber() {
        val currentActivity: HomeActivity = getCurrentActivity() as HomeActivity
        val profileFragment = ProfileFragment()
        currentActivity!!.supportFragmentManager.beginTransaction()
                .replace(R.id.flFragment, profileFragment, "ProfileFragment")
                .addToBackStack(null)
                .commit()

        onView(withId(R.id.etLastNameSetting))
                .perform(scrollTo())
        onView(withId(R.id.tvErrorFirstNameSetting))
                .check(matches(not(isDisplayed())))

        onView(withId(R.id.etFirstNameSetting))
                .perform(typeText("John1"), closeSoftKeyboard())

        // Click registration button
        onView(withId(R.id.btnSaveChangesSetting))
                .perform(scrollTo(), click())

        onView(withId(R.id.etFirstNameSetting))
                .perform(scrollTo())
        onView(withId(R.id.tvErrorFirstNameSetting))
                .check(matches(isDisplayed()))
                .check(matches(withText(R.string.first_name_no_numbers)))
    }


    @Test
    fun invalidRegister_firstNameTooShort() {
        val currentActivity: HomeActivity = getCurrentActivity() as HomeActivity
        val profileFragment = ProfileFragment()
        currentActivity!!.supportFragmentManager.beginTransaction()
                .replace(R.id.flFragment, profileFragment, "ProfileFragment")
                .addToBackStack(null)
                .commit()

        onView(withId(R.id.etLastNameSetting))
                .perform(scrollTo())
        onView(withId(R.id.tvErrorFirstNameSetting))
                .check(matches(not(isDisplayed())))

        onView(withId(R.id.etFirstNameSetting))
                .perform(replaceText("J"), closeSoftKeyboard())

        // Click registration button
        onView(withId(R.id.btnSaveChangesSetting))
                .perform(scrollTo(), click())

        onView(withId(R.id.etFirstNameSetting))
                .perform(scrollTo())
        onView(withId(R.id.tvErrorFirstNameSetting))
                .check(matches(isDisplayed()))
                .check(matches(withText(R.string.first_name_length)))
    }


    @Test
    fun invalidRegister_firstNameTooLong() {
        val currentActivity: HomeActivity = getCurrentActivity() as HomeActivity
        val profileFragment = ProfileFragment()
        currentActivity!!.supportFragmentManager.beginTransaction()
                .replace(R.id.flFragment, profileFragment, "ProfileFragment")
                .addToBackStack(null)
                .commit()

        onView(withId(R.id.etLastNameSetting))
                .perform(scrollTo())
        onView(withId(R.id.tvErrorFirstNameSetting))
                .check(matches(not(isDisplayed())))

        onView(withId(R.id.etFirstNameSetting))
                .perform(replaceText("John".repeat(100)), closeSoftKeyboard())
                .check(matches(withText("John".repeat(100))))

        // Click registration button
        onView(withId(R.id.btnSaveChangesSetting))
                .perform(scrollTo(), click())

        onView(withId(R.id.etFirstNameSetting))
                .perform(scrollTo())
        onView(withId(R.id.tvErrorFirstNameSetting))
                .check(matches(isDisplayed()))
                .check(matches(withText(R.string.first_name_length)))
    }


    @Test
    fun invalidRegister_lastNameContainsNumber() {
        val currentActivity: HomeActivity = getCurrentActivity() as HomeActivity
        val profileFragment = ProfileFragment()
        currentActivity!!.supportFragmentManager.beginTransaction()
                .replace(R.id.flFragment, profileFragment, "ProfileFragment")
                .addToBackStack(null)
                .commit()

        onView(withId(R.id.etPhoneNumberSetting))
                .perform(scrollTo())
        onView(withId(R.id.tvErrorLastNameSetting))
                .check(matches(not(isDisplayed())))

        onView(withId(R.id.etLastNameSetting))
                .perform(replaceText("Doe1"), closeSoftKeyboard())
                .check(matches(withText("Doe1")))

        // Click registration button
        onView(withId(R.id.btnSaveChangesSetting))
                .perform(scrollTo(), click())

        onView(withId(R.id.etLastNameSetting))
                .perform(scrollTo())
        onView(withId(R.id.tvErrorLastNameSetting))
                .check(matches(isDisplayed()))
                .check(matches(withText(R.string.last_name_no_numbers)))
    }


    @Test
    fun invalidRegister_lastNameTooShort() {
        val currentActivity: HomeActivity = getCurrentActivity() as HomeActivity
        val profileFragment = ProfileFragment()
        currentActivity!!.supportFragmentManager.beginTransaction()
                .replace(R.id.flFragment, profileFragment, "ProfileFragment")
                .addToBackStack(null)
                .commit()

        onView(withId(R.id.etPhoneNumberSetting))
                .perform(scrollTo())
        onView(withId(R.id.tvErrorLastNameSetting))
                .check(matches(not(isDisplayed())))

        onView(withId(R.id.etLastNameSetting))
                .perform(replaceText("D"), closeSoftKeyboard())
                .check(matches(withText("D")))

        // Click registration button
        onView(withId(R.id.btnSaveChangesSetting))
                .perform(scrollTo(), click())

        onView(withId(R.id.etLastNameSetting))
                .perform(scrollTo())
        onView(withId(R.id.tvErrorLastNameSetting))
                .check(matches(isDisplayed()))
                .check(matches(withText(R.string.last_name_length)))
    }


    @Test
    fun invalidRegister_lastNameTooLong() {
        val currentActivity: HomeActivity = getCurrentActivity() as HomeActivity
        val profileFragment = ProfileFragment()
        currentActivity!!.supportFragmentManager.beginTransaction()
                .replace(R.id.flFragment, profileFragment, "ProfileFragment")
                .addToBackStack(null)
                .commit()

        onView(withId(R.id.etPhoneNumberSetting))
                .perform(scrollTo())
        onView(withId(R.id.tvErrorLastNameSetting))
                .check(matches(not(isDisplayed())))

        onView(withId(R.id.etLastNameSetting))
                .perform(replaceText("Doe".repeat(100)), closeSoftKeyboard())
                .check(matches(withText("Doe".repeat(100))))

        // Click registration button
        onView(withId(R.id.btnSaveChangesSetting))
                .perform(scrollTo(), click())

        onView(withId(R.id.etLastNameSetting))
                .perform(scrollTo())
        onView(withId(R.id.tvErrorLastNameSetting))
                .check(matches(isDisplayed()))
                .check(matches(withText(R.string.last_name_length)))
    }

    @Test
    fun invalidRegister_phoneNumberContainsLetter() {
        val currentActivity: HomeActivity = getCurrentActivity() as HomeActivity
        val profileFragment = ProfileFragment()
        currentActivity!!.supportFragmentManager.beginTransaction()
                .replace(R.id.flFragment, profileFragment, "ProfileFragment")
                .addToBackStack(null)
                .commit()

        onView(withId(R.id.etPostCodeSetting))
                .perform(scrollTo())
        onView(withId(R.id.tvErrorPhoneNumberSetting))
                .check(matches(not(isDisplayed())))

        onView(withId(R.id.etPhoneNumberSetting))
                .perform(replaceText("67812345a"), closeSoftKeyboard())
                .check(matches(withText("67812345a")))

        // Click registration button
        onView(withId(R.id.btnSaveChangesSetting))
                .perform(scrollTo(), click())

        onView(withId(R.id.etPhoneNumberSetting))
                .perform(scrollTo())
        onView(withId(R.id.tvErrorPhoneNumberSetting))
                .check(matches(isDisplayed()))
                .check(matches(withText(R.string.phone_number_digits)))
    }


    @Test
    fun invalidRegister_phoneNumberTooShort() {
        val currentActivity: HomeActivity = getCurrentActivity() as HomeActivity
        val profileFragment = ProfileFragment()
        currentActivity!!.supportFragmentManager.beginTransaction()
                .replace(R.id.flFragment, profileFragment, "ProfileFragment")
                .addToBackStack(null)
                .commit()

        onView(withId(R.id.etPostCodeSetting))
                .perform(scrollTo())
        onView(withId(R.id.tvErrorPhoneNumberSetting))
                .check(matches(not(isDisplayed())))

        onView(withId(R.id.etPhoneNumberSetting))
                .perform(replaceText("6"), closeSoftKeyboard())
                .check(matches(withText("6")))

        // Click registration button
        onView(withId(R.id.btnSaveChangesSetting))
                .perform(scrollTo(), click())

        onView(withId(R.id.etPhoneNumberSetting))
                .perform(scrollTo())
        onView(withId(R.id.tvErrorPhoneNumberSetting))
                .check(matches(isDisplayed()))
                .check(matches(withText(R.string.phone_number_length)))
    }


    @Test
    fun invalidRegister_phoneNumberTooLong() {
        val currentActivity: HomeActivity = getCurrentActivity() as HomeActivity
        val profileFragment = ProfileFragment()
        currentActivity!!.supportFragmentManager.beginTransaction()
                .replace(R.id.flFragment, profileFragment, "ProfileFragment")
                .addToBackStack(null)
                .commit()

        onView(withId(R.id.etPostCodeSetting))
                .perform(scrollTo())
        onView(withId(R.id.tvErrorPhoneNumberSetting))
                .check(matches(not(isDisplayed())))

        onView(withId(R.id.etPhoneNumberSetting))
                .perform(replaceText("67812345678987"), closeSoftKeyboard())
                .check(matches(withText("67812345678987")))

        // Click registration button
        onView(withId(R.id.btnSaveChangesSetting))
                .perform(scrollTo(), click())

        onView(withId(R.id.etPhoneNumberSetting))
                .perform(scrollTo())
        onView(withId(R.id.tvErrorPhoneNumberSetting))
                .check(matches(isDisplayed()))
                .check(matches(withText(R.string.phone_number_length)))
    }


    @Test
    fun invalidRegister_postCodeTooShort() {
        val currentActivity: HomeActivity = getCurrentActivity() as HomeActivity
        val profileFragment = ProfileFragment()
        currentActivity!!.supportFragmentManager.beginTransaction()
                .replace(R.id.flFragment, profileFragment, "ProfileFragment")
                .addToBackStack(null)
                .commit()

        onView(withId(R.id.etStreetSetting))
                .perform(scrollTo())
        onView(withId(R.id.tvErrorPostCodeSetting))
                .check(matches(not(isDisplayed())))

        onView(withId(R.id.etPostCodeSetting))
                .perform(replaceText("80"), closeSoftKeyboard())
                .check(matches(withText("80")))

        // Click registration button
        onView(withId(R.id.btnSaveChangesSetting))
                .perform(scrollTo(), click())

        onView(withId(R.id.etPostCodeSetting))
                .perform(scrollTo())
        onView(withId(R.id.tvErrorPostCodeSetting))
                .check(matches(isDisplayed()))
                .check(matches(withText(R.string.post_code_length)))
    }


    @Test
    fun invalidRegister_postCodeTooLong() {
        val currentActivity: HomeActivity = getCurrentActivity() as HomeActivity
        val profileFragment = ProfileFragment()
        currentActivity!!.supportFragmentManager.beginTransaction()
                .replace(R.id.flFragment, profileFragment, "ProfileFragment")
                .addToBackStack(null)
                .commit()

        onView(withId(R.id.etStreetSetting))
                .perform(scrollTo())
        onView(withId(R.id.tvErrorPostCodeSetting))
                .check(matches(not(isDisplayed())))

        onView(withId(R.id.etPostCodeSetting))
                .perform(replaceText("801045702095"), closeSoftKeyboard())
                .check(matches(withText("801045702095")))

        // Click registration button
        onView(withId(R.id.btnSaveChangesSetting))
                .perform(scrollTo(), click())

        onView(withId(R.id.etPostCodeSetting))
                .perform(scrollTo())
        onView(withId(R.id.tvErrorPostCodeSetting))
                .check(matches(isDisplayed()))
                .check(matches(withText(R.string.post_code_length)))
    }


    @Test
    fun invalidRegister_cityContainsNumber() {
        val currentActivity: HomeActivity = getCurrentActivity() as HomeActivity
        val profileFragment = ProfileFragment()
        currentActivity!!.supportFragmentManager.beginTransaction()
                .replace(R.id.flFragment, profileFragment, "ProfileFragment")
                .addToBackStack(null)
                .commit()

        onView(withId(R.id.etStreetSetting))
                .perform(scrollTo())
        onView(withId(R.id.tvErrorCitySetting))
                .check(matches(not(isDisplayed())))

        onView(withId(R.id.etCitySetting))
                .perform(replaceText("Graz 5"), closeSoftKeyboard())
                .check(matches(withText("Graz 5")))

        // Click registration button
        onView(withId(R.id.btnSaveChangesSetting))
                .perform(scrollTo(), click())

        onView(withId(R.id.etCitySetting))
                .perform(scrollTo())
        onView(withId(R.id.tvErrorCitySetting))
                .check(matches(isDisplayed()))
                .check(matches(withText(R.string.city_name_format)))
    }


    @Test
    fun invalidRegister_cityTooShort() {
        val currentActivity: HomeActivity = getCurrentActivity() as HomeActivity
        val profileFragment = ProfileFragment()
        currentActivity!!.supportFragmentManager.beginTransaction()
                .replace(R.id.flFragment, profileFragment, "ProfileFragment")
                .addToBackStack(null)
                .commit()

        onView(withId(R.id.etStreetSetting))
                .perform(scrollTo())
        onView(withId(R.id.tvErrorCitySetting))
                .check(matches(not(isDisplayed())))

        onView(withId(R.id.etCitySetting))
                .perform(replaceText("G"), closeSoftKeyboard())
                .check(matches(withText("G")))

        // Click registration button
        onView(withId(R.id.btnSaveChangesSetting))
                .perform(scrollTo(), click())

        onView(withId(R.id.etCitySetting))
                .perform(scrollTo())
        onView(withId(R.id.tvErrorCitySetting))
                .check(matches(isDisplayed()))
                .check(matches(withText(R.string.city_name_length)))
    }


    @Test
    fun invalidRegister_cityTooLong() {
        val currentActivity: HomeActivity = getCurrentActivity() as HomeActivity
        val profileFragment = ProfileFragment()
        currentActivity!!.supportFragmentManager.beginTransaction()
                .replace(R.id.flFragment, profileFragment, "ProfileFragment")
                .addToBackStack(null)
                .commit()

        onView(withId(R.id.etStreetSetting))
                .perform(scrollTo())
        onView(withId(R.id.tvErrorCitySetting))
                .check(matches(not(isDisplayed())))

        onView(withId(R.id.etCitySetting))
                .perform(replaceText("Graz".repeat(100)), closeSoftKeyboard())
                .check(matches(withText("Graz".repeat(100))))

        // Click registration button
        onView(withId(R.id.btnSaveChangesSetting))
                .perform(scrollTo(), click())

        onView(withId(R.id.etCitySetting))
                .perform(scrollTo())
        onView(withId(R.id.tvErrorCitySetting))
                .check(matches(isDisplayed()))
                .check(matches(withText(R.string.city_name_length)))
    }


    @Test
    fun invalidRegister_streetDoesNotContainNumber() {
        val currentActivity: HomeActivity = getCurrentActivity() as HomeActivity
        val profileFragment = ProfileFragment()
        currentActivity!!.supportFragmentManager.beginTransaction()
                .replace(R.id.flFragment, profileFragment, "ProfileFragment")
                .addToBackStack(null)
                .commit()

        onView(withId(R.id.btnSaveChangesSetting))
                .perform(scrollTo())
        onView(withId(R.id.tvErrorStreetSetting))
                .check(matches(not(isDisplayed())))

        onView(withId(R.id.etStreetSetting))
                .perform(replaceText("Hauptstraße"), closeSoftKeyboard())
                .check(matches(withText("Hauptstraße")))

        // Click registration button
        onView(withId(R.id.btnSaveChangesSetting))
                .perform(scrollTo(), click())

        onView(withId(R.id.etStreetSetting))
                .perform(scrollTo())
        onView(withId(R.id.tvErrorStreetSetting))
                .check(matches(isDisplayed()))
                .check(matches(withText(R.string.street_name_format)))
    }

    @Test
    fun invalidChangePassword_wrongOldPassword(){
        val currentActivity: HomeActivity = getCurrentActivity() as HomeActivity
        val profileFragment = ProfileFragment()
        currentActivity!!.supportFragmentManager.beginTransaction()
                .replace(R.id.flFragment, profileFragment, "ProfileFragment")
                .addToBackStack(null)
                .commit()

        // Click registration button
        onView(withId(R.id.btnChangePassword))
                .perform(scrollTo(), click())

        onView(withId(R.id.etOldPassword))
                .perform(replaceText("asdfasdfasdf"), closeSoftKeyboard())
                .check(matches(withText("asdfasdfasdf")))

        onView(withId(android.R.id.button1))
                .perform(scrollTo(), click())

        onView(allOf(withId(com.google.android.material.R.id.snackbar_text)))
                .check(matches(withText(R.string.failed_update_password)))
    }

    @Test
    fun invalidChangePassword_NewPasswordTooShort(){
        val currentActivity: HomeActivity = getCurrentActivity() as HomeActivity
        val profileFragment = ProfileFragment()
        currentActivity!!.supportFragmentManager.beginTransaction()
                .replace(R.id.flFragment, profileFragment, "ProfileFragment")
                .addToBackStack(null)
                .commit()

        // Click registration button
        onView(withId(R.id.btnChangePassword))
                .perform(scrollTo(), click())

        onView(withId(R.id.etNewPassword))
                .perform(replaceText("asdfasd"), closeSoftKeyboard())
                .check(matches(withText("asdfasd")))

        onView(withId(android.R.id.button1))
                .perform(scrollTo(), click())

        onView(allOf(withId(com.google.android.material.R.id.snackbar_text)))
                .check(matches(withText(R.string.failed_update_password)))
    }

    @Test
    fun validChangePassword(){
        val currentActivity: HomeActivity = getCurrentActivity() as HomeActivity
        val profileFragment = ProfileFragment()
        currentActivity!!.supportFragmentManager.beginTransaction()
                .replace(R.id.flFragment, profileFragment, "ProfileFragment")
                .addToBackStack(null)
                .commit()

        // Click registration button
        onView(withId(R.id.btnChangePassword))
                .perform(scrollTo(), click())

        onView(withId(R.id.etOldPassword))
                .perform(replaceText("12345678"), closeSoftKeyboard())
                .check(matches(withText("12345678")))

        onView(withId(R.id.etNewPassword))
                .perform(replaceText("87654321"), closeSoftKeyboard())
                .check(matches(withText("87654321")))

        onView(withId(android.R.id.button1))
                .perform(scrollTo(), click())

        onView(allOf(withId(com.google.android.material.R.id.snackbar_text)))
                .check(matches(withText(R.string.update_password_succeeded)))
    }
}
