package com.dmobileapps.dat.app_note.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dmobileapps.dat.app_note.R
import kotlinx.android.synthetic.main.activity_main2.*
import nv.module.brushdraw.ui.BrushUtils

class MainActivity2 : AppCompatActivity() {

    private var isColor = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        initBrush()
        listener()
    }

    //    Test
    private fun listener() {
        btnColor.setOnClickListener {
            brushUtils?.setColorBrush(resources.getColor(R.color.colorBlack))
        }

        btnColorBackground.setOnClickListener {
            brushUtils?.setBackground(resources.getColor(R.color.colorWhite))
        }
    }

    /*
    Add lib
     */

    var brushUtils: BrushUtils? = null
    private fun initBrush() {
        brushUtils = BrushUtils(this, { bitmap ->
            //save
            imgTest.setImageBitmap(bitmap)
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