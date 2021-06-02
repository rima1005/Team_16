package team16.easytracker.utils

import android.app.AlertDialog
import android.content.Context
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import team16.easytracker.BluetoothDevicesFragment
import team16.easytracker.MyApplication
import team16.easytracker.R
import team16.easytracker.database.DbHelper


import team16.easytracker.databinding.FragmentBluetoothDeviceItemBinding
import team16.easytracker.model.WorkerBluetoothDevice

/**
 * [RecyclerView.Adapter] that can display a worker's bluetooth devices.
 */
class BluetoothDeviceRecyclerViewAdapter(
    private val values: Array <WorkerBluetoothDevice>, private val bluetoothDevicesFragment: BluetoothDevicesFragment
) : RecyclerView.Adapter<BluetoothDeviceRecyclerViewAdapter.ViewHolder>() {

    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        return ViewHolder(
            FragmentBluetoothDeviceItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.tvBluetoothDeviceName.text = item.name
        holder.tvBluetoothDeviceMAC.text = item.mac
        holder.btnEditBluetoothDevice.setOnClickListener { view ->
            val bluetoothDevice : WorkerBluetoothDevice? = DbHelper.getInstance().loadBluetoothDevice(item.mac, MyApplication.loggedInWorker!!.getId())
            showDeviceNamePromptBluetooth(bluetoothDevice)
        }
        holder.btnDeleteBluetoothDevice.setOnClickListener {
            Toast.makeText(context, "TODO: Delete Bluetooth Device " + position.toString(), Toast.LENGTH_LONG).show()
        }
    }

    private fun showDeviceNamePromptBluetooth(device: WorkerBluetoothDevice?) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle(R.string.enter_device_name)
        builder.setCancelable(true)

        val promptView = LayoutInflater.from(context).inflate(R.layout.alert_bluetooth_name, null)
        val et = promptView.findViewById<EditText>(R.id.etBluetoothDeviceName)
        et.setText(device!!.name)
        builder.setView(promptView)

        builder.setPositiveButton(R.string.edit) { _, _ ->
            val currentWorker = MyApplication.loggedInWorker!!
            val deviceName = et.text.toString()
            DbHelper.getInstance().updateBluetoothDevice(
                device.mac,
                deviceName,
                currentWorker.getId()
            )

            bluetoothDevicesFragment.reload()
        }

        builder.setNegativeButton(R.string.cancel) { _, _ ->
            Log.d("Bluetooth", "Cancel saving")
        }

        builder.show()
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(binding: FragmentBluetoothDeviceItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val tvBluetoothDeviceName: TextView = binding.tvBluetoothDeviceName
        val tvBluetoothDeviceMAC: TextView = binding.tvBluetoothDeviceMAC
        val btnEditBluetoothDevice: TextView = binding.btnEditBluetoothDevice
        val btnDeleteBluetoothDevice: TextView = binding.btnDeleteBluetoothDevice

        override fun toString(): String {
            return super.toString() + " '" + tvBluetoothDeviceName.text + "'"
        }
    }

}