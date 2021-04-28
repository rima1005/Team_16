package team16.easytracker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Switch
import android.widget.TextView
import androidx.appcompat.widget.SwitchCompat
import team16.easytracker.database.DbHelper
import team16.easytracker.model.Worker
import team16.easytracker.utils.Validator
import java.time.LocalDateTime

class CompanyAdminActivity : AppCompatActivity() {

    lateinit var dbHelper: DbHelper

    lateinit var etWorkerEmail: EditText
    lateinit var tvErrorEmail: TextView
    lateinit var etPosition: EditText
    lateinit var tvErrorPosition: TextView
    lateinit var swAdmin: SwitchCompat
    lateinit var btnAddWorker: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.companyadminactivity)

        dbHelper = DbHelper(this)

        etWorkerEmail = findViewById(R.id.etEmail)
        tvErrorEmail = findViewById(R.id.tvErrorEmail)
        etPosition = findViewById(R.id.etCompanyPosition)
        tvErrorPosition = findViewById(R.id.tvErrorCompanyPosition)
        swAdmin = findViewById(R.id.swAdmin)
        btnAddWorker = findViewById(R.id.btnAddWorker)

        btnAddWorker.setOnClickListener { addWorker() }
    }

    private fun addWorker() {

        resetErrors()

        var errorOccured = false

        val workerEmail = etWorkerEmail.text.toString()
        val emailError = Validator.validateEmail(workerEmail, resources)
        if (emailError != "") {
            errorOccured = true
            tvErrorEmail.text = "Invalid Email Address"
            tvErrorEmail.visibility = View.VISIBLE
        }

        val companyPosition = etPosition.text.toString()
        val positionError = Validator.validatePosition(companyPosition, resources)
        if (positionError != "") {
            errorOccured = true
            tvErrorPosition.text = "Invalid Employee Position"
            tvErrorPosition.visibility = View.VISIBLE
        }

        if(errorOccured)
            return

        val now = LocalDateTime.now().withNano(0)
        val id = dbHelper.saveWorker(
            "Test",
            "WORKER_DUMMY_LAST_NAME",
            now.toLocalDate(),
            "",
            "test@test.com",
            "DUMMY_PASSWORD",
            "",
            now,
            1
        )
        val worker = dbHelper.loadWorker(workerEmail) ?: return
        val companyId = 0 // TODO: get id from logged in CompanyWorker
        dbHelper.addWorkerToCompany(worker.getId(), companyId, companyPosition)
        // TODO: redirect back to worker?
    }

    private fun resetErrors() {
        tvErrorEmail.text = ""
        tvErrorEmail.visibility = View.GONE
        tvErrorPosition.text = ""
        tvErrorPosition.visibility = View.GONE
    }
}