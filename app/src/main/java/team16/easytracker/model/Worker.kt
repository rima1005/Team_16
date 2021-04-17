package team16.easytracker.model

class Worker(private val id: Int, val firstName: String) {
    val position: String? = null
    val companyId: Int? = null

    companion object {
        fun load(id: Int): Worker {
            return Worker(id, "")
        }

        fun save(firstName: String): Int {
            return 0
        }
    }
}