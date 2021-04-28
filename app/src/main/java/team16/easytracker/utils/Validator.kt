package team16.easytracker.utils

import team16.easytracker.R
import java.text.ParseException
import java.text.SimpleDateFormat
import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.provider.Settings.Global.getString
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import team16.easytracker.database.DbHelper
import team16.easytracker.utils.Validator
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class Validator {

    //TODO: Export error stings to strings.xml

    companion object
    {
        var numberRegex = Regex(".*\\d.*")
        var digitRegex = Regex("[0-9]+")

        fun validateTitle(title: String, resources: Resources) : String {
            if (title.isNotEmpty() && title.matches(numberRegex)) {
                return resources.getString(R.string.no_number_title)
            }

            return ""
        }

        fun validateFirstName(firstName: String, resources: Resources) : String {
            if (firstName.isEmpty()) {
                val test = resources.getString(R.string.gender)
                return test
            } else if (firstName.length < 2 || firstName.length > 255) {

                return Resources.getSystem().getString(R.string.first_name_length)
            } else if (firstName.matches(numberRegex)) {

                return resources.getString(R.string.first_name_no_numbers)
            }

            return ""
        }

        fun validateCompanyName(companyName: String, resources: Resources) : String {
            if (companyName.isEmpty()) {
                return resources.getString(R.string.company_name_required)
            } else if (companyName.length < 2 || companyName.length > 255) {
                return resources.getString(R.string.company_name_length)
            }

            return ""
        }

        fun validatePosition(position: String, resources: Resources) : String {
            if (position.isEmpty()) {
                return resources.getString(R.string.position_required)
            } else if (position.length < 2 || position.length > 255) {

                return resources.getString(R.string.position_name_length)
            } else if (position.matches(numberRegex)) {

                return resources.getString(R.string.position_name_no_numbers)
            }

            return ""
        }

        fun validateLastName(lastName: String, resources: Resources) : String {
            if (lastName.isEmpty()) {
                return resources.getString(R.string.last_name_required)
            } else if (lastName.length < 2 || lastName.length > 255) {

                return resources.getString(R.string.last_name_length)
            } else if (lastName.matches(numberRegex)) {

                return resources.getString(R.string.last_name_no_numbers)
            }

            return ""
        }

        fun validateEmail(email: String, resources: Resources) : String {
            if (email.isEmpty()) {
                return resources.getString(R.string.email_required)
            } else if (email.length < 5 || email.length > 255) {
                return resources.getString(R.string.email_length)
            } else if (!email.contains("@") || !email.contains(".")) {
                return resources.getString(R.string.email_valid_address)
            }

            return ""
        }

        fun validateDateOfBirth(dateOfBirth: String, resources: Resources) : String {
            if (dateOfBirth.isEmpty()) {
                return resources.getString(R.string.dob_required)
            } else {
                val dateFormat = SimpleDateFormat("dd.MM.yyyy")
                dateFormat.isLenient = false
                try {
                    dateFormat.parse(dateOfBirth.trim())
                } catch (pe: ParseException) {
                    return resources.getString(R.string.dob_format)
                }
            }

            return ""
        }

        fun validatePhonePrefix(phonePrefix: String, resources: Resources) : String {
            if (phonePrefix.isEmpty()) {
                return resources.getString(R.string.phone_prefix_required)
            } else if (phonePrefix.isNotEmpty() && !phonePrefix.matches(digitRegex)) {
                return resources.getString(R.string.phone_prefix_digits)
            } else if (phonePrefix.length < 2 || phonePrefix.length > 3) {
                return resources.getString(R.string.phone_prefix_length)
            }

            return ""
        }

        fun validatePhoneNumber(phoneNumber: String, resources: Resources) : String {
            if (phoneNumber.isEmpty()) {
                return resources.getString(R.string.phone_nr_required)
            } else if (!phoneNumber.matches(digitRegex)) {
                return  resources.getString(R.string.phone_number_digits)
            } else if (phoneNumber.length < 2 || phoneNumber.length > 12) {
                return  resources.getString(R.string.phone_number_length)
            }

            return ""
        }

        fun validatePostCode(postCode: String, resources: Resources) : String {
            if (postCode.isEmpty()) {
                return resources.getString(R.string.post_code_required)
            } else if (postCode.length < 4 || postCode.length > 10) {
                return resources.getString(R.string.post_code_length)
            }

            return ""
        }

        fun validateCity(city: String, resources: Resources) : String {
            if (city.isEmpty()) {
                return resources.getString(R.string.city_required)
            } else if (city.matches(numberRegex)) {
                return resources.getString(R.string.city_name_format)
            } else if (city.length < 2 || city.length > 255) {
                return resources.getString(R.string.city_name_length)
            }

            return ""
        }

        fun validateStreet(street: String, resources: Resources) : String {
            if (street.isEmpty()) {
                return resources.getString(R.string.street_required)
            } else if (street.split(" ").size < 2) {
                return resources.getString(R.string.street_name_format)
            }

            return ""
        }

        fun validateUsername(username: String, resources: Resources) : String {
            if (username.isEmpty()) {
                return resources.getString(R.string.username_required)
            }
            // TODO: check if username is unique

            return ""
        }

        fun validatePassword(password: String, resources: Resources) : String {
            if (password.isEmpty()) {
                return resources.getString(R.string.pw_required)
            }
            else if (password.length < 8) {
                return resources.getString(R.string.pw_length)
            }

            return ""
        }
    }
}