package team16.easytracker.model

class Company(private val id : Int, val name : String, val addressId : Int) {

    var address : Address? = null

    fun getId() : Int {
        return id
    }
}