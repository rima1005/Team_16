package team16.easytracker.model

import java.time.*
import java.util.*


class Tracking(val id: Int, val name: String, val workerId: Int, val startTime: LocalDateTime, val endTime: LocalDateTime, val description: String, val bluetoothDevice: String) {

    companion object {
        fun load(id: Int): Tracking {
            throw NotImplementedError("Not Implemented!")
        }

        fun save(name: String): Int {
            throw NotImplementedError("Not Implemented!")
        }
    }
}