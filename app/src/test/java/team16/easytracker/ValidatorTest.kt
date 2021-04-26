package team16.easytracker

import org.junit.Assert
import org.junit.Test

import org.junit.Assert.*
import team16.easytracker.utils.Validator

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ValidatorTest {

    @Test
    fun titleIsCorrect() {
        // empty title
        assertEquals("", Validator.validateTitle(""))

        // valid title
        assertEquals("", Validator.validateTitle("Dr."))

        // title contains number(s)
        assertEquals("The title must not contain numbers", Validator.validateTitle("Dr.1"))

        // title is too long
        //assertEquals(false, Validator.validateTitle("Dr.".repeat(100)))
    }

    @Test
    fun firstNameIsCorrect() {
        // valid first name
        assertEquals("", Validator.validateFirstName("Max"))

        // empty first name
        assertEquals("The first name is required", Validator.validateFirstName(""))

        // first name too short
        assertEquals("The first name must be between 2 and 255 characters", Validator.validateFirstName("M"))

        // first name too long
        assertEquals("The first name must be between 2 and 255 characters", Validator.validateFirstName("Max".repeat(100)))

        // first name contains number(s)
        assertEquals("The first name must not contain numbers", Validator.validateFirstName("Max1"))
    }

    @Test
    fun lastNameIsCorrect() {
        // valid last name
        assertEquals("", Validator.validateLastName("Mustermann"))

        // empty last name
        assertEquals("The last name is required", Validator.validateLastName(""))

        // last name too short
        assertEquals("The last name must be between 2 and 255 characters", Validator.validateLastName("M"))

        // last name too long
        assertEquals("The last name must be between 2 and 255 characters", Validator.validateLastName("Mustermann".repeat(100)))

        // last name contains number(s)
        assertEquals("The last name must not contain numbers", Validator.validateLastName("Mustermann1"))
    }

    @Test
    fun emailIsCorrect() {
        // valid email
        assertEquals("", Validator.validateEmail("max.mustermann@gmx.at"))

        // empty email
        assertEquals("The email is required", Validator.validateEmail(""))

        // email without @
        assertEquals("The email must be a valid email address", Validator.validateEmail("max.mustermanngmx.at"))

        // email without .
        assertEquals("The email must be a valid email address", Validator.validateEmail("maxmustermann@gmxat"))

        // email too short
        assertEquals("The email must be between 5 and 255 characters", Validator.validateEmail("m.m"))

        // email too long
        assertEquals("The email must be between 5 and 255 characters", Validator.validateEmail("max.mustermann@gmxat".repeat(100)))
    }

    @Test
    fun dateOfBirthIsCorrect() {
        // valid date of birth
        assertEquals("", Validator.validateDateOfBirth("24.12.2000"))

        // empty date of birth
        assertEquals("The date of birth is required", Validator.validateDateOfBirth(""))

        // date of birth invalid format
        assertEquals("The date of birth must be of format DD.MM.YYYY", Validator.validateDateOfBirth("24.12."))

        // date of birth invalid format
        assertEquals("The date of birth must be of format DD.MM.YYYY", Validator.validateDateOfBirth("24-12-2000"))

        // date of birth without numbers
        assertEquals("The date of birth must be of format DD.MM.YYYY", Validator.validateDateOfBirth("Date"))
    }

    @Test
    fun phonePrefixIsCorrect() {
        // valid phone prefix
        assertEquals("", Validator.validatePhonePrefix("43"))

        // valid phone prefix
        assertEquals("", Validator.validatePhonePrefix("212"))

        // valid phone prefix
        //assertEquals("", Validator.validatePhonePrefix("1242"))

        // empty phone prefix
        assertEquals("The phone prefix is required", Validator.validatePhonePrefix(""))

        // phone prefix too short
        assertEquals("The phone prefix must be between 2 and 3 digits", Validator.validatePhonePrefix("1"))

        // phone prefix too long
        assertEquals("The phone prefix must be between 2 and 3 digits", Validator.validatePhonePrefix("1".repeat(100)))

        // phone prefix contains not only numbers
        assertEquals("The phone prefix must only contain digits", Validator.validatePhonePrefix("pref"))
    }

    @Test
    fun phoneNumberIsCorrect() {
        // valid phone number
        assertEquals("", Validator.validatePhoneNumber("6641234567"))

        // empty phone number
        assertEquals("The phone number is required", Validator.validatePhoneNumber(""))

        // phone number too short
        assertEquals("The phone number must be between 2 and 12 digits", Validator.validatePhoneNumber("1"))

        // phone number too long
        assertEquals("The phone number must be between 2 and 12 digits", Validator.validatePhoneNumber("6641234567".repeat(100)))

        // phone prefix contains not only numbers
        assertEquals("The phone number must only contain digits", Validator.validatePhoneNumber("phone number"))
    }

    @Test
    fun postCodeIsCorrect() {
        // valid post code
        assertEquals("", Validator.validatePostCode("8010"))

        // empty post code
        assertEquals("The post code is required", Validator.validatePostCode(""))

        // post code is too short
        assertEquals("The post code must be between 4 and 10 characters", Validator.validatePostCode("801"))

        // post code is too long
        assertEquals("The post code must be between 4 and 10 characters", Validator.validatePostCode("8010".repeat(100)))

        // post code does not start with a number
        //assertEquals(false, Validator.validatePostCode("postcode"))
    }

    @Test
    fun cityIsCorrect() {
        // valid city
        assertEquals("", Validator.validateCity("Graz"))

        // empty city
        assertEquals("The city is required", Validator.validateCity(""))

        // city too short
        assertEquals("The city must be between 2 and 255 characters", Validator.validateCity("G"))

        // city too long
        assertEquals("The city must be between 2 and 255 characters", Validator.validateCity("Graz".repeat(100)))

        // city contains number(s)
        assertEquals("The city must not contain numbers", Validator.validateCity("Graz1"))
    }

    @Test
    fun streetIsCorrect() {
        // valid street
        assertEquals("", Validator.validateStreet("Hauptplatz 1"))

        // street is empty
        assertEquals("The street is required", Validator.validateStreet(""))

        // street has not two parts
        assertEquals("The street must contain a street name and a street number", Validator.validateStreet("Hauptplatz"))

        // street is too short
        //assertEquals(false, Validator.validateStreet("H"))

        // street is too long
        //assertEquals(false, Validator.validateStreet("Hauptplatz 1".repeat(100)))
    }

    @Test
    fun usernameIsCorrect() {
        // valid username
        assertEquals("", Validator.validateUsername("user_name"))

        // empty username
        assertEquals("The username is required", Validator.validateUsername(""))

        // username too short
        //assertEquals(false, Validator.validateUsername("user"))

        // username too long
        //assertEquals(false, Validator.validateUsername("invalid_user".repeat(100)))
    }

    @Test
    fun passwordIsCorrect() {
        // valid password
        assertEquals("", Validator.validatePassword("validpassword"))

        // empty password
        assertEquals("The password is required", Validator.validatePassword(""))

        // password is too short
        assertEquals("The password must have at least 8 characters", Validator.validatePassword("invalid"))

        // password is too long
        //assertEquals(false, Validator.validatePassword("invalid".repeat(100)))
    }
}