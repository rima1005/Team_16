package team16.easytracker

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import team16.easytracker.database.DbHelper
import team16.easytracker.utils.Validator
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*


class CreateTrackingFragment : Fragment() {

    lateinit var etStartDate : EditText
    lateinit var etStartTime : EditText
    lateinit var etEndDate : EditText
    lateinit var etEndTime : EditText
    lateinit var etTrackingName : EditText
    lateinit var etTrackingNotes : EditText

    lateinit var btnSelectStartDate : Button
    lateinit var btnSelectStartTime : Button
    lateinit var btnSelectEndDate : Button
    lateinit var btnSelectEndTime : Button

    lateinit var tvErrorStartDate : TextView
    lateinit var tvErrorStartTime : TextView
    lateinit var tvErrorEndDate : TextView
    lateinit var tvErrorEndTime : TextView
    lateinit var tvErrorTrackingName : TextView
    lateinit var tvErrorTrackingNotes : TextView

    lateinit var btnSaveTracking : Button
    lateinit var btnBack : Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_tracking, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        etStartDate = view.findViewById(R.id.etTrackingStartDate)
        etStartTime = view.findViewById(R.id.etTrackingStartTime)
        etEndDate = view.findViewById(R.id.etTrackingEndDate)
        etEndTime = view.findViewById(R.id.etTrackingEndTime)
        etTrackingName = view.findViewById(R.id.etTrackingName)
        etTrackingNotes = view.findViewById(R.id.etTrackingNotes)

        btnSelectStartDate = view.findViewById(R.id.btnCreateTrackingSelectStartDate)
        btnSelectStartTime = view.findViewById(R.id.btnCreateTrackingSelectStartTime)
        btnSelectEndDate = view.findViewById(R.id.btnCreateTrackingSelectEndDate)
        btnSelectEndTime = view.findViewById(R.id.btnCreateTrackingSelectEndTime)

        tvErrorStartDate = view.findViewById(R.id.tvErrorTrackingStartDate)
        tvErrorStartTime = view.findViewById(R.id.tvErrorTrackingStartTime)
        tvErrorEndDate = view.findViewById(R.id.tvErrorTrackingEndDate)
        tvErrorEndTime = view.findViewById(R.id.tvErrorTrackingEndTime)
        tvErrorTrackingName = view.findViewById(R.id.tvErrorTrackingName)
        tvErrorTrackingNotes = view.findViewById(R.id.tvErrorTrackingNotes)

        btnSelectStartDate.setOnClickListener { setDate(etStartDate, view) }
        btnSelectStartTime.setOnClickListener { setTime(etStartTime) }
        btnSelectEndDate.setOnClickListener { setDate(etEndDate, view) }
        btnSelectEndTime.setOnClickListener { setTime(etEndTime) }

        btnSaveTracking = view.findViewById(R.id.btnCreateTrackingSave)
        btnBack = view.findViewById(R.id.btnCreateTrackingBack)

        btnSaveTracking.setOnClickListener{ createTracking() }

