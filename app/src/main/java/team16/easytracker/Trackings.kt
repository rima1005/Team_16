package team16.easytracker

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import team16.easytracker.database.DbHelper
import team16.easytracker.utils.TrackingsAdapter
import java.time.LocalDateTime

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class Trackings : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_trackings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val dbHelper = DbHelper(activity!!)

        var listView : ListView = view.findViewById<ListView>(R.id.lvTrackings)
        //remove later just for testing -------------------------------------------------
        var trackingId = dbHelper.saveTracking(
                "test1",
                0,
                LocalDateTime.now() ,
                LocalDateTime.now() ,
                "note asdf",
                "asdfasdfasdfasdf"
        )
        trackingId = dbHelper.saveTracking(
                "test2",
                0,
                LocalDateTime.now() ,
                LocalDateTime.now() ,
                "note asdf",
                "asdfasdfasdfasdf"
        )
        trackingId = dbHelper.saveTracking(
                "test3",
                0,
                LocalDateTime.now() ,
                LocalDateTime.now() ,
                "note asdf",
                "asdfasdfasdfasdf"
        )
        trackingId = dbHelper.saveTracking(
                "test4",
                0,
                LocalDateTime.now() ,
                LocalDateTime.now() ,
                "note asdf",
                "asdfasdfasdfasdf"
        )
        //--------------------------------------------------------------------------

        val trackingsList = dbHelper.loadWorkerTrackings(0)?.toMutableList()
        val listItems = arrayOfNulls<String>(trackingsList!!.size!!)

        if (trackingsList != null) {
            for (i in listItems.indices) {
                val tracking = trackingsList?.get(i)
                listItems?.set(i, tracking.name)
            }
        }

        val adapter = context?.let { TrackingsAdapter(it, trackingsList) }
        listView.adapter = adapter
        //val adapter = TrackingsAdapter(this, trackingsList) //ArrayAdapter(activity!!, android.R.layout.simple_list_item_1, listItems)
        //listView.adapter = adapter
    }

    fun createTracking(view: View) {
    }
}