package team16.easytracker

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import androidx.fragment.app.Fragment

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Profile.newInstance] factory method to
 * create an instance of this fragment.
 */
class Profile : Fragment(R.layout.fragment_profile) {
    // TODO: Rename and change types of parameters
    lateinit var etTitleSetting : EditText
    lateinit var etFirstNameSetting : EditText
    lateinit var etLastNameSetting : EditText
    lateinit var etDateOfBirthSetting : EditText
    lateinit var etPhonePrefixSetting : EditText
    lateinit var etPhoneNumberSetting : EditText
    lateinit var etPostCodeSetting : EditText
    lateinit var etCitySetting : EditText
    lateinit var etStreetSetting : EditText
    lateinit var etUsernameSetting : EditText
    lateinit var etPasswordSetting : EditText

    lateinit var tvErrorGenderSetting : TextView
    lateinit var tvErrorTitleSetting : TextView
    lateinit var tvErrorFirstNameSetting : TextView
    lateinit var tvErrorLastNameSetting : TextView
    lateinit var tvErrorDateOfBirthSetting : TextView
    lateinit var tvErrorPhonePrefixSetting : TextView
    lateinit var tvErrorPhoneNumberSetting : TextView
    lateinit var tvErrorPostCodeSetting : TextView
    lateinit var tvErrorCitySetting : TextView
    lateinit var tvErrorStreetSetting : TextView
    lateinit var tvErrorUsernameSetting : TextView
    lateinit var tvErrorPasswordSetting : TextView

    lateinit var spGenderSetting : Spinner

    lateinit var btnSaveChangesSetting : Button
    lateinit var btnChangePassword : Button


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val languageSpinner = view.findViewById<Spinner>(R.id.spLanguage)
        MyApplication.initLanguageSpinner(languageSpinner, requireActivity())
    }
}