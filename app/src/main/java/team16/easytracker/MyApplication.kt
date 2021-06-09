package team16.easytracker

import android.app.*
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Parcelable
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.AdapterView
import android.widget.Spinner
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import team16.easytracker.adapters.MapSpinnerAdapter
import team16.easytracker.database.DbHelper
import team16.easytracker.model.Tracking
import team16.easytracker.model.Worker
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*


class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this
        createNotificationChannel()
    }

    companion object {
        var menu : Menu? = null
        var loggedInWorker: Worker? = null
            set(value) {
                if(value != null) {

                    val workerTrackings = DbHelper.getInstance().loadWorkerTrackings(value.getId())

                    if (workerTrackings.isNotEmpty()) {
                        val activeTracking = workerTrackings.elementAt(workerTrackings.size - 1)
                        if (activeTracking.endTime == LocalDateTime.MIN) {
                            currentTracking = activeTracking
                        }
                    }

                    if (!discoveryCancelled)
                        startContinuousDeviceDiscovery()
                }
                field = value
            }

        var discoveryCancelled: Boolean = false
        var currentTracking: Tracking? = null
        val pendingDeviceNames = mutableMapOf<String, String>()

        val languageList = mapOf(
            "en" to "English",
            "ru" to "Russian"
        )
        var currentLanguage = "en"
        lateinit var instance: MyApplication
            private set

        fun changeLanguage(activity: Activity, language: String) {
            if (language != currentLanguage) {
                Log.d("LanguageChanged", language)
                currentLanguage = language
                updateResources(activity.applicationContext)
                activity.finish()
                activity.startActivity(activity.intent)
            }
        }

        @Suppress("DEPRECATION") // TODO: sorry :(
        fun updateResources(context: Context) {
            val locale = Locale(currentLanguage)
            Locale.setDefault(locale)
            val resources = context.resources
            val configuration = resources.configuration
            configuration.setLocale(locale)
            context.resources.updateConfiguration(configuration, context.resources.displayMetrics)
        }

        fun initLanguageSpinner(spinner: Spinner, activity: Activity) {
            spinner.adapter = MapSpinnerAdapter(
                activity.applicationContext,
                android.R.layout.simple_spinner_dropdown_item,
                languageList.toMutableMap()
            )

            spinner.setSelection(languageList.keys.indexOf(currentLanguage))

            spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {

                }

                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    val selectedLanguage = parent?.getItemAtPosition(position)!!.toString()
                    changeLanguage(activity, selectedLanguage)
                }
            }
        }

        fun startContinuousDeviceDiscovery() {
            val tag = "ContinuousDeviceDiscovery"
            val dbHelper = DbHelper.getInstance()
            val btAdapter = BluetoothAdapter.getDefaultAdapter()

            val broadcastReceiver = object : BroadcastReceiver() {

                val macsThisRun = mutableListOf<String>()

                override fun onReceive(context: Context?, intent: Intent) {
                    when (intent.action) {
                        BluetoothAdapter.ACTION_DISCOVERY_STARTED -> {
                            macsThisRun.clear()
                            Log.d(tag, "Discovery started")
                        }

                        BluetoothAdapter.ACTION_DISCOVERY_FINISHED -> {
                            Log.d("Bluetooth", "Discovery finished")

                            if (currentTracking != null && !macsThisRun.contains(currentTracking!!.bluetoothDevice)) {
                                stopTracking()
                            }

                            if (!discoveryCancelled) {
                                btAdapter.startDiscovery() // here we go again
                            } else {
                                Log.d("Bluetooth", "Not starting discovery again because manually cancelled (for bonding?)")
                            }
                        }

                        BluetoothDevice.ACTION_FOUND -> {

                            val device =
                                intent.getParcelableExtra<Parcelable>(BluetoothDevice.EXTRA_DEVICE) as BluetoothDevice
                            Log.d("Bluetooth", "Found device: ${device.name} with address ${device.address}")

                            macsThisRun.add(device.address)

                            if (loggedInWorker == null)
                                return

                            if (currentTracking == null) {

                                val workerDevices = dbHelper.loadBluetoothDevicesForWorker(loggedInWorker!!.getId())
                                val macs = workerDevices.map { it.mac }
                                if (macs.contains(device.address)) {
                                    startTracking("Bluetooth " + LocalDateTime.now().format(DateTimeFormatter.ISO_DATE), "Automatic tracking via bluetooth", device.address)
                                }
                            }
                        }
                    }
                }
            }

            val filter = IntentFilter()
            filter.addAction(BluetoothDevice.ACTION_FOUND)
            filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED)
            filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)
            instance.registerReceiver(broadcastReceiver, filter)
            btAdapter.startDiscovery()
        }

        fun startTracking(name: String, description: String = "", deviceMac: String = "") {
            val trackingID = DbHelper.getInstance().saveTracking(
                name,
                loggedInWorker!!.getId(),
                LocalDateTime.now(),
                LocalDateTime.MIN,
                description,
                deviceMac
            )

            Log.d("Tracking", "STARTED")

            val intent = Intent(this.instance.applicationContext, HomeActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }

            val pendingIntent : PendingIntent = PendingIntent.getActivity(this.instance.applicationContext, 0, intent, 0)
            currentTracking = DbHelper.getInstance().loadTracking(trackingID)

            var builder = NotificationCompat.Builder(instance.applicationContext, CHANNEL_ID)
                .setContentTitle("Ongoing Tracking")
                .setContentText("A automatic tracking is currently recording, Disable Bluetooth to stop!")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setOngoing(true)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentIntent(pendingIntent)



            with(NotificationManagerCompat.from(instance.applicationContext))
            {
                notify(1, builder.build())
            }

            // TODO: notification?
        }

        fun stopTracking() {
            if (currentTracking == null) {
                throw IllegalStateException("stopTracking() called with no active tracking!")
            }

            val validTracking = currentTracking!!
            DbHelper.getInstance().updateTracking(
                validTracking.id,
                validTracking.name,
                validTracking.workerId,
                validTracking.startTime,
                LocalDateTime.now(),
                validTracking.description,
                validTracking.bluetoothDevice
            )

            Log.d("Tracking", "STOPPED")
            currentTracking = null

            with(NotificationManagerCompat.from(instance.applicationContext))
            {
                cancel(1)
            }

            // TODO: notification?
        }

        public val CHANNEL_ID = UUID.randomUUID().toString()
        private val CHANNEL_NAME = "EasyTracker"
        private val CHANNEL_DESCRIPTION = "Informs about ongoing Trackings."
    }



    private fun createNotificationChannel()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH).apply { description = CHANNEL_DESCRIPTION }
            val notificationManager : NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}