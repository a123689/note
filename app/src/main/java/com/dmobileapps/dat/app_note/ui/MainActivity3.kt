package com.dmobileapps.dat.app_note.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import com.dmobileapps.dat.app_note.R
import nv.module.audiorecoder.ui.AudioActivity

class MainActivity3 : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main3)

        findViewById<Button>(R.id.btnAudio).setOnClickListener {
          AudioActivity.startActivity(this)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == AudioActivity.REQUEST_AUDIO && resultCode == RESULT_OK) {
            val  requiredValue = data!!.getStringExtra(AudioActivity.KEY_PATH_AUDIO)
            Log.e("TAG", "onActivityResult: $requiredValue" )
        }
    }
}