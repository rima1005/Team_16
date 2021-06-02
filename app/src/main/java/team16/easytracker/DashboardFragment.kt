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
import java.lang.IllegalStateException
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

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
        builder.setTitle(R.string.tracking_name)
        val viewInflated: View = LayoutInflater.from(context)
            .inflate(R.layout.text_input_dialog, view as ViewGroup?, false)
        val input = viewInflated.findViewById<View>(R.id.etActiveTrackingName) as EditText
        builder.setView(viewInflated)

        builder.setPositiveButton(
            android.R.string.ok
        ) { dialog, which ->
            dialog.dismiss()
            val activeTrackingName = input.text.toString()
            DbHelper.getInstance().saveTracking(
                    activeTrackingName,
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

    private fun stopTracking() {
        val workerTrackings = DbHelper.getInstance().loadWorkerTrackings(MyApplication.loggedInWorker!!.getId())

        if (workerTrackings.isEmpty()) {
            throw IllegalStateException("stopTracking() called with no existing Trackings!")
        }

        val activeTracking = workerTrackings.elementAt(workerTrackings.size - 1)

        if (activeTracking.endTime != LocalDateTime.MIN) {
            throw IllegalStateException("stopTracking() called with no ACTIVE Tracking!")
        }

        if (activeTracking.endTime == LocalDateTime.MIN) {
            val builder = AlertDialog.Builder(activity)
            builder.setTitle(getString(R.string.stop_tracking_question))
            builder.setMessage(getString(R.string.do_you_really_want_to_stop_the_tracking_question))
            builder.setCancelable(true)

            builder.setPositiveButton(android.R.string.ok) { dialog, which ->
                DbHelper.getInstance().updateTracking(
                        activeTracking.id,
                        activeTracking.name,
                        activeTracking.workerId,
                        activeTracking.startTime,
                        LocalDateTime.now(),
                        activeTracking.description,
                        activeTracking.bluetoothDevice)

                btnStopTracking.visibility = View.GONE
                tvActiveTracking.visibility = View.GONE
                tvLabelActiveTracking.visibility = View.GONE
                btnStartTracking.visibility = View.VISIBLE
            }
            builder.setNegativeButton(android.R.string.cancel) { dialog, which ->
                // Don't stop the tracking by doing nothing here
            }

            builder.show()
        }
    }

    private fun checkActiveTracking() {
        if (MyApplication.loggedInWorker == null)
            return

        val workerTrackings = DbHelper.getInstance().loadWorkerTrackings(MyApplication.loggedInWorker!!.getId())

        if (workerTrackings.isEmpty())
            return

        else if (workerTrackings.elementAt(workerTrackings.size - 1).endTime == LocalDateTime.MIN) {
            btnStartTracking.visibility = View.GONE
            btnStopTracking.visibility = View.VISIBLE
            tvActiveTracking.visibility = View.VISIBLE
            tvLabelActiveTracking.visibility = View.VISIBLE
            tvActiveTracking.text = workerTrackings.elementAt(workerTrackings.size - 1).startTime.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"))
        }
    }
}