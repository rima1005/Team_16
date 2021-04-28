package team16.easytracker

import android.os.Bundle
import android.text.TextUtils.replace
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomnavigation.BottomNavigationView

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Company.newInstance] factory method to
 * create an instance of this fragment.
 */
class Company : Fragment(R.layout.fragment_company) {
    fun startCreateCompany(view : View)
    {
        val transaction = activity?.supportFragmentManager?.beginTransaction()
        transaction?.replace(R.id.flFragment, createCompany())
        transaction?.disallowAddToBackStack()
        transaction?.commit()
    }
}