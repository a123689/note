package com.dmobileapps.dat.app_note.ui.fragment.viewImage

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.dmobileapps.dat.app_note.R
import com.dmobileapps.dat.app_note.model.ImageObj
import com.dmobileapps.dat.app_note.ui.adapter.ViewPagerAdapter
import com.dmobileapps.dat.app_note.utils.Common
import com.dmobileapps.dat.app_note.utils.DeviceUtil
import com.dmobileapps.dat.app_note.utils.setPreventDoubleClick
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_view_image.*
import kotlinx.android.synthetic.main.toolbar_view_image.*
import java.lang.reflect.Type

class ViewImageAct : AppCompatActivity() {

    private var arrImageObj: ArrayList<ImageObj> = ArrayList()
    private var position = 0
    private val viewpagerAdapter = ViewPagerAdapter(arrImageObj)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_image)
        if (intent != null) {
            val stringImage = intent.getStringExtra("arrImage")
            position = intent.getIntExtra("position", 0)
            if (!stringImage.isNullOrBlank()) {
                val listType: Type = object : TypeToken<ArrayList<ImageObj?>?>() {}.type
                arrImageObj.addAll(Gson().fromJson(stringImage, listType))
//                viewpagerAdapter.notifyDataSetChanged()
//                vpImage.setCurrentItem(position,true)
                Log.e("TAG", "onCreate: $position ", )
            }
        }
        if (Common.checkInterface) {
            tbViewImage.setBackgroundColor(ContextCompat.getColor(this, R.color.colorBlack))
            constrainViewImage.setBackgroundColor(
                ContextCompat.getColor(
                    this,
                    R.color.colorBlack
                )
            )

        } else {

            tbViewImage.setBackgroundColor(ContextCompat.getColor(this, R.color.colorWhite))
            constrainViewImage.setBackgroundColor(
                ContextCompat.getColor(
                    this,
                    R.color.colorWhite
                )
            )
        }
        tbBack.setPreventDoubleClick(300) {
            DeviceUtil.arrImage.clear()
            finish()
        }
        setVpImage()

    }

    private fun setVpImage() {
        vpImage.adapter = viewpagerAdapter
        vpImage.setCurrentItem(position,true)
    }


}