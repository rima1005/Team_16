package team16.easytracker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val btnRegister = findViewById<Button>(R.id.btnRegister)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btnLogin?.setOnClickListener()
        {
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
        }
        btnRegister?.setOnClickListener()
        {
            val intent = Intent(this, Register::class.java)
            startActivity(intent)
        }
    }
}