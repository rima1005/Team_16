package team16.easytracker

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import team16.easytracker.database.DbHelper
import team16.easytracker.model.Tracking
import team16.easytracker.utils.CSVConverter
import team16.easytracker.utils.FileUtility
import team16.easytracker.utils.TrackingsAdapter
import java.lang.Exception

class TrackingsFragment : Fragment() {

    val REQUEST_CODE_CREATE_FILE = 69;

    lateinit var btnCreateTracking : Button
    lateinit var btnExportTimesheet: Button
    lateinit var trackings: List<Tracking>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_trackings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnExportTimesheet = view.findViewById(R.id.btnCreateFile)
        btnCreateTracking = view.findViewById(R.id.btnCreateTracking)

        btnCreateTracking?.setOnClickListener {
            val createTrackingFragment = CreateTrackingFragment()
            activity!!.supportFragmentManager.beginTransaction()
                    .replace(R.id.flFragment, createTrackingFragment, "CreateTrackingFragment")
                    .addToBackStack(null)
                    .commit()
        }

        //--------------------------------------------------------------------------

        trackings = DbHelper.getInstance().loadWorkerTrackings(MyApplication.loggedInWorker!!.getId())

        if (trackings.isEmpty()) {
            btnExportTimesheet.isClickable = false
            // TODO: color?
        } else {
            btnExportTimesheet.setOnClickListener {
                FileUtility.openCreateFileActivity(Uri.parse("."), activity!!, REQUEST_CODE_CREATE_FILE)
            }
        }

        val listView : ListView = view.findViewById(R.id.lvTrackings)
        val adapter = context?.let { TrackingsAdapter(it, trackings.toMutableList(), activity!!) }
        listView.adapter = adapter

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == AppCompatActivity.RESULT_OK) {

            when (requestCode) {
                REQUEST_CODE_CREATE_FILE -> {
                    // Get the url from data
                    val newFileUri = data?.data ?: throw Exception("Failed to create file")
                    val outputStream = context!!.contentResolver.openOutputStream(newFileUri)!!
                    val writer = outputStream.bufferedWriter()
                    val rows = trackings.map { arrayOf(it.name, it.description, it.startTime.toString(), it.endTime.toString(), it.bluetoothDevice) }.toTypedArray()
                    val csvString = CSVConverter.serialize(rows)
                    writer.write(csvString)
                    writer.flush()
                    writer.close()
                    outputStream.flush()
                    outputStream.close()
                }
            }
        }
    }
}