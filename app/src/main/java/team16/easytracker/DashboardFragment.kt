package team16.easytracker

import android.app.AlertDialog
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import team16.easytracker.database.DbHelper
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
            val time = LocalDateTime.now()
            DbHelper.getInstance().saveTracking(
                    activeTrackingName,
                    MyApplication.loggedInWorker!!.getId(),
                    time,
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

    public fun stopTracking() {
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

            builder.setPositiveButton(android.R.string.ok) { _, _ ->
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

                MyApplication.currentTracking = null


                with(NotificationManagerCompat.from(MyApplication.instance.applicationContext))
                {
                    cancel(1)
                }
            }
            builder.setNegativeButton(android.R.string.cancel) { _, _ ->
                // Don't stop the tracking by doing nothing here
            }

            builder.show()
        } else {
            Snackbar.make(view!!, "Tracking has already ended automatically", Snackbar.LENGTH_SHORT).show()
            btnStopTracking.visibility = View.GONE
            tvActiveTracking.visibility = View.GONE
            tvLabelActiveTracking.visibility = View.GONE
            btnStartTracking.visibility = View.VISIBLE
        }
    }

    // TODO: call this periodically
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


            var exists = false

            val notificationManager : NotificationManager = MyApplication.instance.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val notifications = notificationManager!!.activeNotifications
            for (notification in notifications) {
                if (notification.id == 1) {
                    exists = true
                }
            }

            if(exists)
                return

            val intent = Intent(context, HomeActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }

            val pendingIntent : PendingIntent = PendingIntent.getActivity(context, 0, intent, 0)
            var formatter = DateTimeFormatter.ofPattern("HH:mm");
            var notificationBuilder = NotificationCompat.Builder(
                MyApplication.instance.applicationContext,
                MyApplication.CHANNEL_ID
            )
                .setContentTitle("Ongoing Tracking")
                .setContentText("A tracking is currently recording since " + workerTrackings.elementAt(workerTrackings.size -1).startTime.format(formatter))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setOngoing(true)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentIntent(pendingIntent)

            with(NotificationManagerCompat.from(MyApplication.instance.applicationContext))
            {
                notify(1, notificationBuilder.build())
            }

        }
    }
}