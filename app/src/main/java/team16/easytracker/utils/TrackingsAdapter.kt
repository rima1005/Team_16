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
import team16.easytracker.EditTrackingFragment
import team16.easytracker.R
import team16.easytracker.model.Tracking as TrackingModel

class TrackingsAdapter(private val context: Context, private val source: List<TrackingModel>, private val activity: FragmentActivity) : BaseAdapter() {
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

        return convertView
    }
}