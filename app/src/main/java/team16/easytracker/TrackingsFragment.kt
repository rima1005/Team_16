package team16.easytracker

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import team16.easytracker.database.DbHelper
import team16.easytracker.model.Tracking
import team16.easytracker.utils.CSVConverter
import team16.easytracker.utils.FileUtility
import team16.easytracker.utils.TrackingsAdapter

class TrackingsFragment : Fragment() {

    val REQUEST_CODE_CREATE_FILE = 69;

    lateinit var trackings: List<Tracking>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        MyApplication.menu?.clear()
        activity!!.menuInflater.inflate(R.menu.menu_trackings,MyApplication.menu)
        return inflater.inflate(R.layout.fragment_trackings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        trackings = DbHelper.getInstance().loadFinishedWorkerTrackings(MyApplication.loggedInWorker!!.getId())

        val listView : ListView = view.findViewById(R.id.lvTrackings)
        val adapter = context?.let { TrackingsAdapter(it, trackings.toMutableList(), activity!!) }
        listView.adapter = adapter

    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId){
        R.id.itemCreateTracking -> {
            val createTrackingFragment = CreateTrackingFragment()
            activity!!.supportFragmentManager.beginTransaction()
                .replace(R.id.flFragment, createTrackingFragment, "CreateTrackingFragment")
                .addToBackStack(null)
                .commit()
            true
        }
        R.id.itemExportTimesheet -> {
            trackings = DbHelper.getInstance().loadWorkerTrackings(MyApplication.loggedInWorker!!.getId())
            if(trackings.isEmpty())
                Snackbar.make(view!!, R.string.error_export, BaseTransientBottomBar.LENGTH_SHORT).show()
            else
                FileUtility.openCreateFileActivity(Uri.parse("."), activity!!, REQUEST_CODE_CREATE_FILE)
            true
        }
        else -> {
            super.onOptionsItemSelected(item)
        }
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

    override fun onDestroyView() {
        MyApplication.menu?.clear()
        super.onDestroyView()
    }
}