package team16.easytracker

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.text.Editable
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import team16.easytracker.database.DbHelper
import team16.easytracker.model.Tracking
import team16.easytracker.utils.Validator
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class EditTrackingFragment : Fragment(R.layout.fragment_edit_tracking) {
    private var trackingId: Int? = null

    lateinit var etStartDate : EditText
    lateinit var etStartTime : EditText
    lateinit var etEndDate : EditText
    lateinit var etEndTime : EditText
    lateinit var etTrackingName : EditText
    lateinit var etTrackingNotes : EditText
    lateinit var etBluetoothDevice : EditText

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
    lateinit var tvErrorBluetoothDevice : TextView

    lateinit var btnUpdateTracking : Button
    lateinit var btnEditTrackingBack : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            trackingId = it.getInt("id")
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Initialize view fields
        etStartDate = view.findViewById(R.id.etTrackingStartDateEdit)
        etStartTime = view.findViewById(R.id.etTrackingStartTimeEdit)
        etEndDate = view.findViewById(R.id.etTrackingEndDateEdit)
        etEndTime = view.findViewById(R.id.etTrackingEndTimeEdit)
        etTrackingName = view.findViewById(R.id.etTrackingNameEdit)
        etTrackingNotes = view.findViewById(R.id.etTrackingNotesEdit)
        etBluetoothDevice = view.findViewById(R.id.etBluetoothDeviceEdit)

        btnSelectStartDate = view.findViewById(R.id.btnEditTrackingSelectStartDate)
        btnSelectStartTime = view.findViewById(R.id.btnEditTrackingSelectStartTime)
        btnSelectEndDate = view.findViewById(R.id.btnEditTrackingSelectEndDate)
        btnSelectEndTime = view.findViewById(R.id.btnEditTrackingSelectEndTime)

        tvErrorStartDate = view.findViewById(R.id.tvErrorTrackingStartDateEdit)
        tvErrorStartTime = view.findViewById(R.id.tvErrorTrackingStartTimeEdit)
        tvErrorEndDate = view.findViewById(R.id.tvErrorTrackingEndDateEdit)
        tvErrorEndTime = view.findViewById(R.id.tvErrorTrackingEndTimeEdit)
        tvErrorTrackingName = view.findViewById(R.id.tvErrorTrackingNameEdit)
        tvErrorTrackingNotes = view.findViewById(R.id.tvErrorTrackingNotesEdit)
        tvErrorBluetoothDevice = view.findViewById(R.id.tvErrorBluetoothDeviceEdit)

        btnSelectStartDate.setOnClickListener { setDate(etStartDate, view) }
        btnSelectStartTime.setOnClickListener { setTime(etStartTime) }
        btnSelectEndDate.setOnClickListener { setDate(etEndDate, view) }
        btnSelectEndTime.setOnClickListener { setTime(etEndTime) }

        btnUpdateTracking = view.findViewById(R.id.btnUpdateTracking)!!
        btnEditTrackingBack = view.findViewById(R.id.btnEditTrackingBack)!!

        btnUpdateTracking.setOnClickListener { updateTracking() }

        btnEditTrackingBack?.setOnClickListener { backToTrackings() }

        val tracking : Tracking? = DbHelper.getInstance().loadTracking(trackingId!!)

        etStartDate.text = Editable.Factory.getInstance().newEditable(tracking?.startTime?.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")))
        etStartTime.text = Editable.Factory.getInstance().newEditable(tracking?.startTime?.format(DateTimeFormatter.ofPattern("HH:mm")))
        etEndDate.text = Editable.Factory.getInstance().newEditable(tracking?.endTime?.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")))
        etEndTime.text = Editable.Factory.getInstance().newEditable(tracking?.endTime?.format(DateTimeFormatter.ofPattern("HH:mm")))
        etTrackingName.text = Editable.Factory.getInstance().newEditable(tracking?.name)
        etTrackingNotes.text = Editable.Factory.getInstance().newEditable(tracking?.description)
        etBluetoothDevice.text = Editable.Factory.getInstance().newEditable(tracking?.bluetoothDevice)
    }

    private fun setDate(editText: EditText, view: View) {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        val dpd = DatePickerDialog(
            requireActivity(),
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

    private fun updateTracking() {
        // update to database
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
            val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")
            val startDateTime: LocalDateTime = LocalDateTime.parse("$startDate $startTime", formatter)
            val endDateTime: LocalDateTime = LocalDateTime.parse("$endDate $endTime", formatter)

            val workerId = MyApplication.loggedInWorker!!.getId()
            DbHelper.getInstance().updateTracking(
                trackingId,
                trackingName,
                workerId,
                startDateTime,
                endDateTime,
                trackingNotes,
                bluetoothDevice
            )

            Log.i("Tracking edited", "The tracking has been edited with tracking ID " + trackingId)
            showSuccessDialog()
            backToTrackings()
        }
    }

    private fun showSuccessDialog() {
        val builder = AlertDialog.Builder(activity)
        builder.setTitle(getString(R.string.tracking_edited))
        builder.setMessage(getString(R.string.tracking_been_edited))
        builder.setCancelable(false)

        builder.setPositiveButton("OK") { dialog, which ->
            backToTrackings()
        }

        builder.show()
    }

    private fun backToTrackings() {
        val trackings = TrackingsFragment()
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.flFragment, trackings, "EdiTrackingsFragment")
            .addToBackStack(null)
            .commit()
    }

    private fun validateStartDate(startDate: String) : Boolean {
        val errorStartDate = Validator.validateTrackingStartDate(startDate, resources)
        if (errorStartDate != "") {
            tvErrorStartDate.text = errorStartDate
            tvErrorStartDate.visibility = View.VISIBLE
            return false
        }
        return true
    }

    private fun validateStartTime(startTime: String) : Boolean {
        val errorStartDate = Validator.validateTrackingStartTime(startTime, resources)
        if (errorStartDate != "") {
            tvErrorStartTime.text = errorStartDate
            tvErrorStartTime.visibility = View.VISIBLE
            return false
        }
        return true
    }

    private fun validateEndDate(endDate: String) : Boolean {
        val errorStartDate = Validator.validateTrackingEndDate(endDate, resources)
        if (errorStartDate != "") {
            tvErrorEndDate.text = errorStartDate
            tvErrorEndDate.visibility = View.VISIBLE
            return false
        }
        return true
    }

    private fun validateEndTime(endTime: String) : Boolean {
        val errorStartDate = Validator.validateTrackingEndTime(endTime, resources)
        if (errorStartDate != "") {
            tvErrorEndTime.text = errorStartDate
            tvErrorEndTime.visibility = View.VISIBLE
            return false
        }
        return true
    }

    private fun validateTrackingName(trackingName: String) : Boolean {
        val errorTrackingName = Validator.validateTrackingName(trackingName, resources)
        if (errorTrackingName != "") {
            tvErrorTrackingName.text = errorTrackingName
            tvErrorTrackingName.visibility = View.VISIBLE
            return false
        }
        return true
    }
}