package com.dmobileapps.dat.app_note.ui.fragment.choosevideo

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dmobileapps.dat.app_note.R
import com.dmobileapps.dat.app_note.model.ImageObj
import com.dmobileapps.dat.app_note.ui.fragment.BaseFragment
import com.dmobileapps.dat.app_note.utils.DeviceUtil
import com.dmobileapps.dat.app_note.utils.setPreventDoubleClick
import com.dmobileapps.dat.app_note.ui.fragment.choosevideo.adapter.AdapterImage
import kotlinx.android.synthetic.main.fragment_select_image.*
import kotlinx.android.synthetic.main.toolbar_choose_image.*

private const val TAG = "ChooseVideoFrag"

class ChooseVideoFrag : BaseFragment(R.layout.fragment_select_image), View.OnClickListener {
    private val STORAGE_REQUEST = 100
    private var arrImageObj: ArrayList<ImageObj> = ArrayList()
    private var arrImageSelected: ArrayList<ImageObj> = ArrayList()
    private lateinit var adapterImage: AdapterImage
    override fun onFragmentBackPressed() {
        findNavController().popBackStack()

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tbBack.setPreventDoubleClick(300) {
            onFragmentBackPressed()
        }
        setRcvImage()
    }

    override fun onResume() {
        super.onResume()
        Log.e(TAG, "onResume: 00")
        requestStoragePermission()
    }

    private fun setRcvImage() {
        adapterImage = AdapterImage(arrImageObj) {
            val video = arrImageObj[it]
            if (video.isSelected) {
                arrImageSelected.remove(video)
            } else {
                arrImageSelected.add(video)
            }
            arrImageObj[it].isSelected = !arrImageObj[it].isSelected
            adapterImage.notifyItemChanged(it)
        }
        rcvImage.layoutManager =  GridLayoutManager(requireContext(), 3, RecyclerView.VERTICAL, false)
        rcvImage.adapter = adapterImage
    }

    private fun requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                STORAGE_REQUEST
            )
        } else {
            getAllImage()
        }
    }

    //
    private fun getAllImage() {
        arrImageObj.clear()
        if (DeviceUtil.getAllImage(requireContext()) != null) {
            arrImageObj.addAll(DeviceUtil.getAllImage(requireContext())!!)
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
                        requireContext(),
                        "You must grant a write storage permission to use this functionality",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    override fun onClick(v: View?) {
    }

}