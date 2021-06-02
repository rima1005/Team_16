package team16.easytracker.model

import java.time.*

class Worker(
    private val id: Int,
    val firstName: String,
    val lastName: String,
    val dateOfBirth: LocalDate,
    val title: String,
    val email: String,
    val phoneNumber: String,
    val createdAt: LocalDateTime,
    val addressId: Int,
    val admin: Boolean,
    val position: String? = null,
    val company: Company? = null
) {

    fun getId(): Int {
        return id
    }

    override fun toString(): String {
        if (title.isEmpty()) {
            return lastName + " " +
                    firstName + " - " +
                    position
        }
        return lastName + " " +
               firstName + ", " +
               title + " - " +
               position
    }

}