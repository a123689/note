package com.dmobileapps.dat.app_note.ui.fragment.checkList

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.dmobileapps.dat.app_note.R
import com.dmobileapps.dat.app_note.model.ImageObj
import com.dmobileapps.dat.app_note.utils.Common
import com.dmobileapps.dat.app_note.utils.DeviceUtil
import nv.module.brushdraw.ui.BrushUtils

class DrawAct : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_draw)
        initBrush()
        if(Common.checkInterface){
            brushUtils?.setColorBrush( ContextCompat.getColor(this,R.color.colorWhite))
            brushUtils?.setBackground(ContextCompat.getColor(this,R.color.color_black) )

        }else{
            brushUtils?.setColorBrush( ContextCompat.getColor(this,R.color.color_black))
            brushUtils?.setBackground(ContextCompat.getColor(this,R.color.colorWhite) )
//            brushUtils?.setColorBrush(resources.getColor(R.color.color_black))
//            brushUtils?.setBackground(resources.getColor(R.color.colorWhite))
        }

    }
    var brushUtils: BrushUtils? = null
    private fun initBrush() {
        brushUtils = BrushUtils(this, { bitmap ->
            //save
            DeviceUtil.arrImage.clear()
            DeviceUtil.arrImage.add(ImageObj("bitmap${System.currentTimeMillis() / 1000}","",null,bitmap,false ))
            finish()
//            imgTest.setImageBitmap(bitmap)
        }, {
            //back
            DeviceUtil.arrImage.clear()
            finish()
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