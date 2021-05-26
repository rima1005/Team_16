package team16.easytracker

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import androidx.fragment.app.Fragment
import team16.easytracker.database.DbHelper

class CompanyFragment : Fragment(R.layout.fragment_company) {

    lateinit var tvCompanyName: TextView
    lateinit var tvCompanyAddress: TextView
    lateinit var lvCompanyWorkers: ListView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tvCompanyName = view.findViewById(R.id.tvCompanyName)
        tvCompanyAddress = view.findViewById(R.id.tvCompanyAddress)
        lvCompanyWorkers = view.findViewById(R.id.lvCompanyWorkers)

        val company = MyApplication.loggedInWorker?.company!!
        val address = DbHelper.getInstance().loadAddress(company.addressId)
        tvCompanyName.text = company.name
        tvCompanyAddress.text = address!!.street + "\n" + address!!.zipCode + " " + address!!.city

        // TODO: show company data
    }
}