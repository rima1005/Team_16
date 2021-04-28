package team16.easytracker

import android.app.Application
import android.content.Context
import team16.easytracker.model.Worker

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object {
        var loggedInWorker : Worker? = null
        lateinit var instance: MyApplication
            private set
    }

}