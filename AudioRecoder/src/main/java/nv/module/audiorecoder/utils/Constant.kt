package nv.module.audiorecoder.utils

import android.Manifest

object Constant {

    const val TYPE_MP3 = ".mp3"
    const val AudioName = "LastRecord"

    const val VOICE_CHANGER_FOLDER = "AudioNote"
    const val CACHE_FOLDER = ".Voice"
    const val NO_MEDIA_FILE = ".nomedia"

    const val NONE = "none"
    const val START = "start"
    const val STOP = "stop"
    const val RESUME = "RESUME"

    const val PERMISSION_ALL = 1001
    const val SCHEME_PACKAGE = "package"

    val PERMISSIONS = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.RECORD_AUDIO
    )
}