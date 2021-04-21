package team16.easytracker

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import android.widget.Toast

class Login : AppCompatActivity() {

    lateinit var tfMail : TextView
    lateinit var tfPassword : TextView
    lateinit var loginMap : Map<String, String>
    lateinit var btnLogin : Button

    lateinit var etPassword : EditText
    lateinit var etEmail : EditText

    lateinit var tvErrorEmail : TextView
    lateinit var tvErrorPassword : TextView

    lateinit var tvGoToRegister : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.loginactivity)

        btnLogin = findViewById(R.id.btnLogin)

        etPassword = findViewById(R.id.etPassword)
        etEmail = findViewById(R.id.etEmail)

        tvGoToRegister = findViewById(R.id.tvGoToRegister)

        tvErrorEmail = findViewById(R.id.tvErrorEmail)
        tvErrorPassword = findViewById(R.id.tvErrorPassword)

        tfMail = findViewById<EditText>(R.id.etEmail)
        tfPassword = findViewById<EditText>(R.id.etPassword)

        loginMap = mapOf("test" to "123", "user1" to "1", "user2" to "password")

        btnLogin.setOnClickListener(loginListener)

        tvGoToRegister.setOnClickListener {
            val intent = Intent(this, Register::class.java)
            startActivity(intent)
        }
    }

    val loginListener = View.OnClickListener { view ->
        when (view.getId()) {
            R.id.btnLogin -> {

                resetErrorMessages()

                val email = etEmail.text.toString()
                val password = etPassword.text.toString()

                val validEmail = validateEmail(email)
                val validPassword = validatePassword(password)

                if(validEmail && validPassword){
                    Log.i("Valid worker", "The worker is valid: " +
                    "Email: " + email + "," +
                    "Password: " + password + ",")
                } else{
                    Log.i("Invalid worker", "The worker is invalid")
                }
            }
        }
    }

    fun readinputvals(view : View) {
        val email = tfMail.text.toString()
        val password : String = tfPassword.text.toString()
        val userPW = loginMap.get(email)
        if(userPW == password) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        else{
            Toast.makeText(applicationContext, "This user does not exist!", Toast.LENGTH_LONG).show()
        }
    }

    fun validateEmail(email: String) : Boolean {
        if (email.isEmpty()) {
            tvErrorEmail.text = "The email is required"
            tvErrorEmail.visibility = View.VISIBLE
            return false
        } else if (!email.contains("@") || !email.contains(".")) {
            tvErrorEmail.text = "The email must be a valid email address"
            tvErrorEmail.visibility = View.VISIBLE
            return false
        }
        //TODO Datenbankabfrage --> email in der database vorhanden

        return true
    }

    fun validatePassword(password: String) : Boolean {
        if (password.isEmpty()) {
            tvErrorPassword.text = "The password is required"
            tvErrorPassword.visibility = View.VISIBLE
            return false
        }
        //TODO Datenbankabfrage --> password in der database vorhanden

        return true
    }

    fun resetErrorMessages() {
        tvErrorEmail.text = ""
        tvErrorEmail.visibility = View.GONE

        tvErrorPassword.text = ""
        tvErrorPassword.visibility = View.GONE

    }
}