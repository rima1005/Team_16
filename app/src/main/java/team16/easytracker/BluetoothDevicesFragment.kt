package team16.easytracker

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.*
import team16.easytracker.database.DbHelper
import team16.easytracker.placeholder.PlaceholderContent
import team16.easytracker.utils.BluetoothDeviceRecyclerViewAdapter

/**
 * A fragment representing a list of BluetoothDevices.
 */
class BluetoothDevicesFragment : Fragment() {

    private var columnCount = 1

    lateinit var btnBluetoothSettings: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_bluetooth_devices_list, container, false)

        val currentWorker = MyApplication.loggedInWorker!!

        btnBluetoothSettings = view.findViewById(R.id.btnAddBluetoothDevice)
        btnBluetoothSettings.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.flFragment, BluetoothFragment(), "BluetoothFragment")
                .addToBackStack(null)
                .commit()
        }

        // Set the adapter
        val rvBluetoothDevices = view.findViewById<RecyclerView>(R.id.rvBluetoothDevicesList)
        if (rvBluetoothDevices is RecyclerView) {
            with(rvBluetoothDevices) {
                layoutManager = when {
                    columnCount <= 1 -> LinearLayoutManager(context)
                    else -> GridLayoutManager(context, columnCount)
                }
                val workerDevices = DbHelper.getInstance().loadBluetoothDevicesForWorker(currentWorker.getId()).toMutableList()
                adapter = BluetoothDeviceRecyclerViewAdapter(workerDevices)
            }
        }


        var mRecyclerView = view.findViewById(R.id.rvBluetoothDevicesList) as RecyclerView
        mRecyclerView.addItemDecoration(
            DividerItemDecoration(
                activity,
                DividerItemDecoration.VERTICAL
            )
        )

        mRecyclerView.setHasFixedSize(true)
        var mLayoutManager = LinearLayoutManager(activity)
        mRecyclerView.setLayoutManager(mLayoutManager)
        mRecyclerView.setItemAnimator(DefaultItemAnimator())

        return view
    }

    companion object {

        const val ARG_COLUMN_COUNT = "column-count"

        @JvmStatic
        fun newInstance(columnCount: Int) =
            BluetoothDevicesFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_COLUMN_COUNT, columnCount)
                }
            }
    }
}