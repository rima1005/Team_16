package team16.easytracker

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Spinner
import androidx.fragment.app.Fragment

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProfileFragment : Fragment(R.layout.fragment_profile) {
    // TODO: Rename and change types of parameters

    lateinit var spLanguage: Spinner
    lateinit var btnBluetoothSettings: Button

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        spLanguage = view.findViewById<Spinner>(R.id.spLanguage)
        btnBluetoothSettings = view.findViewById(R.id.btnBluetoothSettings)

        MyApplication.initLanguageSpinner(spLanguage, requireActivity())

        btnBluetoothSettings.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.flFragment, BluetoothFragment(), "BluetoothFragment")
                    .addToBackStack(null)
                    .commit()
        }
    }
}