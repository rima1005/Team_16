package team16.easytracker.utils

import java.text.ParseException
import java.text.SimpleDateFormat

class Validator {

    //TODO: Export error stings to strings.xml

    companion object
    {
        var numberRegex = Regex(".*\\d.*")
        var digitRegex = Regex("[0-9]+")

        fun validateTitle(title: String) : String {
            if (title.isNotEmpty() && title.matches(numberRegex)) {
                return "The title must not contain numbers"
            }

            return ""
        }

        fun validateFirstName(firstName: String) : String {
            if (firstName.isEmpty()) {
                return "The first name is required"
            } else if (firstName.length < 2 || firstName.length > 255) {

                return "The first name must be between 2 and 255 characters"
            } else if (firstName.matches(numberRegex)) {

                return "The first name must not contain numbers"
            }

            return ""
        }

        fun validateCompanyName(companyName: String) : String {
            if (companyName.isEmpty()) {
                return "The company name is required"
            } else if (companyName.length < 2 || companyName.length > 255) {
                return "The company name must be between 2 and 255 characters"
            }

            return ""
        }

        fun validatePosition(position: String) : String {
            if (position.isEmpty()) {
                return "The position is required"
            } else if (position.length < 2 || position.length > 255) {

                return "The position must be between 2 and 255 characters"
            } else if (position.matches(numberRegex)) {

                return "The position must not contain numbers"
            }

            return ""
        }

        fun validateLastName(lastName: String) : String {
            if (lastName.isEmpty()) {
                return "The last name is required"
            } else if (lastName.length < 2 || lastName.length > 255) {

                return "The last name must be between 2 and 255 characters"
            } else if (lastName.matches(numberRegex)) {

                return "The last name must not contain numbers"
            }

            return ""
        }

        fun validateEmail(email: String) : String {
            if (email.isEmpty()) {
                return "The email is required"
            } else if (email.length < 5 || email.length > 255) {
                return "The email must be between 5 and 255 characters"
            } else if (!email.contains("@") || !email.contains(".")) {
                return "The email must be a valid email address"
            }

            return ""
        }

        fun validateDateOfBirth(dateOfBirth: String) : String {
            if (dateOfBirth.isEmpty()) {
                return "The date of birth is required"
            } else {
                val dateFormat = SimpleDateFormat("dd.MM.yyyy")
                dateFormat.isLenient = false
                try {
                    dateFormat.parse(dateOfBirth.trim())
                } catch (pe: ParseException) {
                    return "The date of birth must be of format DD.MM.YYYY"
                }
            }

            return ""
        }

        fun validatePhonePrefix(phonePrefix: String) : String {
            if (phonePrefix.isEmpty()) {
                return "The phone prefix is required"
            } else if (phonePrefix.isNotEmpty() && !phonePrefix.matches(digitRegex)) {
                return "The phone prefix must only contain digits"
            } else if (phonePrefix.length < 2 || phonePrefix.length > 3) {
                return "The phone prefix must be between 2 and 3 digits"
            }

            return ""
        }

        fun validatePhoneNumber(phoneNumber: String) : String {
            if (phoneNumber.isEmpty()) {
                return  "The phone number is required"
            } else if (!phoneNumber.matches(digitRegex)) {
                return "The phone number must only contain digits"
            } else if (phoneNumber.length < 2 || phoneNumber.length > 12) {
                return "The phone number must be between 2 and 12 digits"
            }

            return ""
        }

        fun validatePostCode(postCode: String) : String {
            if (postCode.isEmpty()) {
                return "The post code is required"
            } else if (postCode.length < 4 || postCode.length > 10) {
                return "The post code must be between 4 and 10 characters"
            }

            return ""
        }

        fun validateCity(city: String) : String {
            if (city.isEmpty()) {
                return "The city is required"
            } else if (city.matches(numberRegex)) {
                return "The city must not contain numbers"
            } else if (city.length < 2 || city.length > 255) {
                return "The city must be between 2 and 255 characters"
            }

            return ""
        }

        fun validateStreet(street: String) : String {
            if (street.isEmpty()) {
                return "The street is required"
            } else if (street.split(" ").size < 2) {
                return "The street must contain a street name and a street number"
            }

            return ""
        }

        fun validateUsername(username: String) : String {
            if (username.isEmpty()) {
                return "The username is required"
            }
            // TODO: check if username is unique

            return ""
        }

        fun validatePassword(password: String) : String {
            if (password.isEmpty()) {
                return "The password is required"
            }
            else if (password.length < 8) {
                return "The password must have at least 8 characters"
            }

            return ""
        }

        fun validateTrackingStartDate(startDate: String) : String {
            if (startDate.isEmpty()) {
                return "The start date is required"
            } else {
                val dateFormat = SimpleDateFormat("dd.MM.yyyy")
                dateFormat.isLenient = false
                try {
                    dateFormat.parse(startDate.trim())
                } catch (pe: ParseException) {
                    return "The start date must be of format DD.MM.YYYY"
                }
            }

            return ""
        }

        fun validateTrackingEndDate(endDate: String) : String {
            if (endDate.isEmpty()) {
                return "The end date is required"
            } else {
                val dateFormat = SimpleDateFormat("dd.MM.yyyy")
                dateFormat.isLenient = false
                try {
                    dateFormat.parse(endDate.trim())
                } catch (pe: ParseException) {
                    return "The end date must be of format DD.MM.YYYY"
                }
            }

            return ""
        }

        fun validateTrackingStartTime(startTime: String) : String {
            return ""
        }

        fun validateTrackingEndTime(endTime: String) : String {
            return ""
        }
    }
}