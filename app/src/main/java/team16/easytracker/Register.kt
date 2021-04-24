package team16.easytracker

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import team16.easytracker.database.DbHelper
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class Register : AppCompatActivity() {

    var numberRegex = Regex(".*\\d.*")
    var digitRegex = Regex("[0-9]+")

    lateinit var etTitle : EditText
    lateinit var etFirstName : EditText
    lateinit var etLastName : EditText
    lateinit var etEmail : EditText
    lateinit var etDateOfBirth : EditText
    lateinit var etPhonePrefix : EditText
    lateinit var etPhoneNumber : EditText
    lateinit var etPostCode : EditText
    lateinit var etCity : EditText
    lateinit var etStreet : EditText
    lateinit var etUsername : EditText
    lateinit var etPassword : EditText

    lateinit var tvErrorGender : TextView
    lateinit var tvErrorTitle : TextView
    lateinit var tvErrorFirstName : TextView
    lateinit var tvErrorLastName : TextView
    lateinit var tvErrorEmail : TextView
    lateinit var tvErrorDateOfBirth : TextView
    lateinit var tvErrorPhonePrefix : TextView
    lateinit var tvErrorPhoneNumber : TextView
    lateinit var tvErrorPostCode : TextView
    lateinit var tvErrorCity : TextView
    lateinit var tvErrorStreet : TextView
    lateinit var tvErrorUsername : TextView
    lateinit var tvErrorPassword : TextView

    lateinit var spGender : Spinner

    lateinit var btnRegistration : Button
    lateinit var tvGoToLogin : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.registeractivity)

        etTitle = findViewById(R.id.etTitle)
        etFirstName = findViewById(R.id.etFirstName)
        etLastName = findViewById(R.id.etLastName)
        etEmail = findViewById(R.id.etEmail)
        etDateOfBirth = findViewById(R.id.etDateOfBirth)
        etPhonePrefix = findViewById(R.id.etPhonePrefix)
        etPhoneNumber = findViewById(R.id.etPhoneNumber)
        etPostCode = findViewById(R.id.etPostCode)
        etCity = findViewById(R.id.etCity)
        etStreet = findViewById(R.id.etStreet)
        etUsername = findViewById(R.id.etUsername)
        etPassword = findViewById(R.id.etPassword)
        spGender = findViewById(R.id.spGender)

        tvErrorGender = findViewById(R.id.tvErrorGender)
        tvErrorTitle = findViewById(R.id.tvErrorTitle)
        tvErrorFirstName = findViewById(R.id.tvErrorFirstName)
        tvErrorLastName = findViewById(R.id.tvErrorLastName)
        tvErrorEmail = findViewById(R.id.tvErrorEmail)
        tvErrorDateOfBirth = findViewById(R.id.tvErrorDateOfBirth)
        tvErrorPhonePrefix = findViewById(R.id.tvErrorPhonePrefix)
        tvErrorPhoneNumber = findViewById(R.id.tvErrorPhoneNumber)
        tvErrorPostCode = findViewById(R.id.tvErrorPostCode)
        tvErrorCity = findViewById(R.id.tvErrorCity)
        tvErrorStreet = findViewById(R.id.tvErrorStreet)
        tvErrorUsername = findViewById(R.id.tvErrorUsername)
        tvErrorPassword = findViewById(R.id.tvErrorPassword)

        btnRegistration = findViewById(R.id.btnRegistration)
        tvGoToLogin = findViewById(R.id.tvGoToLogin)

        val genders = resources.getStringArray(R.array.genders)
        val adapter = ArrayAdapter(this,
            android.R.layout.simple_spinner_dropdown_item, genders)
        spGender.adapter = adapter

        btnRegistration.setOnClickListener(registrationListener)

        tvGoToLogin.setOnClickListener {
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
        }
    }

    val registrationListener = View.OnClickListener { view ->
        when (view.getId()) {
            R.id.btnRegistration -> {

                resetErrorMessages()

                val gender = spGender.selectedItem.toString()
                val title = etTitle.text.toString()
                val firstName = etFirstName.text.toString()
                val lastName = etLastName.text.toString()
                val email = etEmail.text.toString()
                val dateOfBirth = etDateOfBirth.text.toString()
                val phonePrefix = etPhonePrefix.text.toString()
                val phoneNumber = etPhoneNumber.text.toString()
                val postCode = etPostCode.text.toString()
                val city = etCity.text.toString()
                val street = etStreet.text.toString()
                val username = etUsername.text.toString()
                val password = etPassword.text.toString()

                val validTitle = validateTitle(title)
                val validFirstName = validateFirstName(firstName)
                val validLastName = validateLastName(lastName)
                val validEmail = validateEmail(email)
                val validDateOfBirth = validateDateOfBirth(dateOfBirth)
                val validPhonePrefix = validatePhonePrefix(phonePrefix)
                val validPhoneNumber = validatePhoneNumber(phoneNumber)
                val validPostCode = validatePostCode(postCode)
                val validCity = validateCity(city)
                val validStreet = validateStreet(street)
                val validUsername = validateUsername(username)
                val validPassword = validatePassword(password)

                if (validTitle && validFirstName && validLastName && validEmail && validDateOfBirth &&
                        validPhonePrefix && validPhoneNumber && validPostCode && validCity &&
                        validStreet && validUsername && validPassword) {
                    Log.i("Valid Worker", "The worker is valid: " +
                            "Gender: " + gender + ", " +
                            "Title: " + title + ", " +
                            "First Name: " + firstName + ", " +
                            "Last Name: " + lastName + ", " +
                            "Email: " + email + ", " +
                            "Date of Birth: " + dateOfBirth + ", " +
                            "Phone Prefix: " + phonePrefix + ", " +
                            "Phone Number: " + phoneNumber + ", " +
                            "Post Code: " + postCode + ", " +
                            "City: " + city + ", " +
                            "Street: " + street + ", " +
                            "Username: " + username + ", " +
                            "Password: " + password
                        )

                    // TODO: Check if username is available in validateUsername
                    val dbHelper = DbHelper(this)

                    val addressId = dbHelper.saveAddress(street, postCode, city)

                    val workerId = dbHelper.saveWorker(
                        firstName,
                        lastName,
                        LocalDate.parse(dateOfBirth, DateTimeFormatter.ofPattern("dd.MM.yyyy")),
                        "",
                        email,
                        password,
                        "",
                        LocalDateTime.now().withNano(0),
                        1
                    )

                    val intent = Intent(this, Login::class.java)
                    startActivity(intent)
                } else {
                    Log.i("Invalid worker", "The worker is invalid")
                }
            }
        }
    }

    fun validateTitle(title: String) : Boolean {
        if (title.isNotEmpty() && title.matches(numberRegex)) {
            tvErrorTitle.text = "The title must not contain numbers"
            tvErrorTitle.visibility = View.VISIBLE
            tvErrorGender.visibility = View.VISIBLE
            return false
        }

        return true
    }

    fun validateFirstName(firstName: String) : Boolean {
        if (firstName.isEmpty()) {
            tvErrorFirstName.text = "The first name is required"
            tvErrorFirstName.visibility = View.VISIBLE
            return false
        } else if (firstName.length < 2 || firstName.length > 255) {
            tvErrorFirstName.text = "The first name must be between 2 and 255 characters"
            tvErrorFirstName.visibility = View.VISIBLE
            return false
        } else if (firstName.matches(numberRegex)) {
            tvErrorFirstName.text = "The first name must not contain numbers"
            tvErrorFirstName.visibility = View.VISIBLE
            return false
        }

        return true
    }

    fun validateLastName(lastName: String) : Boolean {
        if (lastName.isEmpty()) {
            tvErrorLastName.text = "The last name is required"
            tvErrorLastName.visibility = View.VISIBLE
            return false
        } else if (lastName.length < 2 || lastName.length > 255) {
            tvErrorLastName.text = "The last name must be between 2 and 255 characters"
            tvErrorLastName.visibility = View.VISIBLE
            return false
        } else if (lastName.matches(numberRegex)) {
            tvErrorLastName.text = "The last name must not contain numbers"
            tvErrorLastName.visibility = View.VISIBLE
            return false
        }

        return true
    }

    fun validateEmail(email: String) : Boolean {
        if (email.isEmpty()) {
            tvErrorEmail.text = "The email is required"
            tvErrorEmail.visibility = View.VISIBLE
            return false
        } else if (email.length < 5 || email.length > 255) {
            tvErrorEmail.text = "The email must be between 5 and 255 characters"
            tvErrorEmail.visibility = View.VISIBLE
            return false
        } else if (!email.contains("@") || !email.contains(".")) {
            tvErrorEmail.text = "The email must be a valid email address"
            tvErrorEmail.visibility = View.VISIBLE
            return false
        }

        return true
    }

    fun validateDateOfBirth(dateOfBirth: String) : Boolean {
        if (dateOfBirth.isEmpty()) {
            tvErrorDateOfBirth.text = "The date of birth is required"
            tvErrorDateOfBirth.visibility = View.VISIBLE
            return false
        } else {
            val dateFormat = SimpleDateFormat("dd.MM.yyyy")
            dateFormat.isLenient = false
            try {
                dateFormat.parse(dateOfBirth.trim())
            } catch (pe: ParseException) {
                tvErrorDateOfBirth.text = "The date of birth must be of format DD.MM.YYYY"
                tvErrorDateOfBirth.visibility = View.VISIBLE
                return false
            }
        }

        return true
    }

    fun validatePhonePrefix(phonePrefix: String) : Boolean {
        if (phonePrefix.isEmpty()) {
            tvErrorPhonePrefix.text = "The phone prefix is required"
            tvErrorPhonePrefix.visibility = View.VISIBLE
            return false
        } else if (phonePrefix.isNotEmpty() && !phonePrefix.matches(digitRegex)) {
            tvErrorPhonePrefix.text = "The phone prefix must only contain digits"
            tvErrorPhonePrefix.visibility = View.VISIBLE
            return false
        } else if (phonePrefix.length < 2 || phonePrefix.length > 3) {
            tvErrorPhonePrefix.text = "The phone prefix must be between 2 and 3 digits"
            tvErrorPhonePrefix.visibility = View.VISIBLE
            return false
        }

        return true
    }

    fun validatePhoneNumber(phoneNumber: String) : Boolean {
        if (phoneNumber.isEmpty()) {
            tvErrorPhoneNumber.text = "The phone number is required"
            tvErrorPhoneNumber.visibility = View.VISIBLE
            tvErrorPhonePrefix.visibility = View.VISIBLE
            return false
        } else if (!phoneNumber.matches(digitRegex)) {
            tvErrorPhoneNumber.text = "The phone number must only contain digits"
            tvErrorPhoneNumber.visibility = View.VISIBLE
            tvErrorPhonePrefix.visibility = View.VISIBLE
            return false
        } else if (phoneNumber.length < 2 || phoneNumber.length > 12) {
            tvErrorPhoneNumber.text = "The phone number must be between 2 and 12 digits"
            tvErrorPhoneNumber.visibility = View.VISIBLE
            tvErrorPhonePrefix.visibility = View.VISIBLE
            return false
        }

        return true
    }

    fun validatePostCode(postCode: String) : Boolean {
        if (postCode.isEmpty()) {
            tvErrorPostCode.text = "The post code is required"
            tvErrorPostCode.visibility = View.VISIBLE
            return false
        } else if (postCode.length < 4 || postCode.length > 10) {
            tvErrorPostCode.text = "The post code must be between 4 and 10 characters"
            tvErrorPostCode.visibility = View.VISIBLE
            return false
        }

        return true
    }

    fun validateCity(city: String) : Boolean {
        if (city.isEmpty()) {
            tvErrorCity.text = "The city is required"
            tvErrorCity.visibility = View.VISIBLE
            tvErrorPostCode.visibility = View.VISIBLE
            return false
        } else if (city.matches(numberRegex)) {
            tvErrorCity.text = "The city must not contain numbers"
            tvErrorCity.visibility = View.VISIBLE
            tvErrorPostCode.visibility = View.VISIBLE
            return false
        } else if (city.length < 2 || city.length > 255) {
            tvErrorCity.text = "The city must be between 2 and 255 characters"
            tvErrorCity.visibility = View.VISIBLE
            tvErrorPostCode.visibility = View.VISIBLE
            return false
        }

        return true
    }

    fun validateStreet(street: String) : Boolean {
        if (street.isEmpty()) {
            tvErrorStreet.text = "The street is required"
            tvErrorStreet.visibility = View.VISIBLE
            return false
        } else if (street.split(" ").size < 2) {
            tvErrorStreet.text = "The street must contain a street name and a street number"
            tvErrorStreet.visibility = View.VISIBLE
            return false
        }

        return true
    }

    fun validateUsername(username: String) : Boolean {
        if (username.isEmpty()) {
            tvErrorUsername.text = "The username is required"
            tvErrorUsername.visibility = View.VISIBLE
            return false
        }
        // TODO: check if username is unique

        return true
    }

    fun validatePassword(password: String) : Boolean {
        if (password.isEmpty()) {
            tvErrorPassword.text = "The password is required"
            tvErrorPassword.visibility = View.VISIBLE
            return false
        }
        else if (password.length < 8) {
            tvErrorPassword.text = "The password must have at least 8 characters"
            tvErrorPassword.visibility = View.VISIBLE
            return false
        }

        return true
    }

    fun resetErrorMessages() {
        tvErrorGender.text = ""
        tvErrorGender.visibility = View.GONE

        tvErrorTitle.text = ""
        tvErrorTitle.visibility = View.GONE

        tvErrorFirstName.text = ""
        tvErrorFirstName.visibility = View.GONE

        tvErrorLastName.text = ""
        tvErrorLastName.visibility = View.GONE

        tvErrorEmail.text = ""
        tvErrorEmail.visibility = View.GONE

        tvErrorDateOfBirth.text = ""
        tvErrorDateOfBirth.visibility = View.GONE

        tvErrorPhonePrefix.text = ""
        tvErrorPhonePrefix.visibility = View.GONE

        tvErrorPhoneNumber.text = ""
        tvErrorPhoneNumber.visibility = View.GONE

        tvErrorPostCode.text = ""
        tvErrorPostCode.visibility = View.GONE

        tvErrorCity.text = ""
        tvErrorCity.visibility = View.GONE

        tvErrorStreet.text = ""
        tvErrorStreet.visibility = View.GONE

        tvErrorUsername.text = ""
        tvErrorUsername.visibility = View.GONE

        tvErrorPassword.text = ""
        tvErrorPassword.visibility = View.GONE

    }
}