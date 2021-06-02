package team16.easytracker.utils

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.DocumentsContract
import team16.easytracker.MyApplication

object FileUtility {

    /**
     * Opens Android File Activity.
     * adapted from https://developer.android.com/training/data-storage/shared/documents-files#create-file
     */
    fun openCreateFileActivity(pickerInitialUri: Uri, currentActivity: Activity, requestCode: Int) {
        val intent = Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "text/csv"
            putExtra(Intent.EXTRA_TITLE, "trackings.csv")

            // Optionally, specify a URI for the directory that should be opened in
            // the system file picker before your app creates the document.
            putExtra(DocumentsContract.EXTRA_INITIAL_URI, pickerInitialUri)
        }
        currentActivity.startActivityForResult(intent, requestCode)
    }
}