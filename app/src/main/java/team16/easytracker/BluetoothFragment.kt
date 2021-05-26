package team16.easytracker

import android.Manifest
import android.app.AlertDialog
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothSocket
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.View
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import team16.easytracker.adapters.MapSpinnerAdapter
import team16.easytracker.database.DbHelper
import team16.easytracker.model.WorkerBluetoothDevice
import java.util.*

/* Resources
 * https://www.youtube.com/watch?v=PtN6UTIu7yw
 * http://www.londatiga.net/it/programming/android/how-to-programmatically-scan-or-discover-android-bluetooth-device/
 */

class BluetoothFragment : Fragment(R.layout.fragment_bluetooth) {

    lateinit var btnDiscoverBluetoothDevices: Button

    lateinit var lvBluetoothDevices: ListView

    lateinit var bluetoothAdapter: BluetoothAdapter

    lateinit var bluetoothReceiver: BroadcastReceiver

    lateinit var deviceListAdapter: MapSpinnerAdapter

    var readyToConnect = false

    var uuid : UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")
    var bluetoothSocket : BluetoothSocket? = null

    val bluetoothDevices: MutableSet<BluetoothDevice> = mutableSetOf()
    var workerDevices = arrayOf<WorkerBluetoothDevice>()
    var workerMacs = listOf<String>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val currentWorker = MyApplication.loggedInWorker!!
        requestPermissions()

        btnDiscoverBluetoothDevices = view.findViewById(R.id.btnDiscoverBluetoothDevices)
        lvBluetoothDevices = view.findViewById(R.id.lvBluetoothDevices)

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        if (!bluetoothAdapter.isEnabled) {
            showBluetoothEnablePrompt()
        }

        workerDevices = DbHelper.getInstance().loadBluetoothDevicesForWorker(currentWorker.getId())
        workerMacs = workerDevices.map { it.mac }

        // paired devices already start in list
        val pairedDevicesMap = mutableMapOf<String, String>()
        Log.d("Bluetooth", "Bonded Devices Size: " + bluetoothAdapter.bondedDevices.size)
        for (device in bluetoothAdapter.bondedDevices) {
            if (!workerMacs.contains(device.address)) {
                pairedDevicesMap[device.address] = device.name
            }
        }

        deviceListAdapter = MapSpinnerAdapter(context!!, R.layout.bluetooth_device, pairedDevicesMap)
        lvBluetoothDevices.adapter = deviceListAdapter

        lvBluetoothDevices.setOnItemClickListener { _, _, index, _ -> onBluetoothDeviceSelected(index) }

        initBluetoothDiscoveryReceiver()

