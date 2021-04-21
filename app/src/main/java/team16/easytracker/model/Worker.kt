package team16.easytracker.model

import java.time.*

class Worker(
    private val id: Int,
    val firstName: String,
    val lastName: String,
    val dateOfBirth: LocalDate,
    val title: String,
    val email: String,
    val password: String,
    val phoneNumber: String,
    val createdAt: LocalDateTime,
    val addressId: Int,
    val position: String? = null,
    val company: Company? = null
) {

    fun getId(): Int {
        return id
    }

    companion object {
        fun load(id: Int): Worker {
            throw NotImplementedError("Not Implemented!")
        }

        fun save(firstName: String): Int {
            throw NotImplementedError("Not Implemented!")
        }

        fun login(email: String, password: String): Worker? {
            throw NotImplementedError("Not Implemented!")
        }
    }
}