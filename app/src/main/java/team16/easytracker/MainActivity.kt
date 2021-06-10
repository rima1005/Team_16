package team16.easytracker

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import org.mindrot.jbcrypt.BCrypt
import team16.easytracker.database.DbHelper
import java.time.LocalDate
import java.time.LocalDateTime


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MyApplication.updateResources(this)
        setContentView(R.layout.activity_main)

        val languageSpinner = findViewById<Spinner>(R.id.spLanguage)

        MyApplication.initLanguageSpinner(languageSpinner, this)

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

        val worker = DbHelper.getInstance().loadWorker("dev@dev.at")
        if(worker == null)
        {
            val addressId = DbHelper.getInstance().saveAddress("DEVSTREET 10", "0000", "DEVCITY")
            DbHelper.getInstance().saveWorker("DEV", "DEV", LocalDate.now(),"DEV","dev@dev.at","dev12345","4355546456",
                LocalDateTime.now(),addressId)
        }

    }
}
