package com.dmobileapps.dat.app_note.ui.fragment.chooseImage

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dmobileapps.dat.app_note.R
import com.dmobileapps.dat.app_note.model.ImageObj
import com.dmobileapps.dat.app_note.ui.fragment.chooseImage.adapter.AdapterImage
import com.dmobileapps.dat.app_note.utils.AppUtil
import com.dmobileapps.dat.app_note.utils.DeviceUtil
import com.dmobileapps.dat.app_note.utils.setPreventDoubleClick
import kotlinx.android.synthetic.main.fragment_select_image.*
import kotlinx.android.synthetic.main.toolbar_choose_image.*

class ChooseImageAct : AppCompatActivity() {

    private val STORAGE_REQUEST = 100
    private var arrImageObj: ArrayList<ImageObj> = ArrayList()
    private var arrImageSelected: ArrayList<ImageObj> = ArrayList()
    private lateinit var adapterImage: AdapterImage
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_select_image)
        tbBack.setPreventDoubleClick(300) {
            DeviceUtil.arrImage.clear()
            finish()
        }
        btnDone.setPreventDoubleClick(300) {
            if (arrImageSelected.isNotEmpty()) {
                DeviceUtil.arrImage.clear()
                    DeviceUtil.arrImage.addAll(arrImageSelected)
                finish()
            } else {
                AppUtil.showToast(this, R.string.please_choose_image)
            }
        }
        setRcvImage()
        requestStoragePermission()

    }
    private fun requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    STORAGE_REQUEST
                )
            }
        } else {

            getAllImage()

        }
    }

    private fun setRcvImage() {
        adapterImage = AdapterImage(arrImageObj) {
            val video = arrImageObj[it]
            if (video.isSelected) {
                arrImageSelected.remove(video)

                arrImageObj[it].isSelected = !arrImageObj[it].isSelected
                adapterImage.notifyItemChanged(it)
            } else {
                if (arrImageSelected.size < 5) {
                    arrImageSelected.add(video)
                    arrImageObj[it].isSelected = !arrImageObj[it].isSelected
                    adapterImage.notifyItemChanged(it)
                } else {
                    AppUtil.showToast(this, R.string.max_video_selected)
                }

            }
        }
        rcvImage.layoutManager = GridLayoutManager(this, 3, RecyclerView.VERTICAL, false)
        rcvImage.adapter = adapterImage
    }

    private fun getAllImage() {
        arrImageObj.clear()
        if (DeviceUtil.getAllImage(this) != null) {
            arrImageObj.addAll(DeviceUtil.getAllImage(this)!!)
        }
        adapterImage.notifyDataSetChanged()
    }

    //
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {

        when (requestCode) {
            STORAGE_REQUEST -> {
                if (grantResults.isNotEmpty()
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED
                ) {
                    getAllImage()
                } else {
                    Toast.makeText(
                        this,
                        "You must grant a write storage permission to use this functionality",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

}