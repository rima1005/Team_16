package team16.easytracker

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment

class CompanyFragment : Fragment(R.layout.fragment_company) {

    lateinit var btnCreateCompany: Button
    lateinit var btnAddWorker: Button

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnCreateCompany = view.findViewById(R.id.btnCreateCompanyFragment)
        btnAddWorker = view.findViewById(R.id.btnAddWorker)

        if (MyApplication.loggedInWorker?.company == null) {
            btnAddWorker.isEnabled = false
            btnCreateCompany.isEnabled = true
            btnCreateCompany.setOnClickListener() {
                startCreateCompany()
            }
        } else {
            btnCreateCompany.isEnabled = false
            if(MyApplication.loggedInWorker?.admin!!) {
                btnAddWorker.isEnabled = true
                btnAddWorker.setOnClickListener() {
                    startAddWorker()
                }
            }

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

    private fun startAddWorker() {
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.flFragment, CompanyAdminFragment())
        transaction.disallowAddToBackStack()
        transaction.commit()
    }
}