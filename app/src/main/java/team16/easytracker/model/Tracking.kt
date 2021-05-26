package team16.easytracker.model

import java.time.*
import java.util.*


class Tracking(val id: Int, val name: String, val workerId: Int, val startTime: LocalDateTime, val endTime: LocalDateTime, val description: String, val bluetoothDevice: String) {

}