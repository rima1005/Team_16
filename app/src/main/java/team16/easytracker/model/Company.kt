package team16.easytracker.model

class Company(private val id : Int, val name : String, val addressId : Int) {

    val address : Address? = null;

    public fun getId() : Int {
        return id;
    }
}