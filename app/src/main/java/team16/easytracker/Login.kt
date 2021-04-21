package team16.easytracker

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class Login : AppCompatActivity() {

    lateinit var tfMail : TextView
    lateinit var tfPassword : TextView
    lateinit var loginMap : Map<String, String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.loginactivity)

        tfMail = findViewById<EditText>(R.id.tfMail)
        tfPassword = findViewById<EditText>(R.id.tfPassword)

        loginMap = mapOf("test" to "123", "user1" to "1", "user2" to "password")

    }

    fun readInputVals(view : View) {
        val email = tfMail.text.toString()
        val password : String = tfPassword.text.toString()
        val userPW = loginMap.get(email)
        if(userPW == password)
        {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}