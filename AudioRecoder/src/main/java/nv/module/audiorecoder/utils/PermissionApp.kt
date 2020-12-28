package nv.module.audiorecoder.utils

import android.R.attr
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.Settings
import android.view.View
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import nv.module.audiorecoder.R
import java.io.File


class PermissionApp {
    private var mIsNotGranted = false

    companion object {
        var mIsDoNotShowClicked = false
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray,
        context: Context
    ) {
        if (requestCode == Constant.PERMISSION_ALL) {
            for (i in permissions.indices) {
                val permission = permissions[i]
                if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                    mIsNotGranted = true
                    if (!shouldShowRequestPermissionRationale(context as Activity, permission!!)) {
                        mIsDoNotShowClicked = true
                    }
                }
            }
            if (!mIsNotGranted) {
                val path =
                    Environment.getExternalStorageDirectory().absolutePath + File.separator + Constant.VOICE_CHANGER_FOLDER + File.separator + Constant.CACHE_FOLDER + File.separator
                val dir = File(path)
                if (!dir.exists()) {
                    dir.mkdirs()
                }

                val fullName = path + Constant.AudioName + Constant.TYPE_MP3
                val file = File(fullName)
                file.createNewFile()
            }
        }
    }

    fun showMainActivity(view: View) {
        view.visibility = View.GONE
    }

    fun requestPermission(context: Context) {
        if (!hasPermissions(context, *Constant.PERMISSIONS)) {
            ActivityCompat.requestPermissions(
                context as Activity,
                Constant.PERMISSIONS,
                Constant.PERMISSION_ALL
            )
        }
    }

    fun hasPermissions(
        context: Context?,
        vararg permissions: String
    ): Boolean {
        if (context != null && permissions != null) {
            for (permission in permissions) {
                if (ActivityCompat.checkSelfPermission(
                        context,
                        permission
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    return false
                }
            }
        }
        return true
    }

    fun gotoDetailSettings(context: Context) {
        val intent = Intent()
        intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
        val uri =
            Uri.fromParts(Constant.SCHEME_PACKAGE, context.packageName, null)
        intent.data = uri
        context.startActivity(intent)
    }
}