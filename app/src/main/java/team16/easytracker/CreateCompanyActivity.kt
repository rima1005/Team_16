package team16.easytracker

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import team16.easytracker.database.DbHelper
import team16.easytracker.model.Company
import team16.easytracker.utils.Validator

class CreateCompanyActivity : AppCompatActivity(){
    lateinit var etCompanyName: EditText
    lateinit var etStreet: EditText
    lateinit var etZipCode: EditText
    lateinit var etCity: EditText
    lateinit var etPosition: EditText
    lateinit var btnCreateCompany: Button

    lateinit var tvErrorCompanyName: TextView
    lateinit var tvErrorCompanyPosition: TextView
    lateinit var tvErrorZipCode : TextView
    lateinit var tvErrorStreet : TextView
    lateinit var tvErrorCity : TextView

    lateinit var dbHelper : DbHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.createcompanyactivity)
        dbHelper = DbHelper(this)

        etCompanyName = findViewById(R.id.etCompanyName)
        etStreet = findViewById(R.id.etStreet)
        etZipCode = findViewById(R.id.etPostCode)
        etCity = findViewById(R.id.etCity)
        etPosition = findViewById(R.id.etCompanyPosition)
        btnCreateCompany = findViewById(R.id.btnCreateCompany)

        tvErrorCompanyName = findViewById(R.id.tvErrorCompanyName)
        tvErrorCompanyPosition = findViewById(R.id.tvErrorCompanyPosition)
        tvErrorZipCode = findViewById(R.id.tvErrorPostCode)
        tvErrorStreet = findViewById(R.id.tvErrorStreet)
        tvErrorCity = findViewById(R.id.tvErrorCity)


        btnCreateCompany.setOnClickListener { createCompany() }


    }

    fun createCompany(){

        resetErrorMessages()

        val companyName = etCompanyName.text.toString()
        val position = etPosition.text.toString()
        val zipCode = etZipCode.text.toString()
        val city = etCity.text.toString()
        val street = etCity.text.toString()

        var errorOccured = false
        val errorCompanyName = Validator.validateCompanyName(companyName, resources)
        if (errorCompanyName != "")
        {
            errorOccured = true
            tvErrorCompanyName.text = errorCompanyName
            tvErrorCompanyName.visibility = View.VISIBLE
        }

        val duplicate = dbHelper.companyExists(companyName)
        if(duplicate) {
            errorOccured = true
            tvErrorCompanyName.text = "Company with this name already exists"
            tvErrorCompanyName.visibility = View.VISIBLE
        }

        val errorPosition = Validator.validatePosition(etPosition.text.toString(), resources)
        if (errorPosition != "")
        {
            errorOccured = true
            tvErrorCompanyPosition.text = errorPosition
            tvErrorCompanyPosition.visibility = View.VISIBLE
        }

        val errorZipCode = Validator.validatePostCode(etZipCode.text.toString(), resources)
        if (errorZipCode != "")
        {
            errorOccured = true
            tvErrorZipCode.text = errorZipCode
            tvErrorZipCode.visibility = View.VISIBLE
        }

        val errorCity = Validator.validateCity(etCity.text.toString(), resources)
        if (errorCity != "")
        {
            errorOccured = true
            tvErrorCity.text = errorCity
            tvErrorCity.visibility = View.VISIBLE
        }

        val errorStreet = Validator.validateStreet(etStreet.text.toString(), resources)
        if (errorStreet != "")
        {
            errorOccured = true
            tvErrorStreet.text = errorStreet
            tvErrorStreet.visibility = View.VISIBLE
        }

        if(errorOccured)
           return

        // TODO: check for duplicate addresses?
        val addressId = dbHelper.saveAddress(street, zipCode, city)
        val companyId = dbHelper.saveCompany(companyName, addressId)
        val workerId = 0; // TODO: set this from logged in worker
        dbHelper.addWorkerToCompany(workerId, companyId, position)
        dbHelper.setCompanyAdmin(workerId, companyId, true)
    }

    fun resetErrorMessages() {
        tvErrorCompanyPosition.text = ""
        tvErrorCompanyPosition.visibility = View.GONE

        tvErrorCity.text = ""
        tvErrorCity.visibility = View.GONE

        tvErrorStreet.text = ""
        tvErrorStreet.visibility = View.GONE

        tvErrorZipCode.text = ""
        tvErrorZipCode.visibility = View.GONE

        tvErrorCompanyName.text = ""
        tvErrorCompanyName.visibility = View.GONE
    }
}