        btnDiscoverBluetoothDevices.setOnClickListener {
            if (!bluetoothAdapter.startDiscovery()) {
                throw Exception("Failed to start Bluetooth discovery")
            }
        }
    }

    override fun onDestroy() {
        requireActivity().unregisterReceiver(bluetoothReceiver)
        super.onDestroy()
    }

    private fun onBluetoothDeviceSelected(index: Int) {
        val bluetoothDevice = bluetoothAdapter.getRemoteDevice(deviceListAdapter.getItem(index))
        Log.d("Bluetooth", "lvBluetoothDevices.selectedItem = " + bluetoothDevice.address)
        showDeviceNamePrompt(bluetoothDevice)
    }

    private fun showDeviceNamePrompt(device: BluetoothDevice) {
        val builder = AlertDialog.Builder(activity)
        builder.setTitle(getString(R.string.enter_device_name))
        builder.setCancelable(true)

        val promptView = layoutInflater.inflate(R.layout.alert_bluetooth_name, null)
        val et = promptView.findViewById<EditText>(R.id.etBluetoothDeviceName)
        et.setText(device.name)
        builder.setView(promptView)

        builder.setPositiveButton(getString(R.string.add_device)) { _, _ ->
            val currentWorker = MyApplication.loggedInWorker!!
            // TODO: error handling?
            // allow same name?
            val deviceName = et.text.toString()
            val success = DbHelper.getInstance().saveBluetoothDevice(
                device.address,
                deviceName,
                currentWorker.getId()
            )
            if (success) {
                bluetoothSocket = device.createInsecureRfcommSocketToServiceRecord(uuid)
                readyToConnect = true
                bluetoothAdapter.cancelDiscovery()
                Log.d("Bluetooth", "Added bluetooth device $deviceName")
            }

            workerDevices = DbHelper.getInstance().loadBluetoothDevicesForWorker(currentWorker.getId())
            workerMacs = workerDevices.map { it.mac }

            deviceListAdapter.remove(device.address)
        }

        builder.setNegativeButton(getString(R.string.cancel)) { _, _ ->
            Log.d("Bluetooth", "Cancel saving")
        }

        builder.show()
    }

    private fun initBluetoothDiscoveryReceiver() {

        bluetoothReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent) {

                when (intent.action) {
                    BluetoothAdapter.ACTION_DISCOVERY_STARTED -> {
                        //discovery starts, we can show progress dialog or perform other tasks
                        Log.d("Bluetooth", "ACTION_DISCOVERY_STARTED")
                        // TODO show loading animation?
                    }

                    BluetoothAdapter.ACTION_DISCOVERY_FINISHED -> {
                        //discovery finishes, dismiss progress dialog
                        Log.d("Bluetooth", "DISCOVERY_FINISHED")
                        if(!bluetoothAdapter.isDiscovering && readyToConnect && bluetoothSocket != null && !bluetoothSocket!!.isConnected)
                        {
                            Log.d("Bluetooth", "Trying to connect to a new device!")
                            bluetoothSocket!!.connect()
                        }
                    }

                    BluetoothDevice.ACTION_FOUND -> {
                        //bluetooth device found
                        val device =
                            intent.getParcelableExtra<Parcelable>(BluetoothDevice.EXTRA_DEVICE) as BluetoothDevice
                        if (workerMacs.contains(device.address)) {
                            // device already added to worker
                            return
                        }
                        if (bluetoothDevices.map { it.address }.contains(device.address)) {
                            // already in the list
                            return
                        }
                        bluetoothDevices.add(bluetoothAdapter.getRemoteDevice(device.address))
                        Log.d("Bluetooth", "Found device: " + device.name)
                        Log.d("Bluetooth", "Found device with address: " + device.address)
                        // TODO: remove mac address from display
                        deviceListAdapter.add(device.address, "${device.name} - ${device.address}")
                    }

                    BluetoothDevice.ACTION_BOND_STATE_CHANGED -> {
                        val device =
                            intent.getParcelableExtra<Parcelable>(BluetoothDevice.EXTRA_DEVICE) as BluetoothDevice
                        Log.d("Bluetooth", "Bond stage changed to: ${device.bondState}")
                    }

                    BluetoothDevice.ACTION_ACL_CONNECTED -> {
                        Log.d("Bluetooth", "Connected a bluetooth device!")
                    }

                }
            }
        }

        val filter = IntentFilter()
        filter.addAction(BluetoothDevice.ACTION_FOUND)
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED)
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)

        requireActivity().registerReceiver(bluetoothReceiver, filter)
    }

    private fun showBluetoothEnablePrompt() {
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
                    closeFragment()
                }
            }
        }
        builder.setNegativeButton(getString(R.string.keep_off)) { _, _ ->
            closeFragment()
        }
        builder.show()
    }

    private fun requestPermissions() {
        val permissionsRequired = arrayOf(
            Manifest.permission.BLUETOOTH,
            Manifest.permission.BLUETOOTH_ADMIN,
            // needed for bluetooth discovery:
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
        )

        ActivityCompat.requestPermissions(requireActivity(), permissionsRequired, 16)

        for (permission in permissionsRequired) {
            if (ContextCompat.checkSelfPermission(requireContext(), permission) != PackageManager.PERMISSION_GRANTED) {
                closeFragment()
                return
            }
        }
    }

    private fun closeFragment() {
        // go back
        requireActivity().supportFragmentManager.popBackStackImmediate()
    }
}