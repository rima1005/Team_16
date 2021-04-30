package team16.easytracker

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import team16.easytracker.database.DbHelper
import team16.easytracker.utils.Validator
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*


class CreateTracking : Fragment() {

    lateinit var etStartDate : EditText
    lateinit var etStartTime : EditText
    lateinit var etEndDate : EditText
    lateinit var etEndTime : EditText
    lateinit var etTrackingName : EditText
    lateinit var etTrackingNotes : EditText
    lateinit var etBluetoothDevice : EditText

    lateinit var tvErrorStartDate : TextView
    lateinit var tvErrorStartTime : TextView
    lateinit var tvErrorEndDate : TextView
    lateinit var tvErrorEndTime : TextView
    lateinit var tvErrorTrackingName : TextView
    lateinit var tvErrorTrackingNotes : TextView
    lateinit var tvErrorBluetoothDevice : TextView

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
        etBluetoothDevice = view.findViewById(R.id.etBluetoothDevice)

        tvErrorStartDate = view.findViewById(R.id.tvErrorTrackingStartDate)
        tvErrorStartTime = view.findViewById(R.id.tvErrorTrackingStartTime)
        tvErrorEndDate = view.findViewById(R.id.tvErrorTrackingEndDate)
        tvErrorEndTime = view.findViewById(R.id.tvErrorTrackingEndTime)
        tvErrorTrackingName = view.findViewById(R.id.tvErrorTrackingName)
        tvErrorTrackingNotes = view.findViewById(R.id.tvErrorTrackingNotes)
        tvErrorBluetoothDevice = view.findViewById(R.id.tvErrorBluetoothDevice)

        /*etStartDate.setOnClickListener { setDate(etStartDate, view) }
        etStartDate.onFocusChangeListener = OnFocusChangeListener { view, hasFocus ->
            if (hasFocus) {
                setDate(etStartDate, view)
            }
        }

        etStartTime.setOnClickListener { setTime(etStartTime) }
        etStartTime.onFocusChangeListener = OnFocusChangeListener { view, hasFocus ->
            if (hasFocus) {
                setTime(etStartTime)
            }
        }

        etEndDate.setOnClickListener { setDate(etEndDate, view) }
        etEndDate.onFocusChangeListener = OnFocusChangeListener { view, hasFocus ->
            if (hasFocus) {
                setDate(etEndDate, view)
            }
        }

        etEndTime.setOnClickListener { setTime(etEndTime) }
        etEndTime.onFocusChangeListener = OnFocusChangeListener { view, hasFocus ->
            if (hasFocus) {
                setTime(etEndTime)
            }
        }*/

        btnSaveTracking = view.findViewById(R.id.btnCreateTrackingSave)
        btnBack = view.findViewById(R.id.btnCreateTrackingBack)

        btnSaveTracking.setOnClickListener{ createTracking() }

        btnBack?.setOnClickListener { backToTrackings() }
    }

    fun setDate(editText: EditText, view: View) {
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

    fun setTime(editText: EditText) {
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

    fun createTracking() {
        val startDate = etStartDate.text.toString()
        val startTime = etStartTime.text.toString()
        val endDate = etEndDate.text.toString()
        val endTime = etEndTime.text.toString()
        val trackingName = etTrackingName.text.toString()
        val trackingNotes = etTrackingNotes.text.toString()
        val bluetoothDevice = etBluetoothDevice.text.toString()

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
                        "Bluetooth Device: " + bluetoothDevice
            )

            val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")
            val startDateTime: LocalDateTime = LocalDateTime.parse("$startDate $startTime", formatter)
            val endDateTime: LocalDateTime = LocalDateTime.parse("$endDate $endTime", formatter)

            val workerId = 0; // TODO: set this from logged in worker
            val trackingId = DbHelper.saveTracking(
                trackingName,
                workerId,
                startDateTime,
                endDateTime,
                trackingNotes,
                bluetoothDevice
            )

            Log.i("Tracking created", "The tracking has been created with tracking ID " + trackingId)
            showSucessDialog()
            backToTrackings()

        } else {
            Log.i("Invalid Tracking", "The tracking is invalid")
        }
    }

    fun showSucessDialog() {
        val builder = AlertDialog.Builder(activity)
        builder.setTitle("Tracking created")
        builder.setMessage("The tracking has been created!")
        builder.setCancelable(false)

        builder.setPositiveButton("OK") { dialog, which ->
            backToTrackings()
        }

        builder.show()
    }

    fun backToTrackings() {
        val trackings = Trackings()
        activity!!.supportFragmentManager.beginTransaction()
            .replace(R.id.flFragment, trackings, "TrackingsFragment")
            .addToBackStack(null)
            .commit()
    }

    fun validateStartDate(startDate: String) : Boolean {
        val errorStartDate = Validator.validateTrackingStartDate(startDate)
        if (errorStartDate != "") {
            tvErrorStartDate.text = errorStartDate
            tvErrorStartDate.visibility = VISIBLE
            return false
        }
        return true
    }

    fun validateStartTime(startTime: String) : Boolean {
        val errorStartDate = Validator.validateTrackingStartTime(startTime)
        if (errorStartDate != "") {
            tvErrorStartTime.text = errorStartDate
            tvErrorStartTime.visibility = VISIBLE
            return false
        }
        return true
    }

    fun validateEndDate(endDate: String) : Boolean {
        val errorStartDate = Validator.validateTrackingEndDate(endDate)
        if (errorStartDate != "") {
            tvErrorEndDate.text = errorStartDate
            tvErrorEndDate.visibility = VISIBLE
            return false
        }
        return true
    }

    fun validateEndTime(endTime: String) : Boolean {
        val errorStartDate = Validator.validateTrackingEndTime(endTime)
        if (errorStartDate != "") {
            tvErrorEndTime.text = errorStartDate
            tvErrorEndTime.visibility = VISIBLE
            return false
        }
        return true
    }

    fun validateTrackingName(trackingName: String) : Boolean {
        val errorTrackingName = Validator.validateTrackingName(trackingName)
        if (errorTrackingName != "") {
            tvErrorTrackingName.text = errorTrackingName
            tvErrorTrackingName.visibility = VISIBLE
            return false
        }
        return true
    }


}