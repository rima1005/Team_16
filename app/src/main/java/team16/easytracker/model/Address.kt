package team16.easytracker.model

class Address(private val id: Int, val street: String) {
    companion object {
        fun load(id: Int): Address {
            return Address(id, "")
        }

        fun save(street: String): Int {
            return 0
        }
    }
}