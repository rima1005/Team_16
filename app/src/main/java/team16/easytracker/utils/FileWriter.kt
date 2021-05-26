package team16.easytracker.utils

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.DocumentsContract
import team16.easytracker.MyApplication

object FileWriter {
    fun write(fileName: String, contents: String) {
        // TODO: Implement
    }

    // Request code for creating a PDF document.
    const val CREATE_FILE = 32

    // https://developer.android.com/training/data-storage/shared/documents-files#create-file
    fun createFile(pickerInitialUri: Uri, currentActivity: Activity) {
        val intent = Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "text/csv"
            putExtra(Intent.EXTRA_TITLE, "trackings.csv")

            // Optionally, specify a URI for the directory that should be opened in
            // the system file picker before your app creates the document.
            putExtra(DocumentsContract.EXTRA_INITIAL_URI, pickerInitialUri)
        }
        currentActivity.startActivityForResult(intent, CREATE_FILE)
    }
}