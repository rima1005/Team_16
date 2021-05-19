package team16.easytracker

import android.Manifest
import android.app.AlertDialog
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.View
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import team16.easytracker.database.DbHelper

/* Resources
 * https://www.youtube.com/watch?v=PtN6UTIu7yw
 * http://www.londatiga.net/it/programming/android/how-to-programmatically-scan-or-discover-android-bluetooth-device/
 */

class BluetoothFragment :  Fragment(R.layout.fragment_bluetooth) {

    lateinit var btnAddBluetoothDevice: Button

    lateinit var lvBluetoothDevices: ListView

    lateinit var bluetoothAdapter: BluetoothAdapter

    lateinit var bluetoothReceiver: BroadcastReceiver

    lateinit var bluetoothDevicesAdapter: ArrayAdapter<String>


    val bluetoothDevices: MutableSet<BluetoothDevice> = mutableSetOf()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnAddBluetoothDevice = view.findViewById(R.id.btnSearchBluetoothDevices)
        lvBluetoothDevices = view.findViewById(R.id.lvBluetoothDevices)

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()

        //val bluetoothDevicesAdapter = context?.let { TrackingsAdapter(it, trackingsList, activity!!) }
        bluetoothDevicesAdapter = ArrayAdapter(context!!, R.layout.bluetooth_device)
        lvBluetoothDevices.adapter = bluetoothDevicesAdapter

        lvBluetoothDevices.setOnItemClickListener { _, _, index, _ ->
            val bluetoothDevice = bluetoothAdapter.getRemoteDevice(bluetoothDevicesAdapter.getItem(index))
            Log.d("Bluetooth", "lvBluetoothDevices.selectedItem = " + bluetoothDevice.address)
            // TODO: Change worker id to logged in worker id
            DbHelper.saveBluetoothDevice(bluetoothDevice.address, bluetoothDevice.name, /*MyApplication.loggedInWorker!!.getId()*/ -1)
        }




        bluetoothReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent) {
                val action = intent.action
                if (BluetoothAdapter.ACTION_DISCOVERY_STARTED == action) {
                    //discovery starts, we can show progress dialog or perform other tasks
                    Log.d("Bluetooth","ACTION_DISCOVERY_STARTED")
                } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED == action) {
                    //discovery finishes, dismis progress dialog
                    Log.d("Bluetooth","DISCOVERY_FINISHED")
                    // TODO update list UI

                } else if (BluetoothDevice.ACTION_FOUND == action) {
                    //bluetooth device found
                    val device = intent.getParcelableExtra<Parcelable>(BluetoothDevice.EXTRA_DEVICE) as BluetoothDevice
                    bluetoothDevices.add(bluetoothAdapter.getRemoteDevice(device.address))
                    Log.d("Bluetooth","Found device: " + device.name)
                    Log.d("Bluetooth","Found device with address: " + device.address)
                    bluetoothDevicesAdapter.add(device.address)
                }
            }
        }

        if (!bluetoothAdapter.isEnabled) {
            // Bluetooth is not on
            val builder = AlertDialog.Builder(activity)
            builder.setTitle(getString(R.string.enable_bluetooth))
            builder.setMessage(getString(R.string.bluetooth_required_message))
            builder.setCancelable(false)

            builder.setPositiveButton(getString(R.string.enable)) { _, _ ->
                if (!bluetoothAdapter.enable()) {
                    Log.e("Bluetooth", "Enabling Bluetooth failed");
                    val failedBuilder = AlertDialog.Builder(activity)
                    failedBuilder.setTitle(getString(R.string.error))
                    failedBuilder.setMessage(getString(R.string.error_enable_bluetooth))
                    failedBuilder.setPositiveButton(R.string.ok) { _, _ ->
                        // TODO back to user settings/profile
                    }
                }
            }
            builder.setNegativeButton(getString(R.string.keep_off)) { _, _ ->
                // TODO back to user settings/profile
            }
            builder.show()
        }

        val filter = IntentFilter()

        filter.addAction(BluetoothDevice.ACTION_FOUND)
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED)
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)

        requireActivity().registerReceiver(bluetoothReceiver, filter)

        ActivityCompat.requestPermissions(requireActivity(),
                arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION),16)

        //bluetoothAdapter.getRemoteDevice("mac address")

        btnAddBluetoothDevice.setOnClickListener {
            Log.d("Bluetooth", "ACCESS_COARSE_LOCATION: " + ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION))
            Log.d("Bluetooth", "ACCESS_FINE_LOCATION: " + ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION))
            Log.d("Bluetooth", "Start discovery state: " + bluetoothAdapter.state)
            Log.d("Bluetooth", "Start discovery: " + bluetoothAdapter.startDiscovery())
        }

    }

    override fun onDestroy() {
        requireActivity().unregisterReceiver(bluetoothReceiver)
        super.onDestroy()
    }
}