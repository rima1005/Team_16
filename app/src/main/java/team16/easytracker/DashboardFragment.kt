package team16.easytracker

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView

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
    }
}