package team16.easytracker

import android.content.ContentResolver
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toFile
import androidx.documentfile.provider.DocumentFile
import androidx.fragment.app.Fragment
import team16.easytracker.utils.FileWriter
import java.io.File
import java.io.OutputStream
import java.lang.Exception
import java.net.URI

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

        val btnCreateFile = view.findViewById<Button>(R.id.btnCreateFile)
        btnCreateFile.setOnClickListener {
            val fileDesc = FileWriter.createFile(Uri.parse("."), activity!!)
        }

        MyApplication.initLanguageSpinner(spLanguage, requireActivity())

        btnBluetoothSettings.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.flFragment, BluetoothFragment(), "BluetoothFragment")
                    .addToBackStack(null)
                    .commit()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == AppCompatActivity.RESULT_OK) {

            when (requestCode) {
                FileWriter.CREATE_FILE -> {
                    // Get the url from data
                    val newFileUri = data?.data ?: throw Exception("Failed to create file")
                    val outputStream = context!!.contentResolver.openOutputStream(newFileUri)!!
                    val writer = outputStream.bufferedWriter()
                    writer.append("Test,Test1,Test2\n1,2,3")
                    outputStream.flush()
                    outputStream.close()
                }
            }
        }
    }
}
