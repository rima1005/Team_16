package team16.easytracker.utils

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast

import team16.easytracker.placeholder.PlaceholderContent.PlaceholderItem
import team16.easytracker.databinding.FragmentBluetoothDevicesBinding

/**
 * [RecyclerView.Adapter] that can display a [PlaceholderItem].
 * TODO: Replace the implementation with code for data [BluetoothDevice].
 */
class BluetoothDeviceRecyclerViewAdapter(
    private val values: List<PlaceholderItem>
) : RecyclerView.Adapter<BluetoothDeviceRecyclerViewAdapter.ViewHolder>() {

    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        return ViewHolder(
            FragmentBluetoothDevicesBinding.inflate(
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
            Toast.makeText(context, "TODO: Delete Bluetooth Device " + position.toString(), Toast.LENGTH_LONG).show()
        }
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(binding: FragmentBluetoothDevicesBinding) :
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