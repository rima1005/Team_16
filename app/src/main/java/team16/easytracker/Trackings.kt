package team16.easytracker

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ListView
import team16.easytracker.database.DbHelper
import team16.easytracker.utils.TrackingsAdapter
import java.time.LocalDateTime

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class Trackings : Fragment() {
    lateinit var btnCreateTracking : Button

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_trackings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnCreateTracking = view.findViewById(R.id.btnCreateTracking)
        btnCreateTracking?.setOnClickListener {
            val createTrackingFragment = CreateTracking()
            activity!!.supportFragmentManager.beginTransaction()
                    .replace(R.id.flFragment, createTrackingFragment, "CreateTrackingFragment")
                    .addToBackStack(null)
                    .commit()
        }

        var listView : ListView = view.findViewById<ListView>(R.id.lvTrackings)
        //remove later just for testing -------------------------------------------------
        var trackingId = DbHelper.saveTracking(
                "test1",
                0,
                LocalDateTime.now() ,
                LocalDateTime.now() ,
                "note asdf",
                "asdfasdfasdfasdf"
        )
        trackingId = DbHelper.saveTracking(
                "test2",
                0,
                LocalDateTime.now() ,
                LocalDateTime.now() ,
                "note asdf",
                "asdfasdfasdfasdf"
        )
        trackingId = DbHelper.saveTracking(
                "test3",
                0,
                LocalDateTime.now() ,
                LocalDateTime.now() ,
                "note asdf",
                "asdfasdfasdfasdf"
        )
        trackingId = DbHelper.saveTracking(
                "test4",
                0,
                LocalDateTime.now() ,
                LocalDateTime.now() ,
                "note asdf",
                "asdfasdfasdfasdf"
        )
        //--------------------------------------------------------------------------

        val trackingsList = DbHelper.loadWorkerTrackings(0)?.toMutableList()
        val listItems = arrayOfNulls<String>(trackingsList!!.size!!)

        if (trackingsList != null) {
            for (i in listItems.indices) {
                val tracking = trackingsList?.get(i)
                listItems?.set(i, tracking.name)
            }
        }

        val adapter = context?.let { TrackingsAdapter(it, trackingsList) }
        listView.adapter = adapter
    }

    fun createTracking(view: View) {
    }
}