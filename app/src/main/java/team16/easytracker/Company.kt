package team16.easytracker

import android.os.Bundle
import android.widget.Button
import androidx.fragment.app.Fragment

class Company : Fragment(R.layout.fragment_company) {

    lateinit var btnCreateCompany: Button

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        btnCreateCompany = view!!.findViewById(R.id.btnCreateCompany)
        btnCreateCompany.setOnClickListener() {
            startCreateCompany()
        }
    }

    private fun startCreateCompany() {
        val transaction = activity!!.supportFragmentManager.beginTransaction()
        transaction.replace(R.id.flFragment, CreateCompanyFragment())
        transaction.disallowAddToBackStack()
        transaction.commit()
    }
}