        btnBack?.setOnClickListener { backToTrackings() }
    }

    private fun setDate(editText: EditText, view: View) {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        val dpd = DatePickerDialog(
            activity!!,
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                editText.setText(
                    "" + (if (dayOfMonth >= 10) dayOfMonth else "0" + dayOfMonth) + "." +
                            (if ((monthOfYear + 1) >= 10) (monthOfYear + 1) else "0" + (monthOfYear + 1)) + "." + year
                )
            },
            year,
            month,
            day
        )
        dpd.show()
    }

    private fun setTime(editText: EditText) {
        val cal = Calendar.getInstance()
        val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
            cal.set(Calendar.HOUR_OF_DAY, hour)
            cal.set(Calendar.MINUTE, minute)
            editText.setText(SimpleDateFormat("HH:mm").format(cal.time))
        }
        TimePickerDialog(
            activity,
            timeSetListener,
            cal.get(Calendar.HOUR_OF_DAY),
            cal.get(Calendar.MINUTE),
            true
        ).show()
    }

    private fun createTracking() {
        resetErrorMessages()

        val startDate = etStartDate.text.toString()
        val startTime = etStartTime.text.toString()
        val endDate = etEndDate.text.toString()
        val endTime = etEndTime.text.toString()
        val trackingName = etTrackingName.text.toString()
        val trackingNotes = etTrackingNotes.text.toString()

        val validStartDate = validateStartDate(startDate)
        val validStartTime = validateStartTime(startTime)
        val validEndDate = validateEndDate(endDate)
        val validEndTime = validateEndTime(endTime)
        val validTrackingName = validateTrackingName(trackingName)

        if (validStartDate && validStartTime && validEndDate && validEndTime && validTrackingName) {
            Log.i(
                "Valid Tracking", "The tracking is valid: " +
                        "Start Date: " + startDate + ", " +
                        "Start Time: " + startTime + ", " +
                        "End Date: " + endDate + ", " +
                        "End Time: " + endTime + ", " +
                        "Tracking Name: " + trackingName + ", " +
                        "Tracking Notes: " + trackingNotes + ", " +
                        "Bluetooth Device: - (manual)"
            )

            val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")
            val startDateTime: LocalDateTime = LocalDateTime.parse("$startDate $startTime", formatter)
            val endDateTime: LocalDateTime = LocalDateTime.parse("$endDate $endTime", formatter)

            if(startDateTime > endDateTime)
            {
                Snackbar.make(view!!, R.string.end_date_before_start_date, BaseTransientBottomBar.LENGTH_SHORT).show()
                return
            }

            val workerId = MyApplication.loggedInWorker!!.getId() 
            val trackingId = DbHelper.getInstance().saveTracking(
                trackingName,
                workerId,
                startDateTime,
                endDateTime,
                trackingNotes,
                ""
            )

            Log.i("Tracking created", "The tracking has been created with tracking ID " + trackingId)
            showSuccessDialog()
            backToTrackings()

        } else {
            Log.i("Invalid Tracking", "The tracking is invalid")
        }
    }

    private fun showSuccessDialog() {
        val builder = AlertDialog.Builder(activity)
        builder.setTitle(getString(R.string.tracking_created))
        builder.setMessage(getString(R.string.tracking_created))
        builder.setCancelable(false)

        builder.setPositiveButton(getString(R.string.ok)) { dialog, which ->
            backToTrackings()
        }

        builder.show()
    }

    private fun backToTrackings() {
        val trackings = TrackingsFragment()
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.flFragment, trackings, "TrackingsFragment")
            .addToBackStack(null)
            .commit()
    }

    private fun validateStartDate(startDate: String) : Boolean {
        val errorStartDate = Validator.validateTrackingStartDate(startDate, resources)
        if (errorStartDate != "") {
            tvErrorStartDate.text = errorStartDate
            tvErrorStartDate.visibility = VISIBLE
            return false
        }
        return true
    }

    private fun validateStartTime(startTime: String) : Boolean {
        val errorStartDate = Validator.validateTrackingStartTime(startTime, resources)
        if (errorStartDate != "") {
            tvErrorStartTime.text = errorStartDate
            tvErrorStartTime.visibility = VISIBLE
            return false
        }
        return true
    }

    private fun validateEndDate(endDate: String) : Boolean {
        val errorStartDate = Validator.validateTrackingEndDate(endDate, resources)
        if (errorStartDate != "") {
            tvErrorEndDate.text = errorStartDate
            tvErrorEndDate.visibility = VISIBLE
            return false
        }
        return true
    }

    private fun validateEndTime(endTime: String) : Boolean {
        val errorStartDate = Validator.validateTrackingEndTime(endTime, resources)
        if (errorStartDate != "") {
            tvErrorEndTime.text = errorStartDate
            tvErrorEndTime.visibility = VISIBLE
            return false
        }
        return true
    }

    private fun validateTrackingName(trackingName: String) : Boolean {
        val errorTrackingName = Validator.validateTrackingName(trackingName, resources)
        if (errorTrackingName != "") {
            tvErrorTrackingName.text = errorTrackingName
            tvErrorTrackingName.visibility = VISIBLE
            return false
        }
        return true
    }

    private fun resetErrorMessages() {
        tvErrorStartDate.text = ""
        tvErrorStartDate.visibility = View.GONE

        tvErrorStartTime.text = ""
        tvErrorStartTime.visibility = View.GONE

        tvErrorEndDate.text = ""
        tvErrorEndDate.visibility = View.GONE

        tvErrorEndTime.text = ""
        tvErrorEndTime.visibility = View.GONE

        tvErrorTrackingName.text = ""
        tvErrorTrackingName.visibility = View.GONE

    }

}