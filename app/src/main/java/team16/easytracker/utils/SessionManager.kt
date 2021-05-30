package team16.easytracker.utils

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences

class SessionManager(context: Context) {
    val MY_PREF : String = "my_preferences"
    var sharedPreferences : SharedPreferences = context.getSharedPreferences(MY_PREF, MODE_PRIVATE)
    var editor : SharedPreferences.Editor? = null

    public fun firstTimeAskPermission(permission: String, firstTime : Boolean)
    {
        doEdit()
        editor!!.putBoolean(permission, firstTime)
        doCommit()

    }

    public fun isFirstTimeAksingPermission(permission: String) : Boolean
    {
        return sharedPreferences.getBoolean(permission, true)
    }

    private fun doEdit()
    {
        if (editor == null)
            editor = sharedPreferences.edit()
    }

    private fun doCommit()
    {
        if(editor != null)
        {
            editor!!.commit()
            editor = null
        }
    }

}