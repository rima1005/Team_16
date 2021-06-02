package team16.easytracker.utils

import android.app.AlertDialog
import android.content.Context
import android.provider.Settings.Global.getString
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import team16.easytracker.MyApplication
import team16.easytracker.R
import team16.easytracker.database.DbHelper

import team16.easytracker.placeholder.PlaceholderContent.PlaceholderItem
import team16.easytracker.databinding.FragmentBluetoothDeviceItemBinding
import team16.easytracker.model.WorkerBluetoothDevice

/**
 * [RecyclerView.Adapter] that can display a worker's bluetooth devices.
 */
class BluetoothDeviceRecyclerViewAdapter(
    private val values: MutableList <WorkerBluetoothDevice>
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
        holder.btnEditBluetoothDevice.setOnClickListener {
            Toast.makeText(context, "TODO: Edit Bluetooth Device " + position.toString(), Toast.LENGTH_LONG).show()
        }
        holder.btnDeleteBluetoothDevice.setOnClickListener {
            val mac = values[position].mac
            val workerId = MyApplication.loggedInWorker!!.getId()
            val success = DbHelper.getInstance().deleteBluetoothDeviceOfWorker(mac, workerId)
            if (success == 1) {
                values.removeAt(position)
                notifyDataSetChanged()
            }
            else {
                Log.e("Delete BluetoothDevice", "Delete bluetooth device failed")
            }

        }
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