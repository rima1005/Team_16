package team16.easytracker

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import team16.easytracker.database.DbHelper
import team16.easytracker.utils.Validator

class CreateCompanyFragment : Fragment(R.layout.fragment_create_company) {
    lateinit var etCompanyName: EditText
    lateinit var etStreet: EditText
    lateinit var etZipCode: EditText
    lateinit var etCity: EditText
    lateinit var etPosition: EditText
    lateinit var btnCreateCompany: Button

    lateinit var tvErrorCompanyName: TextView
    lateinit var tvErrorCompanyPosition: TextView
    lateinit var tvErrorZipCode: TextView
    lateinit var tvErrorStreet: TextView
    lateinit var tvErrorCity: TextView

    lateinit var dbHelper: DbHelper

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        dbHelper = DbHelper(activity!!)

        val fragmentView = view!!

        etCompanyName = fragmentView.findViewById(R.id.etCompanyName)
        etStreet = fragmentView.findViewById(R.id.etStreet)
        etZipCode = fragmentView.findViewById(R.id.etPostCode)
        etCity = fragmentView.findViewById(R.id.etCity)
        etPosition = fragmentView.findViewById(R.id.etCompanyPosition)
        btnCreateCompany = fragmentView.findViewById(R.id.btnCreateCompany)

        tvErrorCompanyName = fragmentView.findViewById(R.id.tvErrorCompanyName)
        tvErrorCompanyPosition = fragmentView.findViewById(R.id.tvErrorCompanyPosition)
        tvErrorZipCode = fragmentView.findViewById(R.id.tvErrorPostCode)
        tvErrorStreet = fragmentView.findViewById(R.id.tvErrorStreet)
        tvErrorCity = fragmentView.findViewById(R.id.tvErrorCity)

        btnCreateCompany.setOnClickListener { createCompany() }
    }

    private fun createCompany() {

        resetErrorMessages()

        val companyName = etCompanyName.text.toString()
        val position = etPosition.text.toString()
        val zipCode = etZipCode.text.toString()
        val city = etCity.text.toString()
        val street = etCity.text.toString()

        var errorOccured = false
        val errorCompanyName = Validator.validateCompanyName(companyName)
        if (errorCompanyName != "") {
            errorOccured = true
            tvErrorCompanyName.text = errorCompanyName
            tvErrorCompanyName.visibility = View.VISIBLE
        }

        val duplicate = dbHelper.companyExists(companyName)
        if (duplicate) {
            errorOccured = true
            tvErrorCompanyName.text = getString(R.string.error_company_exists)
            tvErrorCompanyName.visibility = View.VISIBLE
        }

        val errorPosition = Validator.validatePosition(etPosition.text.toString())
        if (errorPosition != "") {
            errorOccured = true
            tvErrorCompanyPosition.text = errorPosition
            tvErrorCompanyPosition.visibility = View.VISIBLE
        }

        val errorZipCode = Validator.validatePostCode(etZipCode.text.toString())
        if (errorZipCode != "") {
            errorOccured = true
            tvErrorZipCode.text = errorZipCode
            tvErrorZipCode.visibility = View.VISIBLE
        }

        val errorCity = Validator.validateCity(etCity.text.toString())
        if (errorCity != "") {
            errorOccured = true
            tvErrorCity.text = errorCity
            tvErrorCity.visibility = View.VISIBLE
        }

        val errorStreet = Validator.validateStreet(etStreet.text.toString())
        if (errorStreet != "") {
            errorOccured = true
            tvErrorStreet.text = errorStreet
            tvErrorStreet.visibility = View.VISIBLE
        }

        if (errorOccured)
            return

        // TODO: check for duplicate addresses?
        val addressId = dbHelper.saveAddress(street, zipCode, city)
        val companyId = dbHelper.saveCompany(companyName, addressId)
        val workerId = 0 // TODO: set this from logged in worker
        dbHelper.addWorkerToCompany(workerId, companyId, position)
        dbHelper.setCompanyAdmin(workerId, companyId, true)
    }

    private fun resetErrorMessages() {
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