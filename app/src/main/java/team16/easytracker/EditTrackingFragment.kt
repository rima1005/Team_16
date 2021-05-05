package team16.easytracker

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.text.Editable
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import team16.easytracker.R
import team16.easytracker.database.DbHelper
import team16.easytracker.model.Tracking
import team16.easytracker.utils.Validator
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [EditTrackingFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class EditTrackingFragment : Fragment() {
    // TODO: Rename and change types of parameters
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


        Log.e("EditTrackingFragment", "TrackingId: " + trackingId.toString())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        Log.e("EditTrackingFragment", "Tracking: " + (DbHelper.loadTracking(trackingId!!)?.name))
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_tracking, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Initialize view fields
        etStartDate = view.findViewById(R.id.etTrackingStartDate)
        etStartTime = view.findViewById(R.id.etTrackingStartTime)
        etEndDate = view.findViewById(R.id.etTrackingEndDate)
        etEndTime = view.findViewById(R.id.etTrackingEndTime)
        etTrackingName = view.findViewById(R.id.etTrackingName)
        etTrackingNotes = view.findViewById(R.id.etTrackingNotes)
        etBluetoothDevice = view.findViewById(R.id.etBluetoothDevice)

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
        tvErrorBluetoothDevice = view.findViewById(R.id.tvErrorBluetoothDevice)

        btnSelectStartDate.setOnClickListener { setDate(etStartDate, view) }
        btnSelectStartTime.setOnClickListener { setTime(etStartTime) }
        btnSelectEndDate.setOnClickListener { setDate(etEndDate, view) }
        btnSelectEndTime.setOnClickListener { setTime(etEndTime) }

        btnUpdateTracking = view.findViewById(R.id.btnUpdateTracking)!!
        btnEditTrackingBack = view.findViewById(R.id.btnEditTrackingBack)!!

        btnUpdateTracking.setOnClickListener { updateTracking() }

        btnEditTrackingBack?.setOnClickListener { backToTrackings() }

        val tracking : Tracking? = DbHelper.loadTracking(trackingId!!)

        etStartDate.text = Editable.Factory.getInstance().newEditable(tracking?.startTime?.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")))
        etStartTime.text = Editable.Factory.getInstance().newEditable(tracking?.startTime?.format(DateTimeFormatter.ofPattern("hh:mm")))
        etEndDate.text = Editable.Factory.getInstance().newEditable(tracking?.endTime?.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")))
        etEndTime.text = Editable.Factory.getInstance().newEditable(tracking?.endTime?.format(DateTimeFormatter.ofPattern("hh:mm")))
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

            val workerId = 0 // TODO: set this from logged in worker
            /*DbHelper.updateTracking(
                trackingId,
                trackingName,
                workerId,
                startDateTime,
                endDateTime,
                trackingNotes,
                bluetoothDevice
            )*/

            Log.i("Tracking edited", "The tracking has been edited with tracking ID " + trackingId)
            showSuccessDialog()
            backToTrackings()
        }
    }

    private fun showSuccessDialog() {
        val builder = AlertDialog.Builder(activity)
        builder.setTitle("Tracking edited")
        builder.setMessage("The tracking has been edited!")
        builder.setCancelable(false)

        builder.setPositiveButton("OK") { dialog, which ->
            backToTrackings()
        }

        builder.show()
    }

    private fun backToTrackings() {
        val trackings = Trackings()
        activity!!.supportFragmentManager.beginTransaction()
            .replace(R.id.flFragment, trackings, "EdiTrackingsFragment")
            .addToBackStack(null)
            .commit()
    }

    private fun validateStartDate(startDate: String) : Boolean {
        val errorStartDate = Validator.validateTrackingStartDate(startDate)
        if (errorStartDate != "") {
            tvErrorStartDate.text = errorStartDate
            tvErrorStartDate.visibility = View.VISIBLE
            return false
        }
        return true
    }

    private fun validateStartTime(startTime: String) : Boolean {
        val errorStartDate = Validator.validateTrackingStartTime(startTime)
        if (errorStartDate != "") {
            tvErrorStartTime.text = errorStartDate
            tvErrorStartTime.visibility = View.VISIBLE
            return false
        }
        return true
    }

    private fun validateEndDate(endDate: String) : Boolean {
        val errorStartDate = Validator.validateTrackingEndDate(endDate)
        if (errorStartDate != "") {
            tvErrorEndDate.text = errorStartDate
            tvErrorEndDate.visibility = View.VISIBLE
            return false
        }
        return true
    }

    private fun validateEndTime(endTime: String) : Boolean {
        val errorStartDate = Validator.validateTrackingEndTime(endTime)
        if (errorStartDate != "") {
            tvErrorEndTime.text = errorStartDate
            tvErrorEndTime.visibility = View.VISIBLE
            return false
        }
        return true
    }

    private fun validateTrackingName(trackingName: String) : Boolean {
        val errorTrackingName = Validator.validateTrackingName(trackingName)
        if (errorTrackingName != "") {
            tvErrorTrackingName.text = errorTrackingName
            tvErrorTrackingName.visibility = View.VISIBLE
            return false
        }
        return true
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment EditTrackingFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
                EditTrackingFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }
}