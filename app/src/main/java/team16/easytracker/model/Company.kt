package team16.easytracker.model

class Company(private val id : Int, val name : String, val addressId : Int) {

    val address : Address? = null;

    companion object {
        fun load(id : Int, loadAddress : Boolean = false) : Company {
            val name = ""
            val addressId = -1
            return Company(id, name, addressId);
        }

        fun save(name : String, addressId: Int) : Int {
            return 0
        }
    }

    public fun getId() : Int {
        return id;
    }
}