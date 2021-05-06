package team16.easytracker.utils

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import team16.easytracker.EditTrackingFragment
import team16.easytracker.R
import team16.easytracker.database.DbHelper
import team16.easytracker.model.Tracking as TrackingModel

class TrackingsAdapter(private val context: Context, private val source: MutableList<TrackingModel>, private val activity: FragmentActivity) : BaseAdapter() {
    //private val inflater: LayoutInflater
    //        = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getCount(): Int {
        return source.size
    }

    override fun getItem(position: Int): Any {
        return source[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
        //val rowView = inflater.inflate(R.layout., parent, false)
        var convertView = convertView
        convertView = LayoutInflater.from(context).inflate(R.layout.list_tracking, parent, false)
        val tvName: TextView? = convertView?.findViewById(R.id.tvName)
        val tvTime: TextView? = convertView?.findViewById(R.id.tvTime)
        tvName?.text = source[position].name
        val time: String = source[position].startTime.toString() + " - " + source[position].endTime.toString()
        tvTime?.text = time

        val btnEditTracking: Button? = convertView?.findViewById(R.id.btnEditTracking)
        btnEditTracking?.setOnClickListener {
            val bundle: Bundle = Bundle()
            bundle.putInt("id", source[position].id)
            val editTrackingFragment = EditTrackingFragment()
            editTrackingFragment.arguments = bundle
            activity!!.supportFragmentManager.beginTransaction()
                    .replace(R.id.flFragment, editTrackingFragment, "EditTrackingFragment")
                    .addToBackStack(null)
                    .commit()
        }

        val btnDeleteTracking: Button? = convertView?.findViewById(R.id.btnDeleteTracking)
        btnDeleteTracking?.setOnClickListener {
            var tracking = DbHelper.loadTracking(source[position].id)
            var success = DbHelper.deleteTracking(source[position].id)
            val successMessage: Snackbar
            if (success == 1) {
                successMessage = Snackbar.make(convertView, R.string.success_delete_tracking, BaseTransientBottomBar.LENGTH_LONG)
                source.removeAt(position)
                notifyDataSetChanged()
                val onClickListener = object : View.OnClickListener {
                    override fun onClick(v: View?) {
                        if (tracking != null) {
                            val trackingId = DbHelper.saveTracking(tracking!!.name, tracking!!.workerId, tracking!!.startTime, tracking!!.endTime, tracking!!.description, tracking!!.bluetoothDevice)
                            tracking = DbHelper.loadTracking(trackingId)
                            source.add(tracking!!)
                            source.sortByDescending { it.startTime }
                            notifyDataSetChanged()
                        }
                    }
                }
                successMessage.setAction(R.string.undo_delete_tracking, onClickListener)

            } else
                successMessage = Snackbar.make(convertView, R.string.fail_delete_tracking, BaseTransientBottomBar.LENGTH_LONG)
            successMessage.show()
        }

        return convertView
    }

}