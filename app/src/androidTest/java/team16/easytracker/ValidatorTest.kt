package team16.easytracker

import org.junit.*
import android.content.res.Resources
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert.*
import org.junit.runner.RunWith
import team16.easytracker.utils.Validator



/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ValidatorTest {

    val appContext = InstrumentationRegistry.getInstrumentation().targetContext
    lateinit var resources: Resources

    @get:Rule
    val activityRule = ActivityScenarioRule(HomeActivity::class.java)

    @Before
    fun init() {
        resources = appContext.resources
    }

    @Test
    fun titleIsCorrect() {

        // empty title
        assertEquals("", Validator.validateTitle("", resources))

        // valid title
        assertEquals("", Validator.validateTitle("Dr.", resources))

        // title contains number(s)
        assertEquals(resources.getString(R.string.no_number_title), Validator.validateTitle("Dr.1", resources))


    }

    @Test
    fun firstNameIsCorrect() {
        // valid first name
        assertEquals("", Validator.validateFirstName("Max", resources))

        // empty first name
        assertEquals(resources.getString(R.string.first_name_required), Validator.validateFirstName("", resources))

        // first name too short
        assertEquals(resources.getString(R.string.first_name_length), Validator.validateFirstName("M", resources))

        // first name too long
        assertEquals(resources.getString(R.string.first_name_length), Validator.validateFirstName("Max".repeat(100), resources))

        // first name contains number(s)
        assertEquals(resources.getString(R.string.first_name_no_numbers), Validator.validateFirstName("Max1", resources))
    }

    @Test
    fun lastNameIsCorrect() {
        // valid last name
        assertEquals("", Validator.validateLastName("Mustermann", resources))

        // empty last name
        assertEquals(resources.getString(R.string.last_name_required), Validator.validateLastName("", resources))

        // last name too short
        assertEquals(resources.getString(R.string.last_name_length), Validator.validateLastName("M", resources))

        // last name too long
        assertEquals(resources.getString(R.string.last_name_length), Validator.validateLastName("Mustermann".repeat(100), resources))

        // last name contains number(s)
        assertEquals(resources.getString(R.string.last_name_no_numbers), Validator.validateLastName("Mustermann1", resources))
    }

    @Test
    fun emailIsCorrect() {
        // valid email
        assertEquals("", Validator.validateEmail("max.mustermann@gmx.at", resources))

        // empty email
        assertEquals(resources.getString(R.string.email_required), Validator.validateEmail("", resources))

        // email without @
        assertEquals(resources.getString(R.string.email_valid_address), Validator.validateEmail("max.mustermanngmx.at", resources))

        // email without .
        assertEquals(resources.getString(R.string.email_valid_address), Validator.validateEmail("maxmustermann@gmxat", resources))

        // email too short
        assertEquals(resources.getString(R.string.email_length), Validator.validateEmail("m.m", resources))

        // email too long
        assertEquals(resources.getString(R.string.email_length), Validator.validateEmail("max.mustermann@gmxat".repeat(100), resources))
    }

    @Test
    fun dateOfBirthIsCorrect() {
        // valid date of birth
        assertEquals("", Validator.validateDateOfBirth("24.12.2000", resources))

        // empty date of birth
        assertEquals(resources.getString(R.string.dob_required), Validator.validateDateOfBirth("", resources))

        // date of birth invalid format
        assertEquals(resources.getString(R.string.dob_format), Validator.validateDateOfBirth("24.12.", resources))

        // date of birth invalid format
        assertEquals(resources.getString(R.string.dob_format), Validator.validateDateOfBirth("24-12-2000", resources))

        // date of birth without numbers
        assertEquals(resources.getString(R.string.dob_format), Validator.validateDateOfBirth("Date", resources))
    }

    @Test
    fun phonePrefixIsCorrect() {
        // valid phone prefix
        assertEquals("", Validator.validatePhonePrefix("43", resources))

        // valid phone prefix
        assertEquals("", Validator.validatePhonePrefix("212", resources))

        // empty phone prefix
        assertEquals(resources.getString(R.string.phone_prefix_required), Validator.validatePhonePrefix("", resources))

        // phone prefix too short
        assertEquals(resources.getString(R.string.phone_prefix_length), Validator.validatePhonePrefix("1", resources))

        // phone prefix too long
        assertEquals(resources.getString(R.string.phone_prefix_length), Validator.validatePhonePrefix("1".repeat(100), resources))

        // phone prefix contains not only numbers
        assertEquals(resources.getString(R.string.phone_prefix_digits), Validator.validatePhonePrefix("pref", resources))
    }

    @Test
    fun phoneNumberIsCorrect() {
        // valid phone number
        assertEquals("", Validator.validatePhoneNumber("6641234567", resources))

        // empty phone number
        assertEquals(resources.getString(R.string.phone_nr_required), Validator.validatePhoneNumber("", resources))

        // phone number too short
        assertEquals(resources.getString(R.string.phone_number_length), Validator.validatePhoneNumber("1", resources))

        // phone number too long
        assertEquals(resources.getString(R.string.phone_number_length), Validator.validatePhoneNumber("6641234567".repeat(100), resources))

        // phone prefix contains not only numbers
        assertEquals(resources.getString(R.string.phone_number_digits), Validator.validatePhoneNumber("phone number", resources))
    }

    @Test
    fun postCodeIsCorrect() {
        // valid post code
        assertEquals("", Validator.validatePostCode("8010", resources))

        // empty post code
        assertEquals(resources.getString(R.string.post_code_required), Validator.validatePostCode("", resources))

        // post code is too short
        assertEquals(resources.getString(R.string.post_code_length), Validator.validatePostCode("801", resources))

        // post code is too long
        assertEquals(resources.getString(R.string.post_code_length), Validator.validatePostCode("8010".repeat(100), resources))

        // post code does not start with a number
        //assertEquals(false, Validator.validatePostCode("postcode", resources))
    }

    @Test
    fun cityIsCorrect() {
        // valid city
        assertEquals("", Validator.validateCity("Graz", resources))

        // empty city
        assertEquals(resources.getString(R.string.city_required), Validator.validateCity("", resources))

        // city too short
        assertEquals(resources.getString(R.string.city_name_length), Validator.validateCity("G", resources))

        // city too long
        assertEquals(resources.getString(R.string.city_name_length), Validator.validateCity("Graz".repeat(100), resources))

        // city contains number(s)
        assertEquals(resources.getString(R.string.city_name_format), Validator.validateCity("Graz1", resources))
    }

    @Test
    fun streetIsCorrect() {
        // valid street
        assertEquals("", Validator.validateStreet("Hauptplatz 1", resources))

        // street is empty
        assertEquals(resources.getString(R.string.street_required), Validator.validateStreet("", resources))

        // street has not two parts
        assertEquals(resources.getString(R.string.street_name_format), Validator.validateStreet("Hauptplatz", resources))

        // street is too short
        //assertEquals(false, Validator.validateStreet("H", resources))

        // street is too long
        //assertEquals(false, Validator.validateStreet("Hauptplatz 1".repeat(100)))
    }

    @Test
    fun usernameIsCorrect() {
        // valid username
        assertEquals("", Validator.validateUsername("user_name", resources))

        // empty username
        assertEquals(resources.getString(R.string.username_required), Validator.validateUsername("", resources))

        // username too short
        //assertEquals(false, Validator.validateUsername("user", resources))

        // username too long
        //assertEquals(false, Validator.validateUsername("invalid_user".repeat(100)))
    }

    @Test
    fun passwordIsCorrect() {
        // valid password
        assertEquals("", Validator.validatePassword("validpassword", resources))

        // empty password
        assertEquals(resources.getString(R.string.pw_required), Validator.validatePassword("", resources))

        // password is too short
        assertEquals(resources.getString(R.string.pw_length), Validator.validatePassword("invalid", resources))

        // password is too long
        //assertEquals(false, Validator.validatePassword("invalid".repeat(100)))

        // password confirmation does not match
        assertEquals(resources.getString(R.string.pw_matching), Validator.validatePasswordEquality("12345678", "12345687", resources))
    }

    @Test
    fun trackingStartTimeIsCorrect() {
        // valid StartTime
        assertEquals("", Validator.validateTrackingStartTime("10:00", resources))

        // empty StartTime
        assertEquals(resources.getString(R.string.error_start_time_required), Validator.validateTrackingStartTime("", resources))

        // StartTime is too invalid
        assertEquals(resources.getString(R.string.error_invalid_start_time), Validator.validateTrackingStartTime("invalid", resources))
    }

    @Test
    fun trackingEndTimeIsCorrect() {
        // valid EndTime
        assertEquals("", Validator.validateTrackingEndTime("10:00", resources))

        // empty EndTime
        assertEquals(resources.getString(R.string.error_end_time_required), Validator.validateTrackingEndTime("", resources))

        // EndTime is too invalid
        assertEquals(resources.getString(R.string.error_invalid_end_time), Validator.validateTrackingEndTime("invalid", resources))
    }

    @Test
    fun trackingStartDateIsCorrect() {
        // valid StartDate
        assertEquals("", Validator.validateTrackingStartDate("12.05.2019", resources))

        // empty StartDate
        assertEquals(resources.getString(R.string.error_start_date_required), Validator.validateTrackingStartDate("", resources))

        // StartDate is too invalid
        assertEquals(resources.getString(R.string.error_invalid_start_date_format), Validator.validateTrackingStartDate("invalid", resources))
    }

    @Test
    fun trackingEndDateIsCorrect() {
        // valid EndDate
        assertEquals("", Validator.validateTrackingEndDate("12.05.2019", resources))

        // empty EndDate
        assertEquals(resources.getString(R.string.error_end_date_required), Validator.validateTrackingEndDate("", resources))

        // EndDate is too invalid
        assertEquals(resources.getString(R.string.error_invalid_end_date_format), Validator.validateTrackingEndDate("invalid", resources))
    }

    @Test
    fun trackingNameIsCorrect() {
        // valid TrackingName
        assertEquals("", Validator.validateTrackingName("ValidName", resources))

        // empty TrackingName
        assertEquals(resources.getString(R.string.error_tracking_name_required), Validator.validateTrackingName("", resources))
    }

}