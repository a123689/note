package com.dmobileapps.dat.app_note.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dmobileapps.dat.app_note.R
import com.dmobileapps.dat.app_note.model.CheckList
import com.dmobileapps.dat.app_note.ui.adapter.CheckListAdapter
import com.dmobileapps.dat.app_note.ui.fragment.choosevideo.ChooseImageAct
import com.dmobileapps.dat.app_note.utils.setPreventDoubleClick
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.fragment_check_list.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class CheckListFragment : BaseFragment(R.layout.fragment_check_list),
    BottomNavigationView.OnNavigationItemSelectedListener {
    private val arrCheckList: ArrayList<CheckList> = ArrayList()
    private val adapterCheckList: CheckListAdapter = CheckListAdapter(arrCheckList) {}
    private var IS_CHOOSE = 0
    private var POSITION_FOCUS = 0
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
        rcvCheckList.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        rcvCheckList.adapter = adapterCheckList
        bottom_nav.setOnNavigationItemSelectedListener(this)
    }

    override fun onResume() {
        super.onResume()
        if (IS_CHOOSE ==1){
            Log.e("TAG", "onResume: omg" )
        }else if (IS_CHOOSE ==2){}
    }

    override fun onNavigationItemSelected(menu: MenuItem): Boolean {
        when (menu.itemId) {
            R.id.menu_add -> {
                var idCheckList = 0
                if (arrCheckList.size > 0) {
                    idCheckList = arrCheckList.size + 1
                }
                arrCheckList.add(CheckList(idCheckList))
                adapterCheckList.notifyDataSetChanged()
            }
            R.id.menu_draw -> {

            }
            R.id.menu_image -> {
                IS_CHOOSE = 1
                startActivity(Intent(requireContext(),ChooseImageAct::class.java))
            }
            R.id.menu_recording -> {

            }
        }
        return true
    }

}