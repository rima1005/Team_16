package team16.easytracker

import androidx.fragment.app.Fragment
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import team16.easytracker.database.DbHelper
import team16.easytracker.model.Company
import team16.easytracker.utils.Validator

class createCompany : Fragment(R.layout.fragment_create_company) {
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
        dbHelper = DbHelper(activity!!)

        etCompanyName = getView()?.findViewById(R.id.etCompanyName)!!
        etStreet = getView()?.findViewById(R.id.etStreet)!!
        etZipCode = getView()?.findViewById(R.id.etPostCode)!!
        etCity = getView()?.findViewById(R.id.etCity)!!
        etPosition = getView()?.findViewById(R.id.etCompanyPosition)!!
        btnCreateCompany = getView()?.findViewById(R.id.btnCreateCompany)!!

        tvErrorCompanyName = getView()?.findViewById(R.id.tvErrorCompanyName)!!
        tvErrorCompanyPosition = getView()?.findViewById(R.id.tvErrorCompanyPosition)!!
        tvErrorZipCode = getView()?.findViewById(R.id.tvErrorPostCode)!!
        tvErrorStreet = getView()?.findViewById(R.id.tvErrorStreet)!!
        tvErrorCity = getView()?.findViewById(R.id.tvErrorCity)!!


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
        val errorCompanyName = Validator.validateCompanyName(companyName)
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

        val errorPosition = Validator.validatePosition(etPosition.text.toString())
        if (errorPosition != "")
        {
            errorOccured = true
            tvErrorCompanyPosition.text = errorPosition
            tvErrorCompanyPosition.visibility = View.VISIBLE
        }

        val errorZipCode = Validator.validatePostCode(etZipCode.text.toString())
        if (errorZipCode != "")
        {
            errorOccured = true
            tvErrorZipCode.text = errorZipCode
            tvErrorZipCode.visibility = View.VISIBLE
        }

        val errorCity = Validator.validateCity(etCity.text.toString())
        if (errorCity != "")
        {
            errorOccured = true
            tvErrorCity.text = errorCity
            tvErrorCity.visibility = View.VISIBLE
        }

        val errorStreet = Validator.validateStreet(etStreet.text.toString())
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