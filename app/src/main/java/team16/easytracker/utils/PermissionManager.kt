package team16.easytracker.utils

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat

class PermissionManager(private var context: Context) {
    var sessionManager = SessionManager(context)


    public fun shouldAskPermission(context: Context, permission: String) : Boolean {
        val permissionResult = ActivityCompat.checkSelfPermission(context, permission)
        return permissionResult != PackageManager.PERMISSION_GRANTED
    }

    public fun checkPermission(context: Context, permission: String, listener: PermissionAskListener)
    {
        if(shouldAskPermission(context, permission))
        {
            if(ActivityCompat.shouldShowRequestPermissionRationale(context as AppCompatActivity, permission))
            {
                listener.onPermissionPreviouslyDenied()
            }
            else
            {
                if(sessionManager.isFirstTimeAksingPermission(permission))
                {
                    sessionManager.firstTimeAskPermission(permission, false)
                    listener.onNeedPermission()
                }
                else
                {
                    listener.onPermissionPreviouslyDeniedWithNeverAskAgain()
                }
            }
        }
        else
        {
            listener.onPermissionGranted()
        }
    }

    public interface PermissionAskListener {
        fun onNeedPermission()
        fun onPermissionPreviouslyDenied()
        fun onPermissionPreviouslyDeniedWithNeverAskAgain()
        fun onPermissionGranted()
    }
}