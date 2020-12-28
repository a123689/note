package nv.module.audiorecoder.ui

import android.app.Activity
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.view.View
import androidx.annotation.RequiresApi
import kotlinx.android.synthetic.main.activity_audio.*
import nv.module.audiorecoder.R
import nv.module.audiorecoder.manager.AudioRecorder
import nv.module.audiorecoder.utils.Constant
import nv.module.audiorecoder.utils.PermissionApp

open class AudioActivity : AppCompatActivity() {

    private var audioRecorder: AudioRecorder? = null
    private var isRecord = false
    private var stateAudio = Constant.NONE
    private lateinit var permissionApp: PermissionApp

    companion object {

        fun startActivity(activity: Activity) {
            val intent = Intent(activity, AudioActivity::class.java)
            activity.startActivityForResult(intent, REQUEST_AUDIO)
        }

        const val REQUEST_AUDIO = 1000
        const val KEY_PATH_AUDIO = "KEY_PATH_AUDIO"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audio)
        permissionInit()
        audioRecorder = AudioRecorder(this)
        listener()
    }

    private fun listener() {
        btnRecord.setOnClickListener {
            if (!permissionApp.hasPermissions(this, *Constant.PERMISSIONS)) {
                requestPermission()
                return@setOnClickListener
            }

            when (stateAudio) {
                Constant.NONE -> {
                    chronometerListener()
                    animationView()
                    audioRecorder?.startRecorder(System.currentTimeMillis().toString())
                    imgRecord.setImageDrawable(resources.getDrawable(R.drawable.ic_icon_stop_record))
                    stateAudio = Constant.STOP
                }
                Constant.STOP -> {
                    imgRecord.setImageDrawable(resources.getDrawable(R.drawable.ic_record))
                    audioRecorder?.pauseAudio()
                    pauseChronometer()
                    stateAudio = Constant.RESUME
                }
                Constant.RESUME -> {
                    imgRecord.setImageDrawable(resources.getDrawable(R.drawable.ic_icon_stop_record))
                    audioRecorder?.resumeAudio()
                    resumeChronometer()
                    stateAudio = Constant.STOP
                }
            }
        }

        btnDone.setOnClickListener {
            scale(btnDone)
            audioRecorder?.stopRecorder()
            val intent = Intent()
            intent.putExtra(KEY_PATH_AUDIO, audioRecorder?.filePath)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }

        btnClose.setOnClickListener {
            scale(btnClose)
            onBackPressed()
        }

        btnCloseBack.setOnClickListener {
            scale(btnCloseBack)
            onBackPressed()
        }
    }

    private fun animationView() {
        btnRecord.animate()
            .scaleX(1.1f).scaleY(1.1f)
            .alpha(1f)
            .setDuration(200)
            .withEndAction {
                chronometer.visibility = View.VISIBLE
                btnRecord.animate()
                    .scaleX(1f).scaleY(1f)
                    .alpha(1f)
                    .setDuration(200)
                    .withEndAction {

                    }
            }
    }

    private fun chronometerListener() {
        chronometer.visibility = View.VISIBLE
        chronometer.base = SystemClock.elapsedRealtime()
        chronometer.start()
    }

    private var time = SystemClock.elapsedRealtime()
    private fun pauseChronometer() {
        time = chronometer.base
        chronometer.stop()
    }

    private fun resumeChronometer() {
        chronometer.base = time
        chronometer.start()
    }

    override fun onBackPressed() {
        audioRecorder?.stopRecorder()
        isRecord = false
        finish()
    }

    private fun scale(view: View) {
        view.animate()
            .scaleX(0.8f).scaleY(0.8f)
            .alpha(1f)
            .setDuration(200)
            .withEndAction {
                chronometer.visibility = View.VISIBLE
                btnRecord.animate()
                    .scaleX(1f).scaleY(1f)
                    .alpha(1f)
                    .setDuration(200)
                    .withEndAction {
                    }
            }
    }

    private fun permissionInit() {
        permissionApp = PermissionApp()
        requestPermission()
    }

    private fun requestPermission() {
        permissionApp.requestPermission(this)
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        permissionApp.onRequestPermissionsResult(
            requestCode,
            permissions,
            grantResults,
            this
        )
    }
}