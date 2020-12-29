package com.dmobileapps.dat.app_note.ui.fragment.checkList

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dmobileapps.dat.app_note.R
import com.dmobileapps.dat.app_note.model.CheckList
import com.dmobileapps.dat.app_note.ui.adapter.CheckListAdapter
import com.dmobileapps.dat.app_note.ui.fragment.BaseFragment
import com.dmobileapps.dat.app_note.ui.fragment.chooseImage.ChooseImageAct
import com.dmobileapps.dat.app_note.utils.DeviceUtil
import com.dmobileapps.dat.app_note.utils.setPreventDoubleClick
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.fragment_check_list.*
import nv.module.audiorecoder.ui.AudioActivity
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class CheckListFragment : BaseFragment(R.layout.fragment_check_list),
    BottomNavigationView.OnNavigationItemSelectedListener {
    private val arrCheckList: ArrayList<CheckList> = ArrayList()
    private lateinit var adapterCheckList: CheckListAdapter
    private var IS_CHOOSE = 0
    private var POSITION_FOCUS = 0
    private var isOnClick = false
    override fun onFragmentBackPressed() {
        findNavController().popBackStack()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ivBack.setPreventDoubleClick(300) {
            onFragmentBackPressed()
        }
        val currentDate: String = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())
        tvTime.text = currentDate
        initRcv()

        bottom_nav.setOnNavigationItemSelectedListener(this)
    }

    private fun initRcv() {
       adapterCheckList =    CheckListAdapter(arrCheckList, { position, text ->
//                    onFocusText
            POSITION_FOCUS = position
        }
            , {
                arrCheckList.removeAt(it)
                adapterCheckList.notifyDataSetChanged()
            }, { positionCheckList, positionImage ->
//                onDeleteImage
               arrCheckList[positionCheckList].images.removeAt(positionImage)
               adapterCheckList.notifyItemChanged(positionCheckList)
            }, { positionCheckList, positionRecord ->
//                onDeleteRecord

            })
        rcvCheckList.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        rcvCheckList.adapter = adapterCheckList

    }

    override fun onResume() {
        super.onResume()
        if (isOnClick) {
            if (IS_CHOOSE == 1) {
                Log.e("TAG", "onResume: ${DeviceUtil.arrImage} ")
                if (DeviceUtil.arrImage.isNotEmpty()) {
                    if (arrCheckList.isEmpty()) {
                        addItemCheckList()
                    }
                    arrCheckList[POSITION_FOCUS].images.addAll(DeviceUtil.arrImage)
                    adapterCheckList.notifyItemChanged(POSITION_FOCUS)
                }
            } else if (IS_CHOOSE == 2) {
                if (arrCheckList.isEmpty()) {
                    addItemCheckList()
                }
            }
            isOnClick = false
        }
    }

    override fun onNavigationItemSelected(menu: MenuItem): Boolean {
        when (menu.itemId) {
            R.id.menu_add -> {
                addItemCheckList()
            }
            R.id.menu_image -> {
                IS_CHOOSE = 1
                startActivity(Intent(requireContext(), ChooseImageAct::class.java))
                isOnClick = true
            }
            R.id.menu_draw -> {
                DrawAct.startActivity(requireActivity())
                IS_CHOOSE = 2
                isOnClick = true
            }

            R.id.menu_recording -> {
                AudioActivity.startActivity(requireActivity())
                IS_CHOOSE = 3
                isOnClick = true
            }
        }
        return true
    }

    private fun addItemCheckList() {
        var idCheckList = 0
        if (arrCheckList.size > 0) {
            idCheckList = arrCheckList.size + 1
        }
        arrCheckList.add(CheckList(idCheckList))
        adapterCheckList.notifyDataSetChanged()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == AudioActivity.REQUEST_AUDIO && resultCode == AppCompatActivity.RESULT_OK) {
            val  requiredValue = data!!.getStringExtra(AudioActivity.KEY_PATH_AUDIO)
        }
    }
}