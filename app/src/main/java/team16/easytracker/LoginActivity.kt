package team16.easytracker

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import android.widget.Toast
import team16.easytracker.database.DbHelper
import team16.easytracker.utils.Validator

class LoginActivity : AppCompatActivity() {

    lateinit var tfMail : TextView
    lateinit var tfPassword : TextView
    lateinit var btnLogin : Button

    lateinit var etPassword : EditText
    lateinit var etEmail : EditText

    lateinit var tvErrorEmail : TextView
    lateinit var tvErrorPassword : TextView

    lateinit var tvGoToRegister : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.loginactivity)

        MyApplication.updateResources(this)

        btnLogin = findViewById(R.id.btnLogin)

        etPassword = findViewById(R.id.etPassword)
        etEmail = findViewById(R.id.etEmail)

        tvGoToRegister = findViewById(R.id.tvGoToRegister)

        tvErrorEmail = findViewById(R.id.tvErrorEmail)
        tvErrorPassword = findViewById(R.id.tvErrorPassword)

        tfMail = findViewById<EditText>(R.id.etEmail)
        tfPassword = findViewById<EditText>(R.id.etPassword)

        btnLogin.setOnClickListener(loginListener)

        tvGoToRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
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

                Log.i("Data", "Email: " + email + ", PW: " + password)

                if (validEmail && validPassword) {
                    Log.i("Valid worker", "The worker is valid: " +
                    "Email: " + email + "," +
                    "Password: " + password + ",")

                    val worker = DbHelper.loginWorker(email, password)
                    Log.i("Login", "Worker: " + worker?.toString())
                    if (worker != null) {
                        val intent = Intent(this, HomeActivity::class.java)
                        startActivity(intent)
                    } else {
                        tvErrorPassword.text = "Invalid email or password"
                        tvErrorPassword.visibility = View.VISIBLE
                    }
                } else {
                    Log.i("Invalid worker", "The worker is invalid")
                }
            }
        }
    }

    private fun validateEmail(email: String) : Boolean {

        val error = Validator.validateEmail(email, resources)

        if (error.isEmpty()) {
            return true
        }
        tvErrorEmail.text = error
        tvErrorEmail.visibility = View.VISIBLE
        return false
    }

    private fun validatePassword(password: String) : Boolean {

        val error = Validator.validatePassword(password, resources)
        if (error.isEmpty()) {
            return true
        }
        tvErrorPassword.text = error
        tvErrorPassword.visibility = View.VISIBLE
        return false
    }

    private fun resetErrorMessages() {
        tvErrorEmail.text = ""
        tvErrorEmail.visibility = View.GONE

        tvErrorPassword.text = ""
        tvErrorPassword.visibility = View.GONE

    }
}