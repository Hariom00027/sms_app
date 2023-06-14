package com.codeclause.smsapp.tasks

import android.content.Context
import android.util.Log
import com.google.android.gms.drive.DriveFile
import com.codeclause.smsapp.activities.SettingsActivity
import com.codeclause.smsapp.utils.Helpers
import java.io.IOException


class EditContentsAsyncTask(private val context: Context) :
        ApiClientAsyncTask<DriveFile?, Void?, Boolean>(context) {

    private val TAG = EditContentsAsyncTask::class.java.simpleName

     override fun doInBackgroundConnected(vararg params: DriveFile?): Boolean {
        val file = params[0]
        try {
            val driveContentsResult = file?.open(
                    googleApiClient, DriveFile.MODE_WRITE_ONLY, null)?.await()
            if (driveContentsResult != null) {
                if (!driveContentsResult.status.isSuccess) {
                    return false
                }
            }
            val driveContents = driveContentsResult?.driveContents
            driveContents?.outputStream?.write(Helpers.getSMSJson(context)?.toByteArray())
            val status = driveContents?.commit(googleApiClient, null)?.await()
            if (status != null) {
                return status.status.isSuccess
            }
        } catch (e: IOException) {
            Log.e(TAG, "IOException while appending to the output stream", e)
        }
        return false
    }

    override fun onPostExecute(result: Boolean?) {
        if (!result!!) {
            (context as SettingsActivity).showMessage("Error while creating backup.")
            return
        }
        (context as SettingsActivity).showMessage("Backup Successful")
    }

}