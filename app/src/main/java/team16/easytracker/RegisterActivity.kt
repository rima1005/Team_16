package team16.easytracker

import android.content.Intent
import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.thesimplycoder.simpledatepicker.DatePickerHelper
import team16.easytracker.database.DbHelper
import team16.easytracker.utils.Validator
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class RegisterActivity : AppCompatActivity() {

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
    lateinit var etPassword : EditText
    lateinit var etPasswordConfirmation : EditText

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
    lateinit var tvErrorPassword : TextView
    lateinit var tvErrorPasswordConfirmation : TextView

    lateinit var spGender : Spinner

    lateinit var btnShowHidePassword : Button
    lateinit var btnShowHidePasswordConfirmation : Button

    lateinit var btnRegistration : Button
    lateinit var btnDateOfBirth : Button
    lateinit var tvGoToLogin : TextView

    lateinit var datePicker : DatePickerHelper

    private var passwordVisible = false
    private var passwordConfirmationVisible = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.registeractivity)
        MyApplication.updateResources(this)

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
        etPassword = findViewById(R.id.etPassword)
        etPasswordConfirmation = findViewById(R.id.etPasswordConfirmation)
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
        tvErrorPassword = findViewById(R.id.tvErrorPassword)
        tvErrorPasswordConfirmation = findViewById(R.id.tvErrorPasswordConfirmation)

        btnShowHidePassword = findViewById(R.id.btnShowHidePassword)
        btnShowHidePasswordConfirmation = findViewById(R.id.btnShowHidePasswordConfirmation)

        btnRegistration = findViewById(R.id.btnRegistration)
        btnDateOfBirth = findViewById(R.id.btnDateOfBirth)
        tvGoToLogin = findViewById(R.id.tvGoToLogin)

        datePicker = DatePickerHelper(this, true)

        val genders = resources.getStringArray(R.array.genders)
        val adapter = ArrayAdapter(this,
                android.R.layout.simple_spinner_dropdown_item, genders)
        spGender.adapter = adapter

        btnShowHidePassword.setOnClickListener {
            if (passwordVisible) {
                etPassword.setTransformationMethod(PasswordTransformationMethod())
                btnShowHidePassword.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_show_password, 0)
                passwordVisible = false
            }
            else {
                etPassword.setTransformationMethod(null)
                btnShowHidePassword.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_hide_password, 0)
                passwordVisible = true
            }
        }

        btnShowHidePasswordConfirmation.setOnClickListener {
            if (passwordConfirmationVisible) {
                etPasswordConfirmation.setTransformationMethod(PasswordTransformationMethod())
                btnShowHidePasswordConfirmation.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_show_password, 0)
                passwordConfirmationVisible = false
            }
            else {
                etPasswordConfirmation.setTransformationMethod(null)
                btnShowHidePasswordConfirmation.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_hide_password, 0)
                passwordConfirmationVisible = true
            }
        }

        btnRegistration.setOnClickListener { registerWorker() }

        btnDateOfBirth.setOnClickListener { setDateOfBirth(etDateOfBirth) }

        tvGoToLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    fun setDateOfBirth(dateofbirth: EditText) {
        val cal = Calendar.getInstance()
        val d = cal.get(Calendar.DAY_OF_MONTH)
        val m = cal.get(Calendar.MONTH)
        val y = cal.get(Calendar.YEAR)

        datePicker.showDialog(d, m, y, object : DatePickerHelper.Callback {
            override fun onDateSelected(dayofMonth: Int, month: Int, year: Int) {
                val dayStr = if (dayofMonth < 10) "0${dayofMonth}" else "${dayofMonth}"
                val mon = month + 1
                val monthStr = if (mon < 10) "0${mon}" else "${mon}"
                val finalDate = "${dayStr}.${monthStr}.${year}"
                dateofbirth.setText(finalDate)
            }
        })

        //TODO: set the et field to non editable, but make it editable for test again
        //      so they won't fail
    }

    fun registerWorker() {
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
        val password = etPassword.text.toString()
        val passwordConfirmation = etPasswordConfirmation.text.toString()

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
        val validPassword = validatePassword(password, passwordConfirmation)

        if (validTitle && validFirstName && validLastName && validEmail && validDateOfBirth &&
                validPhonePrefix && validPhoneNumber && validPostCode && validCity &&
                validStreet && validPassword) {

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
                    "Password: " + password
            )


            val addressId = DbHelper.getInstance().saveAddress(street, postCode, city)

            // TODO: no check for error! workerId is -1 if email is duplicate
            val workerId = DbHelper.getInstance().saveWorker(
                    firstName,
                    lastName,
                    LocalDate.parse(dateOfBirth, DateTimeFormatter.ofPattern("dd.MM.yyyy")),
                    title,
                    email,
                    password,
                    phonePrefix + phoneNumber,
                    LocalDateTime.now().withNano(0),
                    addressId
            )

            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        } else {
            Log.i("Invalid worker", "The worker is invalid")
        }
    }

    fun validateTitle(title: String) : Boolean {
        val errorTitle = Validator.validateTitle(title, resources)
        if (errorTitle != "") {
            tvErrorTitle.text = errorTitle
            tvErrorTitle.visibility = View.VISIBLE
            tvErrorGender.visibility = View.VISIBLE
            return false
        }
        return true
    }

    fun validateFirstName(firstName: String) : Boolean {
        val errorFirstName = Validator.validateFirstName(firstName, resources)
        if (errorFirstName != "") {
            tvErrorFirstName.text = errorFirstName
            tvErrorFirstName.visibility = View.VISIBLE
            return false
        }
        return true
    }

    fun validateLastName(lastName: String) : Boolean {
        val errorLastName = Validator.validateLastName(lastName, resources)
        if (errorLastName != "") {
            tvErrorLastName.text = errorLastName
            tvErrorLastName.visibility = View.VISIBLE
            return false
        }
        return true
    }

    fun validateEmail(email: String) : Boolean {
        val errorEmail = Validator.validateEmail(email, resources)
        if (errorEmail != "") {
            tvErrorEmail.text = errorEmail
            tvErrorEmail.visibility = View.VISIBLE
            return false
        }
        return true
    }

    fun validateDateOfBirth(dateOfBirth: String) : Boolean {
        val errorDateOfBirth = Validator.validateDateOfBirth(dateOfBirth, resources)
        if (errorDateOfBirth != "") {
            tvErrorDateOfBirth.text = errorDateOfBirth
            tvErrorDateOfBirth.visibility = View.VISIBLE
            return false
        }
        return true
    }

    fun validatePhonePrefix(phonePrefix: String) : Boolean {
        val errorPhonePrefix = Validator.validatePhonePrefix(phonePrefix, resources)
        if (errorPhonePrefix != "") {
            tvErrorPhonePrefix.text = errorPhonePrefix
            tvErrorPhonePrefix.visibility = View.VISIBLE
            return false
        }
        return true
    }

    fun validatePhoneNumber(phoneNumber: String) : Boolean {
        val errorPhoneNumber = Validator.validatePhoneNumber(phoneNumber, resources)
        if (errorPhoneNumber != "") {
            tvErrorPhoneNumber.text = errorPhoneNumber
            tvErrorPhoneNumber.visibility = View.VISIBLE
            tvErrorPhonePrefix.visibility = View.VISIBLE
            return false
        }
        return true
    }

    fun validatePostCode(postCode: String) : Boolean {
        val errorPostCode = Validator.validatePostCode(postCode, resources)
        if (errorPostCode != "") {
            tvErrorPostCode.text = errorPostCode
            tvErrorPostCode.visibility = View.VISIBLE
            return false
        }
        return true
    }

    fun validateCity(city: String) : Boolean {
        val errorCity = Validator.validateCity(city, resources)
        if (errorCity != "") {
            tvErrorCity.text = errorCity
            tvErrorCity.visibility = View.VISIBLE
            tvErrorPostCode.visibility = View.VISIBLE
            return false
        }
        return true
    }

    fun validateStreet(street: String) : Boolean {
        val errorStreet = Validator.validateStreet(street, resources)
        if (errorStreet != "") {
            tvErrorStreet.text = errorStreet
            tvErrorStreet.visibility = View.VISIBLE
            return false
        }
        return true
    }

    fun validatePassword(password: String, passwordConfirmation: String) : Boolean {
        val errorPassword = Validator.validatePassword(password, resources)
        if (errorPassword != "") {
            tvErrorPassword.text = errorPassword
            tvErrorPassword.visibility = View.VISIBLE
            return false
        }

        val errorPasswordConfirmation = Validator.validatePassword(passwordConfirmation, resources)
        if (errorPasswordConfirmation != "") {
            tvErrorPasswordConfirmation.text = errorPasswordConfirmation
            tvErrorPasswordConfirmation.visibility = View.VISIBLE
            return false
        }

        val errorPasswordEquality = Validator.validatePasswordEquality(password, passwordConfirmation, resources)
        if (errorPasswordEquality != "") {
            tvErrorPasswordConfirmation.text = errorPasswordEquality
            tvErrorPasswordConfirmation.visibility = View.VISIBLE
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

        tvErrorPassword.text = ""
        tvErrorPassword.visibility = View.GONE

        tvErrorPasswordConfirmation.text = ""
        tvErrorPasswordConfirmation.visibility = View.GONE

    }
}