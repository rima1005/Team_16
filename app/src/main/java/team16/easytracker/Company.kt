package team16.easytracker

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment

class Company : Fragment(R.layout.fragment_company) {

    lateinit var btnCreateCompany: Button

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnCreateCompany = view.findViewById(R.id.btnCreateCompany)
        if (MyApplication.loggedInWorker?.company == null) {
            btnCreateCompany.setOnClickListener() {
                startCreateCompany()
            }
        } else {
            btnCreateCompany.visibility = View.GONE
            val company = MyApplication.loggedInWorker?.company!!
            // TODO: show company data
        }
    }

    private fun startCreateCompany() {
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.flFragment, CreateCompanyFragment())
        transaction.disallowAddToBackStack()
        transaction.commit()
    }
}