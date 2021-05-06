package team16.easytracker.adapters

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView


/**
 * Adapted from https://android-er.blogspot.com/2014/04/spinner-with-different-displat-text-and.html
 */
class MapSpinnerAdapter (
    context: Context, textViewResourceId: Int,
    private val map: Map<String, String>
) : ArrayAdapter<String>(context, textViewResourceId, map.values.toList()) {

    val keys: List<String> = map.keys.toList()

    override fun getCount(): Int {
        return map.keys.size
    }

    override fun getItem(position: Int): String {
        return keys[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        super.getView(position, convertView, parent)
        val label = TextView(context)
        label.text = map[keys[position]]!!
        return label
    }

    override fun getDropDownView(
        position: Int, convertView: View?,
        parent: ViewGroup?
    ): View {
        super.getDropDownView(position, convertView, parent!!)
        val label = TextView(context)
        label.text = map[keys[position]]!!
        return label
    }
}