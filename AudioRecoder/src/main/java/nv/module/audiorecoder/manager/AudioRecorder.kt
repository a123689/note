package nv.module.audiorecoder.manager

import android.app.Activity
import android.os.Environment
import android.view.View
import com.github.squti.androidwaverecorder.WaveRecorder
import nv.module.audiorecoder.utils.Constant
import java.io.File


class AudioRecorder(private val activity: Activity) {
    private var waveRecorder: WaveRecorder? = null
    var filePath = ""
    private fun configRecorder(nameDefault: String) {
        val path =
            Environment.getExternalStorageDirectory().absolutePath + File.separator + Constant.VOICE_CHANGER_FOLDER + File.separator + Constant.CACHE_FOLDER + File.separator
        val dir = File(path)
        if (!dir.exists()) {
            dir.mkdirs()
        }

        val fullName = path + Constant.AudioName + Constant.TYPE_MP3
        val file = File(fullName)
        file.createNewFile()

        filePath = path + nameDefault + Constant.TYPE_MP3
        waveRecorder = WaveRecorder(filePath)
    }

    fun startRecorder(nameDefault: String) {
        configRecorder(nameDefault)
        waveRecorder?.startRecording()
        waveRecorder?.onAmplitudeListener = {
        }
    }

    fun pauseAudio(){
        waveRecorder?.pauseRecording()
    }

    fun resumeAudio(){
        waveRecorder?.resumeRecording()
    }

    fun stopRecorder() {
        waveRecorder?.stopRecording()
    }


}