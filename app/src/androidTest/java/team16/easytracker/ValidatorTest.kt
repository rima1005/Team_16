package team16.easytracker

import org.junit.*
import android.content.res.Resources
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert.*
import org.junit.runner.RunWith
import team16.easytracker.utils.Validator


// TODO: replace hardcoded strings with resources

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
        assertEquals("The title must not contain numbers", Validator.validateTitle("Dr.1", resources))

        // title is too long
        //assertEquals(false, Validator.validateTitle("Dr.".repeat(100)))
    }

    @Test
    fun firstNameIsCorrect() {
        // valid first name
        assertEquals("", Validator.validateFirstName("Max", resources))

        // empty first name
        assertEquals("The first name is required", Validator.validateFirstName("", resources))

        // first name too short
        assertEquals("The first name must be between 2 and 255 characters", Validator.validateFirstName("M", resources))

        // first name too long
        assertEquals("The first name must be between 2 and 255 characters", Validator.validateFirstName("Max".repeat(100), resources))

        // first name contains number(s)
        assertEquals("The first name must not contain numbers", Validator.validateFirstName("Max1", resources))
    }

    @Test
    fun lastNameIsCorrect() {
        // valid last name
        assertEquals("", Validator.validateLastName("Mustermann", resources))

        // empty last name
        assertEquals("The last name is required", Validator.validateLastName("", resources))

        // last name too short
        assertEquals("The last name must be between 2 and 255 characters", Validator.validateLastName("M", resources))

        // last name too long
        assertEquals("The last name must be between 2 and 255 characters", Validator.validateLastName("Mustermann".repeat(100), resources))

        // last name contains number(s)
        assertEquals("The last name must not contain numbers", Validator.validateLastName("Mustermann1", resources))
    }

    @Test
    fun emailIsCorrect() {
        // valid email
        assertEquals("", Validator.validateEmail("max.mustermann@gmx.at", resources))

        // empty email
        assertEquals("The email is required", Validator.validateEmail("", resources))

        // email without @
        assertEquals("The email must be a valid email address", Validator.validateEmail("max.mustermanngmx.at", resources))

        // email without .
        assertEquals("The email must be a valid email address", Validator.validateEmail("maxmustermann@gmxat", resources))

        // email too short
        assertEquals("The email must be between 5 and 255 characters", Validator.validateEmail("m.m", resources))

        // email too long
        assertEquals("The email must be between 5 and 255 characters", Validator.validateEmail("max.mustermann@gmxat".repeat(100), resources))
    }

    @Test
    fun dateOfBirthIsCorrect() {
        // valid date of birth
        assertEquals("", Validator.validateDateOfBirth("24.12.2000", resources))

        // empty date of birth
        assertEquals("The date of birth is required", Validator.validateDateOfBirth("", resources))

        // date of birth invalid format
        assertEquals("The date of birth must be of format DD.MM.YYYY", Validator.validateDateOfBirth("24.12.", resources))

        // date of birth invalid format
        assertEquals("The date of birth must be of format DD.MM.YYYY", Validator.validateDateOfBirth("24-12-2000", resources))

        // date of birth without numbers
        assertEquals("The date of birth must be of format DD.MM.YYYY", Validator.validateDateOfBirth("Date", resources))
    }

    @Test
    fun phonePrefixIsCorrect() {
        // valid phone prefix
        assertEquals("", Validator.validatePhonePrefix("43", resources))

        // valid phone prefix
        assertEquals("", Validator.validatePhonePrefix("212", resources))

        // valid phone prefix
        //assertEquals("", Validator.validatePhonePrefix("1242", resources))

        // empty phone prefix
        assertEquals("The phone prefix is required", Validator.validatePhonePrefix("", resources))

        // phone prefix too short
        assertEquals("The phone prefix must be between 2 and 3 digits", Validator.validatePhonePrefix("1", resources))

        // phone prefix too long
        assertEquals("The phone prefix must be between 2 and 3 digits", Validator.validatePhonePrefix("1".repeat(100), resources))

        // phone prefix contains not only numbers
        assertEquals("The phone prefix must only contain digits", Validator.validatePhonePrefix("pref", resources))
    }

    @Test
    fun phoneNumberIsCorrect() {
        // valid phone number
        assertEquals("", Validator.validatePhoneNumber("6641234567", resources))

        // empty phone number
        assertEquals("The phone number is required", Validator.validatePhoneNumber("", resources))

        // phone number too short
        assertEquals("The phone number must be between 2 and 12 digits", Validator.validatePhoneNumber("1", resources))

        // phone number too long
        assertEquals("The phone number must be between 2 and 12 digits", Validator.validatePhoneNumber("6641234567".repeat(100), resources))

        // phone prefix contains not only numbers
        assertEquals("The phone number must only contain digits", Validator.validatePhoneNumber("phone number", resources))
    }

    @Test
    fun postCodeIsCorrect() {
        // valid post code
        assertEquals("", Validator.validatePostCode("8010", resources))

        // empty post code
        assertEquals("The post code is required", Validator.validatePostCode("", resources))

        // post code is too short
        assertEquals("The post code must be between 4 and 10 characters", Validator.validatePostCode("801", resources))

        // post code is too long
        assertEquals("The post code must be between 4 and 10 characters", Validator.validatePostCode("8010".repeat(100), resources))

        // post code does not start with a number
        //assertEquals(false, Validator.validatePostCode("postcode", resources))
    }

    @Test
    fun cityIsCorrect() {
        // valid city
        assertEquals("", Validator.validateCity("Graz", resources))

        // empty city
        assertEquals("The city is required", Validator.validateCity("", resources))

        // city too short
        assertEquals("The city must be between 2 and 255 characters", Validator.validateCity("G", resources))

        // city too long
        assertEquals("The city must be between 2 and 255 characters", Validator.validateCity("Graz".repeat(100), resources))

        // city contains number(s)
        assertEquals("The city must not contain numbers", Validator.validateCity("Graz1", resources))
    }

    @Test
    fun streetIsCorrect() {
        // valid street
        assertEquals("", Validator.validateStreet("Hauptplatz 1", resources))

        // street is empty
        assertEquals("The street is required", Validator.validateStreet("", resources))

        // street has not two parts
        assertEquals("The street must contain a street name and a street number", Validator.validateStreet("Hauptplatz", resources))

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
        assertEquals("The username is required", Validator.validateUsername("", resources))

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
        assertEquals("The password is required", Validator.validatePassword("", resources))

        // password is too short
        assertEquals("The password must have at least 8 characters", Validator.validatePassword("invalid", resources))

        // password is too long
        //assertEquals(false, Validator.validatePassword("invalid".repeat(100)))
    }

    @Test
    fun trackingStartTimeIsCorrect() {
        // valid StartTime
        assertEquals("", Validator.validateTrackingStartTime("10:00", resources))

        // empty StartTime
        assertEquals("The start time is required", Validator.validateTrackingStartTime("", resources))

        // StartTime is too invalid
        assertEquals("The start time must be of format H:mm", Validator.validateTrackingStartTime("invalid", resources))
    }

    @Test
    fun trackingEndTimeIsCorrect() {
        // valid EndTime
        assertEquals("", Validator.validateTrackingEndTime("10:00", resources))

        // empty EndTime
        assertEquals("The end time is required", Validator.validateTrackingEndTime("", resources))

        // EndTime is too invalid
        assertEquals("The end time must be of format H:mm", Validator.validateTrackingEndTime("invalid", resources))
    }

    @Test
    fun trackingStartDateIsCorrect() {
        // valid StartDate
        assertEquals("", Validator.validateTrackingStartDate("12.05.2019", resources))

        // empty StartDate
        assertEquals("The start date is required", Validator.validateTrackingStartDate("", resources))

        // StartDate is too invalid
        assertEquals("The start date must be of format DD.MM.YYYY", Validator.validateTrackingStartDate("invalid", resources))
    }

    @Test
    fun trackingEndDateIsCorrect() {
        // valid EndDate
        assertEquals("", Validator.validateTrackingEndDate("12.05.2019", resources))

        // empty EndDate
        assertEquals("The end date is required", Validator.validateTrackingEndDate("", resources))

        // EndDate is too invalid
        assertEquals("The end date must be of format DD.MM.YYYY", Validator.validateTrackingEndDate("invalid", resources))
    }

    @Test
    fun trackingNameIsCorrect() {
        // valid TrackingName
        assertEquals("", Validator.validateTrackingName("ValidName", resources))

        // empty TrackingName
        assertEquals("The tracking name is required", Validator.validateTrackingName("", resources))
    }

}