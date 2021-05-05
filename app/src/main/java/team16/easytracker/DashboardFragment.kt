package team16.easytracker

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import team16.easytracker.database.DbHelper
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime


class DashboardFragment : Fragment(R.layout.fragment_dashboard) {

    lateinit var btnStartTracking: Button
    lateinit var btnStopTracking: Button
    lateinit var tvLabelActiveTracking: TextView
    lateinit var tvActiveTracking: TextView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnStartTracking = view.findViewById(R.id.btnStartTracking)
        btnStopTracking = view.findViewById(R.id.btnStopTracking)

        tvLabelActiveTracking = view.findViewById(R.id.tvLabelActiveTracking)
        tvActiveTracking = view.findViewById(R.id.tvActiveTracking)

        checkActiveTracking()
        btnStartTracking.setOnClickListener{startTracking()}
        btnStopTracking.setOnClickListener{stopTracking()}
    }

    private fun startTracking(){
        val builder = AlertDialog.Builder(context)
        builder.setTitle("TrackingName")
        val viewInflated: View = LayoutInflater.from(context)
            .inflate(R.layout.text_input_dialogue, view as ViewGroup?, false)
        val input = viewInflated.findViewById<View>(R.id.etActiveTracking) as EditText
        builder.setView(viewInflated)

        builder.setPositiveButton(
            android.R.string.ok
        ) { dialog, which ->
            dialog.dismiss()
            val active_tracking = input.text.toString()
            val tracking_id = DbHelper.saveTracking(active_tracking,
                MyApplication.loggedInWorker!!.getId(),
                LocalDateTime.now(),
                LocalDateTime.MIN,
                "",
                "")
            checkActiveTracking()
        }
        builder.setNegativeButton(
            android.R.string.cancel
        ) { dialog, which -> dialog.cancel() }

        builder.show()
    }

    private fun checkActiveTracking(){
        val worker_trackings = DbHelper.loadWorkerTrackings(MyApplication.loggedInWorker!!.getId())
        if(worker_trackings.isNullOrEmpty())
            return
        else if(worker_trackings.elementAt(worker_trackings.size - 1).endTime == LocalDateTime.MIN){
            btnStartTracking.visibility = View.GONE
            btnStopTracking.visibility = View.VISIBLE
            tvActiveTracking.visibility = View.VISIBLE
            tvLabelActiveTracking.visibility = View.VISIBLE
            tvActiveTracking.text = worker_trackings.elementAt(worker_trackings.size - 1).startTime.toString()
        }
    }
    private fun stopTracking(){
        val worker_trackings = DbHelper.loadWorkerTrackings(MyApplication.loggedInWorker!!.getId())
        val active_tracking = worker_trackings!!.elementAt(worker_trackings.size - 1)
        if(active_tracking.endTime == LocalDateTime.MIN){
            DbHelper.updateTracking(active_tracking.id,
                                    active_tracking.name,
                                    active_tracking.workerId,
                                    active_tracking.startTime,
                                    LocalDateTime.now(),
                                    active_tracking.description,
                                    active_tracking.bluetoothDevice)
            btnStopTracking.visibility = View.GONE
            tvActiveTracking.visibility = View.GONE
            tvLabelActiveTracking.visibility = View.GONE
            btnStartTracking.visibility = View.VISIBLE
        }
    }
}