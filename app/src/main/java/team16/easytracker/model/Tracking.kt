package team16.easytracker.model

class Tracking(private val id: Int, val name: String) {

    companion object {
        fun load(id: Int): Tracking {
            return Tracking(id, "")
        }

        fun save(name: String): Int {
            return 0
        }
    }
}