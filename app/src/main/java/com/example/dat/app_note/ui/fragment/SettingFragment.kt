package com.example.dat.app_note.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.dat.app_note.R
import com.example.dat.app_note.utils.setPreventDoubleClick
import kotlinx.android.synthetic.main.fragment_setting.*


class SettingFragment : BaseFragment(R.layout.fragment_setting) {
    override fun onFragmentBackPressed() {
        findNavController().popBackStack()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ivBack.setPreventDoubleClick(300){
            onFragmentBackPressed()
        }
    }

}