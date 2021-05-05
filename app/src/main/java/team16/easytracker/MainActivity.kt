package team16.easytracker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val btnRegister = findViewById<Button>(R.id.btnRegister)
        btnRegister?.setOnClickListener() {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
        btnLogin?.setOnClickListener() {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }
}
