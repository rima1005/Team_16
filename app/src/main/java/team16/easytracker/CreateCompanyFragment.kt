package team16.easytracker

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.android.material.navigation.NavigationView
import team16.easytracker.database.DbHelper
import team16.easytracker.utils.Validator
import java.time.LocalDateTime

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        etCompanyName = view.findViewById(R.id.etCompanyName)
        etStreet = view.findViewById(R.id.etStreet)
        etZipCode = view.findViewById(R.id.etPostCode)
        etCity = view.findViewById(R.id.etCity)
        etPosition = view.findViewById(R.id.etCompanyPosition)
        btnCreateCompany = view.findViewById(R.id.btnCreateCompany)

        tvErrorCompanyName = view.findViewById(R.id.tvErrorCompanyName)
        tvErrorCompanyPosition = view.findViewById(R.id.tvErrorCompanyPosition)
        tvErrorZipCode = view.findViewById(R.id.tvErrorPostCode)
        tvErrorStreet = view.findViewById(R.id.tvErrorStreet)
        tvErrorCity = view.findViewById(R.id.tvErrorCity)

        btnCreateCompany.setOnClickListener { createCompany() }
    }

    private fun createCompany() {

        resetErrorMessages()

        val companyName = etCompanyName.text.toString()
        val position = etPosition.text.toString()
        val zipCode = etZipCode.text.toString()
        val city = etCity.text.toString()
        val street = etStreet.text.toString()
        val createCompanyBtn = btnCreateCompany

        var errorOccured = false
        val errorCompanyName = Validator.validateCompanyName(companyName, resources)
        if (errorCompanyName != "") {
            errorOccured = true
            tvErrorCompanyName.text = errorCompanyName
            tvErrorCompanyName.visibility = View.VISIBLE
        }

        val duplicate = DbHelper.getInstance().companyExists(companyName)
        if (duplicate) {
            errorOccured = true
            tvErrorCompanyName.text = getString(R.string.error_company_exists)
            tvErrorCompanyName.visibility = View.VISIBLE
        }

        val errorPosition = Validator.validatePosition(etPosition.text.toString(), resources)
        if (errorPosition != "") {
            errorOccured = true
            tvErrorCompanyPosition.text = errorPosition
            tvErrorCompanyPosition.visibility = View.VISIBLE
        }

        val errorZipCode = Validator.validatePostCode(etZipCode.text.toString(), resources)
        if (errorZipCode != "") {
            errorOccured = true
            tvErrorZipCode.text = errorZipCode
            tvErrorZipCode.visibility = View.VISIBLE
        }

        val errorCity = Validator.validateCity(etCity.text.toString(), resources)
        if (errorCity != "") {
            errorOccured = true
            tvErrorCity.text = errorCity
            tvErrorCity.visibility = View.VISIBLE
        }

        val errorStreet = Validator.validateStreet(etStreet.text.toString(), resources)
        if (errorStreet != "") {
            errorOccured = true
            tvErrorStreet.text = errorStreet
            tvErrorStreet.visibility = View.VISIBLE
        }



        if (errorOccured)
            return
        createCompanyBtn.visibility = View.INVISIBLE
        // TODO: check for duplicate addresses?
        val worker = MyApplication.loggedInWorker!!
        val addressId = DbHelper.getInstance().saveAddress(street, zipCode, city)
        val companyId = DbHelper.getInstance().saveCompany(companyName, addressId)
        DbHelper.getInstance().addWorkerToCompany(worker.getId(), companyId, position)
        DbHelper.getInstance().setCompanyAdmin(worker.getId(), companyId, true)

        val navigationView = activity?.findViewById<NavigationView>(R.id.navigationView)!!
        navigationView.menu.findItem(R.id.itemAddEmployee).isVisible = true
        navigationView.menu.findItem(R.id.itemOverview).isVisible = true
        navigationView.menu.findItem(R.id.itemCreateCompany).isVisible = false
        navigationView.menu.findItem(R.id.itemOverview).isChecked = true
        // update worker because he now has a company and is admin
        MyApplication.loggedInWorker = DbHelper.getInstance().loadWorker(worker.getId())

        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.flFragment, CompanyFragment())
        transaction.disallowAddToBackStack()
        transaction.commit()
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