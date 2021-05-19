package team16.easytracker

import android.R
import android.app.Activity
import android.app.Application
import android.content.Context
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.Spinner
import team16.easytracker.adapters.MapSpinnerAdapter
import team16.easytracker.model.Worker
import java.util.*


class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object {
        var loggedInWorker : Worker? = null
        val languageList = mapOf(
            "en" to "English",
            "ru" to "Russian"
        )
        var currentLanguage = "en"
        lateinit var instance: MyApplication
            private set

        fun changeLanguage(activity: Activity, language: String) {
            if(language != currentLanguage) {
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
            spinner.adapter = MapSpinnerAdapter(activity.applicationContext,
                R.layout.simple_spinner_dropdown_item,
                languageList.toMutableMap()
            )

            spinner.setSelection(languageList.keys.indexOf(currentLanguage))

            spinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {

                }
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    val selectedLanguage = parent?.getItemAtPosition(position)!!.toString()
                    changeLanguage(activity, selectedLanguage)
                }
            }
        }
    }

}