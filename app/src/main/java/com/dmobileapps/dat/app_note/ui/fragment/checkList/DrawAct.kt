package com.dmobileapps.dat.app_note.ui.fragment.checkList

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.dmobileapps.dat.app_note.R
import com.dmobileapps.dat.app_note.ui.fragment.chooseImage.adapter.AdapterImage
import nv.module.brushdraw.ui.BrushUtils

class DrawAct : AppCompatActivity() {
    companion object {

        fun startActivity(activity: Activity) {
            val intent = Intent(activity, DrawAct::class.java)
            activity.startActivityForResult(intent, REQUEST_BITMAP)
        }

        const val REQUEST_BITMAP = 2000
        const val KEY_BITMAP = "KEY_BITMAP"
    }
    private lateinit var adapterImage: AdapterImage
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_draw)
        initBrush()

        brushUtils?.setColorBrush(resources.getColor(R.color.color_black))
        brushUtils?.setBackground(resources.getColor(R.color.colorWhite))

    }
    var brushUtils: BrushUtils? = null
    private fun initBrush() {
        brushUtils = BrushUtils(this, { bitmap ->
            //save

//            imgTest.setImageBitmap(bitmap)
        }, {
            //back
        })

        brushUtils?.createBrush(
            findViewById(R.id.btnUndo),
            findViewById(R.id.btnRedo),
            findViewById(R.id.btnBack),
            findViewById(R.id.btnSave),
            findViewById(R.id.canvasView)
        )
    }

}